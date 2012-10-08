<!DOCTYPE html>
<%@ include file="/WEB-INF/jsp/_common/html-tag.jsp" %>
<head>
<meta charset="utf-8"/>
<%@ include file="/WEB-INF/jsp/_common/meta.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/html/links.jsp" %>


<title>${model.pageTitle}</title>

<script type="text/javascript" src="https://app.e2ma.net/app2/audience/tts_signup/1722088/1c99e86abb6bc30f8e24592a87471334/1403149/?v=a"></script>

</head>
<body>

<c:if test="${not empty model.announceMsg}">${model.announceMsg}</c:if>
