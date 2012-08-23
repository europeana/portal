<%@ tag trimDirectiveWhitespaces="true" %>

<%-- parameters --%>
<%@ attribute name="topField" required="true" type="java.lang.Object" %><%-- FieldInfo --%>
<%@ attribute name="docElement" required="true" type="java.lang.Object" %>
<%@ attribute name="elementStatus" type="java.lang.Object" %>
<%@ attribute name="total" required="true" type="java.lang.Object" %>

<%-- tag libs --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>

<c:if test="${elementStatus != null && total > 1}">
  <tr><td colspan="2"><h5>${elementStatus.index + 1}</h5></td></tr>
</c:if>

<c:forEach items="${model.schemaMap[topField.type]}" var="field" varStatus="fieldStatus">
  <c:if test="${!empty docElement[field.propertyName]}">
    <tr valign="top">
      <td>
        <c:choose>
          <c:when test="${field.element == null}">
            ${field.schemaName}:
          </c:when>
          <c:otherwise>
            <a href="${field.element.fullQualifiedURI}" target="_new">${field.schemaName}</a>:
          </c:otherwise>
        </c:choose>
      </td>
      <c:set var="semanticAttributes">
        <c:if test="${field.element != null}">
          <c:out value=" "/>property="<c:choose>
            <c:when test="${field.schemaOrgElement != null}">${field.schemaOrgElement}</c:when>
            <c:otherwise>${field.element.elementName}</c:otherwise>
          </c:choose><c:out value=" "/>${field.element.fullQualifiedURI}"
          <c:if test="${field.schemaOrgElementIsURL}">
            <c:out value=" "/>href="${docElement[field.propertyName]}"</c:if>
        </c:if>
      </c:set>
      <td <c:if test="${!field.multiValue || fn:length(docElement[field.propertyName]) == 1}">${semanticAttributes}</c:if>>
        <c:choose>

          <c:when test="${field.schemaName == 'edm:WebResource'}">
            <table>
              <c:forEach items="${docElement[field.propertyName]}" var="fieldInstance" varStatus="resouceStatus">
                <europeana:displayDocElement topField="${model.webResourceField}" docElement="${fieldInstance}" 
                  elementStatus="${resouceStatus}" total="${fn:length(docElement[field.propertyName])}" />
              </c:forEach>
            </table>
          </c:when>

          <c:when test="${field.schemaName == 'relatedItems'}">
            <table>
              <c:forEach items="${docElement[field.propertyName]}" var="fieldInstance" varStatus="resouceStatus">
                <europeana:displayDocElement topField="${model.briefBeanField}" docElement="${fieldInstance}" 
                  elementStatus="${resouceStatus}" total="${fn:length(docElement[field.propertyName])}" />
              </c:forEach>
            </table>
          </c:when>

          <c:when test="${field.collectionOfMaps}">
            <c:forEach items="${docElement[field.propertyName]}" var="mapInstance" varStatus="fieldInstanceStatus">
              <c:forEach items="${mapInstance}" var="fieldInstance">
                <span ${semanticAttributes}>${fieldInstance.key} / ${fieldInstance.value}</span><c:if test="${!fieldInstanceStatus.last}"><br /></c:if>
              </c:forEach>
            </c:forEach>
          </c:when>

          <c:when test="${field.map}">
            <c:forEach items="${docElement[field.propertyName]}" var="fieldInstance" varStatus="fieldInstanceStatus">
              <span ${semanticAttributes}>${fieldInstance.key} / ${fieldInstance.value}</span><c:if test="${!fieldInstanceStatus.last}"><br /></c:if>
            </c:forEach>
          </c:when>

          <c:when test="${field.collection}">
            <c:choose>
              <c:when test="${fn:length(docElement[field.propertyName]) > 1}">
                <c:forEach items="${docElement[field.propertyName]}" var="fieldInstance" varStatus="fieldInstanceStatus">
                  <span ${semanticAttributes}>${fieldInstance}</span><c:if test="${!fieldInstanceStatus.last}"><br /></c:if>
                </c:forEach>
              </c:when>
              <c:otherwise>${docElement[field.propertyName][0]}</c:otherwise>
            </c:choose>
          </c:when>

          <c:otherwise>
            ${docElement[field.propertyName]}
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </c:if>
</c:forEach>
