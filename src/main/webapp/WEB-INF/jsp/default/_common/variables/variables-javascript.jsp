<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%--
	escape all localized messages from the message.properties file
	in order to make sure they do not break the javascript strings
	first need convert spring.messages into freemarker variables
	as the ?js_string won't apply to returned macro strings
--%>
<c:set var="ajax_date_retrieval_error"><spring:message code="AjaxDataRetrievalError_t" /></c:set>
<c:set var="close"><spring:message code="Close_Button_t" /></c:set>
<c:set var="date"><spring:message code="dc_date_t" /></c:set>
<c:set var="error_occurred"><spring:message code="AnErrorOccurred_t" /></c:set>
<c:set var="initial_rows" value='' />
<c:if test="${!empty RequestParameters.initial_rows}">
	<c:set var="initial_rows" value="${RequestParameters.initial_rows}" />
</c:if>
<c:set var="item_not_removed"><spring:message code="ItemNotRemoved_t" /></c:set>
<c:set var="matches"><spring:message code="MatchesFor_t" /></c:set>
<c:set var="no_saved_items"><spring:message code="NoSavedItems_t" /></c:set>
<c:set var="no_saved_searches"><spring:message code="NoSavedSearches_t" /></c:set>
<c:set var="no_saved_tags"><spring:message code="NoSavedTags_t" /></c:set>
<c:set var="required_field"><spring:message code="RequiredField_t" /></c:set>
<c:set var="results"><spring:message code="Results_t" /></c:set>
<c:set var="return_to_language"><spring:message code="ReturnToOriginalLanguage_t" /></c:set>
<c:set var="rows" value='' />

<%--
<c:if test="${!empty RequestParameters.rows}">
	<c:set var="rows" value='${RequestParameters.rows}' />
</c:if>
 --%>


<c:set var="rows">
 <%= request.getParameter("rows") %>
</c:set>


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
<c:set var="search_saved"><spring:message code="SearchSaved_t" /></c:set>
<c:set var="search_save_failed"><spring:message code="SearchSavedFailed_t" /></c:set>
<c:set var="see_less"><spring:message code="SeeLess_t" /></c:set>
<c:set var="see_more"><spring:message code="SeeMore_t" /></c:set>
<c:set var="select_language"><spring:message code="SelectLanguage_t" /></c:set>
<c:set var="startFrom" value="" />
<c:if test="${!empty RequestParameters.startFrom}">
	<c:set var="startFrom" value="${RequestParameters.startFrom}" />
</c:if>
<c:set var="translate_with"><spring:message code="essTranslateWith_t" /></c:set>


<%-- Citation (fulldoc) --%>

<%-- Andy: these two no longer in use: --%>
<c:set var="citation_tab_citation"><spring:message code="Cite_Tab_Title_Citation_t" /></c:set>
<c:set var="citation_tab_footnote"><spring:message code="Cite_Tab_Title_Footnote_t" /></c:set>

<%-- Andy: these two are in use: --%>
<c:set var="citation_header"><spring:message code="Cite_Header_t" /></c:set>

<c:set var="search_addthis_pubid">
	<c:choose>
		<c:when test="${!empty model.addThisId}">${model.addThisId}</c:when>
		<c:otherwise></c:otherwise>
	</c:choose>
</c:set>


<script type="text/javascript">
<!--
window.eu = { europeana : { vars : { msg : { cite:{} }, item : {}, suppresResize: false } } };
window.js = {
	min_suffix      : '',
	min_directory   : '',
	cache_helper	: '',
	empty_console	: {
		log		: function() {},
		error	: function() {},
		info	: function() {}
	}
};

if ( !window.console ) { window.console = js.empty_console; }
js.console = js.empty_console;


<c:if test="${!empty model.debug && model.debug}">
	js.debug = true;
	js.cache_helper = '?' + new Date().getTime();
	js.console = window.console;
</c:if>

