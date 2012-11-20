<html

<c:choose>
	<c:when test="${model.pageName == 'full-doc.html' }">
		xmlns="http://www.w3.org/1999/xhtml"
		xmlns:dcterms="http://purl.org/dc/terms/"
		xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
		xmlns:europeana="http://www.europeana.eu/schemas/ese/"
		xmlns:cc="http://creativecommons.org/ns#"


		xmlns:enrichment="http://www.europeana.eu/schemas/ese/enrichment/"
		xmlns:dc="http://purl.org/dc/elements/1.1/"
	</c:when>
</c:choose>
	>

<head>
	<meta charset="utf-8" />
	<title>${model.pageTitle}</title>
	<%@ include file="/WEB-INF/jsp/default/_common/html/links.jsp" %>
	<%@ include file="/WEB-INF/jsp/default/_common/html/meta.jsp" %>
	
</head>
<body class="locale-${model.locale}">
