<!DOCTYPE html>
<%@ include file="/WEB-INF/jsp/_common/html-tag.jsp" %>
<head>
<meta charset="utf-8"/>
<%@ include file="/WEB-INF/jsp/_common/meta.jsp" %>
<%@ include file="/WEB-INF/jsp/vanilla/_common/links.jsp" %>
<%@ include file="/WEB-INF/jsp/vanilla/_common/scripts-head.jsp" %>

<title>${model.pageTitle}</title>
</head>
<body>
<c:if test="${not empty model.announceMsg}">${model.announceMsg}</c:if>
