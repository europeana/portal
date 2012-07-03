<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:if test='{model.returnTo == "BD" && model.fullBeanView.docIdWindowPager.next}'>
	<li>
		<a href="${model.fullBeanView.docIdWindowPager.nextFullDocUrl}" title="<spring:message code="Next_t" />" ><spring:message code="Next_t" /></a>
	</li>
</c:if>