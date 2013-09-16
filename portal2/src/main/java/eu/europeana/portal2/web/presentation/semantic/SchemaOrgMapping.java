package eu.europeana.portal2.web.presentation.semantic;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import eu.europeana.corelib.web.support.Configuration;

/**
 * Mapping of EDM elements and schema.org elements
 * 
 * @author peter.kiraly@kb.nl
 */
public class SchemaOrgMapping {

	private static final String DC_TITLE = "dc:title";
	private static final String DC_PUBLISHER = "dc:publisher";
	
	private Map<String, SchemaOrgElement> edm2schemaOrg;
	private Map<String, String> semanticAttributesMap = new ConcurrentHashMap<String, String>();
	private Map<String, Boolean> semanticUrlMap = new ConcurrentHashMap<String, Boolean>();

	@Autowired
	public SchemaOrgMapping(Configuration config) {
		edm2schemaOrg = readFromProperty(config.getSchemaOrgMappingFile());
	}

	public Map<String, SchemaOrgElement> getMap() {
		return edm2schemaOrg;
	}

	public SchemaOrgElement get(String edmElement) {
		if (edm2schemaOrg.containsKey(edmElement)) {
			return edm2schemaOrg.get(edmElement);
		}
		return null;
	}

	public SchemaOrgElement get(Element edmElement) {
		return get(edmElement.getQualifiedName());
	}

	public void initialize(String filename) {
		edm2schemaOrg = readFromProperty(filename);
		/*
		 * edm2schemaOrg = new ConcurrentHashMap<String, SchemaOrgElement>(); Map<String, SchemaOrgElement> mapping =
		 * readFromProperty(config.getSchemaOrgMappingFile()); for (Map.Entry<String, SchemaOrgElement> entry :
		 * mapping.entrySet()) { // TODO: add more features to SchemaOrgElement later, like attributes, parents etc.
		 * edm2schemaOrg.put(entry.getKey(), entry.getValue()); //new SchemaOrgElement(entry.getValue())); }
		 */
	}

	private Map<String, SchemaOrgElement> readFromProperty(String file) {
		Map<String, SchemaOrgElement> mapping = new ConcurrentHashMap<String, SchemaOrgElement>();
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
				if (edmParentName.equals("edm:ProvidedCHO")) {
					edmParentName = "edm:Proxy";
				}

				SchemaOrgElement schemaElement;
				if (schema.indexOf("/") == -1) {
					schemaElement = new SchemaOrgElement(schema);
				} else {
					String[] schemaParts = schema.split("/", 2);
					String schemaParent = schemaParts[0];
					String schemaElementName = schemaParts[1];
					schemaElement = new SchemaOrgElement(schemaElementName, new String[] { schemaParent },
							edmElementName);
				}
				mapping.put(edmElementName, schemaElement);
				mapping.put(edmParentName + "/" + edmElementName, schemaElement);
			}
		} catch (Exception e) {
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

	public String getSemanticAttributes(String field, String contextualEntity) {
		String schemaKey = field;
		if (!StringUtils.isBlank(contextualEntity)) {
			schemaKey = contextualEntity + '/' + field;
		}

		if (!semanticAttributesMap.containsKey(schemaKey)) {
			Element element = EdmSchemaMapping.getEdmElements(this).get(field);
			if (element == null) {
				semanticAttributesMap.put(schemaKey, "");
			} else {
				String semanticAttributes = element.getFullQualifiedURI();
				if (edm2schemaOrg.containsKey(schemaKey)) {
					SchemaOrgElement elementMapping = edm2schemaOrg.get(schemaKey);
					Element schemaOrgElement = elementMapping.getElement();
					Element edmElement = elementMapping.getEdmElement();
					semanticAttributes = edmElement.getFullQualifiedURI() + " " + schemaOrgElement.getElementName();
				}
				if (field.equals(DC_TITLE) || field.equals(DC_PUBLISHER)) {
					semanticAttributes = semanticAttributes + ' ' + field;
				}
				semanticAttributesMap.put(schemaKey, "property=\"" + semanticAttributes + "\"");
			}
		}

		return semanticAttributesMap.get(schemaKey);
	}

	public Boolean isSemanticUrl(String schemaKey) {
		if (!semanticUrlMap.containsKey(schemaKey)) {
			boolean isSemanticUrl = false;
			if (edm2schemaOrg.containsKey(schemaKey)) {
				isSemanticUrl = edm2schemaOrg.get(schemaKey).isSemanticUrl();
			}
			semanticUrlMap.put(schemaKey, isSemanticUrl);
		}
		return semanticUrlMap.get(schemaKey);
	}

	public Boolean isSemanticUrl(String field, String contextualEntity) {
		String schemaKey = field;
		if (!StringUtils.isBlank(contextualEntity)) {
			schemaKey = contextualEntity + '/' + field;
		}
		return isSemanticUrl(schemaKey);
	}
}
