<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="facets-actions" class="sidebar">
	
	<!-- breaadcrumbs -->

	<%@ include file="/WEB-INF/jsp/default/_common/html/sidebar/filters.jspf" %>

	<!-- facets -->
	<c:if test="${!empty model.briefBeanView.facetQueryLinks}">
	
		<h2><spring:message code="RefineYourSearch_t" />:</h2>
		
		

				
		<ul id="filter-search">
			<li>
				<h3>
					<a class="facet-section icon-arrow-6 active">
						<spring:message code="AddKeywords_t" />
					</a>
				</h3>

				<form id="refine-search-form" method="get" action="${query_action}">
				
					<input type="hidden" name="query" value="${model.query}"/>
					<c:forEach var="refinement" items="${model.refinements}">
						<input type="hidden" name="qf" value="${refinement}"/>
					</c:forEach>
					
					<ul id="refinements">
						<li>
							<input type="text" name="qf"/>
							<span>
								<input class="submit" type="submit" value="<spring:message code="AddKeywordsSubmitLabel_t" />">
							</span>
						</li>
					</ul>
					
				</form>
			</li>
		
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
					<a id="save-search" class="share-section" rel="nofollow">
						<span class="icon-saveditem"></span>
						<spring:message code="SaveToMyEuropeana_t" />
					</a>
					
					<c:if test="${!empty model.briefBeanView}">
						<input type="hidden" id="query-to-save" value="${model.briefBeanView.pagination.presentationQuery.queryToSave}"/>
					</c:if>
					<c:if test="${!empty model.query}">
						<input type="hidden" id="query-string-to-save" value="${fn:escapeXml(model.query)}"/>
					</c:if>					
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
				<a id="shares-link" class="icon-share share-section" rel="nofollow">
					<span title="<spring:message code="Share_item_link_alt_t" />"><spring:message code="Share_item_link_t" /></span>
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
