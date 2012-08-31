<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<div class="twelve columns">


	<h4 id="collapse-header-1">
		<span class="left">
			<spring:message code='from_the_blog_t'/>
		</span>
		<span class="collapse-icon"></span>
		<a class="feed-link" href="http://blog.europeana.eu/feed/" target="_blank" title="RSS Feed"></a>		
	</h4>
	
	<div class="collapse-content">
		
		<c:choose>
			<c:when test='${not empty model.feedEntries}'>
				<c:forEach items="${model.feedEntries}" var="entry" varStatus="status">
					<c:if test="${status.index < 2}">
					
					
					
					  <h3><a href="${entry.link}" target="_self">${entry.title}</a></h3>
					  <p>${entry.description}</p>
					  
					  IMG = <p>${entry.images[0] }</p> (end img)
					
					  
					  
					</c:if>
				</c:forEach>
			</c:when>
			<c:otherwise>
			  <%--
			    fallback content in case the blog feed is not working
			    # or if the portal server blog feed cache is cleared after a server restart
			  --%>
			  <h3><a href="<spring:message code='notranslate_blog-item-1_a_url'/>" target="<spring:message code='notranslate_blog-item-1_a_target'/>"><spring:message code='notranslate_blog-item-1_h3'/></a></h3> 
			  <p><spring:message code='notranslate_blog-item-1_p'/></p>
			  <h3><a href="<@spring.message 'notranslate_blog-item-2_a_url'/>" target="<spring:message code='notranslate_blog-item-2_a_target'/>"><spring:message code='notranslate_blog-item-2_h3'/></a></h3> 
			  <p><spring:message code='notranslate_blog-item-2_p'/></p>  
			  <h3><a href="<spring:message code='notranslate_blog-item-3_a_url'/>" target="<spring:message code='notranslate_blog-item-3_a_target'/>"><spring:message code='notranslate_blog-item-3_h3'/></a></h3> 
			  <p><spring:message code='notranslate_blog-item-3_p'/></p>
			</c:otherwise>
		</c:choose>
	</div>
</div>
	