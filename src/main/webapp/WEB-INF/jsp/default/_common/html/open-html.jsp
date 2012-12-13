<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="xmlns" value='' />
<c:choose>
	<c:when test="${model.pageName == 'full-doc.html' }">
		<c:set var="xmlns" value='xmlns:cc="http://creativecommons.org/ns#"'/>
	</c:when>
</c:choose>

<c:set var="schemaOrgAttributes" value="" />
<c:if test="${model.pageName == 'full-doc.html'}">
  <c:url var="aboutUrl" value="/record${model.document.about}.html" />
  <c:set var="about" value="${aboutUrl}" />
  <c:set var="schemaOrgAttributes"> about="${about}" vocab="http://schema.org/" typeof="CreativeWork"</c:set>
</c:if>

<!--[if IE 8]>
<html  ${xmlns} class="ie ie8" lang="${lang}"<c:if test="${schemaOrgAttributes != ''}">${" "}${schemaOrgAttributes}</c:if>>
<![endif]-->

<!--[if IE 9]>
	<html ${xmlns} class="ie ie9" lang="${lang}"<c:if test="${schemaOrgAttributes != ''}">${" "}${schemaOrgAttributes}</c:if>>
<![endif]-->

<!--[if !IE]>-->
<html ${xmlns} lang="${lang}"<c:if test="${schemaOrgAttributes != ''}">${" "}${schemaOrgAttributes}</c:if>>
<!--<![endif]-->
	
<head>
	<meta charset="utf-8" />
	<title>${model.pageTitle}</title>
	<%@ include file="/WEB-INF/jsp/default/_common/html/links.jsp" %>
	<%@ include file="/WEB-INF/jsp/default/_common/html/meta.jsp" %>
</head>
<body class="locale-${model.locale}">

<c:if test="${model.pageName == 'index.html'}">
	<c:if test="${not empty model.announceMsg}">${model.announceMsg}</c:if>
</c:if>