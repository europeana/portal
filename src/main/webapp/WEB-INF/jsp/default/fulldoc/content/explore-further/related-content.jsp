<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:if test="${model.fullBeanView.parent || model.fullBeanView.children}">
	<h3><a href="#related-content"><spring:message code="RelatedContent_t" /></a></h3>
	<div id="related-content" class="carousel">
		<c:if test="${model.fullBeanView.parent}">
			<#-- EXAMPLE CODE FOR PARENT BREADCRUMB -->
			<c:forEach items="${model.fullBeanView.parents}" var="parent">
				${parent.title}&nbsp;&gt;
			</c:forEach>
			${model.document.title}
			<#-- END OF EXAMPLE CODE FOR PARENT BREADCRUMB -->
			
			<a href="${model.fullBeanView.parent.fullDocUrl}" title="${model.fullBeanView.parent.title}" class="parent" rel="nofollow">
				<c:choose>
					<c:when test="${model.useCache}">
						<img class="thumb"
							 src="${model.fullBeanView.parent.thumbnail}"
							 alt="${model.fullBeanView.parent.title}"
							 data-type="${fn:toLowerCase(model.fullBeanView.parent.type)}"/>
					</c:when>
					<c:otherwise>
						<img class="thumb"
							 src="${model.fullBeanView.parent.thumbnail}"
							 alt="${model.fullBeanView.parent.title}"
							 data-type="${fn:toLowerCase(model.fullBeanView.parent.type)}" />
					</c:otherwise>
				</c:choose>
				${model.fullBeanView.parent.title}
			</a>
		</c:if>
			
		<div class="carousel-container" about="${model.document.id}">
			<c:if test="${model.fullBeanView.children}">
				<ul>
					<c:forEach items="${model.fullBeanView.children}" var="child">
						<li>
							<c:set var="relItemQuery" value='europeana_uri:"${model.document.id}"' />
							<a  href='/${model.portalName}/${fn:replace(child.id, "http://www.europeana.eu/resolve/", "")}.html?query=${relItemQuery}&amp;startPage=1&amp;pageId=brd'
								rel="rdfs:seeAlso" resource="${child.id}"
								title="${child.title}">
								<img src="${child.thumbnail}" alt="${child.title}" width="70" data-type="${child.type?lower_case}"/>
							</a>
						</li>
					</c:forEach>
				</ul>
			</c:if>
		</div>
	</div>
</c:if>