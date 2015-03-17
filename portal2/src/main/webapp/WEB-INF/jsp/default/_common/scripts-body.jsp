<c:if test="${empty param.noJs}">
	<noscript><div><spring:message code="NoScript_t" />. <a href="http://www.google.com/adsense/support/bin/answer.py?hl=${model.locale}&amp;answer=12654" target="_blank"><spring:message code="FindOutMore_t" /> ...</a></div></noscript>

	<%@ include file="/WEB-INF/jsp/default/_common/variables/variables-javascript.jsp" %>
	<%-- Andy: variables-js and variables-javascript should be merged --%>
	<%--
	<%@ include file="/WEB-INF/jsp/default/_common/variables/variables-javascript.jsp" %>
	 --%>
	<c:choose>
		<c:when test="${!empty model.debug && !model.debug}">
			<script src="/themes/default/js/eu/europeana/bootstrap/min/bootstrap.min.js"></script>
		</c:when>
		<c:otherwise>
			<script src="/themes/default/js/eu/europeana/bootstrap/bootstrap.js"></script>
		</c:otherwise>
	</c:choose>
</c:if>