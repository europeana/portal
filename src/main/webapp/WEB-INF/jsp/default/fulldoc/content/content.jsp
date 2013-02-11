<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="content" class="row">
	<div class="twelve columns">

		<div class="row">
			<%@ include file="/WEB-INF/jsp/default/fulldoc/content/navigation/navigation.jsp" %>
		</div>

		<div class="row" about="${model.document.cannonicalUrl}" vocab="http://schema.org/" typeof="CreativeWork">
			<div class="three-cols-fulldoc-sidebar">
				<%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/sidebar-left.jsp" %>
			</div>

			<div class="nine-cols-fulldoc" id="main-fulldoc-area">
				<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/full-excerpt.jsp" %>
			</div>
		</div>

		<div class="row">
			<div class="sidebar-right show-on-x">
				<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/see-also.jspf" %>
			</div>
		</div>

		<c:if test="${!empty model.moreLikeThis}">

			<div class="row">
				<div class="twelve columns">

					<%-- data for carousel --%>
					<script type="text/javascript">

						var carousel2Data = [];
						<c:forEach items="${model.moreLikeThis}" var="doc">
							<c:set var="objectTitle">${fn:join(doc.titleBidi, ' ')}</c:set>
							carousel2Data[carousel2Data.length] = {
								image:			decodeURI( "${fn:escapeXml(doc.thumbnail)}" ).replace(/&amp;/g, '&').replace(/&amp;/g, '&'),
								title:			'${ objectTitle }',
						        <c:url var="url" value="${model.portalName}/${doc.fullDocUrl}">
						          <c:param name="rows" value="${model.rows}"/>
						        </c:url>
								europeanaLink:	'/${url}'
							};
						</c:forEach>
					</script>
					
					<%--
						markup for carousel:
						SEO friendly html rendering of images used as an image dimension measuring utility: has to live outside of the initially hidden #similar-content div
					--%>

					<div id="carousel-2-img-measure">
						<c:forEach var="similar" items="${model.moreLikeThis}">
							<c:set var="title" value="${fn:escapeXml(fn:join(similar.title, ' '))}" />
							<img src="${fn:replace(fn:escapeXml(similar.thumbnail), '&amp;', '&')}" alt="${title}" title="${title}" data-type="${fn:toLowerCase(similar.type)}" class="no-show" />
						</c:forEach>
					</div>
					<div id="explore-further">
						<div class="section">
							<a href="#"><spring:message code="SimilarContent_t" /></a>
							<div class="content">
								<div id="carousel-2" about="${model.document.id}" class="europeana-carousel"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</c:if>
	</div>
</div>