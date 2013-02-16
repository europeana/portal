<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>

<div id="excerpt">
  <div id="item-details">
    <div class="sidebar-right hide-on-x">
      <%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/see-also.jspf" %>
    </div>

    <c:set var="schemaOrgMapping" value="${model.schemaOrgMapping['dc:title']}" />
    <c:set var="schemaOrgElement" value="${schemaOrgMapping.element}" />
    <c:set var="edmElement" value="${schemaOrgMapping.edmElement}" />
    <c:set var="semanticAttributes">
      ${"property=\""}${edmElement.fullQualifiedURI}${" "}${schemaOrgElement.elementName} dc:title${"\""}
    </c:set>
    <h1 class="hide-on-phones" ${semanticAttributes}>${model.objectTitle}</h1>

    <c:forEach items="${model.document.dcTitle}" var="title">
      <c:if test="${title != model.objectTitle }">
        <div class="item-metadata">
          <span class="bold notranslate"><spring:message code="dc_title_t" />:</span>
          <span class="translate" ${semanticAttributes}>${title}</span>
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
      <c:set var="prop"><eu:semanticAttributes field="${hiddenField.key.fieldName}" schemaOrgMapping="${model.schemaOrgMapping}" /></c:set>
      <c:forEach items="${hiddenField.value}" var="value">
        <c:if test="${value != null && value != ''}"><meta resource="${value}" ${prop}/></c:if>
      </c:forEach>
    </c:forEach>

    <%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/fields-enrichment.jspf" %>
    <c:if test="${model.formatLabels}">
      <%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/schema.jspf" %>
    </c:if>
  </div>
</div>

