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
		String text = "Toutes nos notions, toute la science, toute la vie pratique elle-meme sont fondees sur la representation"
				+ " que nous nous faisons des aspects successifs des choses. Notre esprit, aide par nos sens, classe avant tout"
				+ " celles-ci dans le temps et dans l'espace, qui sont les deux cadres où nous fixons d'abord ce qui nous est"
				+ " sensible dans le monde exterieur. ecrivons-nous une lettre: nous mettons en suscription le lieu et la date."
				+ " Ouvrons-nous un journal: ce sont ces indications qui y precedent toutes les depeches. Il en est de meme en"
				+ " tout et pour tout. Le temps et l'espace, la situation des choses et leur epoque apparaissent ainsi comme les"
				+ " piliers jumeaux de toute connaissance, les deux colonnes sur lesquelles repose l'edifice de l'entendement humain."
				+ " Leconte de Lisle l'a bien senti, lorsque avec sa profonde et philosophique intelligence il ecrivait, "
				+ " s'adressant pathetiquement a la divine mort:"
				+ " Delivre-nous du temps, du nombre et de l'espace Et rends-nous le repos que la vie a trouble. Le nombre"
				+ " n'est ici que pour definir quantitativement le temps et l'espace, et Leconte de Lisle a bien exprime,"
				+ " dans ces vers magnifiques et celebres, que ce qui existe pour nous dans le vaste monde, ce que nous y savons,"
				+ " voyons, tout l'ineffable et trouble ecoulement des phenomenes ne nous presente un aspect defini, une forme"
				+ " precise qu'apres avoir traverse ces deux filtres superposes que notre entendement interpose: le temps et"
				+ " l'espace."
				+ " Ce qui donne aux travaux d'Einstein leur importance, c'est qu'il a montre, comme nous allons voir, que l'idee que nous nous faisions du temps et de l'espace doit etre completement revisee. Si cela est, la science tout entiere,-et avec elle la psychologie,-doit etre refondue. Telle est la premiere partie de l'oeuvre d'Einstein. Mais la ne s'est pas bornee l'action de son profond genie. Si elle n'etait que cela, elle n'eut ete que negative."
				+ " Apres avoir demoli, apres avoir deblaye nos connaissances de ce qu'on croyait en etre le piedestal inebranlable et qui n'etait, selon lui, qu'un echafaudage fragile masquant les harmonieuses proportions de l'edifice, il a reconstruit. Il a creuse dans le monument de vastes fenetres qui permettent maintenant de jeter un regard emerveille sur les tresors qu'il recele. En un mot, Einstein a d'une part montre, avec une acuite et une profondeur etonnantes, que la base de nos connaissances semble n'etre pas ce qu'on a cru et doit etre refaite avec un nouveau ciment. D'autre part, il a, sur cette base, renouvele, rebâti l'edifice demoli dans ses fondements memes, et lui a donne une forme hardie dont la beaute et l'unite sont grandioses."
				+ " Il me reste maintenant a tâcher de preciser, d'une maniere concrete et aussi exacte que possible, ces generalites. Mais je dois insister d'abord sur un point qui a une signification considerable: si Einstein s'etait borne a la premiere partie de son oeuvre,-telle que je viens de l'esquisser,-celle qui ebranle les notions classiques de temps et d'espace, il n'aurait point, dans le monde de la pensee, la gloire qui, des aujourd'hui, aureole son nom."
				+ " La chose est d'importance, car la plupart de ceux qui,-en dehors des specialistes purs,-ont ecrit sur Einstein, ont insiste surtout, et souvent exclusivement, sur ce cote en quelque sorte demolisseur de son intervention. Or, on va voir qu'a ce point de vue, Einstein n'a pas ete le premier ni le seul. Il n'a fait qu'aiguiser davantage et enfoncer un peu plus, entre les blocs mal joints de la science classique, le burin que d'autres avant lui, et surtout le grand Henri Poincare, y avaient des longtemps porte. Ensuite il me restera a expliquer, si je puis, le grand, l'immortel titre d'Einstein a la reconnaissance des hommes, qui est, sur cette oeuvre critique, d'avoir reconstruit, reedifie par ses propres forces quelque chose de magnifique et de neuf: et ici, sa gloire est sans partage.";
		text = text.replaceAll("-", " ").replaceAll("-", " ").replace(".", " ")
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
		return new String[]{"http", "th", "centuri", "го", "of", "www", "eu", "right", "europeana", "rr", "the", "de", "20", "f", 
			"e", "httpwwweuropeanaeurightsrrf", "org", "1", "0", "creativecommon", "earli", "siecl", "quarter", "quart", 
			"10", "imag", "a", "publicdomain", "foto", "3", "4", "mark", "in", "text", "httpcreativecommonsorgpublicdomainmark", 
			"en", "fotograf", "bild", "document", "dokument", "19", "documentazion", "photograph", "dokumentasjon", "photo", "dokumentacja", 
			"documentati", "la", "dokumentala", "documentacion", "documentacao", "dokumentacia", "dokumentacija", 
			"documentati", "asiakirja", "aineisto", "dokumentatsioon", "dokumentointi", "asiakirjaaineisto", "dokumentac", 
			"dokumentazio", "dokumantasyon", "dokumentazzjoni", "dokumentacija", "dokumentatz", "fotografi", "fotografija", "fotografia", 
			"fotografia", "immagin", "nuotrauka", "fenykep", "imagem", "valokuva",
			"2", "o", "21", "by", "s", "p", "an", "d", "public", "bilimi", "par", "stillimag", "scienc", "franc", "i", "l", 
			"tal", "ciencia", "vedi", "scienz", "moksla", "humain", "xjenza", "zinatn", "vede", "human", "stiinta", "о",
			"zientziak", "spa", "videnskab", "vedi", "umana", "tieteet", "uman", "insan", "nauki", "spoleczn", "humaniora",
			"humana", "giza", "humanistisk", "cilveku", "bniedem", "talbniedem", "humanidad", "spolecenske", "cloveku", 
			"menswetenschappen", "manniskovetenskap", "humann", "humantudomani", "humanistiset", "inimteadus", 
			"humanwissenschaften", "des", "still", "licens", "nation", "et", "r", "30", "der", "01", "na", "deutsch", "domain", "republ", 
			"und", "for", "04", "mid", "physic", "02", "du", "n", "httpwwweuropeanaeurightsrrp", "h", "and", "from", "y", 
			"print", "national", "bibliothequ", "materi", "fre", "art", "espana", "sa", "18", "inform", "nr", "número", "standort", 
			"aufbewahrung", "aufbewahrungstandort", "record", "pari", "languag", "dede", "imprime", "11", "proprieta", "12", "og", "fur", 
			"number", "nederland", "di", "architectur", "yn", "late", "it", "yr", "van", "cultura", "object", "voor", "archeologi",
			"archaeolog", "archiv", "deutschland", "architektur", "museum", "germani", "type", "kingdom", "librari", "monograph", "1911", 
			"singl", "arkeolog", "product", "hous", "del", "archaologi", "feder", "archeologia", "arheologi", "arqueologia", 
			"httpcreativecommonsorglicensesbysa", "archeologi", "arqueologia", "regeszet", "arkaeolog", "arkeologia", 
			"arheologija", "arheoloogia", "archeologia", "archeologija", "no", "to", "ireland", "objekt", 
			"httpwwweuropeanaeurightsrrr", "serial", "den", "ano", "serie", "date", "2004", "allemagn", "duitsland", "niemci", "alemania", 
			"germania", "tyskland", "bundesrepublik", "saksa", "nemecko", "saksamaa", "nemetorszag", "alemanha", 
			"jerman", "nemecko", "nemcija", "vokietija", "duitschland", "physicalobject", "epiteszet", "item", "kulturhistoria", 
			"von", "17", "resid", "vacija", "alemanya", "almanya", "alemana", "nemacka", "almayn", "germanio", "njemacka", 
			"germania", "giriman", "jarmal", "siaman", "gjermani", "jamanì", "dútslân", "gjermania", "jerman", "alamagn", "almaen", 
			"alimaniya", "germanja", "toitshi", "udachi", "alimanya",
			"almaniya", "ghearmailt", "daitschland", "ghermaan", "olmoniya", "duc", "tyskland", "ujerumani", "deutan",
			"tiamana", "ghearmain", "germanujo", "duiska", "jarmalka", "ijalimani", "duutsjlandj", "heremani", "þyskaland", "alemani", 
			"census", "duiskka", "alemanyi", "eireann", "heireann", "19110402", "cartlann", "naisiúnta", "departement", "el", "with", "m", 
			"bildarchiv", "architektura", "architettura", "het", "sn", "05", "v", "arquitectura", "2005", "arkitektur", "cultur", 
			"architectuur", "arhitectura", "architektūra", "arhitektura", "architektúra", "arhitektuur", "arkkitehtuuri", "fysisk", 
			"provid", "not", "digit", "druck", "w", "c", "16", "carta", "06", "le", "madrid", "j", "ou", "kunstgeschicht", "digital", "nl", 
			"projekt", "cart", "marburg", "herstellung", "dokumentationszentrum", "zero", "propiedad", "is", 
			"httpcreativecommonsorgpublicdomainzero", "okand", "03", "09", "nc", "this", "specimen", "08", "a", "druk", "fisica", "physiqu",
			"geografia", "properti", "fyzikalni", "5", "fizica", "physikalisch", "common", "15", "pain", "fizikai", "fisiko", 
			"fuusikalin", "fiziksel", "fysisch", "fysikaliska", "07", "propriete", "kenngross", "fizika", "trykk", "fisich", "av", 
			"fizikalna", "fyzikalna", "fizikala", "egenskap", "propriedad", "propriet", "fizyczna",  
			"omadus", "egenskab", "savybe", "ominaisuudet", "wlasciwosc", "fizine"};
	}
}
