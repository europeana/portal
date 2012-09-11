<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  escape all localized messages from the message.properties file
  in order to make sure they do not break the javascript strings
  first need convert spring.messages into freemarker variables
  as the ?js_string won't apply to returned macro strings
--%>
<c:set var="ajax_date_retrieval_error"><spring:message code="AjaxDataRetrievalError_t" /></c:set>
<c:set var="close"><spring:message code='Close_Button_t' /></c:set>
<c:set var="date"><spring:message code='dc_date_t' /></c:set>
<c:set var="error_occurred"><spring:message code="AnErrorOccurred_t" /></c:set>
<c:set var="initial_rows" value='' />
<c:if test="${!empty RequestParameters.initial_rows}">
<c:set var="initial_rows" value="${RequestParameters.initial_rows}" />
</c:if>
<c:set var="item_not_removed"><spring:message code="ItemNotRemoved_t" /></c:set>
<c:set var="mapview_noresults"><spring:message code="MapViewNoResults_t" /></c:set>
<c:set var="matches"><spring:message code='MatchesFor_t' /></c:set>
<c:set var="no_saved_items"><spring:message code='NoSavedItems_t' /></c:set>
<c:set var="no_saved_searches"><spring:message code='NoSavedSearches_t' /></c:set>
<c:set var="no_saved_tags"><spring:message code='NoSavedTags_t' /></c:set>
<c:set var="required_field"><spring:message code='RequiredField_t' /></c:set>
<c:set var="results"><spring:message code='Results_t' /></c:set>
<c:set var="return_to_language"><spring:message code="ReturnToOriginalLanguage_t" /></c:set>
<c:set var="rows" value='' />
<c:if test="${!empty RequestParameters.rows}">
<c:set var="rows" value='${RequestParameters.rows}' />
</c:if>
<c:set var="sample_map_data" value='false' />
<c:if test="${RequestParameters.sample_map_data}">
<c:set var="sample_map_data" value='${RequestParameters.sample_map_data}' />
</c:if>
<c:set var="save_item_failed"><spring:message code="ItemSaveFailed_t" /></c:set>
<c:set var="save_tag_failed"><spring:message code="TagAdditionFailed_t" /></c:set>
<c:set var="saved_item"><spring:message code="ItemSaved_t" /></c:set>
<c:set var="saved_item_removed"><spring:message code="SavedItemRemoved_t" /></c:set>
<c:set var="saved_search_removed"><spring:message code="SavedSearchRemoved_t" /></c:set>
<c:set var="saved_tag"><spring:message code="SavedTags_t" /></c:set>
<c:set var="saved_tag_removed"><spring:message code="SavedTagRemoved_t" /></c:set>
<c:set var="search_error"><spring:message code="ErrorTryAnotherSearch_t" /></c:set>
<c:set var="search_saved"><spring:message code='SearchSaved_t' /></c:set>
<c:set var="search_save_failed"><spring:message code='SearchSavedFailed_t' /></c:set>
<c:set var="see_less"><spring:message code="SeeLess_t" /></c:set>
<c:set var="see_more"><spring:message code="SeeMore_t" /></c:set>
<c:set var="select_language"><spring:message code="SelectLanguage_t" /></c:set>
<c:set var="startFrom" value="" />
<c:if test="${!empty RequestParameters.startFrom}">
<c:set var="startFrom" value="${RequestParameters.startFrom}" />
</c:if>
<c:set var="translate_with"><spring:message code="essTranslateWith_t" /></c:set>
<c:set var="timeline_result_limit_exceeded"><spring:message code="timelineDisclaimer_t" /></c:set>

<%-- Citation (fulldoc) --%>
<c:set var="citation_tab_citation"><spring:message code="Cite_Tab_Title_Citation_t" /></c:set>
<c:set var="citation_tab_footnote"><spring:message code="Cite_Tab_Title_Footnote_t" /></c:set>

<%-- Map Controls --%>
<c:set var="mapview_zoom"><spring:message code="mapview_zoom_t" /></c:set>
<c:set var="mapview_zoomIn"><spring:message code="mapview_zoomIn_t" /></c:set>
<c:set var="mapview_zoomOut"><spring:message code="mapview_zoomOut_t" /></c:set>
<c:set var="mapview_zoomToSel"><spring:message code="mapview_zoomIntoSelection_t" /></c:set>
<c:set var="mapview_chooseMapType"><spring:message code="mapview_chooseMapType_t" /></c:set>
<c:set var="mapview_navigateMap"><spring:message code="mapview_navigateMap_t" /></c:set>
<c:set var="mapview_freeForm"><spring:message code="mapview_freeForm_t" /></c:set>
<c:set var="mapview_noresult_1"><spring:message code="NoMapItemsFound1_t" /></c:set>
<c:set var="mapview_noresult_2"><spring:message code="NoMapItemsFound2_t" /></c:set>

