<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<div id="additional-info" class="sidebar" about="${model.document.id}">

	<h1 id="phone-object-title" class="show-on-phones" aria-hidden="true">${model.objectTitle}</h1>
		
	<%-- hidden seo images --%>
	
	<div id="carousel-1-img-measure">
	
		<!-- TODO: make sure all item images are listed here -->
		
		<img	src			= "${model.thumbnailUrl}"
				alt			= "${fn:escapeXml(model.pageTitle)}"
				data-type	= "${fn:toLowerCase(model.document.edmType)}"
				class		= "no-show"/>
	</div>
		
	<div id="carousel-1" class="europeana-bordered">
		<script type="text/javascript">
			var carouselData = [];
			carouselData[0] = {
				"image":		decodeURI("${model.thumbnailUrl}").replace(/&amp;/g, '&'),
				"title":		('${fn:escapeXml(model.objectTitle)}'),
				"dataType":		"${fn:toLowerCase(model.document.edmType)}",
				"lightboxable": {
					"url" 	: "http://0.tqn.com/d/animatedtv/1/0/T/x/fGuy_BlueHarvest_sc459_0034f.jpg",
					"type"	: "image"
				}
					
			};
			/*
			carouselData[1] = {"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+3%2F18%2F1M16_B145142_572.jpg&size=FULL_DOC","title":"Stadsvy"},{"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+3%2F18%2F1M16_B145142_572.jpg&size=FULL_DOC?x=y","title":"StadsvyXXX"};
			carouselData[2] = {"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+3%2F18%2F1M16_B145142_572.jpg&size=FULL_DOC","title":"Stadsvy"},{"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+3%2F18%2F1M16_B145142_572.jpg&size=FULL_DOC?x=y","title":"StadsvyXXX"};
			carouselData[3] = {"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+3%2F18%2F1M16_B145142_572.jpg&size=FULL_DOC","title":"Stadsvy"},{"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+3%2F18%2F1M16_B145142_572.jpg&size=FULL_DOC?x=y","title":"StadsvyXXX"};
			carouselData[4] = {"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+3%2F18%2F1M16_B145142_572.jpg&size=FULL_DOC","title":"Stadsvy"},{"image":"http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fmedia1.vgregion.se%2Fvastarvet%2FVGM%2FFotobilder%2FBilder+3%2F18%2F1M16_B145142_572.jpg&size=FULL_DOC?x=y","title":"StadsvyXXX"};
			*/
		</script>
	</div>
	
	<div class="original-context">
		<%@ include file="/WEB-INF/jsp/default/fulldoc/macros/rights.jsp" %>
		<%-- Original context link --%>
		<%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/original-context.jsp" %>
		<br>
		<div class="clear"></div>
	</div>
		
	<div class="actions">	
		<%-- Shares link 
		<a id="shares-link" class="icon-share action-link action-title"	rel="nofollow">
			<span	title="<spring:message code="Share_item_link_alt_t" />"><spring:message code="Share_item_link_t" /></span>
		</a>
		--%>
		
		<%-- Shares link --%>
		<a id="shares-link" class="icon-share action-link" rel="nofollow">
			<span class="action-title" title="<spring:message code="Share_item_link_alt_t" />"><spring:message code="Share_item_link_t" /></span>
		</a>
		
		<%-- Citation link --%>
	 	<a href="" id="citation-link" class="icon-cite action-link" title="<spring:message code="AltCiteInfo_t" />" rel="nofollow">
	 		<span class="action-title"><spring:message code="Cite_Button_t" /></span>
	 	</a>
		
		<div id="citation">
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
			<a href="" id="item-save" rel="nofollow" class="icon-unsaveditem action-link">
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
		 	<a href="${switchlabelLink}" id="format-link" class="icon-info action-link" title="${switchlabelTitle}" rel="nofollow">
		 		<span class="action-title">${switchlabelTitle}</span>
		 	</a>
		 	
		 	<span class="stretch"></span>
		 	
		 	<div class="clear"></div>
		 	
	 	</div>
	 	<div id="translate-container">
		 	<!-- translate services -->
		 	
		<a href="" id="translate-item" class="bold">
		<spring:message code="TranslateDetails_t" />
		<span class="iconP"></span></a>
	 	</div>
	 	
</div>


