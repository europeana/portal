
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
				var edmRights = [<c:forEach items="${model.document.edmRights}" var="rights" varStatus="s">"${rights}"<c:if test="${!s.last}">,</c:if></c:forEach>];

				<c:forEach items="${model.allImages}" var="image">

					<c:set var="title">${fn:replace(model.objectTitle, newLineChar1, ' ')}</c:set>
					<c:set var="title">${fn:replace(title, newLineChar2, ' ')}</c:set>
				
					carouselData[carouselData.length] = {
						"image":	decodeURI("${image.thumbnail}").replace(/&amp;/g, '&'),
						"title":	('${fn:escapeXml(title)}'),
						"dataType":	"${fn:toLowerCase(dataType)}",
						"edmField": "${image.edmField}"
					};

						
					<%--NORMALISE MEDIA HERE--%>
					
					<c:choose>
						<c:when test="${fn:length(image.full) > 0}">
							<% pageContext.setAttribute("newLineChar", "\n"); %>
							<c:set var="rightsToParse" value="${image.rightsValue}" />
							<c:set var="rightsString"><%@ include file="/WEB-INF/jsp/default/fulldoc/macros/rights.jsp" %></c:set>
	
							carouselData[carouselData.length-1].external = {
								"unescaped_url" : "${image.full}",
								"url" 	: "${image.escapedFull}",
								"type"	: "${fn:toLowerCase(image.type)}",
								"rights": '${fn:replace(rightsString, newLineChar, "")}'
							}
						</c:when>
						<c:otherwise>
						
							<c:if test="${!empty image.mediaService}">
							
								<% pageContext.setAttribute("newLineChar", "\n"); %>
								<c:set var="rightsToParse" value="${image.rightsValue}" />
								<c:set var="rightsString"><%@ include file="/WEB-INF/jsp/default/fulldoc/macros/rights.jsp" %></c:set>
								
								carouselData[carouselData.length-1].external = {
									"unescaped_url" : "${image.mediaService.embeddedUrl}",
									"url" 	:         "${image.mediaService.embeddedUrl}",
									"type"  :         "${fn:toLowerCase(image.mediaService.dataType)}"
									"rights":         '${fn:replace(rightsString, newLineChar, "")}'
								}
							
							</c:if>
						</c:otherwise>
					</c:choose>
					
					<%--END NORMALISE MEDIA--%>
						

					<c:set var="isSoundCloudAwareCollection" value="false"/>
					<c:forEach var="item" items="${model.soundCloudAwareCollections}">
						<c:if test="${item eq collectionId}">
							<c:set var="isSoundCloudAwareCollection" value="true" />
						</c:if>
					</c:forEach>

					
					<c:if test="${isSoundCloudAwareCollection}">
						<% pageContext.setAttribute("newLineChar", "\n"); %>
						<c:set var="rightsToParse" value="${image.rightsValue}" />
						<c:set var="rightsString"><%@ include file="/WEB-INF/jsp/default/fulldoc/macros/rights.jsp" %></c:set>

						carouselData[carouselData.length-1].external = {
							"unescaped_url" : "https://w.soundcloud.com/player/?url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%2F" + "${model.objectDcIdentifier}" + "&color=4c7ecf&auto_play=false&show_artwork=true",
							"url" 			: "https://w.soundcloud.com/player/?url=http%3A%2F%2Fapi.soundcloud.com%2Ftracks%2F" + "${model.objectDcIdentifier}" + "&color=4c7ecf&auto_play=false&show_artwork=true",
							"type"			: "sound",
							"rights"        : '${fn:replace(rightsString, newLineChar, '')}'
						}
					</c:if>
						

					<%--
					<c:if test="${fn:indexOf(model.isShownAt, '//audioboo.fm/boos/') > -1}">
						carouselData[carouselData.length-1].external.audio_boo = true;
					</c:if>
					<c:if test="${!empty image.mediaService}">
						<c:if test="${fn:indexOf(image.mediaService.embeddedUrl, '//audioboo.fm/boos/') > -1}">
							carouselData[carouselData.length-1].external.audio_boo = true;
						</c:if>
					</c:if>
					--%>
					
					<c:if test="${model.debug}">
						// start dummy media service data
						
						function gup(name){
							name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
							var regexS = "[\\?&]"+name+"=([^&#]*)";
							var regex = new RegExp( regexS );
							var results = regex.exec( window.location.href );
							if( results == null ){
							  return null;						  
							}
							else{
							  return results[1];						  
							}
						};
							
						if(gup('testDailyMotion')){
							
							console.log('testDailyMotion - fake data');
							
							carouselData[carouselData.length-1].external = {
								"unescaped_url" : "http://www.dailymotion.com/embed/video/x18cxlz",
								"url" 	:         "http://www.dailymotion.com/embed/video/x18cxlz",
								"type"	:         "video",
								"rights":         '${fn:replace(rightsString, newLineChar, "")}'
							}

							
						}
						
						else if(gup('testAudioBoo')){
							
							console.log('testAudioBoo - fake data');
							
							carouselData[carouselData.length-1].external = {
								"unescaped_url" : "https://audioboo.fm/boos/793330/embed/v2?eid=AQAAAGBQf1PyGgwA",
								"url" 	:         "https://audioboo.fm/boos/793330/embed/v2?eid=AQAAAGBQf1PyGgwA",
								"type"	:         "sound",
								"rights":         '${fn:replace(rightsString, newLineChar, "")}'
							}
						}
						
						else if(gup('testVimeo')){
							
							console.log('testVimeo - fake data');

							carouselData[carouselData.length-1].external = {
								"unescaped_url" : "http://player.vimeo.com/video/24453345?title=0&byline=0&portrait=0",
								"url" 	:         "http://player.vimeo.com/video/24453345?title=0&byline=0&portrait=0",
								"type"	:         "video",
								"rights":         '${fn:replace(rightsString, newLineChar, "")}'
							}
						}
						
						else if(gup('testSoundCloud')){
							
							console.log('testSoundCloud - fake data');
							
							carouselData[carouselData.length-1].external = {
								"unescaped_url" : "https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/tracks/150424305&amp;auto_play=false&amp;hide_related=false&amp;visual=true",
								"url" 	:         "https://w.soundcloud.com/player/?url=https%3A//api.soundcloud.com/tracks/150424305&amp;auto_play=false&amp;hide_related=false&amp;visual=true",
								"type"	:         "sound",
								"rights":         '${fn:replace(rightsString, newLineChar, "")}'
							}
						}
						// end dummy media service data
	
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
		<c:set var="rightsToParse" value="${model.rightsOption}" />		
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
		
			<label for="add-tag" class="icon-tag action-link">
				<span class="action-title"><spring:message code="AddATag_t" /></span>
			</label>


				<style>
				.submit-tag{
					-moz-box-sizing:border-box;
				}

				</style>

			<form id="item-save-tag">
				<fieldset>
								
						<input id="item-tag-submit" type="submit" class="submit-button deans-button-1 submit-tag" value="<spring:message code="Add_t" />" />
						<span>
							<input type="text" id="item-tag" maxlength="50" />
						</span>
					
				</fieldset>
			</form>
			
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