<c:if test="${model[maxMapResults] != null}">
<c:set var="map_limit">${model[maxMapResults]}</c:set>
<c:set var="map_limit_array" value="${map_limit}" />
<c:set var="mapview_result_limit_exceeded"><@spring.messageArgs "MapDisclaimer_t" map_limit_array/></c:set>
</c:if>

<c:set var="mapview_no_place_available"><spring:message code="MapNoPlaceAvailable_t" /></c:set>
<c:set var="mapview_layers_tiles_attribution"><spring:message code="mapview_tiles_attribution_t" /></c:set>

<%-- Map Layers --%>
<c:set var="mapview_layers_google_satellite"><spring:message code="mapview_layer_google_satellite_t" /></c:set>
<c:set var="mapview_layers_google_hybrid"><spring:message code="mapview_layer_google_hybrid_t" /></c:set>
<c:set var="mapview_layers_google_street"><spring:message code="mapview_layer_google_street_t" /></c:set>
<c:set var="mapview_layers_google_physical"><spring:message code="mapview_layer_google_physical_t" /></c:set>
<c:set var="mapview_layers_osm_tiles"><spring:message code="mapview_layer_osm_tiles_t" /></c:set>

<c:set var="search_query">
<c:choose><c:when test="${model.query}">true</c:when><c:otherwise>false</c:otherwise></c:choose>
</c:set>
<c:set var="search_addthis_pubid">
<c:choose>
<c:when test="${!empty model.addThisId}">${model.addThisId}</c:when>
<c:otherwise></c:otherwise>
</c:choose>
</c:set>

<script>
window.eu = { europeana : { vars : { msg : { cite:{} }, item : {}, mapview : {} } } };
window.js = {
  min_suffix      : '',
  min_directory   : '',
  cache_helper  : '',
  empty_console : {
    log   : function() {},
    error : function() {},
    info  : function() {}
  }
};

if ( !window.console ) { window.console = js.empty_console; }
js.console = js.empty_console;

<c:if test="${model.debug}">
js.debug = true;
js.cache_helper = '?' + new Date().getTime();
js.console = window.console;
</c:if>

<c:if test="${!empty model.minify && model.minify}">
js.min_suffix = '.min';
js.min_directory = 'min/';
</c:if>

eu.europeana.vars.portal_name = '${model.portalName}';
eu.europeana.vars.page_name = '${model.pageName}';
eu.europeana.vars.branding = '/${branding}';
eu.europeana.vars.gaId = '${model.googleAnalyticsId}';
eu.europeana.vars.locale = '${model.locale}';
eu.europeana.vars.msg.search_error = '${search_error}';
eu.europeana.vars.addthis_pubid = '${search_addthis_pubid}';
eu.europeana.vars.query = ${search_query};

// url variables to check for timeline / map content 
eu.europeana.vars.timeline = {};

<c:if test="${model[mapJsonUrl]}">
eu.europeana.vars.mapview.json_url = '${model.mapJsonUrl}';
</c:if>

<c:if test="${model[jsonUrlTimeline]}">
eu.europeana.vars.timeline.json_url = '${model.jsonUrlTimeline}';
</c:if>

<c:choose>
<c:when test="${model.pageName == 'index.html'}">
eu.europeana.vars.pinterest = {};
eu.europeana.vars.pinterest.item = {};
eu.europeana.vars.pinterest.europeana = '${model.pinterestUrl}';
<c:if test="${!empty model[pinterestItem]}">
eu.europeana.vars.pinterest.item.title = '${model.pinterestItem.title}';
eu.europeana.vars.pinterest.item.description = '${model.pinterestItem.descriptionFull}';
eu.europeana.vars.pinterest.item.link = '${model.pinterestItem.link}';
</c:if>
</c:when>

<c:when test="${model.pageName == 'full-doc.html'}">
eu.europeana.vars.msg.translate_with = '${translate_with}';
eu.europeana.vars.msg.return_to_language = '${return_to_language}';
eu.europeana.vars.msg.more = '${see_more}';
eu.europeana.vars.msg.less = '${see_less}';
eu.europeana.vars.msg.select_language = '${select_language}';

<c:if test="${model[googleTranslateId]}">
eu.europeana.vars.google_translate_key = '${model.googleTranslateId}';
</c:if>

eu.europeana.vars.bing_translate_key = '${model.bingTranslateId}';
eu.europeana.vars.msg.cite.citation = '${citation_tab_citation}';
eu.europeana.vars.msg.cite.footnote = '${citation_tab_footnote}';

<c:if test="${model[document.positionAvailable]}">
eu.europeana.vars.mapview.kml_url = '${model.document.urlKml}';
</c:if>

<c:if test="${!empty model.user}">
eu.europeana.vars.msg.error_occurred = '${error_occurred}';
eu.europeana.vars.msg.saved_item = '${saved_item}';
eu.europeana.vars.msg.save_item_failed = '${save_item_failed}';
eu.europeana.vars.msg.saved_tag = '${saved_tag}'; 
eu.europeana.vars.msg.save_tag_failed = '${save_tag_failed}';         
eu.europeana.vars.item.uri = '${model.document.id}';
</c:if>

