<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="meta-data${li_class}">
	<c:if test="${!empty cell.title}">	
		<h4>${title}</h4>
	</c:if>
	
	<a href="${cell.fullDocUrl}?theme=${model.theme}" title="${cell.title}" style="float: left; margin-right: 10px;"><img src="${cell.thumbnail}" /></a>
	
	<c:if test="${!empty cell.creator}">
		<span class="creator">${fn:substring(cell.creatorXML, 0, providerNameMaxLength)}</span><br />
	</c:if>
	
	<c:if test="${!empty cell.dataProvider}">
		<span class="data-provider">${fn:substring(cell.dataProvider, 0, providerMaxLength)}</span><br />
	</c:if>
	
	<c:if test="${!empty cell.provider}">
		<span class="provider">${fn:substring(cell.provider, 0, providerMaxLength)}</span><br />
	</c:if>
	
	<c:if test='${cell.year != "" && cell.year != "0000"}'>
		<span class="year">${cell.year}</span>
	</c:if>
</div>