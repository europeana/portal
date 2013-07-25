package eu.europeana.portal2.web.presentation.semantic;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Resolves name spaces, and provides some utility functions.
 * 
 * @author peter.kiraly@kb.nl
 */
public class NamespaceResolver {
	
	private final static Logger log = LoggerFactory.getLogger(NamespaceResolver.class);

	private static final Pattern NAME_PATTERN = Pattern.compile("^@?([^:]+):([^:]+)$");

	private static Map<String, Namespace> namespaces = new HashMap<String, Namespace>(){
		private static final long serialVersionUID = 1L;
		{
			put("dc", new Namespace("dc", "http://purl.org/dc/elements/1.1/"));
			put("dcterms", new Namespace("dcterms", "http://purl.org/dc/terms/"));
			put("edm", new Namespace("edm", "http://www.europeana.eu/schemas/edm/"));
			put("foaf", new Namespace("foaf", "http://xmlns.com/foaf/0.1"));
			put("ore", new Namespace("ore", "http://www.openarchives.org/ore/terms/"));
			put("owl", new Namespace("owl", "http://www.w3.org/2002/07/owl#"));
			put("rdaGr2", new Namespace("rdaGr2", "http://rdvocab.info/ElementsGr2"));
			put("rdf", new Namespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"));
			put("rdfs", new Namespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#"));
			put("skos", new Namespace("skos", "http://www.w3.org/2004/02/skos/core#"));
			put("wgs84_pos", new Namespace("wgs84_pos", "http://www.w3.org/2003/01/geo/wgs84_pos#"));
			put("schema", new Namespace("schema", "http://schema.org/"));
		}
	};

	/**
	 * Return an element object belongs to an XML element
	 * @param element
	 * @return
	 */
	public static Element createElement(String element) {
		Matcher nameMatcher = NAME_PATTERN.matcher(element);
		if (nameMatcher.find()) {
			String prefix = nameMatcher.group(1);
			String elementName = nameMatcher.group(2);
			if (namespaces.containsKey(prefix)) {
				return new Element(namespaces.get(prefix), elementName);
			} else {
				log.warn(String.format("Unregistered namespace prefix: %s", prefix));
			}
		}
		else {
			// log.severe(String.format("Element %s did not match the pattern %s", element, NAME_PATTERN.pattern()));
		}
		return null;
	}
}