<c:if test="${model[lightboxRef]}">
eu.europeana.vars.lightbox_swf = '/${branding}/js/jwplayer/mediaplayer-5.8/player.swf';
eu.europeana.vars.lightbox_rights = '<@displayRights true/>';
</c:if>
</c:when>

<c:when test="${model.pageName == 'myeuropeana.html'}">
<c:if test="${!empty model.user}">
eu.europeana.vars.msg.error_occurred = '${error_occurred}';
eu.europeana.vars.msg.item_not_removed = '${item_not_removed}';
eu.europeana.vars.msg.saved_search_removed = '${saved_search_removed}';
eu.europeana.vars.msg.no_saved_searches = '${no_saved_searches}';
eu.europeana.vars.msg.saved_item_removed = '${saved_item_removed}';
eu.europeana.vars.msg.no_saved_items = '${no_saved_items}';
eu.europeana.vars.msg.saved_tag_removed = '${saved_tag_removed}';
eu.europeana.vars.msg.no_saved_tags = '${no_saved_tags}';
</c:if>
</c:when>

<c:when test="${model.pageName == 'search.html'}">
eu.europeana.vars.msg.required = '${required_field}';
eu.europeana.vars.msg.close = '${close}';
eu.europeana.vars.msg.search_saved = '${search_saved}';
eu.europeana.vars.msg.search_save_failed = '${search_save_failed}';
</c:when>

<c:when test="${model.pageName == 'map.html'}">
<%-- localisation of results and map controls --%>
eu.europeana.vars.msg.results = '${results}';

eu.europeana.vars.mapview.noresults1 = '${mapview_noresult_1}';
eu.europeana.vars.mapview.noresults2 = '${mapview_noresult_2}';
eu.europeana.vars.google_maps_key = '${model.googleMapsId}';
eu.europeana.vars.msg.zoom = '${mapview_zoom}';
eu.europeana.vars.msg.zoomIn = '${mapview_zoomIn}';
eu.europeana.vars.msg.zoomOut = '${mapview_zoomOut}';
eu.europeana.vars.msg.zoomToSel = '${mapview_zoomToSel}';
eu.europeana.vars.msg.chooseMapType = '${mapview_chooseMapType}';
eu.europeana.vars.msg.navigateMap = '${mapview_navigateMap}';
eu.europeana.vars.msg.freeForm = '${mapview_freeForm}';
eu.europeana.vars.msg.matches = '${matches}';

eu.europeana.vars.mapview.limit = parseInt("${map_limit}".replace(",", ""));
eu.europeana.vars.mapview.limit_exceeded = '${mapview_result_limit_exceeded}';
eu.europeana.vars.mapview.no_place = '${mapview_no_place_available}';
eu.europeana.vars.mapview.tiles_attribution = '${mapview_layers_tiles_attribution}';

var labels = {}, mapping = {};
labels.googleSatellite = 'Google Satellite'; 
labels.googleHybrid = 'Google Hybrid';
labels.googleStreet = 'Google Streets';
labels.googlePhysical = 'Google Physical';
labels.openStreetMapTiles = 'OSM Tiles@Home';

mapping[labels.googleSatellite] = '${mapview_layers_google_satellite}';
mapping[labels.googleHybrid] = '${mapview_layers_google_hybrid}';
mapping[labels.googleStreet] = '${mapview_layers_google_street}';
mapping[labels.googlePhysical] = '${mapview_layers_google_physical}';
mapping[labels.openStreetMapTiles] = '${mapview_layers_osm_tiles}';

eu.europeana.vars.mapview.layers = {'mapping':mapping, 'labels':labels};

<c:choose>
<c:when test="${!empty model.mapJsonUrl && 'true' != sample_map_data}">
eu.europeana.vars.mapview.json_url = '${model.mapJsonUrl}';
eu.europeana.vars.mapview.json_noresult1 = '${mapview_noresult_1}';
eu.europeana.vars.mapview.json_noresult2 = '${mapview_noresult_2}';
</c:when>

<c:otherwise>
eu.europeana.vars.mapview.json_url = '/${branding}/js/sti/e4D-javascript/krucifix.json';
</c:otherwise>
</c:choose>
</c:when>

<c:when test="${model.pageName == 'timeline.html'}">
eu.europeana.vars.msg.ajax_data_retrieval_error = '${ajax_date_retrieval_error}';
eu.europeana.vars.limit_exceeded = '${timeline_result_limit_exceeded}';
eu.europeana.vars.event_source_url = '${model.jsonUrlTimeline}';
eu.europeana.vars.msg.results = '${results}';
eu.europeana.vars.msg.matches = '${matches}';
eu.europeana.vars.msg.date = '${date}';
eu.europeana.vars.startFrom = '${startFrom}';
eu.europeana.vars.rows = '${rows}';
eu.europeana.vars.initial_rows = '${initial_rows}';
</c:when>
</c:choose>
</script>
