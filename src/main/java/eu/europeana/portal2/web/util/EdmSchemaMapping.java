package eu.europeana.portal2.web.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EdmSchemaMapping {

	private static final List<String> mapNames = Arrays.asList(
		new String[]{"ProvidedCHO", "WebResource", "Agent", "Place", "Timespan", 
					"Concept", "Aggregation", "Proxy", "EuropeanaAggregation"}
	);

	private static Map<String, List<FieldInfo>> fieldMap = new LinkedHashMap<String, List<FieldInfo>>();
	private static List<FieldInfo> topLevelTypes;
	
	public static List<String> getMapNames() {
		return mapNames;
	}

	public static Map<String, List<FieldInfo>> getFullMap() {
		for (String mapName : mapNames) {
			getMap(mapName);
		}
		return fieldMap;
	}
	
	public static List<FieldInfo> getTopLevel() {
		if (topLevelTypes == null) {
			topLevelTypes = new LinkedList<FieldInfo>(Arrays.asList(
				new FieldInfo("edm:ProvidedCHO", "providedCHOs", "ProvidedCHO"),
				new FieldInfo("edm:Agent", "agents", "Agent"),
				new FieldInfo("edm:Place", "places", "Place"),
				new FieldInfo("edm:TimeSpan", "timespans", "TimeSpan"),
				new FieldInfo("skos:Concept", "concepts", "Concept"),
				new FieldInfo("ore:Aggregation", "aggregations", "Aggregation"),
				new FieldInfo("edm:Proxy", "proxies", "Proxy"),
				new FieldInfo("edm:EuropeanaAggregation", "europeanaAggregation", "EuropeanaAggregation"),
				new FieldInfo("edm:WebResource", "webResources", "WebResource")
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
					new FieldInfo("@rdf:about", "about", "String"),
					new FieldInfo("owl:sameAs", "owlSameAs", "String[]"),
					new FieldInfo("[id]", "id", "ObjectId")
				)));
			}

			else if (mapName.equals("WebResource")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String"),
					new FieldInfo("dc:description", "dcDescription", "String[]"),
					new FieldInfo("dc:format", "dcFormat", "String[]"),
					new FieldInfo("dc:rights", "webResourceDcRights", "String[]"),
					new FieldInfo("dc:source", "dcSource", "String[]"),
					new FieldInfo("dcterms:conformsTo", "dctermsConformsTo", "String[]"),
					new FieldInfo("dcterms:created", "dctermsCreated", "String[]"),
					new FieldInfo("dcterms:extent", "dctermsExtent", "String[]"),
					new FieldInfo("dcterms:hasPart", "dctermsHasPart", "String[]"),
					new FieldInfo("dcterms:isFormatOf", "dctermsIsFormatOf", "String[]"),
					new FieldInfo("dcterms:issued", "dctermsIssued", "String[]"),
					new FieldInfo("edm:isNextInSequence", "isNextInSequence", "String"),
					new FieldInfo("edm:rights", "webResourceEdmRights", "String"),
					new FieldInfo("[id]", "id", "ObjectId")
				)));
			}

			else if (mapName.equals("Agent")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String"),
					new FieldInfo("skos:prefLabel", "prefLabel", "Map<String,String>"),
					new FieldInfo("skos:altLabel", "altLabel", "Map<String,String>"),
					new FieldInfo("skos:note", "note", "String[]"),
					new FieldInfo("dc:date", "dcDate", "String[]"),
					new FieldInfo("dc:identifier", "dcIdentifier", "String[]"),
					new FieldInfo("edm:begin", "begin", "String"),
					new FieldInfo("edm:end", "end", "String"),
					new FieldInfo("edm:hasMet", "edmHasMet", "String[]"),
					new FieldInfo("edm:isRelatedTo", "edmIsRelatedTo", "String[]"),
					new FieldInfo("foaf:name", "foafName", "String[]"),
					new FieldInfo("rdaGr2:biographicalInformation", "rdaGr2BiographicalInformation", "String"),
					new FieldInfo("rdaGr2:dateOfBirth", "rdaGr2DateOfBirth", "String[]"),
					new FieldInfo("rdaGr2:dateOfDeath", "rdaGr2DateOfDeath", "String[]"),
					new FieldInfo("rdaGr2:dateOfEstablishment", "rdaGr2DateOfEstablishment", "String[]"),
					new FieldInfo("rdaGr2:dateOfTermination", "rdaGr2DateOfTermination", "String[]"),
					new FieldInfo("rdaGr2:gender", "rdaGr2Gender", "String[]"),
					new FieldInfo("rdaGr2:professionOrOccupation", "rdaGr2ProfessionOrOccupation", "String[]"),
					new FieldInfo("owl:sameAs", "owlSameAs", "String[]"),
					new FieldInfo("[edmWasPresentAt]", "edmWasPresentAt", "String[]"),
					new FieldInfo("[id]", "id", "ObjectId")
				)));
			}

			else if (mapName.equals("Place")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String"),
					new FieldInfo("wgs84_pos:lat", "latitude", "float"),
					new FieldInfo("wgs84_pos:long", "longitude", "float"),
					new FieldInfo("wgs84_pos:alt", "altitude", "float"),
					new FieldInfo("skos:prefLabel", "prefLabel", "Map<String,String>"),
					new FieldInfo("skos:altLabel", "altLabel", "Map<String,String>"),
					new FieldInfo("skos:note", "note", "String[]"),
					new FieldInfo("dcterms:hasPart", "dcTermsHasPart", "String[]"),
					new FieldInfo("dcterms:isPartOf", "isPartOf", "String[]"),
					new FieldInfo("owl:sameAs", "owlSameAs", "String[]"),
					new FieldInfo("[id]", "id", "ObjectId")
				)));
			}

			else if (mapName.equals("TimeSpan")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String"),
					new FieldInfo("skos:prefLabel", "prefLabel", "Map<String,String>"),
					new FieldInfo("skos:altLabel", "altLabel", "Map<String,String>"),
					new FieldInfo("skos:note", "note", "String[]"),
					new FieldInfo("dcterms:hasPart", "dcTermsHasPart", "String[]"),
					new FieldInfo("dcterms:isPartOf", "isPartOf", "String[]"),
					new FieldInfo("edm:begin", "begin", "String"),
					new FieldInfo("edm:end", "end", "String"),
					new FieldInfo("owl:sameAs", "owlSameAs", "String[]"),
					new FieldInfo("[crmP79FBeginningIsQualifiedBy]", "crmP79FBeginningIsQualifiedBy", "String[]"),
					new FieldInfo("[crmP80FEndIsQualifiedBy]", "crmP80FEndIsQualifiedBy", "String[]"),
					new FieldInfo("[id]", "id", "ObjectId")
				)));
			}

			else if (mapName.equals("Concept")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String"),
					new FieldInfo("skos:prefLabel", "prefLabel", "Map<String,String>"),
					new FieldInfo("skos:altLabel", "altLabel", "Map<String,String>"),
					new FieldInfo("skos:broader", "broader", "String[]"),
					new FieldInfo("skos:narrower", "narrower", "String[]"),
					new FieldInfo("skos:related", "related", "String[]"),
					new FieldInfo("skos:broadMatch", "broadMatch", "String[]"),
					new FieldInfo("skos:narrowMatch", "narrowMatch", "String[]"),
					new FieldInfo("skos:relatedMatch", "relatedMatch", "String[]"),
					new FieldInfo("skos:exactMatch", "exactMatch", "String[]"),
					new FieldInfo("skos:closeMatch", "closeMatch", "String[]"),
					new FieldInfo("skos:note", "note", "String[]"),
					new FieldInfo("skos:notation", "notation", "String[]"),
					new FieldInfo("skos:inScheme", "inScheme", "String[]"),
					new FieldInfo("[id]", "id", "ObjectId")
				)));
			}

			else if (mapName.equals("Aggregation")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String"),
					new FieldInfo("edm:aggregatedCHO", "aggregatedCHO", "String"),
					new FieldInfo("edm:dataProvider", "edmDataProvider", "String"),
					new FieldInfo("edm:hasView", "hasView", "String[]"),
					new FieldInfo("edm:isShownAt", "edmIsShownAt", "String"),
					new FieldInfo("edm:isShownBy", "edmIsShownBy", "String"),
					new FieldInfo("edm:object", "edmObject", "String"),
					new FieldInfo("edm:provider", "edmProvider", "String"),
					new FieldInfo("dc:rights", "dcRights", "String[]"),
					new FieldInfo("edm:rights", "edmRights", "String"),
					new FieldInfo("edm:ugc", "edmUgc", "String"),
					new FieldInfo("[edmPreviewNoDistribute]", "edmPreviewNoDistribute", "Boolean"),
					new FieldInfo("edm:WebResource", "webResources", "List<WebResourceImpl>"),
					new FieldInfo("[aggregates]", "aggregates", "String[]"),
					new FieldInfo("[edmUnstored]", "edmUnstored", "String[]"),
					new FieldInfo("[id]", "id", "ObjectId")
				)));
			}

			else if (mapName.equals("Proxy")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String"),
					new FieldInfo("dc:contributor", "dcContributor", "String[]"),
					new FieldInfo("dc:coverage", "dcCoverage", "String[]"),
					new FieldInfo("dc:creator", "dcCreator", "String[]"),
					new FieldInfo("dc:date", "dcDate", "String[]"),
					new FieldInfo("dc:description", "dcDescription", "String[]"),
					new FieldInfo("dc:format", "dcFormat", "String[]"),
					new FieldInfo("dc:identifier", "dcIdentifier", "String[]"),
					new FieldInfo("dc:language", "dcLanguage", "String[]"),
					new FieldInfo("dc:publisher", "dcPublisher", "String[]"),
					new FieldInfo("dc:relation", "dcRelation", "String[]"),
					new FieldInfo("dc:rights", "dcRights", "String[]"),
					new FieldInfo("dc:source", "dcSource", "String[]"),
					new FieldInfo("dc:subject", "dcSubject", "String[]"),
					new FieldInfo("dc:title", "dcTitle", "String[]"),
					new FieldInfo("dc:type", "dcType", "String[]"),
					new FieldInfo("dcterms:alternative", "dctermsAlternative", "String[]"),
					new FieldInfo("dcterms:conformsTo", "dctermsConformsTo", "String[]"),
					new FieldInfo("dcterms:created", "dctermsCreated", "String[]"),
					new FieldInfo("dcterms:extent", "dctermsExtent", "String[]"),
					new FieldInfo("dcterms:hasFormat", "dctermsHasFormat", "String[]"),
					new FieldInfo("dcterms:hasPart", "dctermsHasPart", "String[]"),
					new FieldInfo("dcterms:hasVersion", "dctermsHasVersion", "String[]"),
					new FieldInfo("dcterms:isFormatOf", "dctermsIsFormatOf", "String[]"),
					new FieldInfo("dcterms:isPartOf", "dctermsIsPartOf", "String[]"),
					new FieldInfo("dcterms:isReferencedBy", "dctermsIsReferencedBy", "String[]"),
					new FieldInfo("dcterms:isReplacedBy", "dctermsIsReplacedBy", "String[]"),
					new FieldInfo("dcterms:isRequiredBy", "dctermsIsRequiredBy", "String[]"),
					new FieldInfo("dcterms:issued", "dctermsIssued", "String[]"),
					new FieldInfo("dcterms:isVersionOf", "dctermsIsVersionOf", "String[]"),
					new FieldInfo("dcterms:medium", "dctermsMedium", "String[]"),
					new FieldInfo("dcterms:provenance", "dctermsProvenance", "String[]"),
					new FieldInfo("dcterms:references", "dctermsReferences", "String[]"),
					new FieldInfo("dcterms:replaces", "dctermsReplaces", "String[]"),
					new FieldInfo("dcterms:requires", "dctermsRequires", "String[]"),
					new FieldInfo("dcterms:spatial", "dctermsSpatial", "String[]"),
					new FieldInfo("dcterms:tableOfContents", "dctermsTOC", "String[]"),
					new FieldInfo("dcterms:temporal", "dctermsTemporal", "String[]"),
					new FieldInfo("edm:currentLocation", "edmCurrentLocation", "String"),
					new FieldInfo("edm:hasMet", "edmHasMet", "String[]"),
					new FieldInfo("edm:hasType", "edmHasType", "String[]"),
					new FieldInfo("edm:incorporates", "edmIncorporates", "String[]"),
					new FieldInfo("edm:isDerivativeOf", "edmIsDerivativeOf", "String[]"),
					new FieldInfo("edm:isNextInSequence", "edmIsNextInSequence", "String"),
					new FieldInfo("edm:isRelatedTo", "edmIsRelatedTo", "String[]"),
					new FieldInfo("edm:isRepresentationOf", "edmIsRepresentationOf", "String"),
					new FieldInfo("edm:isSimilarTo", "edmIsSimilarTo", "String[]"),
					new FieldInfo("edm:isSuccessorOf", "edmIsSuccessorOf", "String[]"),
					new FieldInfo("edm:realizes", "edmRealizes", "String[]"),
					new FieldInfo("edm:type", "edmType", "DocType"),
					new FieldInfo("edm:europeanaProxy", "europeanaProxy", "boolean"),
					new FieldInfo("ore:proxyFor", "proxyFor", "String"),
					new FieldInfo("ore:proxyIn", "proxyIn", "String"),
					new FieldInfo("[edmUnstored]", "edmUnstored", "String[]"),
					new FieldInfo("[edmRights]", "edmRights", "String"),
					new FieldInfo("[edmWasPresentAt]", "edmWasPresentAt", "String[]"),
					new FieldInfo("[id]", "id", "ObjectId")
					// MISSING implementations:
					// new FieldInfo("edm:userTag", "???", ""),
					// new FieldInfo("edm:year", "???", ""),
					// new FieldInfo("owl:sameAs", "???", ""),
				)));
			}

			else if (mapName.equals("EuropeanaAggregation")) {
				fieldMap.put(mapName, new LinkedList<FieldInfo>(Arrays.asList(
					new FieldInfo("@rdf:about", "about", "String"),
					new FieldInfo("dc:creator", "dcCreator", "String"),
					new FieldInfo("edm:aggregatedCHO", "aggregatedCHO", "String"),
					new FieldInfo("edm:country", "edmCountry", "String"),
					new FieldInfo("edm:hasView", "edmHasView", "String[]"),
					new FieldInfo("edm:isShownBy", "edmIsShownBy", "String"),
					new FieldInfo("edm:landingPage", "edmLandingPage", "String"),
					new FieldInfo("edm:language", "edmLanguage", "String"),
					new FieldInfo("edm:rights", "edmRights", "String"),
					new FieldInfo("ore:aggregates", "aggregates", "String[]"),
					new FieldInfo("edm:WebResource", "webResources", "List<WebResourceImpl>"),
					new FieldInfo("[id]", "id", "ObjectId")
				)));
			}
		}

		return fieldMap.get(mapName);
	}
}
