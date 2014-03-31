package eu.europeana.portal2.web.presentation.semantic;

import java.util.HashMap;
import java.util.Map;

/**
 * Resolves name spaces, and provides some utility functions.
 * 
 * @author peter.kiraly@kb.nl
 */
public class NamespaceResolver {

	private NamespaceResolver() {}

	protected static final Map<String, Namespace> namespaces = new HashMap<String, Namespace>();
	static {
		namespaces.put("dc", new Namespace("dc", "http://purl.org/dc/elements/1.1/"));
		namespaces.put("dcterms", new Namespace("dcterms", "http://purl.org/dc/terms/"));
		namespaces.put("edm", new Namespace("edm", "http://www.europeana.eu/schemas/edm/"));
		namespaces.put("foaf", new Namespace("foaf", "http://xmlns.com/foaf/0.1"));
		namespaces.put("ore", new Namespace("ore", "http://www.openarchives.org/ore/terms/"));
		namespaces.put("owl", new Namespace("owl", "http://www.w3.org/2002/07/owl#"));
		namespaces.put("rdaGr2", new Namespace("rdaGr2", "http://rdvocab.info/ElementsGr2"));
		namespaces.put("rdf", new Namespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#"));
		namespaces.put("rdfs", new Namespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#"));
		namespaces.put("skos", new Namespace("skos", "http://www.w3.org/2004/02/skos/core#"));
		namespaces.put("wgs84_pos", new Namespace("wgs84_pos", "http://www.w3.org/2003/01/geo/wgs84_pos#"));
		namespaces.put("schema", new Namespace("schema", "http://schema.org/"));
	}
}
