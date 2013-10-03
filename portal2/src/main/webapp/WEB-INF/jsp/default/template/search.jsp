<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>

<%-- 
	/portal/template.html?id=search
--%>

<c:set var="isSearchWidget" value="true"/>
<c:set var="includeFacets" value="true"/>


<c:choose>
	<c:when test="${!empty param.noAjax}">

		<div class="container">
			<%@ include file="/WEB-INF/jsp/default/_common/header.jsp" %>
			<%@ include file="/WEB-INF/jsp/default/search/content/content.jsp" %>
		</div>

	</c:when>
	<c:otherwise>


		
		<c:set var="newLineChar" value="<%= \"\n\" %>" />
		<c:set var="markup">
			<div class="container">
				<script type="text/javascript">var rootUrl = "${model.portalServer}${model.portalName}";</script>
				<%@ include file="/WEB-INF/jsp/default/_common/header.jsp" %>
				<%@ include file="/WEB-INF/jsp/default/search/content/content.jsp" %>
				<div class="row"><div id="footer-logo"></div></div>
			</div>
		</c:set>
		

		searchWidget.init({
			"markup": '${fn:replace(markup, newLineChar, "")}' 
		});

	</c:otherwise>
</c:choose>




