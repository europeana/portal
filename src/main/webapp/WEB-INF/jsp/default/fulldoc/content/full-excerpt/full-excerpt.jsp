<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>

<div id="excerpt">
	<c:set var="about" value=""/>
	<c:if test="${not empty model['document']}">
	<c:url var="aboutUrl" value="/record${model.document.about}.html" />
		<c:set var="about" value="${aboutUrl}" />
	</c:if>

	<div id="item-details" about="${about}" vocab="http://schema.org/" typeof="CreativeWork">
		<div class="sidebar-right hide-on-phones">
			<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/see-also.jspf" %>
		</div>

		<c:set var="schemaOrgMapping" value="${model.schemaOrgMapping['dc:title']}" />
		<c:set var="schemaOrgElement" value="${schemaOrgMapping.element}" />
		<c:set var="edmElement" value="${schemaOrgMapping.edmElement}" />
		<c:set var="semanticAttributes">
			${"property=\""}${schemaOrgElement.elementName}${" "}${edmElement.fullQualifiedURI}${"\""}
		</c:set>
		<h1 class="hide-on-phones" ${semanticAttributes}>${model.objectTitle}</h1>

		<c:forEach items="${model.document.dcTitle}" var="title">
			<c:if test="${title != model.objectTitle }">
				<div class="item-metadata">
					<span class="bold notranslate"><spring:message code="dc_title_t" />:</span>
					<span class="translate" ${semanticAttributes}>${title}</span>
				</div>
			</c:if>
		</c:forEach>

		<%--
			model.metaDataFileds = a collection of all metadata on the object pre-formated for <meta> element output in the <head>
			model.formatLabels = a boolean that is triggered via the url query string &format=labels
			the macro @displayEseDataAsMeta will output the meta data in its typical <meta> format or
			in html viable format if &format=labels is present in the query sting
			
			model.fields = a subset collection of meta data pre-formated for html presentation
			model.additionalFields = a subset collection of meta data pre-formated for html presentation
			model.model.enrichmentFields = a subset collection of meta data pre-formated for html presentation
		--%>

		<%-- 
			${model.formatLabels}
			${!empty model.metaDataFields}
			${!empty model.fields}
			${fn:length(model.fields) > 0}
		--%>

		<c:if test="${!model.formatLabels && !empty model['fields'] && fn:length(model.fields) > 0}">
			<europeana:displayEseDataAsHtml listCollection="${model.fields}" wrapper="div" ugc="false" ess="true" />
		</c:if>
		<c:if test="${!empty model['fieldsAdditional']}">
			<europeana:displayEseDataAsHtml listCollection="${model.fieldsAdditional}" wrapper="div" ugc="${model.document.userGeneratedContent}" ess="true" />
		</c:if>

		<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/fields-enrichment.jspf" %>

		<c:if test="${model.formatLabels}">
			<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/schema.jspf" %>
		</c:if>
	</div>
</div>

<div class="sidebar-right show-on-phones">
	<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/see-also.jspf" %>
</div>