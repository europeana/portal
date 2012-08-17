package eu.europeana.portal2.web.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EdmSchemaMapping {

	private static final List<String> mapNames = Arrays.asList(
		new String[]{"objects", "document", "ProvidedCHO", "Agent", "Place", 
					"Timespan", "Concept", "Aggregation", "Proxy", 
					"EuropeanaAggregation"}
	);
	private static Map<String, Map<String, String>> map;
	
	public static List<String> getMapNames() {
		return mapNames;
	}

	public static Map<String, Map<String, String>> getFullMap() {
		for (String mapName : mapNames) {
			getMap(mapName);
		}
		return map;
	}

	public static Map<String, String> getMap(String mapName) {
		if (map == null) {
			map = new LinkedHashMap<String, Map<String, String>>();
		}
		
		if (!mapNames.contains(mapName)) {
			return null;
		}
		
		if (!map.containsKey(mapName)) {
			if (mapName.equals("document")) {
				map.put(mapName, new LinkedHashMap<String, String>(){
					private static final long serialVersionUID = 1L;
					{
						put("edm:ProvidedCHO", "providedCHOs");
						put("edm:Agent", "agents");
						put("edm:Place", "places");
						put("edm:TimeSpan", "timespans");
						put("skos:Concept", "concepts");
						put("ore:Aggregation", "aggregations");
						put("edm:Proxy", "proxies");
						put("edm:EuropeanaAggregation", "europeanaAggregation");
					}
				});
			}

			// TODO: move elsewhere as object type
			if (mapName.equals("objects")) {
				map.put(mapName, new LinkedHashMap<String, String>(){
					private static final long serialVersionUID = 1L;
					{
						put("providedCHOs", "ProvidedCHO");
						put("agents", "Agent");
						put("places", "Place");
						put("timespans", "TimeSpan");
						put("concepts", "Concept");
						put("aggregations", "Aggregation");
						put("proxies", "Proxy");
						put("europeanaAggregation", "EuropeanaAggregation");
						put("webResources", "WebResource");
					}
				});
			}

			else if (mapName.equals("ProvidedCHO")) {
				// TODO: change the value for the map as FieldInfo, which contains a bunch of things, 
				// like field type etc.
				// so we need something like this: 
				// put("about", new FieldInfo("@rdf:about", "String" ...)
				map.put(mapName, new LinkedHashMap<String, String>(){
					private static final long serialVersionUID = 1L;
					{
						put("@rdf:about", "about");
						put("owl:sameAs", "owlSameAs");
						put("[id]", "id");
					}
				});
			}

			else if (mapName.equals("WebResource")) {
				map.put(mapName, new LinkedHashMap<String, String>(){
					private static final long serialVersionUID = 1L;
					{
						put("@rdf:about", "about");
						put("dc:description", "dcDescription");
						put("dc:format", "dcFormat");
						put("dc:rights", "webResourceDcRights");
						put("dc:source", "dcSource");
						put("dcterms:conformsTo", "dctermsConformsTo");
						put("dcterms:created", "dctermsCreated");
						put("dcterms:extent", "dctermsExtent");
						put("dcterms:hasPart", "dctermsHasPart");
						put("dcterms:isFormatOf", "dctermsIsFormatOf");
						put("dcterms:issued", "dctermsIssued");
						put("edm:isNextInSequence", "isNextInSequence");
						put("edm:rights", "webResourceEdmRights");
						put("[id]", "id");
					}
				});
			}

			else if (mapName.equals("Agent")) {
				map.put(mapName, new LinkedHashMap<String, String>(){
					private static final long serialVersionUID = 1L;
					{
						put("@rdf:about", "about");
						put("skos:prefLabel", "prefLabel");
						put("skos:altLabel", "altLabel");
						put("skos:note", "note");
						put("dc:date", "dcDate");
						put("dc:identifier", "dcIdentifier");
						put("edm:begin", "begin");
						put("edm:end", "end");
						put("edm:hasMet", "edmHasMet");
						put("edm:isRelatedTo", "edmIsRelatedTo");
						put("foaf:name", "foafName");
						put("rdaGr2:biographicalInformation", "rdaGr2BiographicalInformation");
						put("rdaGr2:dateOfBirth", "rdaGr2DateOfBirth");
						put("rdaGr2:dateOfDeath", "rdaGr2DateOfDeath");
						put("rdaGr2:dateOfEstablishment", "rdaGr2DateOfEstablishment");
						put("rdaGr2:dateOfTermination", "rdaGr2DateOfTermination");
						put("rdaGr2:gender", "rdaGr2Gender");
						put("rdaGr2:professionOrOccupation", "rdaGr2ProfessionOrOccupation");
						put("owl:sameAs", "owlSameAs");
						put("[edmWasPresentAt]", "edmWasPresentAt");
						put("[id]", "id");
					}
				});
			}

			else if (mapName.equals("Place")) {
				map.put(mapName, new LinkedHashMap<String, String>(){
					private static final long serialVersionUID = 1L;
					{
						put("@rdf:about", "about");
						put("wgs84_pos:lat", "latitude");
						put("wgs84_pos:long", "longitude");
						put("wgs84_pos:alt", "altitude");
						put("skos:prefLabel", "prefLabel");
						put("skos:altLabel", "altLabel");
						put("skos:note", "note");
						put("dcterms:hasPart", "dcTermsHasPart");
						put("dcterms:isPartOf", "isPartOf");
						put("owl:sameAs", "owlSameAs");
						put("[id]", "id");
					}
				});
			}

			else if (mapName.equals("TimeSpan")) {
				map.put(mapName, new LinkedHashMap<String, String>(){
					private static final long serialVersionUID = 1L;
					{
						put("@rdf:about", "about");
						put("skos:prefLabel", "prefLabel");
						put("skos:altLabel", "altLabel");
						put("skos:note", "note");
						put("dcterms:hasPart", "dcTermsHasPart");
						put("dcterms:isPartOf", "isPartOf");
						put("edm:begin", "begin");
						put("edm:end", "end");
						put("owl:sameAs", "owlSameAs");
						put("[crmP79FBeginningIsQualifiedBy]", "crmP79FBeginningIsQualifiedBy");
						put("[crmP80FEndIsQualifiedBy]", "crmP80FEndIsQualifiedBy");
						put("[id]", "id");
					}
				});
			}

			else if (mapName.equals("Concept")) {
				map.put(mapName, new LinkedHashMap<String, String>(){
					private static final long serialVersionUID = 1L;
					{
						put("@rdf:about", "about");
						put("skos:prefLabel", "prefLabel");
						put("skos:altLabel", "altLabel");
						put("skos:broader", "broader");
						put("skos:narrower", "narrower");
						put("skos:related", "related");
						put("skos:broadMatch", "broadMatch");
						put("skos:narrowMatch", "narrowMatch");
						put("skos:relatedMatch", "relatedMatch");
						put("skos:exactMatch", "exactMatch");
						put("skos:closeMatch", "closeMatch");
						put("skos:note", "note");
						put("skos:notation", "notation");
						put("skos:inScheme", "inScheme");
						put("[id]", "id");
					}
				});
			}

			else if (mapName.equals("Aggregation")) {
				map.put(mapName, new LinkedHashMap<String, String>(){
					private static final long serialVersionUID = 1L;
					{
						put("@rdf:about", "about");
						put("edm:aggregatedCHO", "aggregatedCHO");
						put("edm:dataProvider", "edmDataProvider");
						put("edm:hasView", "hasView");
						put("edm:isShownAt", "edmIsShownAt");
						put("edm:isShownBy", "edmIsShownBy");
						put("edm:object", "edmObject");
						put("edm:provider", "edmProvider");
						put("dc:rights", "dcRights");
						put("edm:rights", "edmRights");
						put("edm:ugc", "edmUgc");
						put("[edmPreviewNoDistribute]", "edmPreviewNoDistribute");
						put("[webResources]", "webResources");
						put("[aggregates]", "aggregates");
						put("[edmUnstored]", "edmUnstored");
						put("[id]", "id");
					}
				});
			}

			else if (mapName.equals("Proxy")) {
				map.put(mapName, new LinkedHashMap<String, String>(){
					private static final long serialVersionUID = 1L;
					{
						put("@rdf:about", "about");
						put("dc:contributor", "dcContributor");
						put("dc:coverage", "dcCoverage");
						put("dc:creator", "dcCreator");
						put("dc:date", "dcDate");
						put("dc:description", "dcDescription");
						put("dc:format", "dcFormat");
						put("dc:identifier", "dcIdentifier");
						put("dc:language", "dcLanguage");
						put("dc:publisher", "dcPublisher");
						put("dc:relation", "dcRelation");
						put("dc:rights", "dcRights");
						put("dc:source", "dcSource");
						put("dc:subject", "dcSubject");
						put("dc:title", "dcTitle");
						put("dc:type", "dcType");
						put("dcterms:alternative", "dctermsAlternative");
						put("dcterms:conformsTo", "dctermsConformsTo");
						put("dcterms:created", "dctermsCreated");
						put("dcterms:extent", "dctermsExtent");
						put("dcterms:hasFormat", "dctermsHasFormat");
						put("dcterms:hasPart", "dctermsHasPart");
						put("dcterms:hasVersion", "dctermsHasVersion");
						put("dcterms:isFormatOf", "dctermsIsFormatOf");
						put("dcterms:isPartOf", "dctermsIsPartOf");
						put("dcterms:isReferencedBy", "dctermsIsReferencedBy");
						put("dcterms:isReplacedBy", "dctermsIsReplacedBy");
						put("dcterms:isRequiredBy", "dctermsIsRequiredBy");
						put("dcterms:issued", "dctermsIssued");
						put("dcterms:isVersionOf", "dctermsIsVersionOf");
						put("dcterms:medium", "dctermsMedium");
						put("dcterms:provenance", "dctermsProvenance");
						put("dcterms:references", "dctermsReferences");
						put("dcterms:replaces", "dctermsReplaces");
						put("dcterms:requires", "dctermsRequires");
						put("dcterms:spatial", "dctermsSpatial");
						put("dcterms:tableOfContents", "dctermsTOC");
						put("dcterms:temporal", "dctermsTemporal");
						put("edm:currentLocation", "edmCurrentLocation");
						put("edm:hasMet", "edmHasMet");
						put("edm:hasType", "edmHasType");
						put("edm:incorporates", "edmIncorporates");
						put("edm:isDerivativeOf", "edmIsDerivativeOf");
						put("edm:isNextInSequence", "edmIsNextInSequence");
						put("edm:isRelatedTo", "edmIsRelatedTo");
						put("edm:isRepresentationOf", "edmIsRepresentationOf");
						put("edm:isSimilarTo", "edmIsSimilarTo");
						put("edm:isSuccessorOf", "edmIsSuccessorOf");
						put("edm:realizes", "edmRealizes");
						put("edm:type", "edmType");
						/*
						put("edm:europeanaProxy", "europeanaProxy");
						put("ore:proxyFor", "proxyFor");
						put("ore:proxyIn", "proxyIn");
						put("[edmUnstored]", "edmUnstored");
						put("[edmRights]", "edmRights");
						put("[edmWasPresentAt]", "edmWasPresentAt");
						put("[id]", "id");
						*/
						// MISSING implementations:
						// put("edm:userTag", "???");
						// put("edm:year", "???");
						// put("owl:sameAs", "???");
					}
				});
			}

			else if (mapName.equals("EuropeanaAggregation")) {
				map.put(mapName, new LinkedHashMap<String, String>(){
					private static final long serialVersionUID = 1L;
					{
						put("@rdf:about", "about");
						put("dc:creator", "dcCreator");
						put("edm:aggregatedCHO", "aggregatedCHO");
						put("edm:country", "edmCountry");
						put("edm:hasView", "edmHasView");
						put("edm:isShownBy", "edmIsShownBy");
						put("edm:landingPage", "edmLandingPage");
						put("edm:language", "edmLanguage");
						put("edm:rights", "edmRights");
						put("ore:aggregates", "aggregates");
						put("edm:WebResource", "webResources");
						put("[id]", "id");
					}
				});
			}
		}

		return map.get(mapName);
	}

}
