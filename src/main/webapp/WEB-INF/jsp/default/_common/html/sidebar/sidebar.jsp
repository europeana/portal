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

				<form id="refine-search-form" method="get" action="${query_action}" onsubmit="return eu.europeana.search.checkKeywordSupplied()">
					<input type="hidden" name="query" value="<c:out value="${model.query}"/>"/>
					<input type="hidden" name="rows" id="rows" value="${model.rows}"/>
					<c:forEach var="refinement" items="${model.refinements}">
						<input type="hidden" name="qf" value="${refinement}"/>
					</c:forEach>

					<ul id="refinements">
						<li>
							<input id="newKeyword" type="text" name="qf"/>
							<span>
								<input class="submit deans-button-1" type="submit" value="<spring:message code="AddKeywordsSubmitLabel_t" />">
							</span>
						</li>
					</ul>

				</form>
			</li>

			<c:set var="rowsParam" value=""/>
			<c:if test="${!empty model.rows}">
				<c:set var="rowsParam" value="&rows=${model.rows}"/>
			</c:if>

			<%--
			<c:set var="startParam" value=""/>
			<c:if test="${!empty model.rows}">
				<c:set var="startParam" value="&start=${model.start}"/>
			</c:if>
			--%>

			<c:forEach var="facet" items="${model.briefBeanView.facetQueryLinks}">
				<%@ include file="/WEB-INF/jsp/default/_common/macros/facet-sections.jsp" %>
			</c:forEach>

			<li class="ugc-li">
				<h3>
				<%--
					<c:set var="ugcClassName">
						<c:choose>
							<c:when test="${model.UGCFilter}">icon-yes</c:when>
							<c:otherwise>icon-no</c:otherwise>
						</c:choose>
					</c:set>
					
					<a class="facet-ugc ${ugcClassName}" rel="nofollow" href="${model.UGCUrl}">
						<spring:message code="IncludeUGC_t" />
					</a>
				 --%>

					<c:set var="checkedValue" value='checked="checked"' />
					<c:choose>
						<c:when test="${model.UGCFilter}">
							<c:set var="checkedValue" value='' />
						</c:when>
					</c:choose>

					<input type="checkbox" ${checkedValue} id="cb-ugc" name="cb-ugc"/>

					<a  href="${model.UGCUrl}"
						title="${model.UGCUrl}" rel="nofollow">
						<label for="cb-ugc" style="display:inline"> &nbsp;<spring:message code="IncludeUGC_t" /></label>
					</a>

				</h3>
			</li>
		</ul>
		<!-- /facets -->

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

			<%--
			<li>
				<a class="share-section icon-print">
					<span class="action-title">Print</span>
				</a>
			</li>
			<li>
			
				<a class="share-section icon-rss">
					<span class="action-title">Subscribe via RSS</span>
					
				</a>
			</li>
			 --%>
			<li id="shares-link" class="share-section">
				<span class="icon-share" title="<spring:message code="Share_item_link_alt_t" />"> </span><span class="action-title" title="<spring:message code="Share_item_link_alt_t" />"><spring:message code="Share_item_link_t" /></span>
			</li>
			<%--
			<li>
				<a class="share-section icon-mail">
					<span class="action-title">Mail</span>
				</a>
			</li>
			 --%>
			<li class="stretch"></li>
		</ul>
	</c:if>

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
