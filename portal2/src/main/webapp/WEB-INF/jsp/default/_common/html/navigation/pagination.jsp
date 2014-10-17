<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="eu" tagdir="/WEB-INF/tags" %>

<c:if test="${!empty model.briefBeanView && !empty model.briefBeanView.pagination.presentationQuery.queryForPresentation}">
	<c:set var="pageSizeOps">12,24,48,96</c:set>
	<c:if test="${isSearchWidget}">
		<c:set var="pageSizeOps">6,12,18</c:set>
	</c:if>
	<div class="${position_class} nav">
		<c:if test="${!model.embedded}">
			<div class="menu show-on-22">
				<span><spring:message code="ResultsPerPage_t" />:</span>
				<div class="eu-menu" aria-hidden="true"   title="<spring:message code="ResultsPerPageTooltip_t" />">
					<span class="menu-label"></span>
					<span class="icon-arrow-3 open-menu"></span>
					<ul>
						<c:forEach var="size" items="${pageSizeOps}">
							<li class="${model.rows == size ? 'item active' : 'item'}">	<a href="" class="${size}">${size}</a></li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</c:if>

		<div class="count">
			<span>
				<spring:message code="Results_t" />&nbsp;
				<span class="first-vis-record">${model.briefBeanView.pagination.start}</span><span class="spaced">-</span><span class="last-vis-record">${model.briefBeanView.pagination.lastViewableRecord}</span>	
				<span class="of"> <spring:message code="Of_t" /> </span>
				<span class="of-bracket last-record"><fmt:formatNumber 
					value="${model.briefBeanView.pagination.numFound}" type="NUMBER" maxFractionDigits="0"
				/></span>
			</span>
		</div>

		<div class="pagination">
			<ul class="result-pagination">
				<%-- first arrow --%>
				<li class="nav-first">
					<c:if test="${!model.briefBeanView.pagination.first || isSearchWidget}" >
						<a href="${model.firstPageUrl}" title="<spring:message code="AltFirstPage_t" />">&lt;&lt; &nbsp;</a>
					</c:if>
				</li>

				<%-- previous arrow --%>
				<li class="nav-prev">
					<c:if test="${model.briefBeanView.pagination.previous || isSearchWidget}">
						<a href="${model.previousPageUrl}" title="<spring:message code="AltPreviousPage_t" />">&nbsp; &lt; &nbsp; </a>
					</c:if>
				</li>

				<li class="nav-numbers">
				
				
					<form method="get" action="${query_action}?" class="jump-to-page">
						<%--
						<input type="number" id="start-page" value="${model.pageNumber}" min="1" max="${model.numberOfPages}" pattern="[0-9]*" /> of ${model.numberOfPages}
						 --%>

						<c:forEach items="${model.queryTranslationLinks}" var="qt" varStatus="status">
							<c:if test="${fn:length(qt.languageCode) > 0}">
								<input type="hidden" name="qt" value="0:5:${qt.languageCode}:${qt.text}"/>
							</c:if>
						</c:forEach>

						<input type="text" id="start-page" value="${model.pageNumber}" />
						<span class="of"> <spring:message code="Of_t" /> </span>
						<span class="of-bracket">
							${model.numberOfPages}
						</span>

						<input type="hidden" name="start" id="start" />
						<input type="hidden" name="rows" id="rows" value="${model.rows}" />
						<input type="hidden" name="query" value="<c:out value="${model.query}" />" />
						
						<c:if test="${model.languagesRemoved}">
							<input type="hidden" name="qt" id="qt" value="false"/>
						</c:if>
						
						<c:forEach items="${model.refinements}" var="qf">
							<input type="hidden" name="qf" value="<c:out value="${qf}" />" />
						</c:forEach>
						<input type="hidden" id="max-rows" value="${model.numberOfPages}" />
						<input type="submit" style="position: absolute; left: -9999px; width: 1px; height: 1px;" />
					</form>
				</li>

				<%-- next arrow --%>
				<li class="nav-next">
					<c:if test="${model.briefBeanView.pagination.next || isSearchWidget}">
						<a href="${model.nextPageUrl}" title="<spring:message code="AltNextPage_t" />"> &nbsp; &gt; &nbsp;</a>
					</c:if>
				</li>

				<%-- last arrow --%>
				<li class="nav-last">
					<c:if test="${!model.briefBeanView.pagination.last || isSearchWidget}">
						<a href="${model.lastPageUrl}" title="<spring:message code="AltLastPage_t" />">&nbsp; &gt;&gt;</a>
					</c:if>
				</li>
			</ul>
		</div>
	</div>
</c:if>
