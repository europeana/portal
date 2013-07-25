<!-- blog-feed -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="tout">
	<h2><a id="blog-feed" href="http://blog.europeana.eu/feed/" target="_blank" title="RSS Feed"></a><spring:message code="from_the_blog_t" /></h2>

	<c:choose>
		<c:when test="${model[feedEntries]}">
			<c:forEach items="${model.feedEntries}" var="entry">
				<h3><a href="${entry.link}" target="_self">${entry.title}</a></h3>
				<p>${entry.description}</p>
			</c:forEach>
		</c:when>
		<c:otherwise>
		<%-- fallback content in case the blog feed is not working or if the portal server 
		     blog feed cache is cleared after a server restart --%>
			<h3><a href="<spring:message code="notranslate_blog-item-1_a_url" />" target="<spring:message code="notranslate_blog-item-1_a_target" />"><spring:message code="notranslate_blog-item-1_h3" /></a></h3>
			<p><spring:message code="notranslate_blog-item-1_p" /></p>
		
			<h3><a href="<spring:message code="notranslate_blog-item-2_a_url"/>" target="<spring:message code="notranslate_blog-item-2_a_target" />"><spring:message code="notranslate_blog-item-2_h3"/></a></h3>
			<p><spring:message code="notranslate_blog-item-2_p" /></p>
		
			<h3><a href="<spring:message code="notranslate_blog-item-3_a_url"/>" target="<spring:message code="notranslate_blog-item-3_a_target"/>"><spring:message code="notranslate_blog-item-3_h3"/></a></h3>
			<p><spring:message code="notranslate_blog-item-3_p"/></p>
		</c:otherwise>
	</c:choose>
</div>
<!-- /blog-feed -->
