<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="xmlns" value='' />
<c:if test="${model.pageName == 'full-doc.html' }">
  <c:set var="xmlns" value='xmlns:cc="http://creativecommons.org/ns#"'/>
</c:if>

<!--[if IE 8]>
<html  ${xmlns} class="ie ie8" lang="${lang}">
<![endif]-->

<!--[if IE 9]>
	<html ${xmlns} class="ie ie9" lang="${lang}">
<![endif]-->

<!--[if !IE]>-->
<html ${xmlns} lang="${lang}">
<!--<![endif]-->

<c:set var="schemaOrgAttributes" value="" />
<c:if test="${model.pageName == 'full-doc.html'}">
  <c:set var="schemaOrgAttributes"> about="${model.document.cannonicalUrl}" vocab="http://schema.org/" typeof="CreativeWork"</c:set>
</c:if>

<head<c:if test="${schemaOrgAttributes != ''}">${" "}${schemaOrgAttributes}</c:if>>
  <meta charset="utf-8" />
  <title>${model.pageTitle}</title>
  <%@ include file="/WEB-INF/jsp/default/_common/html/links.jsp" %>
  <%@ include file="/WEB-INF/jsp/default/_common/html/meta.jsp" %>
</head>
<body class="locale-${model.locale}">

<c:if test="${model.pageName == 'index.html'}">
  <c:if test="${not empty model.announceMsg}">${model.announceMsg}</c:if>
</c:if>