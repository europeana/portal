<!-- navigation -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul>
<c:choose>
	<c:when test="${!empty model.fullBeanView.docIdWindowPager}">
		<%@ include file="/WEB-INF/jsp/vanilla/fulldoc/content/navigation/return.jsp" %>
		<%@ include file="/WEB-INF/jsp/vanilla/fulldoc/content/navigation/next.jsp" %>
		<%@ include file="/WEB-INF/jsp/vanilla/fulldoc/content/navigation/previous.jsp" %>
	</c:when>
	<c:otherwise>
		<li>&nbsp;</li>
	</c:otherwise>
</c:choose>
</ul>
<!-- /navigation -->
