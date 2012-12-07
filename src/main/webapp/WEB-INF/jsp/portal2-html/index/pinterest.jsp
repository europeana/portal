<div class="twelve columns">
<h3 id="collapse-header-3"><span class="left collapse-header-text"><spring:message code='latest_on_pinterest_t'/></span><span class="icon-pinterest left"></span><span class="collapse-icon"></span><!-- a class="icon-rss feed-link" href="http://pinterest.europeana.eu.feed" target="_blank" title="RSS Feed"></a--></h3>
<div class="collapse-content">
<c:if test='${not empty model.pinterestItems}'>
<%-- div id="carousel-3-header" class="europeana-header"></div --%>
<div id="carousel-3" class="europeana-carousel">
<c:forEach var="item" items="${model.pinterestItems}">
<a href="${item.link}" class="hidden"><img src="${item.images[0].src}" alt="${fn:escapeXml(item.plainDescription)}" title="${fn:escapeXml(item.plainDescription)}"/>	</a>
</c:forEach>
</div>
<%-- div id="carousel-3-footer" class="europeana-footer"></div  --%>
</c:if>
</div>
</div>
<%--
<h2><spring:message code='recent_pinterest_activities_t'/>&nbsp;<a href="http://www.pinterest.com/europeana" target="_blank"><img src="/${model.portalName}/themes/common/images/logos/Pinterest_Logo.png" width="70" height="18" alt="Follow Me on Pinterest" /></a></h2>
<div id="pinterest" class="rssFeed"></div>
 --%>