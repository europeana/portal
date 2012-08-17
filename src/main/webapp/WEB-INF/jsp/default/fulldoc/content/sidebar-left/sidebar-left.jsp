
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

	<%-- Shares link --%>
	<div id="shares-link-wrapper">
		 <a href="" id="shares-link" class="block-link bold" title="<spring:message code="Share_item_link_alt_t" />" rel="nofollow"><spring:message code="Share_item_link_t" /></a>
	</div>
	
	<%-- Citation link --%>
	<div id="citation-link-wrapper">
    	<a href="" id="citation-link" class="block-link bold" title="<spring:message code="AltCiteInfo_t" />" rel="nofollow"><spring:message code="Cite_Button_t" /></a>
	</div>
	
	<div id="citation">
		<c:forEach items="${model.citeStyles}" var="citeStyle">
			<div class="header">
				<div class="heading"><spring:message code="Cite_Header_t" />
					<a href="" class="close-button" title="<spring:message code="Close_Button_t" />" rel="nofollow">&nbsp;</a>
				</div>
			</div>
		
			<div id="citations">
				<div class="citation">
					${citeStyle.citeText}
				</div>
				<div class="citation">
					&lt;ref&gt;${citeStyle.citeText}&lt;/ref&gt;
				</div>
			</div>
		</c:forEach>
	</div>


	<%-- Save page to myeuropeana --%>
	<c:if test="${model.user}">
	  <a href="" id="item-save" rel="nofollow" class="block-link bold"><spring:message code="SaveToMyEuropeana_t" /></a>
	</c:if>


	<%-- Embed link --%>
	<div id="embed-link-wrapper">
		<a href="${model.embedRecordUrl}" id="item-embed" class="block-link bold" target="_blank" rel="nofollow"><spring:message code="embed_t" /></a>
	</div>    

	<%-- Add tag --%>
    <%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/add-tag.jsp" %>

	<%-- Format labels --%>
    <%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/format-link.jspf" %>

</div>
TRANSLATE ICON (then main data on collapse)