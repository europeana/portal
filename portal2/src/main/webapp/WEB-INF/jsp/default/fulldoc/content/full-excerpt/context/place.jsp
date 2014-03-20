<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>

<c:set var="place" value="${contextualItem}" />

      <c:if test="${!empty place.labels && (inContext == 1 || !place.showInContext)}">
        <c:if test="${inContext == 1}">
          <div class="contextual-header">
            <c:choose>
              <c:when test="${place.matchUrl}">${place.prefLabelLang[0]}</c:when>
              <c:otherwise>${value.value}</c:otherwise>
            </c:choose>
            &nbsp;<span class="smaller">(</span><a class="more-info smaller"><spring:message code="enrichment_category_more_info_t" /></a><span class="smaller">)</span>
          </div>
        </c:if>

        <div<c:if test="${inContext == 1}"> class="contextual-body"</c:if>>
          <p>
            <c:url var="searchUrl" value="/search.html">
              <c:param name="query">edm_place:"${place.about}"</c:param>
            </c:url>
            <a href="${searchUrl}" id="${fn:replace(place.about, '/', '.')}" class="icon-external-right">
              <c:forEach items="${place.labels}" var="label" varStatus="t"><c:if test="${!t.first}">, </c:if>${label}</c:forEach>
              <c:forEach items="${place.labels}" var="label"><input type="hidden" name="placename" value="${label}"></c:forEach>
            </a>

            <c:if test="${!empty place.prefLabelLang && !empty place.altLabelLang}">
              (<c:forEach items="${place.altLabelLang}" var="item" varStatus="t">
                ${item}<c:if test="${!t.last}">, </c:if>
              </c:forEach>)
            </c:if>
          </p>

          <c:if test="${!empty place.noteLang}">
            <p>
              <c:forEach items="${place.noteLang}" var="item" varStatus="t">
                <c:if test="${!t.first}"><br/></c:if>
                ${item}
              </c:forEach>
            </p>
          </c:if>

          <c:if test="${!empty place.latitude or !empty place.longitude or !empty place.altitude}">
            <%--c:set var="location" value="" / --%>
            <c:if test="${!empty place.latitude}">
              <%--c:set var="location" value="latitude: ${place.latitude}" /--%>
              <input type="hidden" id="latitude" value="${place.latitude}">
            </c:if>
            <c:if test="${!empty place.longitude}">
              <%--c:if test="${fn:length(location) > 0}"><c:set var="location">${location} &mdash;&nbsp;</c:set></c:if--%>
              <input type="hidden" id="longitude" value="${place.longitude}">
              <%--c:set var="location">${location}longitude: ${place.longitude}</c:set--%>
            </c:if>
            <c:if test="${!empty place.altitude}">
              <%--c:if test="${fn:length(location) > 0}"><c:set var="location">${location} &mdash;&nbsp;</c:set></c:if--%>
              <%--c:set var="location">${location}altitude: ${place.altitude}</c:set--%>
            </c:if>
            <%--c:if test="${location != ''}"><p>${location}</p></c:if--%>
          </c:if>

          <c:if test="${!empty place.dcTermsHasPartLang}">
            <p>
              <c:set var="msg_key" value="context_place_dcTermsHasPart_t" />
              <c:if test="${fn:length(place.dcTermsHasPartLang) > 1}">
                <c:set var="msg_key" value="context_place_dcTermsHasParts_t" />
              </c:if>
              <spring:message code="${msg_key}" />: 

              <c:forEach items="${place.dcTermsHasPartLang}" var="label" varStatus="t"><c:if test="${!t.first}">, </c:if>${label}</c:forEach>
            </p>
          </c:if>

          <c:if test="${!empty place.isPartOfLinks}">
            <p>
              <spring:message code="context_place_isPartOf_t" />: 
              <europeana:optionalMapList map="${place.isPartOfLinks}" />
            </p>
          </c:if>

          <c:if test="${!empty place.owlSameAs}">
            <p>
              <spring:message code="context_place_owlSameAs_t" />: 
              <europeana:optionalList list="${place.owlSameAs}" />
            </p>
          </c:if>
        </div>
      </c:if>