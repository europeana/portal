<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>

<c:set var="agent" value="${contextualItem}" />
<c:if test="${!empty agent.labels && (inContext == 1 || !agent.showInContext)}">
<%--
  <c:if test="${inContext == 1}">
    <c:set var="title">
      <c:choose>
        <c:when test="${agent.matchUrl}">${agent.prefLabelLang[0]}</c:when>
        <c:otherwise>${value.value}</c:otherwise>
      </c:choose>
    </c:set>
    <div class="contextual-header" id="${agent.htmlId}">${title}</div>
  </c:if>
 --%>
  <div<c:if test="${inContext == 1}"> class="contextual-body" id="${agent.htmlId}"</c:if>>
    <p>
      <c:url var="searchUrl" value="/search.html">
        <c:param name="query">edm_agent:"${agent.about}"</c:param>
      </c:url>
      <a href="${searchUrl}" id="${fn:replace(agent.about, '/', '.')}">
        <c:forEach items="${agent.labels}" var="item" varStatus="t">${item}<c:if test="${!t.last}">, </c:if></c:forEach>
      </a>
<%--
      <a href="${agent.about}" target="_blank" class="icon-external-right"></a>
 --%>
      <c:if test="${!empty agent.prefLabelLang && !empty agent.altLabelLang}">
        (<c:forEach items="${agent.altLabelLang}" var="item" varStatus="t">${item}<c:if test="${!t.last}">; </c:if></c:forEach>)
      </c:if>
    </p>

    <c:if test="${!empty agent.beginLang || !empty agent.endLang}">
      <p>
        <c:if test="${!empty agent.beginLang}">
          <spring:message code="context_agent_begin_t" />: 
          <c:forEach items="${agent.beginLang}" var="label" varStatus="t"><c:if test="${!t.first}">, </c:if>${label}</c:forEach>
        </c:if>

        <c:if test="${!empty agent.beginLang && !empty agent.endLang}">&mdash;</c:if>

        <c:if test="${!empty agent.endLang}">
          <spring:message code="context_agent_end_t" />: 
          <c:forEach items="${agent.endLang}" var="label" varStatus="t"><c:if test="${!t.first}">, </c:if>${label}</c:forEach>
        </c:if>
      </p>
    </c:if>

    <c:if test="${!empty agent.noteLang}">
      <p>
        <c:forEach items="${agent.noteLang}" var="item" varStatus="t"><c:if test="${!t.first}"><br/></c:if>${item}</c:forEach>
      </p>
    </c:if>

    <c:if test="${!empty agent.edmIsRelatedToLang}">
      <p>
        <c:set var="msg_key" value="context_concept_Related_t" />
        <c:if test="${fn:length(concept.edmIsRelatedToLang) > 1}">
          <c:set var="msg_key" value="context_concept_Relateds_t" />
        </c:if>
        <spring:message code="${msg_key}" />: 
        <c:forEach items="${agent.edmIsRelatedToLang}" var="label" varStatus="t">
          <c:if test="${!empty label}">
            <c:if test="${!t.first}">, </c:if>${label}
          </c:if>
        </c:forEach>
      </p>
    </c:if>

    <c:if test="${!empty agent.rdaGr2DateOfBirthLang}">
      <p>
        <spring:message code="context_agent_dateOfBirth_t" />: 
        <c:if test="${!empty agent.rdaGr2DateOfBirthLang}">
          <c:forEach items="${agent.rdaGr2DateOfBirthLang}" var="label" varStatus="t">
            <c:if test="${!empty label}"><c:if test="${!t.first}">, </c:if>${label}</c:if>
          </c:forEach>
        </c:if>
      </p>
    </c:if>

    <c:if test="${!empty agent.rdaGr2DateOfDeathLang}">
      <p>
        <spring:message code="context_agent_dateOfDeath_t" />: 
        <c:if test="${!empty agent.rdaGr2DateOfDeathLang}">
          <c:forEach items="${agent.rdaGr2DateOfDeathLang}" var="label" varStatus="t">
            <c:if test="${!empty label}">
              <c:if test="${!t.first}">, </c:if>${label}
            </c:if>
          </c:forEach>
        </c:if>
      </p>
    </c:if>

<%-- 

          <c:if test="${!empty timespan.isPartOfLinks}">
            <p>
              <spring:message code="context_isPartOf_t" />: 
              <europeana:optionalMapList map="${timespan.isPartOfLinks}" />
            </p>
          </c:if>

--%>
  </div>
</c:if>
