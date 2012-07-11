<!-- additional-info -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<a href="${cell.fullDocUrl}" title="${fn:join(cell.title, ', ')}" ${targetArg} class="result-additional-info" rel="nofollow">


	<c:if test="${!empty cell.dcCreator}">
		<%-- TODO: find out what is the original cell.creatorXML --%>
		<!-- cell.dcCreator -->
		<c:set var="dc_creators">
			<c:set var="sep" value="" />
			<c:forEach var="dc_creator" items="${cell.dcCreator}">
				<c:out value="${sep}" /><c:out value="${dc_creator}" />
				<c:set var="sep" value=", " />
			</c:forEach>
		</c:set>
		<c:out value="${fn:substring(dc_creators, 0, providerNameMaxLength)}" /><br />
	</c:if>

	<c:if test="${!empty cell.year}">
		<!-- cell.year -->
		<c:set var="sep" value="" />
		<c:forEach var="year" items="${cell.year}">
			<c:if test='${year != "0000"}'>
				<c:out value="${sep}" /><c:out value="${year}" />
				<c:set var="sep" value=", " />
			</c:if>
		</c:forEach>
		<!-- /cell.year -->
		<br />
	</c:if>

	<c:set var="providerMaxLength" value="25" />
	<c:if test="${empty model.embedded}">
		<c:set var="providerMaxLength" value="80" />
	</c:if>

	<c:if test="${!empty cell.dataProvider}">
		<!-- cell.dataProvider -->
		<c:forEach var="dataProviderFull" items="${cell.dataProvider}">
			<c:set var="dataProviderShort" value="${dataProviderFull}" />
			<c:if test="${fn:length(dataProviderShort) > providerMaxLength}">
				<c:set var="dataProviderShort" value="${fn:substring(dataProviderShort, 0, providerMaxLength)}..." />
			</c:if>
			<span title="${dataProviderFull}">${dataProviderShort}</span>
		</c:forEach>
		<br />
	</c:if>
	
	<c:if test="${!empty cell.provider}">
		<!-- cell.provider -->
		<c:forEach var="cell_provider" items="${cell.provider}">
			<c:set var="provider" value="${cell_provider}" />
			<c:if test="${fn:length(provider) > providerMaxLength}">
				<c:set var="provider" value="${fn:substring(cell_provider, 0, providerMaxLength)}..." />
			</c:if>
			<span title="${cell_provider}">${provider}</span>
		</c:forEach>
		<br />
	</c:if>
</a>
<!-- /additional-info -->
