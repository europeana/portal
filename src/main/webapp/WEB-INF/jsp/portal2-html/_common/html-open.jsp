<!DOCTYPE html>
<%@ include file="/WEB-INF/jsp/portal2-html/_common/html-tag.jsp" %>
<head>
<meta charset="utf-8"/>
<%@ include file="/WEB-INF/jsp/portal2-html/_common/meta.jsp" %>
<%@ include file="/WEB-INF/jsp/portal2-html/_common/links.jsp" %>
<c:if test="${model.theme == 'portal2-html-css' || model.theme == 'portal2-html-css-js'}">
<%@ include file="/WEB-INF/jsp/portal2-html-css/_common/links-debug.jsp" %>
</c:if>
<c:if test="${model.theme == 'portal2-html-css-js'}">
<%@ include file="/WEB-INF/jsp/portal2-html-css-js/_common/links-debug.jsp" %>
</c:if>
<title>${model.pageTitle}</title>
</head>
<body>
<c:if test="${not empty model.announceMsg}">${model.announceMsg}</c:if>