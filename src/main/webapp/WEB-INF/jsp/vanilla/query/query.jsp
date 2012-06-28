<h3><spring:message code="query_heading_t"/></h3>
<%@ include file="/WEB-INF/jsp/vanilla/query/form.jsp" %>
<%--@ include file="/WEB-INF/jsp/vanilla/query/refinement.jsp" --%>
<if !model.embedded && model.showDidYouMean>
<spring:message code="Didyoumean_t"/>:
<a href="/{model.portalName}/search.html?query={model.briefBeanView.spellCheck.collatedResult}">{model.briefBeanView.spellCheck.collatedResult}</a>
</if>