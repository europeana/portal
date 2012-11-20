<html

<c:choose>
	<c:when test="${model.pageName == 'full-doc.html' }">
		xmlns:cc="http://creativecommons.org/ns#"
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
