package eu.europeana.portal2.web.controllers;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.HttpRequestHandlerAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import eu.europeana.corelib.definitions.solr.model.Term;
import eu.europeana.corelib.solr.exceptions.SolrTypeException;
import eu.europeana.corelib.solr.service.SearchService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/servlet/portal2-mvc.xml", "/internal/portal2-development.xml"})
public class SuggestionControllerTest {

	@Resource
	private SearchService searchService;

	// private String baseUrl = "http://europeana-ese2edm.isti.cnr.it:9191/solr/search/select?&wt=javabin&rows=0&version=2&";
	private String baseUrl = "http://10.101.38.1:9595/solr/";

	private HttpSolrServer solrServer = null;

	private int cents = 3;
	private int iterations = 100 * cents;

	private List<String> words;

	// @Inject
	private ApplicationContext applicationContext;

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private HttpRequestHandlerAdapter handlerAdapter;
	private SuggestionController controller;

	@Test
	public void allSpeedTest() {
		words = randomWords(iterations);
		solrJSpeedTest();
		suggestionServiceSpeedTest();
		restSpeedTest();
		// solrSpeedTest();
	}

	// @Test
	public void escapingTest() {
		String[] terms = new String[]{"'baking in bavaria", "\"baking in bavaria", "(baking in bavaria", "(baking in bavaria)"};
		for (String term : terms) {
			term = clearSuggestionTerm(term);
			assertEquals("baking in bavaria", term);
		}

		terms = new String[]{"text/image"};
		for (String term : terms) {
			term = clearSuggestionTerm(term);
			assertEquals("text\\/image", term);
		}

	}
	private String clearSuggestionTerm(String term) {
		term = term.replaceAll("[\"'()]", "").replace("/", "\\/");

		return term;
	}

