<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>

<c:set var="concept" value="${contextualItem}" />
<c:if test="${!empty concept.labels && (inContext == 1 || !concept.showInContext)}">
<%--
  <c:if test="${inContext == 1}">
    <c:set var="title">
      <c:choose>
        <c:when test="${concept.matchUrl}">${concept.prefLabelLang[0]}</c:when>
        <c:otherwise>${value.value}</c:otherwise>
      </c:choose>
    </c:set>
    <div class="contextual-header" id="${concept.htmlId}">${title}</div>
  </c:if>
 --%>
  <div<c:if test="${inContext == 1}"> class="contextual-body" id="${concept.htmlId}"</c:if>>
    <p>
      <c:url var="searchUrl" value="/search.html">
        <c:param name="query">skos_concept:"${concept.about}"</c:param>
      </c:url>
      <a href="${searchUrl}">
        <c:forEach items="${concept.labels}" var="item" varStatus="t"
          >${item}<c:if test="${!t.last}">, </c:if>
        </c:forEach>
      </a>
<%--
      <a href="${concept.about}" target="_blank" class="icon-external-right"></a>
 --%>
      <c:if test="${!empty concept.prefLabelLang && !empty concept.altLabelLang}">
        <c:forEach items="${concept.altLabelLang}" var="item" varStatus="t">
          ${item}<c:if test="${!t.last}">, </c:if>
        </c:forEach>
      </c:if>
    </p>

    <c:if test="${!empty concept.noteLang}">
      <p>
        <c:forEach items="${concept.noteLang}" var="item" varStatus="t">
          ${item}<c:if test="${!t.last}"><br/></c:if>
        </c:forEach>
      </p>
    </c:if>

    <c:if test="${!empty concept.notationLang}">
      <p>
        <c:set var="msg_key" value="context_concept_Notation_t" />
        <c:if test="${fn:length(concept.broaderLinks) > 1}">
          <c:set var="msg_key" value="context_concept_Notations_t" />
        </c:if>
        <spring:message code="${msg_key}" />: 
        <c:forEach items="${concept.notationLang}" var="item" varStatus="t">
          ${item}<c:if test="${!t.last}"><br/></c:if>
        </c:forEach>
      </p>
    </c:if>

    <c:if test="${!empty concept.broaderLinks}">
      <p>
        <c:set var="msg_key" value="context_concept_Broader_t" />
        <c:if test="${fn:length(concept.broaderLinks) > 1}">
          <c:set var="msg_key" value="context_concept_Broaders_t" />
        </c:if>
        <spring:message code="${msg_key}" />: 
        <europeana:optionalMapList map="${concept.broaderLinks}" />
      </p>
    </c:if>

    <c:if test="${!empty concept.narrowerLinks}">
      <p>
        <c:set var="msg_key" value="context_concept_Narrower_t" />
        <c:if test="${fn:length(concept.narrowerLinks) > 1}">
          <c:set var="msg_key" value="context_concept_Narrowers_t" />
        </c:if>
        <spring:message code="${msg_key}" />: 
        <europeana:optionalMapList map="${concept.narrowerLinks}" />
      </p>
    </c:if>

    <c:if test="${!empty concept.relatedLinks}">
      <p>
        <c:set var="msg_key" value="context_concept_Related_t" />
        <c:if test="${fn:length(concept.relatedLinks) > 1}">
          <c:set var="msg_key" value="context_concept_Relateds_t" />
        </c:if>
        <spring:message code="${msg_key}" />: 
        <europeana:optionalMapList map="${concept.relatedLinks}" />
      </p>
    </c:if>

    <c:if test="${!empty concept.broadMatchLinks}">
      <p>
        <c:set var="msg_key" value="context_concept_broadMatch_t" />
        <c:if test="${fn:length(concept.broadMatchLinks) > 1}">
          <c:set var="msg_key" value="context_concept_broadMatches_t" />
        </c:if>
        <spring:message code="${msg_key}" />: 
        <europeana:optionalMapList map="${concept.broadMatchLinks}" />
      </p>
    </c:if>

    <c:if test="${!empty concept.narrowMatchLinks}">
            <p>
        <c:set var="msg_key" value="context_concept_narrowMatch_t" />
        <c:if test="${fn:length(concept.narrowMatchLinks) > 1}">
          <c:set var="msg_key" value="context_concept_narrowMatches_t" />
        </c:if>
        <spring:message code="${msg_key}" />: 
        <europeana:optionalMapList map="${concept.narrowMatchLinks}" />
      </p>
    </c:if>

    <c:if test="${!empty concept.relatedMatchLinks}">
      <p>
        <c:set var="msg_key" value="context_concept_relatedMatch_t" />
        <c:if test="${fn:length(concept.relatedMatchLinks) > 1}">
          <c:set var="msg_key" value="context_concept_relatedMatches_t" />
        </c:if>
        <spring:message code="${msg_key}" />: 
        <europeana:optionalMapList map="${concept.relatedMatchLinks}" />
      </p>
    </c:if>

    <c:if test="${!empty concept.exactMatchLinks}">
      <p>
        <c:set var="msg_key" value="context_concept_exactMatch_t" />
        <c:if test="${fn:length(concept.exactMatchLinks) > 1}">
          <c:set var="msg_key" value="context_concept_exactMatches_t" />
        </c:if>
        <spring:message code="${msg_key}" />: 
        <europeana:optionalMapList map="${concept.exactMatchLinks}" />
      </p>
    </c:if>

    <c:if test="${!empty concept.closeMatchLinks}">
      <p>
        <c:set var="msg_key" value="context_concept_closeMatch_t" />
        <c:if test="${fn:length(concept.closeMatchLinks) > 1}">
          <c:set var="msg_key" value="context_concept_closeMatches_t" />
        </c:if>
        <spring:message code="${msg_key}" />: 
        <europeana:optionalMapList map="${concept.closeMatchLinks}" />
      </p>
    </c:if>
  </div>
</c:if>
