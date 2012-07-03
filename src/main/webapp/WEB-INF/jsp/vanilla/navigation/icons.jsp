<ul>
	<%-- grid view --%>
	<c:if test="${!empty model.viewUrlTable}">
		<li>
			<a href="${model.viewUrlTable}" title="<spring:message code="AltGridView_t" />" rel="nofollow"></a>
		</li>
	</c:if>

	<%-- timeline --%>
	<c:if test="${!empty model.viewUrlTimeline}">
		<li style="display:none">
			<a href="${model.viewUrlTimeline}" title="<spring:message code="AltTimelineView_t" />" rel="nofollow"></a>
		</li>
	</c:if>

	<%-- map --%>
	<li style="display:none">
		<a href="${model.viewUrlMap}" title="<spring:message code="AltMapView_t" />" rel="nofollow"></a>
	</li>

	<%-- 
		wikipedia
		2011-07-21
		removing until wikipedia is available
	--%>
	<%--
		<li>
			<a href="" title="<spring:message code="AltWikipediaView_t" />" rel="nofollow" class="wikipedia"></a>
		</li>
	--%>
</ul>