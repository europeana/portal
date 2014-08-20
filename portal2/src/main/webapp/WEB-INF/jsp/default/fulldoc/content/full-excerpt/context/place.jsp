<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>

<c:set var="place" value="${contextualItem}" />
<c:if test="${!empty place.labels && (inContext == 1 || !place.showInContext)}">

  <%--
  <c:if test="${inContext == 1}">
    <c:set var="title">
      <c:choose>
        <c:when test="${place.matchUrl}">${place.prefLabelLang[0]}</c:when>
        <c:otherwise>${value.value}</c:otherwise>
      </c:choose>
    </c:set>
    <div class="contextual-header" id="${place.htmlId}">${title}</div>
  </c:if>
 --%>
  
  <c:set var="placeElOpen"><li></c:set>
  <c:set var="placeElClose"></li></c:set>
  <c:set var="placeElContent" value=""/>
  <c:set var="localSeparator" value=""/>
  
  <c:if test="${inContext == 1 && autoGeneratedTags == 0}">
  	<c:set var="placeElOpen"><span class="contextual-body"  id="${concept.htmlId}"></c:set>
  	<c:set var="placeElClose"></span></c:set>
  </c:if>
  
  <c:url var="searchUrl" value="/search.html">
    <c:param name="query">edm_place:"${place.about}"</c:param>
  </c:url>
      
  <c:if test="${autoGeneratedTags == 0}">
    <c:set var="sectionContent"><a href="${searchUrl}" id="${fn:replace(place.about, '/', '.')}"><c:forEach items="${place.labels}" var="label" varStatus="t"><c:if test="${!t.first}">, </c:if>${label}</c:forEach></a></c:set>

	<c:if test="${fn:length(sectionContent)>0}"><c:set var="localSeparator">LS-1;</c:set></c:if>
	
    <c:set var="hiddenFields"><c:forEach items="${place.labels}" var="label"><input type="hidden" name="placename" value="${label}"/></c:forEach></c:set>
	<c:set var="sectionContent">${sectionContent}${hiddenFields}</c:set>
	<c:set var="placeElContent">${placeElContent}${sectionContent}</c:set>
	
    <c:if test="${!empty place.prefLabelLang && !empty place.altLabelLang}">
      <c:set var="sectionContent">(<c:forEach items="${place.altLabelLang}" var="item" varStatus="t">${item}<c:if test="${!t.last}">, </c:if></c:forEach>)</c:set>
      <c:set var="placeElContent">${placeElContent}${sectionContent}</c:set>
      <c:set var="localSeparator">;</c:set>
    </c:if>
  </c:if>  

  <c:if test="${!empty place.noteLang}">
    <c:set var="sectionContent"><c:forEach items="${place.noteLang}" var="item" varStatus="t">
      <c:if test="${!t.first}"><br/></c:if>
      ${item}
    </c:forEach></c:set>
    <c:set var="placeElContent">${placeElContent}${localSeparator} ${sectionContent}</c:set>
    <c:set var="localSeparator">;</c:set>
  </c:if>

  <c:if test="${!empty place.latitude or !empty place.longitude or !empty place.altitude}">
    <%--c:set var="location" value="" / --%>

    <c:if test="${!empty place.latitude}">
      <%--c:set var="location" value="latitude: ${place.latitude}" /--%>
      <c:set var="sectionContent"><input type="hidden" id="latitude" value="${place.latitude}"></c:set>
      <c:set var="placeElContent">${placeElContent}${sectionContent}</c:set>
    </c:if>
    
    <c:if test="${!empty place.longitude}">
      
      <%--c:if test="${fn:length(location) > 0}"><c:set var="location">${location} &mdash;&nbsp;</c:set></c:if--%>
    
      <c:set var="sectionContent"><input type="hidden" id="longitude" value="${place.longitude}"></c:set>
    
      <%--c:set var="location">${location}longitude: ${place.longitude}</c:set--%>
    
      <c:set var="placeElContent">${placeElContent}${sectionContent}</c:set>
    </c:if>
    
    <c:if test="${!empty place.altitude}">
      <%--c:if test="${fn:length(location) > 0}"><c:set var="location">${location} &mdash;&nbsp;</c:set></c:if--%>
      <%--c:set var="location">${location}altitude: ${place.altitude}</c:set--%>
    </c:if>
    <%--c:if test="${location != ''}"><p>${location}</p></c:if--%>
  </c:if>



  <c:if test="${!empty place.dcTermsHasPartLang}">
    <c:set var="msg_key" value="context_place_dcTermsHasPart_t" />
    <c:if test="${fn:length(place.dcTermsHasPartLang) > 1}">
      <c:set var="msg_key" value="context_place_dcTermsHasParts_t" />
    </c:if>
    <c:set var="sectionContent"><spring:message code="${msg_key}" />:
      <c:forEach items="${place.dcTermsHasPartLang}" var="label" varStatus="t"><c:if test="${!t.first}">, </c:if>${label}</c:forEach></c:set>
    <c:set var="placeElContent">${placeElContent}${localSeparator} ${sectionContent}</c:set>
    <c:set var="localSeparator">;</c:set>
  </c:if>


  <c:if test="${!empty place.isPartOfLinks && autoGeneratedTags == 1}">
    <c:set var="sectionContent"><spring:message code="context_place_isPartOf_t" />:
      <europeana:optionalMapList map="${place.isPartOfLinks}" /></c:set>
    <c:set var="placeElContent">${placeElContent}${localSeparator} ${sectionContent}</c:set>
    <c:set var="localSeparator">;</c:set>
  </c:if>

  <c:if test="${!empty place.owlSameAs}">
    <c:set var="sectionContent"><spring:message code="context_place_owlSameAs_t" />:
      <europeana:optionalList list="${place.owlSameAs}" /></c:set>
    <c:set var="placeElContent">${placeElContent}${localSeparator} ${sectionContent}</c:set>
    <c:set var="localSeparator">;</c:set>
  </c:if>
  
  ${placeElOpen}${placeElContent}${placeElClose}
</c:if>
