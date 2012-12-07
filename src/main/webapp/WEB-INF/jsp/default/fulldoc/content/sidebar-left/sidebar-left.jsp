<%@ page import="java.net.URLDecoder"%>

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
		
	<div id="carousel-1-img-measure">
	
		<!-- TODO: make sure all item images are listed here -->
		<c:set var="thumbnail"	value=""/>
		<c:set var="dataType"	value=""/>
		<c:set var="alt"		value=""/>
		
		
		<c:if test="${not empty model['thumbnailUrl']}">
			<c:set var="thumbnail" value="${model.thumbnailUrl}"/>
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
						{"src": decodeURI("${model.allImages[0].thumbnail}").replace(/&amp;/g, '&')},
						{"src": decodeURI("${model.allImages[1].thumbnail}").replace(/&amp;/g, '&')}
					];
				</c:if>
			
				<c:forEach items="${model.allImages}" var="image">
					
					
					carouselData[carouselData.length] = {
						"image":		decodeURI("${image.thumbnail}").replace(/&amp;/g, '&'),
						"title":		('${fn:escapeXml(model.objectTitle)}'),
						"dataType":		"${fn:toLowerCase(dataType)}"
					};

					<c:if test="${fn:length(image.full) > 0}">
						carouselData[carouselData.length-1].external = {
							"url" 	: "${image.full}",
							"type"	: "${fn:toLowerCase(image.type)}"
						}
					</c:if>
				
						
// js manipulation pdf:
/*
	if( carouselData[carouselData.length-1].image == "http://europeanastatic.eu/api/image?uri=http://hdl.handle.net/10978/F475B1DF-116F-4723-AC7A-26A5FEB48367?locatt=view:level2&size=FULL_DOC"   ){
		carouselData[carouselData.length-1].external = {
				"url" 	: "http://cgil.maas.ccr.it/cgil/AJAXAttachment.ashx?resource=cgilcongenealogia/pdf/001065.pdf",
				"type"	: "pdf"
		}
	}
*/	   
    		   
    		   
    		   

<%-- double data to test carousel init--%>
<%--
var carouselTest = [
	{"src": decodeURI("${model.allImages[0].full}").replace(/&amp;/g, '&')},
];

carouselData[carouselData.length] = {
	"image":		"http://www.2fort.cz/wp-content/forum-avatars/stewie_evil%20%283%29.jpg",
	"title":		('${fn:escapeXml(model.objectTitle)}'),
	"dataType":		"${fn:toLowerCase(dataType)}"
};

<c:if test="${fn:length(image.full) > 0}">
	carouselData[carouselData.length-1].external = {
		"url" 	: "http://content6.flixster.com/question/65/91/23/6591236_std.jpg",
		"type"	: "${fn:toLowerCase(image.type)}"
	}
</c:if>
--%>
<%-- end double data to test carousel init--%>

				</c:forEach>
			</c:if>

			
// js manipulation alternative lightbox:
     

//carouselData[0].external.type = 'pdf';

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
	
	
	<div id="citation">
		<c:if test="${not empty model['citeStyles']}">
			<div id="citations">
				<c:forEach items="${model.citeStyles}" var="citeStyle"  varStatus="status">
					<c:if test="${status.first}">
						<div class="citation">
							${citeStyle.citeText}
						</div>
						<div class="citation">
							&lt;ref&gt;${citeStyle.citeText}&lt;/ref&gt;
						</div>
					</c:if>
				</c:forEach>
			</div>
		</c:if>
	</div>
		
	<div class="actions">
		
		
		<%-- Shares link --%>
		
		<div class="action-link shares-link">
			<span class="icon-share" title="<spring:message code="Share_item_link_alt_t" />"> <span class="action-title" title="<spring:message code="Share_item_link_alt_t" />"><spring:message code="Share_item_link_t" /></span>
		</div>
		
		<%-- Citation link --%>
		
		<a href="" id="citation-link" class="icon-cite action-link" title="<spring:message code="AltCiteInfo_t" />" rel="nofollow">
			<span class="action-title"><spring:message code="Cite_Button_t" /></span>
		</a>
		
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
			<c:set var="savedText">
				<spring:message code="SaveToMyEuropeana_t" />
			</c:set>
					
			<c:forEach items="${model.user.savedItems}" var="item">
				<c:if test="${model.document.about == item.europeanaUri}">
					<c:set var="savedIcon" value="icon-saveditem" />
					<c:set var="savedText">
						<spring:message code="ItemSaved_t" />
					</c:set>
				</c:if>
			</c:forEach>
		
			<a href="#" id="item-save" rel="nofollow" class="${savedIcon} action-link">
				<span class="action-title">${savedText}</span>
			</a>
			
		</c:if>
	
		<%-- Format labels --%>
		<c:if test="${model.debug}">
			<c:choose>
				<c:when test="${fn:contains(model.currentUrl, '&format=label')} || ${fn:contains(model.currentUrl, '?format=label')}">
					<c:choose>
						<c:when test="${fn:contains(model.currentUrl, '&format=label')}">
							<c:set var="switchlabelLink">${fn:replace(model.currentUrl, '&format=label', '')}</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="switchlabelLink">${fn:replace(model.currentUrl, '?format=label', '')}</c:set>
						</c:otherwise>
					</c:choose>
					<c:set var="switchlabelTitle">Normal format</c:set>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${fn:contains(model.currentUrl, '?')}">
							<c:set var="switchlabelLink">${model.currentUrl}&format=label</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="switchlabelLink">${model.currentUrl}?format=label</c:set>
						</c:otherwise>
					</c:choose>
					<c:set var="switchlabelTitle">Label format</c:set>
				</c:otherwise>
			</c:choose>
			<a href="${switchlabelLink}" id="format-link" class="icon-info action-link" title="${switchlabelTitle}" rel="nofollow">
				<span class="action-title">${switchlabelTitle}</span>
			</a>
		</c:if>
			

		
		<c:if test="${!empty model.user}">
			<form id="item-save-tag">
				<fieldset>
					<label for="add-tag" class="icon-tag action-link">
						<span class="action-title"><spring:message code="AddATag_t" /></span>
					</label>
					
					<div style="display:table">
						<input type="text" id="item-tag" maxlength="50" />
						<span>
							<input type="submit" class="submit-button deans-button-1" value="<spring:message code="Add_t" />" />
						</span>
					</div>
					
				</fieldset>
			</form>
		</c:if>
		
		
		<div id="translate-container">
			<span class="icon-translate"></span>
				<!-- translate services -->
			<a href="" id="translate-item" class="bold">
				<spring:message code="TranslateDetails_t" />
				<span class="iconP icon-arrow-6"></span>
			</a>
		</div>
		
		
	</div>
	
	

</div>