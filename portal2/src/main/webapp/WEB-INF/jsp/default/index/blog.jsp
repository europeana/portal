<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div class="twelve columns">
	<h3 id="collapse-header-1">
		<span class="left collapse-header-text">
			<spring:message code="from_the_blog_t" />
		</span>
		<span class="collapse-icon"></span>
		<a class="feed-link icon-rss" href="${model.blogFeedUrl}" target="_blank" title="RSS Feed"></a>
	</h3>
	<div class="row collapse-content">
	</div>
</div>
	