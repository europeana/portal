package eu.europeana.portal2.web.presentation.semantic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class EdmSchemaMapping {

	protected final static Logger log = Logger.getLogger("EdmSchemaMapping");

	private static final List<String> mapNames = Arrays.asList(
		new String[]{"ProvidedCHO", "WebResource", "Agent", "Place", "Timespan", 
					"Concept", "Aggregation", "Proxy", "EuropeanaAggregation", "FullBean", 
					"BriefBean"}
	);

	private static Map<String, List<FieldInfo>> fieldMap = new LinkedHashMap<String, List<FieldInfo>>();
	private static List<FieldInfo> topLevelTypes;
	private static Map<String, Element> edmElements;

	public static List<String> getMapNames() {
		return mapNames;
	}

	public static Map<String, List<FieldInfo>> getFullMap() {
		for (String mapName : mapNames) {
			getMap(mapName);
		}
		return fieldMap;
	}

	public static Map<String, Element> getEdmElements() {
		if (edmElements == null) {
			edmElements = new HashMap<String, Element>();
			for (String mapName : mapNames) {
				List<FieldInfo> fields = getMap(mapName);
				if (fields != null) {
					for (FieldInfo field : fields) {
						if (field.getElement() != null) {
							edmElements.put(field.getSchemaName(), field.getElement());
						}
					}
				}
			}
		}
		return edmElements;
	}

	public static List<FieldInfo> getTopLevel() {
		if (topLevelTypes == null) {
			topLevelTypes = new LinkedList<FieldInfo>(Arrays.asList(
				new FieldInfo("fullBean", "fullBean", "FullBean"),
				new FieldInfo("edm:ProvidedCHO", "providedCHOs", "ProvidedCHO"),
				new FieldInfo("edm:Agent", "agents", "Agent"),
				new FieldInfo("edm:Place", "places", "Place"),
				new FieldInfo("edm:TimeSpan", "timespans", "TimeSpan"),
				new FieldInfo("skos:Concept", "concepts", "Concept"),
				new FieldInfo("ore:Aggregation", "aggregations", "Aggregation"),
				new FieldInfo("edm:Proxy", "proxies", "Proxy"),
				new FieldInfo("edm:EuropeanaAggregation", "europeanaAggregation", "EuropeanaAggregation"),
				new FieldInfo("edm:WebResource", "webResources", "WebResource"),
				new FieldInfo("briefBean", "briefBean", "BriefBean")
			));
		}
		return topLevelTypes;
	}

	public static List<FieldInfo> getMap(String mapName) {
		if (fieldMap == null) {
			fieldMap = new LinkedHashMap<String, List<FieldInfo>>();
		}

		if (!mapNames.contains(mapName)) {
			return null;
		}

		if (!fieldMap.containsKey(mapName)) {
			if (mapName.equals("ProvidedCHO")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String", mapName),
					new FieldInfo("owl:sameAs", "owlSameAs", "String[]", mapName),
					new FieldInfo("[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("WebResource")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String", mapName),
					new FieldInfo("dc:description", "dcDescription", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:format", "dcFormat", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:rights", "webResourceDcRights", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:source", "dcSource", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:conformsTo", "dctermsConformsTo", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:created", "dctermsCreated", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:extent", "dctermsExtent", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:hasPart", "dctermsHasPart", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:isFormatOf", "dctermsIsFormatOf", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:issued", "dctermsIssued", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:isNextInSequence", "isNextInSequence", "String", mapName),
					new FieldInfo("edm:rights", "webResourceEdmRights", "Map<String,List<String>>", mapName),
					new FieldInfo("[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("Agent")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String", mapName),
					new FieldInfo("skos:prefLabel", "prefLabel", "Map<String,List<String>>", mapName),
					new FieldInfo("skos:altLabel", "altLabel", "Map<String,List<String>>", mapName),
					new FieldInfo("skos:note", "note", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:date", "dcDate", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:identifier", "dcIdentifier", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:begin", "begin", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:end", "end", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:hasMet", "edmHasMet", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:isRelatedTo", "edmIsRelatedTo", "Map<String,List<String>>", mapName),
					new FieldInfo("foaf:name", "foafName", "Map<String,List<String>>", mapName),
					new FieldInfo("rdaGr2:biographicalInformation", "rdaGr2BiographicalInformation", "Map<String,List<String>>", mapName),
					new FieldInfo("rdaGr2:dateOfBirth", "rdaGr2DateOfBirth", "Map<String,List<String>>", mapName),
					new FieldInfo("rdaGr2:dateOfDeath", "rdaGr2DateOfDeath", "Map<String,List<String>>", mapName),
					new FieldInfo("rdaGr2:dateOfEstablishment", "rdaGr2DateOfEstablishment", "Map<String,List<String>>", mapName),
					new FieldInfo("rdaGr2:dateOfTermination", "rdaGr2DateOfTermination", "Map<String,List<String>>", mapName),
					new FieldInfo("rdaGr2:gender", "rdaGr2Gender", "Map<String,List<String>>", mapName),
					new FieldInfo("rdaGr2:professionOrOccupation", "rdaGr2ProfessionOrOccupation", "Map<String,List<String>>", mapName),
					new FieldInfo("owl:sameAs", "owlSameAs", "String[]", mapName),
					new FieldInfo("[edmWasPresentAt]", "edmWasPresentAt", "String[]", mapName),
					new FieldInfo("[id]", "id", "ObjectId", mapName)
				))); // TODO: hiddenLabel???
			}

			else if (mapName.equals("Place")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String", mapName),
					new FieldInfo("wgs84_pos:lat", "latitude", "float", mapName),
					new FieldInfo("wgs84_pos:long", "longitude", "float", mapName),
					new FieldInfo("wgs84_pos:alt", "altitude", "float", mapName),
					new FieldInfo("skos:prefLabel", "prefLabel", "Map<String,List<String>>", mapName),
					new FieldInfo("skos:altLabel", "altLabel", "Map<String,List<String>>", mapName),
					new FieldInfo("skos:note", "note", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:hasPart", "dcTermsHasPart", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:isPartOf", "isPartOf", "Map<String,List<String>>", mapName),
					new FieldInfo("owl:sameAs", "owlSameAs", "String[]", mapName),
					new FieldInfo("[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("TimeSpan")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String", mapName),
					new FieldInfo("skos:prefLabel", "prefLabel", "Map<String,List<String>>", mapName),
					new FieldInfo("skos:altLabel", "altLabel", "Map<String,List<String>>", mapName),
					new FieldInfo("skos:note", "note", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:hasPart", "dcTermsHasPart", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:isPartOf", "isPartOf", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:begin", "begin", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:end", "end", "Map<String,List<String>>", mapName),
					new FieldInfo("owl:sameAs", "owlSameAs", "String[]", mapName),
					new FieldInfo("[crmP79FBeginningIsQualifiedBy]", "crmP79FBeginningIsQualifiedBy", "String[]", mapName),
					new FieldInfo("[crmP80FEndIsQualifiedBy]", "crmP80FEndIsQualifiedBy", "String[]", mapName),
					new FieldInfo("[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("Concept")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String", mapName),
					new FieldInfo("skos:prefLabel", "prefLabel", "Map<String,List<String>>", mapName),
					new FieldInfo("skos:altLabel", "altLabel", "Map<String,List<String>>", mapName),
					new FieldInfo("skos:broader", "broader", "String[]", mapName),
					new FieldInfo("skos:narrower", "narrower", "String[]", mapName),
					new FieldInfo("skos:related", "related", "String[]", mapName),
					new FieldInfo("skos:broadMatch", "broadMatch", "String[]", mapName),
					new FieldInfo("skos:narrowMatch", "narrowMatch", "String[]", mapName),
					new FieldInfo("skos:relatedMatch", "relatedMatch", "String[]", mapName),
					new FieldInfo("skos:exactMatch", "exactMatch", "String[]", mapName),
					new FieldInfo("skos:closeMatch", "closeMatch", "String[]", mapName),
					new FieldInfo("skos:note", "note", "Map<String,List<String>>", mapName),
					new FieldInfo("skos:notation", "notation", "Map<String,List<String>>", mapName),
					new FieldInfo("skos:inScheme", "inScheme", "String[]", mapName),
					new FieldInfo("[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("Aggregation")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String", mapName),
					new FieldInfo("edm:aggregatedCHO", "aggregatedCHO", "String", mapName),
					new FieldInfo("edm:dataProvider", "edmDataProvider", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:hasView", "hasView", "String[]", mapName),
					new FieldInfo("edm:isShownAt", "edmIsShownAt", "String", mapName),
					new FieldInfo("edm:isShownBy", "edmIsShownBy", "String", mapName),
					new FieldInfo("edm:object", "edmObject", "String", mapName),
					new FieldInfo("edm:provider", "edmProvider", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:rights", "dcRights", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:rights", "edmRights", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:ugc", "edmUgc", "String", mapName),
					new FieldInfo("[edmPreviewNoDistribute]", "edmPreviewNoDistribute", "Boolean", mapName),
					new FieldInfo("edm:WebResource", "webResources", "List<WebResourceImpl>", mapName),
					new FieldInfo("[aggregates]", "aggregates", "String[]", mapName),
					new FieldInfo("[edmUnstored]", "edmUnstored", "String[]", mapName),
					new FieldInfo("[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("Proxy")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String", mapName),
					new FieldInfo("dc:contributor", "dcContributor", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:coverage", "dcCoverage", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:creator", "dcCreator", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:date", "dcDate", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:description", "dcDescription", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:format", "dcFormat", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:identifier", "dcIdentifier", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:language", "dcLanguage", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:publisher", "dcPublisher", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:relation", "dcRelation", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:rights", "dcRights", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:source", "dcSource", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:subject", "dcSubject", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:title", "dcTitle", "Map<String,List<String>>", mapName),
					new FieldInfo("dc:type", "dcType", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:alternative", "dctermsAlternative", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:conformsTo", "dctermsConformsTo", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:created", "dctermsCreated", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:extent", "dctermsExtent", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:hasFormat", "dctermsHasFormat", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:hasPart", "dctermsHasPart", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:hasVersion", "dctermsHasVersion", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:isFormatOf", "dctermsIsFormatOf", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:isPartOf", "dctermsIsPartOf", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:isReferencedBy", "dctermsIsReferencedBy", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:isReplacedBy", "dctermsIsReplacedBy", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:isRequiredBy", "dctermsIsRequiredBy", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:issued", "dctermsIssued", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:isVersionOf", "dctermsIsVersionOf", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:medium", "dctermsMedium", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:provenance", "dctermsProvenance", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:references", "dctermsReferences", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:replaces", "dctermsReplaces", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:requires", "dctermsRequires", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:spatial", "dctermsSpatial", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:tableOfContents", "dctermsTOC", "Map<String,List<String>>", mapName),
					new FieldInfo("dcterms:temporal", "dctermsTemporal", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:currentLocation", "edmCurrentLocation", "String", mapName),
					new FieldInfo("edm:hasMet", "edmHasMet", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:hasType", "edmHasType", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:incorporates", "edmIncorporates", "String[]", mapName),
					new FieldInfo("edm:isDerivativeOf", "edmIsDerivativeOf", "String[]", mapName),
					new FieldInfo("edm:isNextInSequence", "edmIsNextInSequence", "String", mapName),
					new FieldInfo("edm:isRelatedTo", "edmIsRelatedTo", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:isRepresentationOf", "edmIsRepresentationOf", "String", mapName),
					new FieldInfo("edm:isSimilarTo", "edmIsSimilarTo", "String[]", mapName),
					new FieldInfo("edm:isSuccessorOf", "edmIsSuccessorOf", "String[]", mapName),
					new FieldInfo("edm:realizes", "edmRealizes", "String[]", mapName),
					new FieldInfo("edm:type", "edmType", "DocType", mapName),
					new FieldInfo("edm:europeanaProxy", "europeanaProxy", "boolean", mapName),
					new FieldInfo("ore:proxyFor", "proxyFor", "String", mapName),
					new FieldInfo("ore:proxyIn", "proxyIn", "String[]", mapName),
					// new FieldInfo("[edmUnstored]", "edmUnstored", "String[]", mapName),
					new FieldInfo("[edmRights]", "edmRights", "Map<String,List<String>>", mapName),
					new FieldInfo("[edmWasPresentAt]", "edmWasPresentAt", "String[]", mapName),
					new FieldInfo("[rdfType]", "rdfType", "String[]", mapName),
					new FieldInfo("[id]", "id", "ObjectId", mapName),
					// MISSING implementations:
					new FieldInfo("edm:userTag", "userTags", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:year", "year", "Map<String,List<String>>", mapName)
					// new FieldInfo("owl:sameAs", "???", ""),
				)));
			}

			else if (mapName.equals("EuropeanaAggregation")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String", mapName),
					new FieldInfo("dc:creator", "dcCreator", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:aggregatedCHO", "aggregatedCHO", "String", mapName),
					new FieldInfo("edm:country", "edmCountry", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:hasView", "edmHasView", "String[]", mapName),
					new FieldInfo("edm:isShownBy", "edmIsShownBy", "String", mapName),
					new FieldInfo("edm:landingPage", "edmLandingPage", "String", mapName),
					new FieldInfo("edm:language", "edmLanguage", "Map<String,List<String>>", mapName),
					new FieldInfo("edm:rights", "edmRights", "Map<String,List<String>>", mapName),
					new FieldInfo("ore:aggregates", "aggregates", "String[]", mapName),
					new FieldInfo("edm:WebResource", "webResources", "List<WebResourceImpl>", mapName),
					new FieldInfo("[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("FullBean")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("id", "id", "ObjectId", mapName),
					new FieldInfo("about", "about", "String", mapName),
					new FieldInfo("title", "title", "String[]", mapName),
					new FieldInfo("year", "year", "String[]", mapName),
					new FieldInfo("provider", "provider", "String[]", mapName),
					new FieldInfo("language", "language", "String[]", mapName),
					new FieldInfo("timestamp", "timestamp", "Date", mapName),
					new FieldInfo("type", "type", "DocType", mapName),
					new FieldInfo("europeanaCompleteness", "europeanaCompleteness", "int", mapName),
					new FieldInfo("optOut", "optedOut", "boolean", mapName),
					// new FieldInfo("similarItems", "similarItems", "List<BriefBeanImpl>", mapName),
					// new FieldInfo("who", "who", "String[]", mapName),
					// new FieldInfo("what", "what", "String[]", mapName),
					// new FieldInfo("where", "where", "String[]", mapName),
					// new FieldInfo("when", "when", "String[]", mapName),
					// new FieldInfo("edmTimespanBroaderTerm", "edmTimespanBroaderTerm", "String[]", mapName),
					// new FieldInfo("edmTimespanBroaderLabel", "edmTimespanBroaderLabel", "List<Map<String,String>>", mapName),
					// new FieldInfo("edmConceptBroaderLabel", "edmConceptBroaderLabel", "List<Map<String,String>>", mapName),
					// new FieldInfo("edmPlaceBroaderTerm", "edmPlaceBroaderTerm", "String[]", mapName),
					// new FieldInfo("previewNoDistribute", "previewNoDistribute", "String", mapName),
					new FieldInfo("europeanaCollectionName", "europeanaCollectionName", "String[]", mapName)
				)));
			}

			else if (mapName.equals("BriefBean")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("fullDocUrl", "fullDocUrl", "String", mapName),
					new FieldInfo("timestamp", "timestamp", "Date", mapName),
					new FieldInfo("provider", "provider", "String[]", mapName),
					new FieldInfo("edmDataProvider", "dataProvider", "String[]", mapName),
					new FieldInfo("edmObject", "edmObject", "String[]", mapName),
					new FieldInfo("europeanaCompleteness", "europeanaCompleteness", "int", mapName),
					new FieldInfo("docType", "type", "DocType", mapName),
					new FieldInfo("language", "language", "String[]", mapName),
					new FieldInfo("year", "year", "String[]", mapName),
					new FieldInfo("rights", "rights", "String[]", mapName),
					new FieldInfo("title", "title", "String[]", mapName),
					new FieldInfo("dcCreator", "dcCreator", "String[]", mapName),
					new FieldInfo("dcContributor", "dcContributor", "String[]", mapName),
					new FieldInfo("edmPlace", "edmPlace", "String[]", mapName),
					new FieldInfo("edmPlacePrefLabel", "edmPlaceLabel", "List<Map<String,String>>", mapName),
					new FieldInfo("edmPlaceLatitude", "edmPlaceLatitude", "Float", mapName),
					new FieldInfo("edmPlaceLongitude", "edmPlaceLongitude", "Float", mapName),
					new FieldInfo("edmTimespan", "edmTimespan", "String[]", mapName),
					new FieldInfo("edmTimespanLabel", "edmTimespanLabel", "List<Map<String,String>>", mapName),
					new FieldInfo("edmTimespanBegin", "edmTimespanBegin", "String[]", mapName),
					new FieldInfo("edmTimespanEnd", "edmTimespanEnd", "String[]", mapName),
					new FieldInfo("edmAgentTerm", "edmAgent", "String[]", mapName),
					new FieldInfo("edmAgentLabel", "edmAgentLabel", "List<Map<String,String>>", mapName),
					new FieldInfo("dctermsHasPart", "dctermsHasPart", "String[]", mapName),
					new FieldInfo("dctermsSpatial", "dctermsSpatial", "String[]", mapName)
				)));
			}
		}
		return fieldMap.get(mapName);
	}
}
