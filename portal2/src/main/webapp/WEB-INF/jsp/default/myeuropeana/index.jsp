<%@ include file="/WEB-INF/jsp/default/_common/include.jsp" %>

<%@ include file="/WEB-INF/jsp/default/_common/html/doctype.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/variables/variables.jsp" %>
<%@ include file="/WEB-INF/jsp/default/myeuropeana/variables/variables.jspf" %>

<%@ include file="/WEB-INF/jsp/default/_common/html/open-html.jsp" %>

	<div class="container">
		<%@ include file="/WEB-INF/jsp/default/_common/header.jsp" %>
		<c:choose>
			<c:when test="${!empty model.debug && model.debug}">
				<%@ include file="/WEB-INF/jsp/default/myeuropeana/content/index.jspf" %>
			</c:when>
			<c:otherwise>
				<div id="myeuropeana" class="row">
					<h2><spring:message code="MyEuropeana_t" /></h2>
					<ul id="panel-links" class="columns three" role="menu"></ul>
					<div class="columns nine">
						<p>New page in development.</p>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		<%@ include file="/WEB-INF/jsp/default/_common/footer.jsp" %>
	</div>

<%@ include file="/WEB-INF/jsp/default/_common/html/javascripts.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/html/close-html.jsp" %>
