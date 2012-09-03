<!-- featured-item -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="tout">
	<c:if test="${!empty model.featuredItem}">
		<h2><spring:message code="${model.featuredItem.heading}" /></h2>
		<a href="/${model.portalName}<spring:message code="${model.featuredItem.anchorUrl}" />" title="<spring:message code="${model.featuredItem.anchorTitle}" />" target="<spring:message code="${model.featuredItem.anchorTarget}" />" class="image"><img src="/${model.portalName}<spring:message code="${model.featuredItem.imgUrl}" />" alt="<spring:message code="${model.featuredItem.imgAlt}" />" /></a>
		<p><jsp:include page="/WEB-INF/jsp/devel/_common/macros/msg-property-limiter.jsp">
				<jsp:param name="msg_property_tag" value="${model.featuredItem.p}" />
				<jsp:param name="length_limit" value="${featured_item_snipet_limit}" />
			</jsp:include></p>
	</c:if>
</div>
<!-- featured-item -->
