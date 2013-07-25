<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="twelve columns">
	<h3 id="collapse-header-3">
		<span class="left collapse-header-text">
			<spring:message code="latest_on_pinterest_t" />
		</span>

		<%-- without the &nbsp before the icon span this link will not take tab focus in chrome --%>

		<a	title="<spring:message code="europeana-pinterest-title_t" />"
			href="<spring:message code="europeana-pinterest-url" />"
			target="<spring:message code="europeana-pinterest-target" />"
			rel="me">
				&nbsp;<span class="icon-pinterest left"></span>
		</a>

		<span class="collapse-icon"></span>
	</h3>

	<div class="collapse-content">
	</div>
</div>
