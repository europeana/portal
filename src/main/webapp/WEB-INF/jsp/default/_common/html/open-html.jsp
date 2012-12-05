<c:set var="xmlns" value='' />
<c:choose>
	<c:when test="${model.pageName == 'full-doc.html' }">
		<c:set var="xmlns" value='xmlns:cc="http://creativecommons.org/ns#"'/>
	</c:when>
</c:choose>

<!--[if IE 8]>
<html  ${xmlns} class="ie ie8" lang="${lang}">
<![endif]-->

<!--[if IE 9]>
	<html ${xmlns} class="ie ie9" lang="${lang}" >
<![endif]-->

<!--[if !IE]>-->
<html ${xmlns} lang="${lang}">
<!--<![endif]-->
	
<head>
	<meta charset="utf-8" />
	<title>${model.pageTitle}</title>
	<%@ include file="/WEB-INF/jsp/default/_common/html/links.jsp" %>
	<%@ include file="/WEB-INF/jsp/default/_common/html/meta.jsp" %>
	
</head>
<body class="locale-${model.locale}">

	<c:if test="${model.pageName == 'index.html' }">
		<c:if test="${not empty model.announceMsg}">${model.announceMsg}</c:if>
	</c:if>

