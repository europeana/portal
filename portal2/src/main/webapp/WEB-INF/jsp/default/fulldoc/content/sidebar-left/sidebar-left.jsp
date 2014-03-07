<%@ page import="java.net.URLDecoder"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="eu" tagdir="/WEB-INF/tags"%>

<% pageContext.setAttribute("newLineChar1", "\r"); %>
<% pageContext.setAttribute("newLineChar2", "\n"); %>

<c:set var="about" value=""/>
<c:if test="${not empty model.document['about']}">
	<c:set var="about" value="${model.document.cannonicalUrl}" />
</c:if>

<div id="additional-info" class="sidebar" about="${about}">
	<h2 id="phone-object-title" class="show-on-phones" aria-hidden="true">${model.objectTitle}</h2>
	<%-- thumbnail (hidden seo image) --%>
	<div id="carousel-1-img-measure">
		<%-- TODO: make sure all item images are listed here --%>
		<c:set var="thumbnail" value=""/>
		<c:set var="dataType" value=""/>
		<c:set var="alt" value=""/>
		<c:set var="id" value=""/>

		<c:if test="${not empty model['thumbnailUrl']}">
			<c:set var="thumbnail" value="${model.thumbnailUrl}"/>
		</c:if>

		<c:if test="${not empty model.document['edmType']}">
			<c:set var="dataType" value="${fn:toLowerCase(model.document.edmType)}"/>
		</c:if>

		<c:if test="${not empty model['pageTitle']}">
			<c:set var="alt" value="${fn:escapeXml(model.pageTitle)}"/>
		</c:if>

    <c:set var="semanticAttributes" value="" />
    <c:if test="${!model.optedOut}">
      <%--
      <c:set var="semanticAttributes"><eu:semanticAttributes field="edm:preview" schemaOrgMapping="${model.schemaOrgMapping}" /></c:set>
       --%>
    </c:if>

    <img src="${thumbnail}" alt="${alt}" data-type="${dataType}" class="no-show" ${semanticAttributes} />
  </div>


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
				
				// var carouselDataIsBy	= 	${model.europeanaIsShownBy};
				// var carouselDataIsAt	= 	${model.europeanaIsShownAt};
				var isShownBy			= 	"${model.isShownBy}";
				var isShownAt			= 	"${model.isShownAt}";
				
				<c:forEach items="${model.allImages}" var="image">

					<c:set var="title">${fn:replace(model.objectTitle, newLineChar1, ' ')}</c:set>
					<c:set var="title">${fn:replace(title, newLineChar2, ' ')}</c:set>
				
					carouselData[carouselData.length] = {
						"image":		decodeURI("${image.thumbnail}").replace(/&amp;/g, '&'),
						"title":		('${fn:escapeXml(title)}'),
						"dataType":		"${fn:toLowerCase(dataType)}",
						"edmField": "${image.edmField}"
					};

					<c:if test="${fn:length(image.full) > 0}">
						carouselData[carouselData.length-1].external = {
							"unescaped_url" : "${image.full}",
							"url" 	: "${image.escapedFull}",
							"type"	: "${fn:toLowerCase(image.type)}"
						}
					</c:if>
						
					<c:if test="${collectionId == '2021613'}">
						carouselData[carouselData.length-1].external = {
							"unescaped_url" : "https://w.soundcloud.com/player/?url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%2F" + "${model.objectDcIdentifier}" + "&color=4c7ecf&auto_play=false&show_artwork=true",
							"url" 			: "https://w.soundcloud.com/player/?url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%2F" + "${model.objectDcIdentifier}" + "&color=4c7ecf&auto_play=false&show_artwork=true",
							"type"			: "sound"
						}
					</c:if>
						
						
						
				</c:forEach>
			</c:if>

			// temporary fix until we get multiple thumbanils
			for(var i=1; i<carouselData.length; i++){
				if(carouselData[i].external && carouselData[i].external.type=="image"){
					carouselData[i].image = carouselData[i].external.url; 
				}
			}
		</script>
	</div>

	<div class="original-context">

		<%-- Rights --%>
		<%@ include file="/WEB-INF/jsp/default/fulldoc/macros/rights.jsp" %>

  		<c:if test="${model.document.userGeneratedContent}">
			<span class="icon-ugc"></span><spring:message code="UserGeneratedContent_t" />
		</c:if>
		
		<%-- Original context link --%>
		<%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/original-context.jsp" %>
		
		
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
			<span class="icon-share" title="<spring:message code="Share_item_link_alt_t" />"><span class="action-title" title="<spring:message code="Share_item_link_alt_t" />"><spring:message code="Share_item_link_t" /></span></span>
		</div>

		<%-- Citation link --%>
		<div>
			<a href="" id="citation-link" class="icon-cite action-link" title="<spring:message code="AltCiteInfo_t" />" rel="nofollow">
				<span class="action-title"><spring:message code="Cite_Button_t" /></span>
			</a>
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

			<div>
				<a href="#" id="item-save" rel="nofollow" class="${savedIcon} action-link">
					<span class="action-title">${savedText}</span>
				</a>
			</div>
		</c:if>

		<%-- Format labels --%>
		<c:if test="${model.debug}">
			<c:choose>
				<c:when test="${fn:contains(model.currentUrl, '&format=label') || fn:contains(model.currentUrl, '?format=label')}">
					<c:choose>
						<c:when test="${fn:contains(model.currentUrl, '&format=label')}">
							<c:set var="switchlabelLink">${fn:replace(model.currentUrl, '&format=label', '')}</c:set>
						</c:when>
						<c:otherwise>
							<c:set var="switchlabelLink">${fn:replace(model.currentUrl, '?format=label', '')}</c:set>
						</c:otherwise>
					</c:choose>
					<c:set var="switchlabelTitle" value="Normal format" />
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
			<div>
				<a href="${switchlabelLink}" id="format-link" class="icon-info action-link" title="${switchlabelTitle}" rel="nofollow">
					<span class="action-title">${switchlabelTitle}</span>
				</a>
			</div>
		</c:if>

		<c:if test="${!empty model.user}">
			<form id="item-save-tag">
				<fieldset>
					<label for="add-tag" class="icon-tag action-link">
						<span class="action-title"><spring:message code="AddATag_t" /></span>
					</label>
					<div>
						<input type="text" id="item-tag" maxlength="50" /><span style="display:table-cell;"><input type="submit" class="submit-button deans-button-1 submit-tag" value="<spring:message code="Add_t" />" /></span>
					</div>
				</fieldset>
			</form>
			
			<script type="text/javascript">
				/* IE10 / iPhone fix */
				if( (!(window.ActiveXObject) && "ActiveXObject")  ||  navigator.userAgent.match(/iPhone/i)   ){
					$('.submit-tag').parent().css('vertical-align', 'top');	
				}
			</script>
			
		</c:if>

		<div id="translate-container">
			<span class="icon-translate"></span>
				<!-- translate services -->
			<a href="" id="translate-item">
				<spring:message code="TranslateDetails_t" />
				<span class="iconP icon-arrow-6"></span>
			</a>
		</div>
	</div>
</div>