<c:if test="${!empty model.debug && !model.debug}">
	js.debug = false;
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
eu.europeana.vars.query = '${fn:escapeXml(model.query)}';

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
		eu.europeana.vars.galleria = {};
		<c:choose>
			<c:when test="${!empty model.debug && model.debug}">
				eu.europeana.vars.galleria.css = 'galleria.europeanax.css';
			</c:when>
			<c:otherwise>
				eu.europeana.vars.galleria.css = 'galleria.europeanax.min.css';
			</c:otherwise>
		</c:choose>
	</c:when>
	
	<c:when test="${model.pageName == 'full-doc.html'}">
		eu.europeana.vars.msg.translate_with = '${fn:escapeXml(translate_with)}';
		eu.europeana.vars.msg.return_to_language = '${fn:escapeXml(return_to_language)}';
		eu.europeana.vars.msg.more = '${see_more}';
		eu.europeana.vars.msg.less = '${see_less}';
		eu.europeana.vars.msg.select_language = '${select_language}';
		<c:if test="${model[googleTranslateId]}">
			eu.europeana.vars.google_translate_key = '${model.googleTranslateId}';
		</c:if>
		eu.europeana.vars.bing_translate_key = '${model.bingTranslateId}';
		eu.europeana.vars.msg.cite.citation = '${citation_tab_citation}';
		eu.europeana.vars.msg.cite.footnote = '${citation_tab_footnote}';
		eu.europeana.vars.msg.cite.citation_header	= '${fn:escapeXml(citation_header)}';
		eu.europeana.vars.msg.cite.close			= '${fn:escapeXml(close)}';
		eu.europeana.vars.galleria = {};
		
		<c:choose>
			<c:when test="${!empty model.debug && model.debug}">
				eu.europeana.vars.galleria.css = 'galleria.europeanax.css';
			</c:when>
			<c:otherwise>
				eu.europeana.vars.galleria.css = 'galleria.europeanax.min.css';
			</c:otherwise>
		</c:choose>

		<%-- Translation data for lightbox triggers: map lightboxble type to message --%>
		
		eu.europeana.vars.external = {
			triggers: {
				labels : {
					'image'		:	'<spring:message code="view_t"		/>',
					'play'		:	'<spring:message code="play_t"		/>',
					'download'	:	'<spring:message code="download_t"	/>',
					'pdf'		:	'<spring:message code="read_t"		/>',
					'sound'		:	'<spring:message code="play_t"		/>',
					'video'		:	'<spring:message code="play_t"		/>',
					'text'		:	'<spring:message code="read_t"		/>',
				}
			}
		};
		
		
		<c:if test="${!empty model.user}">
			eu.europeana.vars.msg.error_occurred = '${error_occurred}';
			eu.europeana.vars.msg.saved_item = '${saved_item}';
			eu.europeana.vars.msg.save_item_failed = '${save_item_failed}';
			eu.europeana.vars.msg.saved_tag = '${saved_tag}';	
			eu.europeana.vars.msg.save_tag_failed = '${save_tag_failed}';
			eu.europeana.vars.item.uri = '${model.document.about}';
		</c:if>

		<c:if test="${model[lightboxRef]}">
			eu.europeana.vars.lightbox_swf = '/${branding}/js/jwplayer/mediaplayer-5.8/player.swf';
			eu.europeana.vars.lightbox_rights = '<@displayRights true/>';
		</c:if>
	</c:when>
	
	
	<c:when test="${model.pageName == 'login.html'}">
		<c:choose>
			<c:when test="${!empty model.user}">
				eu.europeana.vars.user = true;
			</c:when>
			<c:otherwise>
				eu.europeana.vars.user = false;
			</c:otherwise>
		</c:choose>
	</c:when>
	
	
	<c:when test="${model.pageName == 'myeuropeana.html'}">
		<c:choose>
			<c:when test="${!empty model.user}">
				eu.europeana.vars.user = true;
				eu.europeana.vars.msg.error_occurred = '${error_occurred}';
				eu.europeana.vars.msg.item_not_removed = '${item_not_removed}';
				eu.europeana.vars.msg.saved_search_removed = '${saved_search_removed}';
				eu.europeana.vars.msg.no_saved_searches = '${no_saved_searches}';
				eu.europeana.vars.msg.saved_item_removed = '${saved_item_removed}';
				eu.europeana.vars.msg.no_saved_items = '${no_saved_items}';
				eu.europeana.vars.msg.saved_tag_removed = '${saved_tag_removed}';
				eu.europeana.vars.msg.no_saved_tags = '${no_saved_tags}';
			</c:when>
			<c:otherwise>
				eu.europeana.vars.user = false;
			</c:otherwise>
		</c:choose>

	</c:when>

	<c:when test="${model.pageName == 'search.html'}">
		eu.europeana.vars.msg.required = '${fn:escapeXml(required_field)}';
		eu.europeana.vars.msg.close = '${fn:escapeXml(close)}';
		eu.europeana.vars.msg.search_saved = '${fn:escapeXml(search_saved)}';
		eu.europeana.vars.msg.search_save_failed = '${fn:escapeXml(search_save_failed)}';
	</c:when>

</c:choose>

eu.europeana.vars.rows = '${rows}';

<%-- 'for share-this' --%>
var switchTo5x = true;
if ( window.stLight ) { stLight.options({publisher : '${model.shareThisId}'}); }

//-->
</script>