
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>


<div id="additional-info" class="sidebar" about="${model.document.id}">

	<h1 id="phone-object-title" class="show-on-phones">${model.objectTitle}</h1>

<%--
	<%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/image.jsp" %>
 --%>


	<div id="carousel-1">
		<script type="text/javascript">
			var carouselData = [];
			carouselData[carouselData.length] = {
				image:			decodeURI("${model.thumbnailUrl}").replace(/&amp;/g, '&'),
				title:			"${model.objectTitle}"
			};
		</script>
	</div>
	
	<%@ include file="/WEB-INF/jsp/default/fulldoc/macros/rights.jsp" %>

    <%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/original-context.jsp" %>
	
	<c:if test="${ !empty model.fieldsAdditional}">
		<europeana:displayEseDataAsHtml listCollection="${model.fieldsAdditional}" wrapper="div" ugc="${model.document.userGeneratedContent}" ess="true" />
	</c:if>


</div>