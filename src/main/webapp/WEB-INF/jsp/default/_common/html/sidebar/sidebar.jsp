<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="facets-actions" class="sidebar">
	
	<!-- breaadcrumbs -->

	<%@ include file="/WEB-INF/jsp/default/_common/html/sidebar/filters.jspf" %>

	<!-- facets -->
	<c:if test="${!empty model.briefBeanView.facetQueryLinks}">
	
		<h2><spring:message code="RefineYourSearch_t" />:</h2>
		<%--
		<form id="xrefine-search-form" method="get" action="${query_action}">
			<ul id="refinements">
				<li>
					<input name="rq" value="notre dame"/>
				</li>

				<li>${fn:length(model.refinements)}</li>
				<li>${model.refinements}</li>
				<li>
					<input type="submit">
				</li>
			</ul>
		</form>
		--%>		
				
		<ul id="filter-search">
			<c:forEach var="facet" items="${model.briefBeanView.facetQueryLinks}">
				<%@ include file="/WEB-INF/jsp/default/_common/macros/facet-sections.jsp" %>
			</c:forEach>
	
			<li class="ugc-li">
				<h3>
					<c:set var="ugcClassName">
						<c:choose>
							<c:when test="${model.UGCFilter}">icon-yes</c:when>
							<c:otherwise>icon-no</c:otherwise>
						</c:choose>
					</c:set>
					<a class="facet-ugc ${ugcClassName}" rel="nofollow" href="${model.UGCUrl}">
						<spring:message code="IncludeUGC_t" />
					</a>
				</h3>
			</li>
		</ul>
		
		<h2>Share and Subscribe:</h2>
		
		<ul id="share-subscribe">
			<c:if test="${!empty model.user}">
				<li>
					<a class="share-section">
						<span class="icon-saveditem"></span>
						<spring:message code="SaveToMyEuropeana_t" />
					</a>
				</li>
			</c:if>
			<li>
				<a class="share-section">
					<span class="icon-print"></span>
					Print
				</a>
			</li>
			<li>
			
				<a class="share-section">
					<span class="icon-rss"></span>
					Subscribe via RSS
				</a>
			</li>
			<li>
				<a class="share-section">
					<span class="icon-share"></span>
					Share
				</a>
			</li>
			<li>
				<a class="share-section">
					<span class="icon-mail"></span>
					Mail
				</a>
			</li>
		</ul>
		
	</c:if>
	<!-- /facets -->
	
	
	<!-- legend -->
	<%--
	<h2 id="legend"><spring:message code="Legend_t" />:</h2>
	
	<div id="legend-icons">
		<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:IMAGE"	class="icon-image"	title="<spring:message code="Image_t" />"	>&nbsp;<spring:message code="Image_t" /></a>
		<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:VIDEO"	class="icon-video"	title="<spring:message code="Video_t" />"	>&nbsp;<spring:message code="Video_t" /></a>
		<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:TEXT"	class="icon-text"	title="<spring:message code="Text_t" />"	>&nbsp;<spring:message code="Text_t" /></a>
		<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:SOUND"	class="icon-audio"	title="<spring:message code="Sound_t" />"	>&nbsp;<spring:message code="Sound_t" /></a>
		<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:3D"	class="icon-3d"		title="<spring:message code="3D_t" />"		>&nbsp;<spring:message code="3D_t" /></a>
	</div>
	--%>
	<!-- end legend -->
</div>
