<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div id="excerpt">
	<div id="item-details" about="${model.document.id}">
		<h1 class="hide-on-phones">${model.objectTitle}</h1>
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
			<%@ include file="/WEB-INF/jsp/default/fulldoc/macros/display-ese-data-as-meta.jsp" %>
		</c:if>
		<c:if test="${!empty model.fields && fn:length(model.fields) > 0}">
			<%@ include file="/WEB-INF/jsp/default/fulldoc/macros/display-ese-data-as-html.jsp" %>
		</c:if>
		
		<c:if test="${ !empty model.fieldsAdditional}">
			<europeana:displayEseDataAsHtml listCollection="${model.fieldsAdditional}" wrapper="div" ugc="${model.document.userGeneratedContent}" ess="true" />
		</c:if>
		
	    <%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/fields-enrichment.jspf" %>
	
	</div>
	
	
	
</div>

<%--
<h1>ANDY: copy of ese-data-as-html</h1>

<c:if test="${!empty model.fields && fn:length(model.fields) > 0}">

<br>
<br>
<br>
<c:set var="wrapper" value="div" />
<c:set var="ugc" value="false" />
<c:set var="ess" value="true" />

	${model.objectTitle}
	
	

<script type="text/javascript">
	var sidebarRightData = [];
	sidebarRightData[sidebarRightData.length] = {
		"filterField"	: "title",
		"translation"	: "Der Title",
		"terms"			: ["${model.objectTitle}".toLowerCase()]
	};
	var filterFields = {
		"dc:type"		:	"what",
		"dc:subject"	:	"what",
		"dc:creator"	:	"who"
	};
</script>

<c:set var="sidebarRightFields" value="(dc:type)(dc:subject)(dc:creator)" />

<c:forEach items="${model.fields}" var="data">

	<c:if test="${fn:contains( sidebarRightFields, (data.fieldName) )}">

		<script type="text/javascript">

			var values = [];
			<c:forEach items="${data.fieldValues}" var="value">
				values[values.length] = "${value.value}";
			</c:forEach>
			
			sidebarRightData[sidebarRightData.length] = {
				"filterField"	: filterFields["${data.fieldName}"],
				"translation"	: "Der Title",
				"terms"			: values
			};
		</script>	
	</c:if>
</c:forEach>


</c:if>
--%>
