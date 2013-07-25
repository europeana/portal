package eu.europeana.portal2.web.presentation.semantic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EdmSchemaMapping {

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

	public static Map<String, List<FieldInfo>> getFullMap(SchemaOrgMapping mapping) {
		for (String mapName : mapNames) {
			getMap(mapping, mapName);
		}
		return fieldMap;
	}

	public static Map<String, Element> getEdmElements(SchemaOrgMapping mapping) {
		if (edmElements == null) {
			edmElements = new HashMap<String, Element>();
			for (String mapName : mapNames) {
				List<FieldInfo> fields = getMap(mapping, mapName);
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

	public static List<FieldInfo> getTopLevel(SchemaOrgMapping mapping) {
		if (topLevelTypes == null) {
			topLevelTypes = new LinkedList<FieldInfo>(Arrays.asList(
				new FieldInfo(mapping, "fullBean", "fullBean", "FullBean"),
				new FieldInfo(mapping, "edm:ProvidedCHO", "providedCHOs", "ProvidedCHO"),
				new FieldInfo(mapping, "edm:Agent", "agents", "Agent"),
				new FieldInfo(mapping, "edm:Place", "places", "Place"),
				new FieldInfo(mapping, "edm:TimeSpan", "timespans", "Timespan"),
				new FieldInfo(mapping, "skos:Concept", "concepts", "Concept"),
				new FieldInfo(mapping, "ore:Aggregation", "aggregations", "Aggregation"),
				new FieldInfo(mapping, "edm:Proxy", "proxies", "Proxy"),
				new FieldInfo(mapping, "edm:EuropeanaAggregation", "europeanaAggregation", "EuropeanaAggregation"),
				new FieldInfo(mapping, "edm:WebResource", "webResources", "WebResource"),
				new FieldInfo(mapping, "briefBean", "briefBean", "BriefBean")
			));
		}
		return topLevelTypes;
	}

	public static List<FieldInfo> getMap(SchemaOrgMapping mapping, String mapName) {
		if (fieldMap == null) {
			fieldMap = new LinkedHashMap<String, List<FieldInfo>>();
		}

		if (!mapNames.contains(mapName)) {
			return null;
		}

		if (!fieldMap.containsKey(mapName)) {
			if (mapName.equals("ProvidedCHO")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo(mapping, "@rdf:about", "about", "String", mapName),
					new FieldInfo(mapping, "owl:sameAs", "owlSameAs", "String[]", mapName),
					new FieldInfo(mapping, "[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("WebResource")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo(mapping, "@rdf:about", "about", "String", mapName),
					new FieldInfo(mapping, "dc:description", "dcDescription", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:format", "dcFormat", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:rights", "webResourceDcRights", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:source", "dcSource", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:conformsTo", "dctermsConformsTo", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:created", "dctermsCreated", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:extent", "dctermsExtent", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:hasPart", "dctermsHasPart", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:isFormatOf", "dctermsIsFormatOf", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:issued", "dctermsIssued", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:isNextInSequence", "isNextInSequence", "String", mapName),
					new FieldInfo(mapping, "edm:rights", "webResourceEdmRights", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("Agent")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo(mapping, "@rdf:about", "about", "String", mapName),
					new FieldInfo(mapping, "skos:prefLabel", "prefLabel", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:altLabel", "altLabel", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:hiddenLabel", "hiddenLabel", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:note", "note", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:date", "dcDate", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:identifier", "dcIdentifier", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:begin", "begin", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:end", "end", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:hasMet", "edmHasMet", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:isRelatedTo", "edmIsRelatedTo", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "foaf:name", "foafName", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "rdaGr2:biographicalInformation", "rdaGr2BiographicalInformation", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "rdaGr2:dateOfBirth", "rdaGr2DateOfBirth", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "rdaGr2:dateOfDeath", "rdaGr2DateOfDeath", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "rdaGr2:dateOfEstablishment", "rdaGr2DateOfEstablishment", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "rdaGr2:dateOfTermination", "rdaGr2DateOfTermination", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "rdaGr2:gender", "rdaGr2Gender", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "rdaGr2:professionOrOccupation", "rdaGr2ProfessionOrOccupation", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "owl:sameAs", "owlSameAs", "String[]", mapName),
					new FieldInfo(mapping, "[edmWasPresentAt]", "edmWasPresentAt", "String[]", mapName),
					new FieldInfo(mapping, "[id]", "id", "ObjectId", mapName)
				))); // TODO: hiddenLabel???
			}

			else if (mapName.equals("Place")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo(mapping, "@rdf:about", "about", "String", mapName),
					new FieldInfo(mapping, "wgs84_pos:lat", "latitude", "float", mapName),
					new FieldInfo(mapping, "wgs84_pos:long", "longitude", "float", mapName),
					new FieldInfo(mapping, "wgs84_pos:alt", "altitude", "float", mapName),
					new FieldInfo(mapping, "skos:prefLabel", "prefLabel", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:altLabel", "altLabel", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:hiddenLabel", "hiddenLabel", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:note", "note", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:hasPart", "dcTermsHasPart", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:isPartOf", "isPartOf", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "owl:sameAs", "owlSameAs", "String[]", mapName),
					new FieldInfo(mapping, "[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("Timespan")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo(mapping, "@rdf:about", "about", "String", mapName),
					new FieldInfo(mapping, "edm:begin", "begin", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:end", "end", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "owl:sameAs", "owlSameAs", "String[]", mapName),
					new FieldInfo(mapping, "dcterms:isPartOf", "isPartOf", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:hasPart", "dctermsHasPart", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:prefLabel", "prefLabel", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:altLabel", "altLabel", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:hiddenLabel", "hiddenLabel", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:note", "note", "Map<String,List<String>>", mapName),
					// new FieldInfo(mapping, "[crmP79FBeginningIsQualifiedBy]", "crmP79FBeginningIsQualifiedBy", "String[]", mapName),
					// new FieldInfo(mapping, "[crmP80FEndIsQualifiedBy]", "crmP80FEndIsQualifiedBy", "String[]", mapName),
					new FieldInfo(mapping, "[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("Concept")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo(mapping, "@rdf:about", "about", "String", mapName),
					new FieldInfo(mapping, "skos:prefLabel", "prefLabel", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:altLabel", "altLabel", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:hiddenLabel", "hiddenLabel", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:broader", "broader", "String[]", mapName),
					new FieldInfo(mapping, "skos:narrower", "narrower", "String[]", mapName),
					new FieldInfo(mapping, "skos:related", "related", "String[]", mapName),
					new FieldInfo(mapping, "skos:broadMatch", "broadMatch", "String[]", mapName),
					new FieldInfo(mapping, "skos:narrowMatch", "narrowMatch", "String[]", mapName),
					new FieldInfo(mapping, "skos:relatedMatch", "relatedMatch", "String[]", mapName),
					new FieldInfo(mapping, "skos:exactMatch", "exactMatch", "String[]", mapName),
					new FieldInfo(mapping, "skos:closeMatch", "closeMatch", "String[]", mapName),
					new FieldInfo(mapping, "skos:note", "note", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:notation", "notation", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "skos:inScheme", "inScheme", "String[]", mapName),
					new FieldInfo(mapping, "[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("Aggregation")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo(mapping, "@rdf:about", "about", "String", mapName),
					new FieldInfo(mapping, "edm:aggregatedCHO", "aggregatedCHO", "String", mapName),
					new FieldInfo(mapping, "edm:dataProvider", "edmDataProvider", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:hasView", "hasView", "String[]", mapName),
					new FieldInfo(mapping, "edm:isShownAt", "edmIsShownAt", "String", mapName),
					new FieldInfo(mapping, "edm:isShownBy", "edmIsShownBy", "String", mapName),
					new FieldInfo(mapping, "edm:object", "edmObject", "String", mapName),
					new FieldInfo(mapping, "edm:provider", "edmProvider", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:rights", "dcRights", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:rights", "edmRights", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:ugc", "edmUgc", "String", mapName),
					new FieldInfo(mapping, "[edmPreviewNoDistribute]", "edmPreviewNoDistribute", "Boolean", mapName),
					new FieldInfo(mapping, "edm:WebResource", "webResources", "List<WebResourceImpl>", mapName),
					new FieldInfo(mapping, "[aggregates]", "aggregates", "String[]", mapName),
					new FieldInfo(mapping, "[edmUnstored]", "edmUnstored", "String[]", mapName),
					new FieldInfo(mapping, "[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("Proxy")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo(mapping, "@rdf:about", "about", "String", mapName),
					new FieldInfo(mapping, "dc:contributor", "dcContributor", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:coverage", "dcCoverage", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:creator", "dcCreator", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:date", "dcDate", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:description", "dcDescription", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:format", "dcFormat", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:identifier", "dcIdentifier", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:language", "dcLanguage", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:publisher", "dcPublisher", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:relation", "dcRelation", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:rights", "dcRights", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:source", "dcSource", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:subject", "dcSubject", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:title", "dcTitle", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dc:type", "dcType", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:alternative", "dctermsAlternative", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:conformsTo", "dctermsConformsTo", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:created", "dctermsCreated", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:extent", "dctermsExtent", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:hasFormat", "dctermsHasFormat", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:hasPart", "dctermsHasPart", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:hasVersion", "dctermsHasVersion", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:isFormatOf", "dctermsIsFormatOf", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:isPartOf", "dctermsIsPartOf", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:isReferencedBy", "dctermsIsReferencedBy", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:isReplacedBy", "dctermsIsReplacedBy", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:isRequiredBy", "dctermsIsRequiredBy", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:issued", "dctermsIssued", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:isVersionOf", "dctermsIsVersionOf", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:medium", "dctermsMedium", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:provenance", "dctermsProvenance", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:references", "dctermsReferences", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:replaces", "dctermsReplaces", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:requires", "dctermsRequires", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:spatial", "dctermsSpatial", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:tableOfContents", "dctermsTOC", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "dcterms:temporal", "dctermsTemporal", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:currentLocation", "edmCurrentLocation", "String", mapName),
					new FieldInfo(mapping, "edm:hasMet", "edmHasMet", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:hasType", "edmHasType", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:incorporates", "edmIncorporates", "String[]", mapName),
					new FieldInfo(mapping, "edm:isDerivativeOf", "edmIsDerivativeOf", "String[]", mapName),
					new FieldInfo(mapping, "edm:isNextInSequence", "edmIsNextInSequence", "String", mapName),
					new FieldInfo(mapping, "edm:isRelatedTo", "edmIsRelatedTo", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:isRepresentationOf", "edmIsRepresentationOf", "String", mapName),
					new FieldInfo(mapping, "edm:isSimilarTo", "edmIsSimilarTo", "String[]", mapName),
					new FieldInfo(mapping, "edm:isSuccessorOf", "edmIsSuccessorOf", "String[]", mapName),
					new FieldInfo(mapping, "edm:realizes", "edmRealizes", "String[]", mapName),
					new FieldInfo(mapping, "edm:type", "edmType", "DocType", mapName),
					new FieldInfo(mapping, "edm:europeanaProxy", "europeanaProxy", "boolean", mapName),
					new FieldInfo(mapping, "ore:proxyFor", "proxyFor", "String", mapName),
					new FieldInfo(mapping, "ore:proxyIn", "proxyIn", "String[]", mapName),
					// new FieldInfo(mapping, "[edmUnstored]", "edmUnstored", "String[]", mapName),
					new FieldInfo(mapping, "[edmRights]", "edmRights", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "[edmWasPresentAt]", "edmWasPresentAt", "String[]", mapName),
					new FieldInfo(mapping, "[rdfType]", "rdfType", "String[]", mapName),
					new FieldInfo(mapping, "[id]", "id", "ObjectId", mapName),
					// MISSING implementations:
					new FieldInfo(mapping, "edm:userTag", "userTags", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:year", "year", "Map<String,List<String>>", mapName)
					// new FieldInfo(mapping, "owl:sameAs", "???", ""),
				)));
			}

			else if (mapName.equals("EuropeanaAggregation")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo(mapping, "@rdf:about", "about", "String", mapName),
					new FieldInfo(mapping, "dc:creator", "dcCreator", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:aggregatedCHO", "aggregatedCHO", "String", mapName),
					new FieldInfo(mapping, "edm:country", "edmCountry", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:hasView", "edmHasView", "String[]", mapName),
					new FieldInfo(mapping, "edm:isShownBy", "edmIsShownBy", "String", mapName),
					new FieldInfo(mapping, "edm:landingPage", "edmLandingPage", "String", mapName),
					new FieldInfo(mapping, "edm:language", "edmLanguage", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "edm:preview", "edmPreview", "String", mapName),
					new FieldInfo(mapping, "edm:rights", "edmRights", "Map<String,List<String>>", mapName),
					new FieldInfo(mapping, "ore:aggregates", "aggregates", "String[]", mapName),
					new FieldInfo(mapping, "edm:WebResource", "webResources", "List<WebResourceImpl>", mapName),
					new FieldInfo(mapping, "[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("FullBean")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo(mapping, "@rdf:about", "about", "String", mapName),
					new FieldInfo(mapping, "title", "title", "String[]", mapName),
					new FieldInfo(mapping, "year", "year", "String[]", mapName),
					new FieldInfo(mapping, "provider", "provider", "String[]", mapName),
					new FieldInfo(mapping, "language", "language", "String[]", mapName),
					new FieldInfo(mapping, "timestamp", "timestamp", "Date", mapName),
					new FieldInfo(mapping, "type", "type", "DocType", mapName),
					new FieldInfo(mapping, "europeanaCompleteness", "europeanaCompleteness", "int", mapName),
					new FieldInfo(mapping, "optOut", "optedOut", "boolean", mapName),
					new FieldInfo(mapping, "country", "country", "String[]", mapName),
					new FieldInfo(mapping, "userTags", "userTags", "String[]", mapName),
					new FieldInfo(mapping, "europeanaCollectionName", "europeanaCollectionName", "String[]", mapName),
					new FieldInfo(mapping, "[id]", "id", "ObjectId", mapName)
				)));
			}

			else if (mapName.equals("BriefBean")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo(mapping, "fullDocUrl", "fullDocUrl", "String", mapName),
					new FieldInfo(mapping, "timestamp", "timestamp", "Date", mapName),
					new FieldInfo(mapping, "provider", "provider", "String[]", mapName),
					new FieldInfo(mapping, "edmDataProvider", "dataProvider", "String[]", mapName),
					new FieldInfo(mapping, "edmObject", "edmObject", "String[]", mapName),
					new FieldInfo(mapping, "europeanaCompleteness", "europeanaCompleteness", "int", mapName),
					new FieldInfo(mapping, "docType", "type", "DocType", mapName),
					new FieldInfo(mapping, "language", "language", "String[]", mapName),
					new FieldInfo(mapping, "year", "year", "String[]", mapName),
					new FieldInfo(mapping, "rights", "rights", "String[]", mapName),
					new FieldInfo(mapping, "title", "title", "String[]", mapName),
					new FieldInfo(mapping, "dcCreator", "dcCreator", "String[]", mapName),
					new FieldInfo(mapping, "dcContributor", "dcContributor", "String[]", mapName),
					new FieldInfo(mapping, "edmPlace", "edmPlace", "String[]", mapName),
					new FieldInfo(mapping, "edmPlacePrefLabel", "edmPlaceLabel", "List<Map<String,String>>", mapName),
					new FieldInfo(mapping, "edmPlaceLatitude", "edmPlaceLatitude", "List<String>", mapName),
					new FieldInfo(mapping, "edmPlaceLongitude", "edmPlaceLongitude", "List<String>", mapName),
					new FieldInfo(mapping, "edmTimespan", "edmTimespan", "String[]", mapName),
					new FieldInfo(mapping, "edmTimespanLabel", "edmTimespanLabel", "List<Map<String,String>>", mapName),
					new FieldInfo(mapping, "edmTimespanBegin", "edmTimespanBegin", "String[]", mapName),
					new FieldInfo(mapping, "edmTimespanEnd", "edmTimespanEnd", "String[]", mapName),
					new FieldInfo(mapping, "edmAgentTerm", "edmAgent", "String[]", mapName),
					new FieldInfo(mapping, "edmAgentLabel", "edmAgentLabel", "List<Map<String,String>>", mapName),
					new FieldInfo(mapping, "dctermsHasPart", "dctermsHasPart", "String[]", mapName),
					new FieldInfo(mapping, "dctermsSpatial", "dctermsSpatial", "String[]", mapName)
				)));
			}
		}
		return fieldMap.get(mapName);
	}
}
