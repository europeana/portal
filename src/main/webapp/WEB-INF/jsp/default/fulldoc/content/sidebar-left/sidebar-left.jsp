<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<c:set var="about" value=""/>
<c:if test="${not empty model.document['id']}">
	<c:set var="about" value="${model.document.id}"/>
</c:if>

<div id="additional-info" class="sidebar" about="${about}">

	<h2 id="phone-object-title" class="show-on-phones" aria-hidden="true">${model.objectTitle}</h2>
		
	<%-- thumbnail (hidden seo image) --%>
	
	
	<%-- 
		model.thumbnailUrl = ${model.thumbnailUrl} 
	--%>
	
		
	<div id="carousel-1-img-measure">
	
		<!-- TODO: make sure all item images are listed here -->
		<c:set var="thumbnail"	value=""/>
		<c:set var="dataType"	value=""/>
		<c:set var="alt"		value=""/>
		
		
		
		<c:if test="${not empty model['thumbnailUrl']}">
			<%--c:set var="thumbnail" value="${model.thumbnailUrl}"/--%>
			<c:set var="thumbnail" value="${fn:escapeXml(model.thumbnailUrl)}"/>
			
									
									
		</c:if>

		<c:if test="${not empty model.document['edmType']}">
			<c:set var="dataType" value="${fn:toLowerCase(model.document.edmType)}"/>
		</c:if>

		<c:if test="${not empty model['pageTitle']}">
			<c:set var="alt" value="${fn:escapeXml(model.pageTitle)}"/>
		</c:if>

		<img	src			= "${thumbnail}"
				alt			= "${alt}"
				data-type	= "${dataType}"
				class		= "no-show"/>
	</div>

	<%--"dataType":		"${fn:toLowerCase(model.document.edmType)}"--%>
		
	<div id="carousel-1" class="europeana-bordered">
		<script type="text/javascript">
			var carouselData = [];
			<c:if test="${!empty model.allImages}">
			
				<%-- single img (other than thumbnail) used to test if we can create a carousel --%>
				<c:if test="${fn:length(model.allImages) > 1}">
					var carouselTest = [
						{"src": decodeURI("${model.allImages[0].full}").replace(/&amp;/g, '&')},
						{"src": decodeURI("${model.allImages[1].full}").replace(/&amp;/g, '&')}
					];
				</c:if>
			
				<c:forEach items="${model.allImages}" var="image">
					carouselData[carouselData.length] = {
						"image":		decodeURI("${image.full}").replace(/&amp;/g, '&'),
						"title":		('${fn:escapeXml(model.objectTitle)}'),
						"dataType":		"${fn:toLowerCase(dataType)}"
					};
					
					<c:if test="${image.full != model.thumbnailUrl}">
						carouselData[carouselData.length-1].lightboxable = {
							"url" 	: "${image.full}",
							"type"	: "${fn:toLowerCase(image.type)}"
						}
					</c:if>
						
<%-- double data to test carousel init--%>					
				
<%--					
var carouselTest = [
					{"src": decodeURI("${model.allImages[0].full}").replace(/&amp;/g, '&')},
					{"src": decodeURI("${model.allImages[1].full}").replace(/&amp;/g, '&')}
				];
						
carouselData[carouselData.length] = {
	"image":		decodeURI("${image.full}").replace(/&amp;/g, '&'),
	"title":		('${fn:escapeXml(model.objectTitle)}'),
	"dataType":		"${fn:toLowerCase(dataType)}"
	
};

<c:if test="${image.full != model.thumbnailUrl}">
	carouselData[carouselData.length-1].lightboxable = {
		"url" 	: "${image.full}",
		"type"	: "${fn:toLowerCase(image.type)}"
	}
	
