package eu.europeana.portal2.web.presentation.semantic;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Mapping of EDM elements and schema.org elements
 *
 * @author peter.kiraly@kb.nl
 */
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

	public static void initialize(String filename) {
		edm2schemaOrg = new HashMap<String, SchemaOrgElement>();
		Map<String, SchemaOrgElement> mapping = readFromProperty(filename);
		for (Map.Entry<String, SchemaOrgElement> entry : mapping.entrySet()) {
			// TODO: add more features to SchemaOrgElement later, like attributes, parents etc.
			edm2schemaOrg.put(entry.getKey(), entry.getValue()); //new SchemaOrgElement(entry.getValue()));
		}
	}

	private static Map<String, SchemaOrgElement> readFromProperty(String file) {
		Map<String, SchemaOrgElement> mapping = new HashMap<String, SchemaOrgElement>();
		DataInputStream in = null;
		BufferedReader br = null;
		try {
			in = new DataInputStream(new FileInputStream(file));
			br = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = br.readLine()) != null) {
				if (isSkippable(line)) {
					continue;
				}
				String[] parts = line.split("\\s?=\\s?");
				String edm = parts[0];
				String schema = parts[1];

				String[] edmParts = edm.split("/", 2);
				// TODO: handle edmParentName
				String edmParentName = edmParts[0];
				String edmElementName = edmParts[1];

				SchemaOrgElement schemaElement;
				if (schema.indexOf("/") == -1) {
					schemaElement = new SchemaOrgElement(schema);
				} else {
					String[] schemaParts = schema.split("/", 2);
					String schemaParent = schemaParts[0];
					String schemaElementName = schemaParts[1];
					schemaElement = new SchemaOrgElement(schemaElementName, new String[]{schemaParent});
				}
				mapping.put(edmElementName, schemaElement);
			}
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return mapping;
	}

	private static boolean isSkippable(String line) {
		if (line.equals("")) {
			return true;
		} else if (line.indexOf("#") == 0) {
			return true;
		} else if (line.indexOf("=") == -1) {
			return true;
		}
		return false;
	}
}
