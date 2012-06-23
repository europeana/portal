<!-- navigation -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul id="navigation" class="navigation notranslate">
<c:choose>
	<c:when test="${!empty model.fullBeanView.docIdWindowPager}">
		<%@ include file="/WEB-INF/jsp/default/fulldoc/content/navigation/return.jsp" %>
		<%@ include file="/WEB-INF/jsp/default/fulldoc/content/navigation/next.jsp" %>
		<%@ include file="/WEB-INF/jsp/default/fulldoc/content/navigation/previous.jsp" %>
	</c:when>
	<c:otherwise>
		<li>&nbsp;</li>
	</c:otherwise>
</c:choose>
</ul>
<!-- /navigation -->