</c:if>
	--%>

				</c:forEach>
			</c:if>
	
		</script>
	</div>

		
		
	<div class="original-context">
	
		<%-- Rights --%>
		<%@ include file="/WEB-INF/jsp/default/fulldoc/macros/rights.jsp" %>
		
		<%-- Original context link --%>
		<%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/original-context.jsp" %>
		
		<br>
		
		<div class="clear"></div>
		
	</div>
		
	<div class="actions">	
		
		
		<%-- Shares link --%>
		
		<a id="shares-link" class="icon-share action-link" rel="nofollow">
			<span class="action-title" title="<spring:message code="Share_item_link_alt_t" />"><spring:message code="Share_item_link_t" /></span>
		</a>
		
		<%-- Citation link --%>
		
		<a href="" id="citation-link" class="icon-cite action-link" title="<spring:message code="AltCiteInfo_t" />" rel="nofollow">
			<span class="action-title"><spring:message code="Cite_Button_t" /></span>
		</a>
		
		<div id="citation">
			<c:if test="${not empty model['citeStyles']}">
				<c:forEach items="${model.citeStyles}" var="citeStyle">
					<div class="header">
						<div class="heading"><spring:message code="Cite_Header_t" />
							<a href="" class="close-button icon-remove" title="<spring:message code="Close_Button_t" />" rel="nofollow">&nbsp;</a>
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
			</c:if>
		</div>
		
		<%-- Embed link --%>
		<%--
		<a href="" id="item-embed" class="icon-embed action-link" title="<spring:message code="embed_t" />" rel="nofollow">
			<span class="action-title"><spring:message code="embed_t" /></span>
		</a>
		--%>
			
		<%--
		<div id="embed-link-wrapper">
			<a href="${model.embedRecordUrl}" id="item-embed" class="block-link bold" target="_blank" rel="nofollow"><spring:message code="embed_t" /></a>
		</div>		
		--%>

		<%-- Save page to myeuropeana --%>

		<c:if test="${!empty model.user}">
		
			<c:set var="savedIcon" value="icon-unsaveditem" />
					
			<c:forEach items="${model.user.savedItems}" var="item">
				<c:if test="${model.document.about == item.europeanaUri}">
					<c:set var="savedIcon" value="icon-saveditem" />
				</c:if>
			</c:forEach>
		
			<a href="" id="item-save" rel="nofollow" class="${savedIcon} action-link">
				<span class="action-title"><spring:message code="SaveToMyEuropeana_t" /></span>
			</a>
			
		</c:if>
	
		<%-- Add tag --%>
		
		<c:if test="${!empty model.user}">
			<form id="item-save-tag">
				<fieldset>
					<label for="add-tag" class="icon-tag">
						<span class="action-title"><spring:message code="AddATag_t" /></span>
					</label>
					
					<br />
					
					<div style="display:table">
						<input type="text" id="item-tag" maxlength="50" />
						<span>
							<input type="submit" class="submit-button deans-button-1" value="<spring:message code="Add_t" />" />
						</span>
					</div>
					
				</fieldset>
			</form>
		</c:if>
	
		<c:choose>
			<c:when test="${fn:contains(model.currentUrl, '&format=label')}">
				<c:set var="switchlabelLink">${fn:replace(model.currentUrl, '&format=label', '')}</c:set>
				<c:set var="switchlabelTitle">Normal format</c:set>
			</c:when>
			<c:otherwise>
				<c:set var="switchlabelLink">${model.currentUrl}&format=label</c:set>
				<c:set var="switchlabelTitle">Label format</c:set>
			</c:otherwise>
		</c:choose>
	
		<%-- Format labels --%>
		<c:if test="${model.debug}">		
			<a href="${switchlabelLink}" id="format-link" class="icon-info action-link" title="${switchlabelTitle}" rel="nofollow">
				<span class="action-title">${switchlabelTitle}</span>
			</a>
		</c:if>
			
		<span class="stretch"></span>
			
		<div class="clear"></div>
			
	</div>
	
	
	<div id="translate-container">
	
 		<!-- translate services -->
		<a href="" id="translate-item" class="bold">
			<spring:message code="TranslateDetails_t" />
			<span class="iconP"></span>
		</a>
	</div>

</div>