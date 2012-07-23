<noscript><div><spring:message code='NoScript_t'/>. <a href="http://www.google.com/adsense/support/bin/answer.py?hl=${model.locale}&amp;answer=12654" target="_blank"><spring:message code='FindOutMore_t'/> ...</a></div></noscript>
<%@ include file="/WEB-INF/jsp/default/_common/variables-js.jsp" %>
<script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.2/jquery.min.js"></script>
<script>if(!window.jQuery){document.write('<script src="themes/common/js/com/jquery/jquery-1.4.4.min.js"><\/script>');}</script>
<c:choose>
<c:when test='${model.pageName == "index.html"}'>
<script src="themes/default/js/eu/europeana/index.js"></script>
</c:when>
</c:choose>