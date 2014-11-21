<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%-- query action --%>
<c:set var="query_action" value="/${model.portalName}/search.html"/>


<%-- form --%>
	<form id="query-search" action="${query_action}" method="get">
		<table class="query-table" cellspacing="0" cellpadding="0">
			<tr>
				
				<td class="query-cell">
					<input	type="text"
							name="query"
							role="search"
							id="query-input"
							maxlength="175"
							title="<spring:message code="SearchTerm_t" />"
							value="<c:out value="${model.query}"/>"
							valueForBackButton="<c:out value="${model.query}"/>" />
				</td>
				<td class="submit-cell hide-cell-on-phones">
					<button	class="icon-mag deans-button-1"
							type="submit"></button>
				</td>
			</tr>
			<tr>
				<td colspan="3" class="submit-cell show-cell-on-phones">
					<button	class="icon-mag deans-button-1"
							type="submit"></button>
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
			<input type="hidden" name="lang" value="${model.locale}"/>
		</c:if>

		<input type="hidden" name="rows" id="rows" value="${model.rows}" />
	</form>