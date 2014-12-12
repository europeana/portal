<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 
<c:if test="${!empty cell.thumbnail}">
	<a href="${homeUrl}${fn:replace(cell.fullDocUrl, '"', '&quot;')}&rows=${model.rows}"
		title="${cell.titleBidi[0]}"
		${targetArg}>
		<img class="thumbnail" src="${cell.thumbnail}" alt="${cell.title[0]}" data-type="${fn:toLowerCase(cell.type)}" />
	</a>
</c:if>

