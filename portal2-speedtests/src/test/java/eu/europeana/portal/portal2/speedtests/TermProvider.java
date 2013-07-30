package eu.europeana.portal.portal2.speedtests;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class TermProvider {

	public static List<String> getRandomWords(int count) {

		List<String> queries = null;
		try {
			queries = FileUtils.readLines(new File("/home/peterkiraly/words.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<String> random = new ArrayList<String>();
		String query;
		while (random.size() < count) {
			int startRecord = (int) (Math.random()* queries.size());
			query = queries.get(startRecord).trim().replaceAll("[\"'()]", "").replace("/", "\\/");
			if (query.length() >= 3 && !random.contains(query)) {
				random.add(query);
			}
		}

		return random;
	}

	public static String[] getTopWords() {
		return new String[]{"http", "centuri", "www", "right", "europeana", "the", 
			"httpwwweuropeanaeurightsrrf", "org", "creativecommon", "earli", "siecl", "quarter", "quart", 
			"imag", "publicdomain", "foto", "mark", "text", "httpcreativecommonsorgpublicdomainmark", 
			"fotograf", "bild", "document", "dokument", "documentazion", "photograph", "dokumentasjon", "photo", "dokumentacja", 
			"documentati", "dokumentala", "documentacion", "documentacao", "dokumentacia", "dokumentacija", 
			"documentati", "asiakirja", "aineisto", "dokumentatsioon", "dokumentointi", "asiakirjaaineisto", "dokumentac", 
			"dokumentazio", "dokumantasyon", "dokumentazzjoni", "dokumentacija", "dokumentatz", "fotografi", "fotografija", "fotografia", 
			"fotografia", "immagin", "nuotrauka", "fenykep", "imagem", "valokuva",
			"public", "bilimi", "par", "stillimag", "scienc", "franc", 
			"tal", "ciencia", "vedi", "scienz", "moksla", "humain", "xjenza", "zinatn", "vede", "human", "stiinta",
			"zientziak", "spa", "videnskab", "vedi", "umana", "tieteet", "uman", "insan", "nauki", "spoleczn", "humaniora",
			"humana", "giza", "humanistisk", "cilveku", "bniedem", "talbniedem", "humanidad", "spolecenske", "cloveku", 
			"menswetenschappen", "manniskovetenskap", "humann", "humantudomani", "humanistiset", "inimteadus", 
			"humanwissenschaften", "des", "still", "licens", "nation", "der", "deutsch", "domain", "republ", 
			"und", "for", "mid", "physic", "httpwwweuropeanaeurightsrrp", "and", "from",  
			"print", "national", "bibliothequ", "materi", "fre", "art", "espana", "inform", "numero", "standort", 
			"aufbewahrung", "aufbewahrungstandort", "record", "pari", "languag", "dede", "imprime", "proprieta", "fur", 
			"number", "nederland", "architectur", "late", "van", "cultura", "object", "voor", "archeologi",
			"archaeolog", "archiv", "deutschland", "architektur", "museum", "germani", "type", "kingdom", "librari", "monograph", "1911", 
			"singl", "arkeolog", "product", "hous", "del", "archaologi", "feder", "archeologia", "arheologi", "arqueologia", 
			"httpcreativecommonsorglicensesbysa", "archeologi", "arqueologia", "regeszet", "arkaeolog", "arkeologia", 
			"arheologija", "arheoloogia", "archeologia", "archeologija", "ireland", "objekt", 
			"httpwwweuropeanaeurightsrrr", "serial", "den", "ano", "serie", "date", "2004", "allemagn", "duitsland", "niemci", "alemania", 
			"germania", "tyskland", "bundesrepublik", "saksa", "nemecko", "saksamaa", "nemetorszag", "alemanha", 
			"jerman", "nemecko", "nemcija", "vokietija", "duitschland", "physicalobject", "epiteszet", "item", "kulturhistoria", 
			"von", "resid", "vacija", "alemanya", "almanya", "alemana", "nemacka", "almayn", "germanio", "njemacka", 
			"germania", "giriman", "jarmal", "siaman", "gjermani", "jamani", "dutslan", "gjermania", "jerman", "alamagn", "almaen", 
			"alimaniya", "germanja", "toitshi", "udachi", "alimanya",
			"almaniya", "ghearmailt", "daitschland", "ghermaan", "olmoniya", "duc", "tyskland", "ujerumani", "deutan",
			"tiamana", "ghearmain", "germanujo", "duiska", "jarmalka", "ijalimani", "duutsjlandj", "heremani", "alemani", 
			"census", "duiskka", "alemanyi", "eireann", "heireann", "19110402", "cartlann", "naisiunta", "departement", "with",
			"bildarchiv", "architektura", "architettura", "het", "arquitectura", "2005", "arkitektur", "cultur", 
			"architectuur", "arhitectura", "architektura", "arhitektura", "architektura", "arhitektuur", "arkkitehtuuri", "fysisk", 
			"provid", "not", "digit", "druck", "carta", "madrid", "kunstgeschicht", "digital", 
			"projekt", "cart", "marburg", "herstellung", "dokumentationszentrum", "zero", "propiedad", 
			"httpcreativecommonsorgpublicdomainzero", "okand", "this", "specimen", "druk", "fisica", "physiqu",
			"geografia", "properti", "fyzikalni", "fizica", "physikalisch", "common", "pain", "fizikai", "fisiko", 
			"fuusikalin", "fiziksel", "fysisch", "fysikaliska", "propriete", "kenngross", "fizika", "trykk", "fisich", 
			"fizikalna", "fyzikalna", "fizikala", "egenskap", "propriedad", "propriet", "fizyczna",  
			"omadus", "egenskab", "savybe", "ominaisuudet", "wlasciwosc", "fizine"};
	}

	public static String[] getFrenchWords() {
		String text = "Toutes nos notions, toute la science, toute la vie pratique elle-meme sont fondees sur la representation"
				+ " que nous nous faisons des aspects successifs des choses. Notre esprit, aide par nos sens, classe avant tout"
				+ " celles-ci dans le temps et dans l'espace, qui sont les deux cadres ou nous fixons d'abord ce qui nous est"
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
				+ " Apres avoir demoli, apres avoir deblaye nos connaissances de ce qu'on croyait en etre le piedestal inebranlable et qui n'etait, selon lui, qu'un echafaudage fragile masquant les harmonieuses proportions de l'edifice, il a reconstruit. Il a creuse dans le monument de vastes fenetres qui permettent maintenant de jeter un regard emerveille sur les tresors qu'il recele. En un mot, Einstein a d'une part montre, avec une acuite et une profondeur etonnantes, que la base de nos connaissances semble n'etre pas ce qu'on a cru et doit etre refaite avec un nouveau ciment. D'autre part, il a, sur cette base, renouvele, rebati l'edifice demoli dans ses fondements memes, et lui a donne une forme hardie dont la beaute et l'unite sont grandioses."
				+ " Il me reste maintenant a tacher de preciser, d'une maniere concrete et aussi exacte que possible, ces generalites. Mais je dois insister d'abord sur un point qui a une signification considerable: si Einstein s'etait borne a la premiere partie de son oeuvre,-telle que je viens de l'esquisser,-celle qui ebranle les notions classiques de temps et d'espace, il n'aurait point, dans le monde de la pensee, la gloire qui, des aujourd'hui, aureole son nom."
				+ " La chose est d'importance, car la plupart de ceux qui,-en dehors des specialistes purs,-ont ecrit sur Einstein, ont insiste surtout, et souvent exclusivement, sur ce cote en quelque sorte demolisseur de son intervention. Or, on va voir qu'a ce point de vue, Einstein n'a pas ete le premier ni le seul. Il n'a fait qu'aiguiser davantage et enfoncer un peu plus, entre les blocs mal joints de la science classique, le burin que d'autres avant lui, et surtout le grand Henri Poincare, y avaient des longtemps porte. Ensuite il me restera a expliquer, si je puis, le grand, l'immortel titre d'Einstein a la reconnaissance des hommes, qui est, sur cette oeuvre critique, d'avoir reconstruit, reedifie par ses propres forces quelque chose de magnifique et de neuf: et ici, sa gloire est sans partage.";
		text = text.replaceAll("-", " ").replaceAll("-", " ").replace(".", " ")
				.replaceAll(",", " ").replaceAll(":", " ").replace(" ", " ")
				.replaceAll("d'", " ").replaceAll("l'", " ").replaceAll("s'", " ")
				.replaceAll("  ", " ")
				;
		String[] words = text.split("\\s+");
		return words;
	}

}
