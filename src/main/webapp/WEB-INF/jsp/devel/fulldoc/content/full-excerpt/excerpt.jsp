<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>
<div id="item-details" about="${model.document.id}">
  <h1>${model.objectTitle}</h1>
  <%--
    model.metaDataFileds = a collection of all metadata on the object pre-formated for <meta> element output in the <head>
    model.formatLabels = a boolean that is triggered via the url query string &format=labels
    the macro @displayEseDataAsMeta will output the meta data in its typical <meta> format or
    in html viable format if &format=labels is present in the query sting
    
    model.fields = a subset collection of meta data pre-formated for html presentation
    model.additionalFields = a subset collection of meta data pre-formated for html presentation
    model.model.enrichmentFields = a subset collection of meta data pre-formated for html presentation
    
  --%>
  <%-- c:if test="${model.formatLabels && !empty model.metaDataFields}" --%>
  <c:if test="${model.formatLabels}">
    <%@ include file="/WEB-INF/jsp/devel/fulldoc/content/full-excerpt/schema.jspf" %>
  </c:if>
  <c:if test="${!model.formatLabels && !empty model.fields && fn:length(model.fields) > 0}">
    <europeana:displayEseDataAsHtml listCollection="${model.fields}" wrapper="div" ugc="false" ess="true" />
  </c:if>
</div>
