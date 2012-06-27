<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="pintenterestEuropeana" value="http://www.pinterest.com/europeana" />

<div class="tout right">
	<div id="pinterest-wrapper">
		<h2 class="pinterest-header"><spring:message code="recent_pinterest_activities_t" /><a class="pinterest-button" a target="blank" href="${pintenterestEuropeana}">&nbsp;<img src="${branding}/images/icons/Pinterest_Logo.png" width="70" height="18" alt="Follow Me on Pinterest" /></a></h2>
		<div id="pinterest" class="rssFeed"></div>
	</div>
</div>