	// @Test
	public void suggestionServiceTest() {
		try {
			List<Term> suggestions = searchService.suggestions("pari", 10);
			assertNotNull(suggestions);
			// TODO: change it when it is OK.
			assertEquals(10, suggestions.size());
			for (Term term : suggestions) {
				System.out.println(StringUtils.join(
					new String[]{term.getField(), term.getTerm(), Long.toString(term.getFrequency())}, " // "));
			}
		} catch (SolrTypeException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void suggestionServiceSpeedTest() {
		try {
			long t = new Date().getTime();
			int i;
			long min = 0, max = 0, t1, t3;
			String maxw = "";
			List<String> slowQueries = new ArrayList<String>();

			for (i = 0; i<iterations; i++) {
				if (i > words.size()-1) {
					break;
				}
				t1 = new Date().getTime();
				List<Term> suggestions = searchService.suggestions(words.get(i), 10);
				t3 = (new Date().getTime() - t1);
				if (t3 > 1000) {
					slowQueries.add(words.get(i) + " (" + t3 + ")");
				}
				if (min == 0) {min = max = t3;}
				if (t3 < min) {min = t3;}
				if (t3 > max) {max = t3; maxw = words.get(i);}
			}
			long time = (new Date().getTime() - t);
			System.out.println("[searchService] took " + ((new Date().getTime() - t)/cents) 
					+ " (" + min + "-" + max + ") " + maxw
					+ ", slow queries: " + slowQueries);
		} catch (SolrTypeException e) {
			e.printStackTrace();
		}
	}

	// @Test
	public void solrJSpeedTest() {

		long t = new Date().getTime();
		int i;
		long total = 0;
		long min = 0, max = 0, t1, t3, time, tmax = 0, tmin = 0;
		String maxw = "", tmaxw = "";
		List<String> slowQueries = new ArrayList<String>();
		Map<String, String> fields = new HashMap<String, String>(){{
			put("who", "suggestWho");
			put("title", "suggestTitle");
			put("what", "suggestWhat");
			put("where", "suggestWhere");
			put("when", "suggestWhen");
		}};
		for (i = 0; i<iterations; i++) {
			if (i > words.size()-1) {
				break;
			}
			t1 = new Date().getTime();
			for (Map.Entry<String, String> field : fields.entrySet()) {
				time = getSuggestions(words.get(i), field.getKey(), field.getValue());
				total += time;
				if (time > 1000) {
					slowQueries.add(field.getKey() + ":" + words.get(i) + " (" + time + ")");
				}
				if (tmax == 0) {tmax = tmin = time;}
				if (time > tmax) {tmax = time; tmaxw = field.getKey() + ":" + words.get(i);}
				if (time < tmin) {tmin = time;}
			}
			t3 = (new Date().getTime() - t1);
			if (min == 0) {min = max = t3;}
			if (t3 < min) {min = t3;}
			if (t3 > max) {max = t3; maxw = words.get(i) + " (" + t3 + ")";}
		}
		System.out.println("[solrJ] took " + ((new Date().getTime() - t)/cents) 
				+ " -- internal part: " + (total/cents) 
				+ " (" + min + "-" + max + ") " + maxw
				+ " (" + tmin + "-" + tmax + ") " + tmaxw
				+ ", slow queries: " + slowQueries
		);
	}

	private long getSuggestions(String query, String field, String rHandler) {
		if (solrServer == null) {
			solrServer = new HttpSolrServer("http://10.101.38.1:9595/solr/search");
			solrServer.setFollowRedirects(false);
			solrServer.setConnectionTimeout(60000);
			solrServer.setDefaultMaxConnectionsPerHost(64);
			solrServer.setMaxTotalConnections(125);
		}

		ModifiableSolrParams params = new ModifiableSolrParams();
		params.set("qt", "/" + rHandler);
		params.set("q", field + ":" + query);
		params.set("rows", 0);

		try {
			if (solrServer != null) {
				QueryResponse qResp = solrServer.query(params);
				return (Integer)qResp.getHeader().get("QTime");
				// return qResp.getElapsedTime();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	// @Test
	public void restSpeedTest() {
		String baseUrl = "http://localhost:8080/portal/suggestions.json?term=%s";

		long t = new Date().getTime();
		long min = 0, max = 0, t1, t3;
		String minw = "", maxw = "";
		int i;
		List<String> slowQueries = new ArrayList<String>();
		for (i = 0; i<iterations; i++) {
			if (i > words.size()-1) {
				break;
			}
			String url = String.format(baseUrl, words.get(i));
			// System.out.println(url);
			t1 = new Date().getTime();
			getWebContent(url);
			t3 = (new Date().getTime() - t1);
			if (t3 > 1000) {
				slowQueries.add(words.get(i) + " (" + t3 + ")");
			}
			if (min == 0) {min = max = t3;}
			if (t3 < min) {min = t3; minw = words.get(i);}
			if (t3 > max) {max = t3; maxw = words.get(i);}
		}
		long time = (new Date().getTime() - t);
		System.out.println("[suggestion] took " + (time/cents) + " (" + min + "-" + max + ") " + maxw
				+ ", slow queries: " + slowQueries);
	}

	// @Test
	public void solrSpeedTest() {
		String path = "search/select?&wt=javabin&rows=0&version=2&";
		String[] urls = new String[]{
			"qt=/suggestTitle&q=title:",
			"qt=/suggestWho&q=who:",
			"qt=/suggestWhat&q=what:",
			"qt=/suggestWhere&q=where:",
			"qt=/suggestWhen&q=when:"
		};

		long t = new Date().getTime();
		int i;
		for (i = 0; i<iterations; i++) {
			if (i > words.size()-1) {
				break;
			}
			if (StringUtils.isBlank(words.get(i))) {
				continue;
			}
			for (String params : urls) {
				String url = baseUrl + path + params + words.get(i);
				// System.out.println(url);
				getWebContent(url);
			}
		}
		long time = (new Date().getTime() - t);
		System.out.println("[solr] took " + (time/cents));
	}

	// @Test
	public void solrSpeedTestTitle() {

		String[] urls = new String[]{
			"qt=/suggestTitle&q=title:%s",
		};

		long t = new Date().getTime();
		int i;
		for (i = 0; i<iterations; i++) {
			if (i > words.size()-1) {
				break;
			}
			for (String params : urls) {
				String url = baseUrl + params + words.get(i);
				// System.out.println(url);
				getWebContent(url);
			}
		}
		long time = (new Date().getTime() - t);
		System.out.println("[title] took " + (time/cents));
	}

	// TODO: find a way to make controller tests working. This is not working right now.
	// @Test
	public void suggestionControllerTest() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		// applicationContext = 
		applicationContext = new ClassPathXmlApplicationContext(
				new String[]{"/servlet/portal2-mvc.xml", "/internal/portal2-development.xml"});
		handlerAdapter = applicationContext.getBean(HttpRequestHandlerAdapter.class);
		controller = new SuggestionController();
		request.addParameter("term", "paris");
		request.addParameter("size", "10");
		request.setRequestURI("/suggestion.json");
		try {
			final ModelAndView mav = handlerAdapter.handle(request, response, controller);
			assertNotNull(mav);
			assertTrue(mav.getModelMap().containsKey("results"));
			List<String> results = (List<String>)mav.getModelMap().get("results");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			fail(e1.getMessage());
			e1.printStackTrace();
		}
	}

	private void getWebContent(String _url) {
		URL url;
		InputStream is = null;
		DataInputStream dis;
		String line;

		try {
			url = new URL(_url);
			is = url.openStream();  // throws an IOException
			dis = new DataInputStream(new BufferedInputStream(is));

			while ((line = dis.readLine()) != null) {
				// System.out.println(line);
			}
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException ioe) {
				// nothing to see here
			}
		}
	}

	private String[] getWords() {
		String text = "Toutes nos notions, toute la science, toute la vie pratique elle-même sont fondées sur la représentation"
				+ " que nous nous faisons des aspects successifs des choses. Notre esprit, aidé par nos sens, classe avant tout"
				+ " celles-ci dans le temps et dans l'espace, qui sont les deux cadres où nous fixons d'abord ce qui nous est"
				+ " sensible dans le monde extérieur. Écrivons-nous une lettre: nous mettons en suscription le lieu et la date."
				+ " Ouvrons-nous un journal: ce sont ces indications qui y précèdent toutes les dépêches. Il en est de même en"
				+ " tout et pour tout. Le temps et l'espace, la situation des choses et leur époque apparaissent ainsi comme les"
				+ " piliers jumeaux de toute connaissance, les deux colonnes sur lesquelles repose l'édifice de l'entendement humain."
				+ " Leconte de Lisle l'a bien senti, lorsque avec sa profonde et philosophique intelligence il écrivait, "
				+ " s'adressant pathétiquement à la divine mort:"
				+ " Délivre-nous du temps, du nombre et de l'espace Et rends-nous le repos que la vie a troublé. Le nombre"
				+ " n'est ici que pour définir quantitativement le temps et l'espace, et Leconte de Lisle a bien exprimé,"
				+ " dans ces vers magnifiques et célèbres, que ce qui existe pour nous dans le vaste monde, ce que nous y savons,"
				+ " voyons, tout l'ineffable et trouble écoulement des phénomènes ne nous présente un aspect défini, une forme"
				+ " précise qu'après avoir traversé ces deux filtres superposés que notre entendement interpose: le temps et"
				+ " l'espace."
				+ " Ce qui donne aux travaux d'Einstein leur importance, c'est qu'il a montré, comme nous allons voir, que l'idée que nous nous faisions du temps et de l'espace doit être complètement revisée. Si cela est, la science tout entière,—et avec elle la psychologie,—doit être refondue. Telle est la première partie de l'œuvre d'Einstein. Mais là ne s'est pas bornée l'action de son profond génie. Si elle n'était que cela, elle n'eût été que négative."
				+ " Après avoir démoli, après avoir déblayé nos connaissances de ce qu'on croyait en être le piédestal inébranlable et qui n'était, selon lui, qu'un échafaudage fragile masquant les harmonieuses proportions de l'édifice, il a reconstruit. Il a creusé dans le monument de vastes fenêtres qui permettent maintenant de jeter un regard émerveillé sur les trésors qu'il recèle. En un mot, Einstein a d'une part montré, avec une acuité et une profondeur étonnantes, que la base de nos connaissances semble n'être pas ce qu'on a cru et doit être refaite avec un nouveau ciment. D'autre part, il a, sur cette base, renouvelé, rebâti l'édifice démoli dans ses fondements mêmes, et lui a donné une forme hardie dont la beauté et l'unité sont grandioses."
				+ " Il me reste maintenant à tâcher de préciser, d'une manière concrète et aussi exacte que possible, ces généralités. Mais je dois insister d'abord sur un point qui a une signification considérable: si Einstein s'était borné à la première partie de son œuvre,—telle que je viens de l'esquisser,—celle qui ébranle les notions classiques de temps et d'espace, il n'aurait point, dans le monde de la pensée, la gloire qui, dès aujourd'hui, auréole son nom."
				+ " La chose est d'importance, car la plupart de ceux qui,—en dehors des spécialistes purs,—ont écrit sur Einstein, ont insisté surtout, et souvent exclusivement, sur ce côté en quelque sorte «démolisseur» de son intervention. Or, on va voir qu'à ce point de vue, Einstein n'a pas été le premier ni le seul. Il n'a fait qu'aiguiser davantage et enfoncer un peu plus, entre les blocs mal joints de la science classique, le burin que d'autres avant lui, et surtout le grand Henri Poincaré, y avaient dès longtemps porté. Ensuite il me restera à expliquer, si je puis, le grand, l'immortel titre d'Einstein à la reconnaissance des hommes, qui est, sur cette œuvre critique, d'avoir reconstruit, réédifié par ses propres forces quelque chose de magnifique et de neuf: et ici, sa gloire est sans partage.";
		text = text.replaceAll("—", " ").replaceAll("-", " ").replace(".", " ")
				.replaceAll(",", " ").replaceAll(":", " ").replace(" ", " ")
				.replaceAll("d'", " ").replaceAll("l'", " ").replaceAll("s'", " ")
				.replaceAll("  ", " ")
				;
		String[] words = text.split("\\s+");
		return words;
	}

	private List<String> randomWords(int count) {
		
		List<String> queries = null;
		try {
			queries = FileUtils.readLines(new File("/home/peterkiraly/words.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> random = new ArrayList<String>();
		String query;
		long size = 0;
		while (random.size() < count) {
			int startRecord = (int) (Math.random()* queries.size());
			query = queries.get(startRecord).trim().replaceAll("[\"'()]", "").replace("/", "\\/");
			if (query.length() >= 3 && !random.contains(query)) {
				size += query.length();
				random.add(query);
			}
		}
		System.out.println("avarage size: " + ((float) size / random.size()));

		return random;
	}

	private String[] topWords() {
		return new String[]{"http", "th", "centuri", "века", "го", "of", "www", "eu", "right", "europeana", "rr", "the", "de", "20", "f", "e", "httpwwweuropeanaeurightsrrf", "org", "1", "0", "creativecommon", "earli", "siècl", "начало", "quarter", "quart", "я", "четверть", "10", "imag", "a", "publicdomain", "foto", "3", "4", "mark", "in", "text", "httpcreativecommonsorgpublicdomainmark", "en", "fotograf", "bild", "document", "dokument", "19", "documentazion", "photograph", "dokumentasjon", "photo", "dokumentacja", "documentati", "la", "dokumentálá", "documentación", "τεκμηρίωση", "documentação", "dokumentácia", "dokumentacija", "документация", "documentaţi", "asiakirja", "aineisto", "dokumentatsioon", "dokumentointi", "asiakirjaaineisto", "dokumentac", "документиране", "dokumentazio", "dokümantasyon", "dokumentazzjoni", "dokumentācija", "dokumentatz", "fotografi", "fotografija", "fotografia", "fotografía", "φωτογραφία", "фотография", "immagin", "nuotrauka", "fénykép", "снимка", "imagem", "изображение", "valokuva", "фотографско", "2", "o", "21", "by", "s", "p", "an", "d", "public", "bilimi", "par", "stillimag", "scienc", "franc", "i", "l", "tal", "за", "ciência", "vedi", "scienz", "moksla", "humain", "xjenza", "zinātn", "επιστήμες", "vede", "human", "ştiinţă", "о", "zientziak", "spa", "videnskab", "vědi", "науки", "umană", "човека", "tieteet", "uman", "insan", "nauki", "społeczn", "humaniora", "humana", "giza", "žmogaus", "humanistisk", "cilvēku", "bniedem", "talbniedem", "humanidad", "společenské", "človeku", "человеке", "menswetenschappen", "ανθρωπιστικές", "människovetenskap", "humánn", "humántudománi", "humanistiset", "inimteadus", "humanwissenschaften", "des", "still", "licens", "nation", "et", "r", "30", "der", "01", "na", "deutsch", "domain", "republ", "und", "for", "04", "mid", "physic", "02", "середина", "du", "n", "httpwwweuropeanaeurightsrrp", "h", "and", "from", "y", "print", "national", "bibliothèqu", "materi", "fre", "art", "españa", "sa", "18", "inform", "nr", "número", "standort", "aufbewahrung", "aufbewahrungstandort", "record", "pari", "languag", "dede", "imprimé", "11", "proprietà", "12", "og", "für", "number", "nederland", "di", "architectur", "yn", "late", "it", "yr", "van", "cultura", "object", "voor", "конец", "archéologi", "archaeolog", "archiv", "deutschland", "architektur", "museum", "germani", "type", "kingdom", "librari", "monograph", "1911", "singl", "arkeolog", "product", "hous", "del", "archäologi", "feder", "archeologia", "arheologi", "arqueología", "httpcreativecommonsorglicensesbysa", "archeologi", "arqueologia", "régészet", "αρχαιολογία", "arkæolog", "arkeologia", "археология", "arheologija", "arheoloogia", "archeológia", "archeologija", "архиология", "no", "to", "ireland", "objekt", "httpwwweuropeanaeurightsrrr", "serial", "den", "año", "série", "date", "2004", "allemagn", "duitsland", "niemci", "alemania", "germania", "tyskland", "bundesrepublik", "saksa", "nemecko", "saksamaa", "германия", "németország", "γερμανία", "alemanha", "jerman", "německo", "nemčija", "vokietija", "німеччина", "duitschland", "physicalobject", "építészet", "item", "kulturhistoria", "von", "17", "resid", "герман", "vācija", "alemanya", "almanya", "alemaña", "nemačka", "almayn", "germanio", "njemačka", "germània", "giriman", "jarmal", "siaman", "gjermani", "jámánì", "dútslân", "gjermania", "jėrman", "alamagn", "немачка", "almaen", "нямеччына", "германи", "alimaniya", "ġermanja", "tôitšhi", "германија", "германія", "алмания", "udachi", "alimanya", "ӂермания", "almaniya", "ghearmailt", "däitschland", "ghermaan", "олмония", "olmoniya", "đức", "týskland", "ujerumani", "deutän", "tiamana", "ghearmáin", "олмон", "germanujo", "duiska", "jarmalka", "ijalimani", "duutsjlandj", "heremani", "þýskaland", "alémani", "census", "duiskka", "alémanyi", "éireann", "héireann", "19110402", "cartlann", "náisiúnta", "département", "el", "with", "m", "bildarchiv", "architektura", "architettura", "het", "sn", "05", "v", "arquitectura", "2005", "arkitektur", "cultur", "αρχιτεκτονική", "architectuur", "arhitectură", "architektūra", "arhitektura", "architektúra", "архитектура", "arhitektuur", "arkkitehtuuri", "fysisk", "provid", "not", "digit", "druck", "w", "c", "16", "carta", "06", "le", "madrid", "j", "ou", "kunstgeschicht", "digital", "nl", "projekt", "cart", "marburg", "herstellung", "dokumentationszentrum", "zero", "propiedad", "is", "httpcreativecommonsorgpublicdomainzero", "okänd", "03", "09", "nc", "this", "specimen", "08", "à", "druk", "física", "physiqu", "geografia", "φυσικές", "properti", "fyzikální", "5", "fizică", "physikalisch", "common", "15", "pain", "fizikai", "fisiko", "füüsikalin", "fiziksel", "fysisch", "fysikaliska", "07", "propriété", "kenngröß", "fiżika", "trykk", "fisich", "av", "физическо", "качество", "fizikalna", "fyzikálna", "fizikāla", "egenskap", "propriedad", "propriet", "fizyczna", "ιδιότητες", "omadus", "egenskab", "savybė", "ominaisuudet", "właściwość", "fizinė"};
	}
}
