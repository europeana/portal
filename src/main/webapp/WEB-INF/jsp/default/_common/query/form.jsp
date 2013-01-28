<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- query action --%>
<c:set var="query_action" value="/${model.portalName}/search.html"/>

<%-- query value --%>
<c:set var="query_value" value=""/>
<c:if test="${not empty model.query}">
	<c:set var="query_value" value="${model.query}"/>
</c:if>

<%-- form --%>
	<form id="query-search" action="${query_action}" method="get">
		<table cellspacing="0" cellpadding="0" class="no-show">
			<tr>
				<td class="menu-cell">
					<div id="search-menu" class="eu-menu" aria-hidden="true">
	
						<span class="hide-ilb-on-phones menu-label"><spring:message code='Search_t'/></span>
						<span class="icon-arrow-3 open-menu"></span>
						<ul>
							<li class="item">
								<a	href=""><spring:message code='FieldedSearchAllFields_t' /></a>
							</li>
							<li class="item">
								<a	href=""
									class="title:"><spring:message code='FieldedSearchTitle_t' /></a>
							</li>
							<li class="item">
								<a	href=""
									class="who:"><spring:message code='FieldedSearchWho_t' /></a>
							</li>
							<li class="item">
								<a	href=""
									class="what:"><spring:message code='FieldedSearchWhat_t' /></a>
							</li>
							<li class="item">
								<a	href=""
									class="when:"><spring:message code='FieldedSearchWhen_t' /></a>
							</li>
							<li class="item">
								<a	href=""
									class="where:"><spring:message code='FieldedSearchWhere_t' /></a>
							</li>
						</ul>
					</div>
				</td>
				<td class="query-cell">
					<input	type="text"
							name="query"
							role="search"
							id="query-input"
							maxlength="175"
							title="<spring:message code='SearchTerm_t'/>"
							value="<c:out value="${model.query}"/>"
							valueForBackButton="<c:out value="${model.query}"/>" />
				</td>
				<td class="submit-cell hide-cell-on-phones">
					<button	class="icon-mag deans-button-1"
							type="submit"><spring:message code='Search_t'/></button>
				</td>
			</tr>
			<tr>
				<td colspan="3" class="submit-cell show-cell-on-phones">
					<button	class="icon-mag deans-button-1"
							type="submit"><spring:message code='Search_t'/></button>
				</td>
			</tr>
		</table>

		<%-- embedded search --%>
		<c:if test="${model.embedded}">
			<input type="hidden" name="embedded" value="${model.embeddedString}"/>
			<input type="hidden" name="embeddedBgColor" value="${model.embeddedBgColor}"/>
			<input type="hidden" name="embeddedForeColor" value="${model.embeddedForeColor}"/>
			<input type="hidden" name="embeddedLogo" value="${model.embeddedLogo}"/>
      <c:forEach items="${model.refinements}" var="qf">
        <input type="hidden" name="qf" value="${qf}"/>
      </c:forEach>
			<input type="hidden" name="rswUserId" value="${model.rswUserId}"/>
			<input type="hidden" name="rswDefqry" value="${model.rswDefqry}"/>
			<input type="hidden" name="lang" value="${model.locale}"/>
		</c:if>

		<input type="hidden" name="rows" id="rows" value="${model.rows}" />
	</form>