<!-- excerpt -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div id="item-details" about="${model.document.id}">
	<h1>${model.objectTitle}</h1>
	<%--
		
		model.metaDataFileds = a collection of all metadata on the object pre-formated for <meta> element output in the <head>
		model.formatLabels = a boolean that is triggered via the url query string &format=labels
		the macro @displayEseDataAsMeta will output the meta data in its typical <meta> format or
		in html viable format if &format=labels is present in the query sting
		
		model.fields = a subset collection of meta data pre-formated for html presentation
		model.additionalFields = a subset collection of meta data pre-formated for html presentation
		model.model.enrichmentFields = a subset collection of meta data pre-formated for html presentation
		
	--%>
	<!-- ${model.formatLabels} -->
	<!-- ${!empty model.metaDataFields} -->
	<!-- ${!empty model.fields} -->
	<!-- ${fn:length(model.fields) > 0} -->
	<c:if test="${model.formatLabels && !empty model.metaDataFields}">
		<!-- @displayEseDataAsMeta model.metaDataFields true / -->
		<%@ include file="/WEB-INF/jsp/devel/fulldoc/macros/display-ese-data-as-meta.jsp" %>
	</c:if>
	<c:if test="${!empty model.fields && fn:length(model.fields) > 0}">
		<!-- @displayEseDataAsHtml model.fields 'div' false true / -->
		<!-- go display-ese-data-as-html -->
		<%@ include file="/WEB-INF/jsp/devel/fulldoc/macros/display-ese-data-as-html.jsp" %>
	</c:if>
</div>
<!-- /excerpt -->
