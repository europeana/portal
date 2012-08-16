<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<div id="actions" class="sidebar notranslate">
  <div id="additional-actions">
    <div id="additional-actions-addthis"></div>
  
	<%-- Save for logged in users --%>
	<c:if test="${model.user}">
	  <a href="" id="item-save" rel="nofollow" class="block-link bold"><spring:message code="SaveToMyEuropeana_t" /></a>
	</c:if>
	
	<%-- Embed link --%>
	<a href="${model.embedRecordUrl}" id="item-embed" class="block-link bold" target="_blank" rel="nofollow"><spring:message code="embed_t" /></a>
    
	<%-- Citation link --%>
    <a href="" id="citation-link" class="block-link bold" title="<spring:message code="AltCiteInfo_t" />" rel="nofollow"><spring:message code="Cite_Button_t" /></a>
    
    <%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-right/add-tag.jspf" %>
    <%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-right/fields-enrichment.jspf" %>
    <%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-right/format-link.jspf" %>
  </div>
</div>

