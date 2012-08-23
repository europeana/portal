package eu.europeana.portal2.web.presentation.semantic;

import java.util.HashMap;
import java.util.Map;

public class SchemaOrgMapping {

	private static Map<String, SchemaOrgElement> edm2schemaOrg;
	
	private static void initialize() {
		edm2schemaOrg = new HashMap<String, SchemaOrgElement>(){
			private static final long serialVersionUID = 1L; {
			put("edm:country", new SchemaOrgElement("schema:addressCountry", new String[]{"schema:Thing"}));
			put("edm:dataProvider", new SchemaOrgElement("schema:provider", new String[]{"schema:CreativeWork"}));
			put("edm:hasView", new SchemaOrgElement("schema:url", new String[]{"schema:MediaObject"}));
			put("edm:isShownAt", new SchemaOrgElement("schema:url", new String[]{"schema:WebPage"}));
			put("edm:isShownBy", new SchemaOrgElement("schema:url", new String[]{"schema:MediaObject"}));
			put("edm:landingPage", new SchemaOrgElement("schema:url", new String[]{"schema:Thing"}));
			put("edm:language", new SchemaOrgElement("schema:inLanguage", new String[]{"schema:CreativeWork"}));
			put("edm:object", new SchemaOrgElement("schema:url", new String[]{"schema:MediaObject"}));
			put("edm:provider", new SchemaOrgElement("schema:provider", new String[]{"schema:CreativeWork"}));
			put("edm:currentLocation", new SchemaOrgElement("schema:contentLocation", new String[]{"schema:CreativeWork"}));
			put("edm:hasType", new SchemaOrgElement("schema:additionalType", new String[]{"schema:Thing"}));
			put("edm:isRepresentationOf", new SchemaOrgElement("schema:about", new String[]{"schema:CreativeWork"}));
			put("edm:userTag", new SchemaOrgElement("schema:keywords", new String[]{"schema:CreativeWork"}));
			put("skos:prefLabel", new SchemaOrgElement("schema:name", new String[]{"schema:Person"}));
			put("skos:altLabel", new SchemaOrgElement("schema:additionalName", new String[]{"schema:Person"}));
			put("skos:hiddenLabel", new SchemaOrgElement("schema:additionalName", new String[]{"schema:Person"}));
			put("skos:note", new SchemaOrgElement("schema:description", new String[]{"schema:Thing"}));
			put("edm:begin", new SchemaOrgElement("schema:birthdate", new String[]{"schema:Person"}));
			put("edm:end", new SchemaOrgElement("schema:deathdate", new String[]{"schema:Person"}));
			put("edm:hasMet", new SchemaOrgElement("schema:knows", new String[]{"schema:Person"}));
			put("edm:isRelatedTo", new SchemaOrgElement("schema:relatedTo", new String[]{"schema:Person"}));
			put("edm:wasPresentAt", new SchemaOrgElement("schema:event", new String[]{"schema:Organization"}));
			put("foaf:name", new SchemaOrgElement("schema:name", new String[]{"schema:Person"}));
			put("foaf:name", new SchemaOrgElement("schema:name", new String[]{"schema:Person"}));
			put("rdaGr2:biographicalInformation", new SchemaOrgElement("schema:description", new String[]{"schema:Thing"}));
			put("rdaGr2:dateOfBirth", new SchemaOrgElement("schema:birthdate", new String[]{"schema:Person"}));
			put("rdaGr2:dateOfDeath", new SchemaOrgElement("schema:deathdate", new String[]{"schema:Person"}));
			put("rdaGr2:dateOfEstablishment", new SchemaOrgElement("schema:foundingDate", new String[]{"schema:Organization"}));
			put("rdaGr2:gender", new SchemaOrgElement("schema:gender", new String[]{"schema:Person"}));
			put("rdaGr2:professionOrOccupation", new SchemaOrgElement("schema:jobTitle", new String[]{"schema:Person"}));
			put("wgs84_pos:lat", new SchemaOrgElement("schema:latitude", new String[]{"schema:Intangible"}));
			put("wgs84_pos:long", new SchemaOrgElement("schema:longitude", new String[]{"schema:Intangible"}));
			put("wgs84_pos:alt", new SchemaOrgElement("schema:elevation", new String[]{"schema:Intangible"}));
			put("skos:prefLabel", new SchemaOrgElement("schema:name", new String[]{""}));
			put("skos:altLabel", new SchemaOrgElement("schema:name", new String[]{""}));
			put("skos:hiddenLabel", new SchemaOrgElement("schema:name", new String[]{""}));
			put("skos:note", new SchemaOrgElement("schema:description", new String[]{"schema:Thing"}));
			put("dcterms:isPartOf", new SchemaOrgElement("schema:containedIn", new String[]{"schema:Place"}));

			// put("", new SchemaOrgElement("", new String[]{""}));
		}};
	}

	public static Map<String, SchemaOrgElement> getMap() {
		if (edm2schemaOrg == null) {
			initialize();
		}
		return edm2schemaOrg;
	}

	public static SchemaOrgElement get(String edmElement) {
		if (edm2schemaOrg == null) {
			initialize();
		}
		if (edm2schemaOrg.containsKey(edmElement)) {
			return edm2schemaOrg.get(edmElement);
		}
		return null;
	}

	public static SchemaOrgElement get(Element edmElement) {
		return get(edmElement.getQualifiedName());
	}
}
