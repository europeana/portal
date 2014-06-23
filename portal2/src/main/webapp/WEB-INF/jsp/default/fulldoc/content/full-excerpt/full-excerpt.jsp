<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="eufn" uri="http://europeana.eu/jsp/tlds/europeanatags"%>

<div id="excerpt">
  <div id="item-details">

    <%-- TODO: delete this block once mlt is done --%>
    <c:if test="${model.europeanaMlt == null || empty model.europeanaMlt}">
      <c:if test="${!empty model.seeAlsoSuggestions && fn:length(model.seeAlsoSuggestions.fields) > 0}">
        <div class="sidebar-right hide-on-x">
          <%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/see-also.jspf" %>
        </div>
      </c:if>
    </c:if>

    <h1 class="hide-on-phones" ${model.semanticTitle}>${fn:escapeXml(model.objectTitle)}</h1>

    <c:forEach items="${model.document.dcTitle}" var="title">
      <c:if test="${title != model.objectTitle && !empty(title)}">
        <c:set var="titleValue" value="${title}" />
        <c:if test="${!empty model.itemLanguage}">
          <c:set var="titleValue" value="${eufn:cleanField(eufn:translate(title, model.itemLanguage))}" />
        </c:if>
        <c:set var="theVal" value="${eufn:cleanField(value.translatedValue)}" />
        <div class="item-metadata">
          <span class="bold notranslate br"><spring:message code="dc_title_t" />:</span>
          <span class="translate" ${semanticAttributes}>${fn:escapeXml(titleValue)}</span>
        </div>
      </c:if>
    </c:forEach>

    <%--
      model.metaDataFileds = a collection of all metadata on the object pre-formated for <meta> element output in the <head>
      model.formatLabels = a boolean that is triggered via the url query string &format=labels
      the macro @displayEseDataAsMeta will output the meta data in its typical <meta> format or
      in html viable format if &format=labels is present in the query sting
      
      model.fields = a subset collection of meta data pre-formated for html presentation
      model.additionalFields = a subset collection of meta data pre-formated for html presentation
      model.model.enrichmentFields = a subset collection of meta data pre-formated for html presentation
    --%>

    <%-- 
      ${model.formatLabels}
      ${!empty model.metaDataFields}
      ${!empty model.fields}
      ${fn:length(model.fields) > 0}
    --%>
    <c:if test="${!model.formatLabels && !empty model['fields'] && fn:length(model.fields) > 0}">
      <europeana:displayEseDataAsHtml listCollection="${model.fields}" wrapper="div" ugc="false" ess="true"/>
    </c:if>
    <c:if test="${!empty model['fieldsAdditional']}">
      <europeana:displayEseDataAsHtml listCollection="${model.fieldsAdditional}" wrapper="div" ugc="${model.document.userGeneratedContent}" ess="true"/>
    </c:if>

    <meta resource="${model.document.checkedEdmLandingPage}" property="url http://www.europeana.eu/schemas/edm/landingPage" />
    <c:forEach items="${model.hiddenFields}" var="hiddenField" varStatus="fieldStatus">
      <c:set var="prop" value="${hiddenField.key.semanticAttributes}" />
      <c:forEach items="${hiddenField.value}" var="value">
        <c:if test="${value != null && value != ''}"><meta resource="${value}" ${prop}/></c:if>
      </c:forEach>
    </c:forEach>

    <c:choose>
      <c:when test="${model.formatLabels}">
        <%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/schema.jspf" %>
      </c:when>
      <c:otherwise>
        <%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/fields-enrichment.jspf" %>
        <%--
        <c:choose>
          <c:when test="${model.showContext}">
            <%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/context.jspf" %>
          </c:when>
          <c:otherwise>
            <%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/fields-enrichment.jspf" %>
          </c:otherwise>
        </c:choose>
         --%>
      </c:otherwise>
    </c:choose>
  </div>
</div>

