<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:choose>
	<c:when test="${RequestParameters.more_meta_data}">
		<c:set var="more_meta_data" value="${RequestParameters.more_meta_data}" />
	</c:when>
	<c:otherwise>
		<c:set var="more_meta_data" value="false" />
	</c:otherwise>
</c:choose>

<c:set var="ess_id_count" value="0" />

<c:if test="${model[lightboxRef]}">
	<c:set var="lightboxRef">${model.lightboxRef}</c:set>
</c:if>