<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>

<c:set var="isSearchWidget" value="true"/>

<c:choose>
	<c:when test="${!empty param.noAjax}">

		<div class="container">
			<%@ include file="/WEB-INF/jsp/default/_common/header.jsp" %>
			<%@ include file="/WEB-INF/jsp/default/search/content/content.jsp" %>
		</div>

	</c:when>
	<c:otherwise>

		searchWidget.init({
			"markup":
			'<compress:html enabled="true" compressJavaScript="false"  yuiJsDisableOptimizations="true">
				<div class="container">
					<%@ include file="/WEB-INF/jsp/default/_common/header.jsp" %>
					<%@ include file="/WEB-INF/jsp/default/search/content/content.jsp" %>
				</div>
			</compress:html>'
		});

	</c:otherwise>
</c:choose>




