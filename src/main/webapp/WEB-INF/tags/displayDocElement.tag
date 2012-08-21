<!-- displayDocElement -->
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

<!-- topField: ${topField} -->
<!-- docElement: ${docElement} -->
<!-- elementStatus: ${elementStatus} -->
<!-- total: ${total} -->
<!-- topField.type: "${topField.type}" - ${fn:length(model.schemaMap[topField.type])} fields -->
<c:if test="${elementStatus != null && total > 1}">
  <tr><td colspan="2"><h5>${elementStatus.index + 1}</h5></td></tr>
</c:if>

<c:forEach items="${model.schemaMap[topField.type]}" var="field" varStatus="fieldStatus">
  <!-- field: ${field.propertyName} -->
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
      <td>
        <c:choose>

          <c:when test="${field.schemaName == 'edm:WebResource'}">
            <!-- IS A edm:WebResource -->
            <table>
              <c:forEach items="${docElement[field.propertyName]}" var="fieldInstance" varStatus="resouceStatus">
                <europeana:displayDocElement topField="${model.webResourceField}" docElement="${fieldInstance}" 
                  elementStatus="${resouceStatus}" total="${fn:length(docElement[field.propertyName])}" />
              </c:forEach>
            </table>
          </c:when>

          <c:when test="${field.schemaName == 'relatedItems'}">
            <!-- IS A relatedItems -->
            <table>
              <c:forEach items="${docElement[field.propertyName]}" var="fieldInstance" varStatus="resouceStatus">
                <europeana:displayDocElement topField="${model.briefBeanField}" docElement="${fieldInstance}" 
                  elementStatus="${resouceStatus}" total="${fn:length(docElement[field.propertyName])}" />
              </c:forEach>
            </table>
          </c:when>

          <c:when test="${field.collectionOfMaps}">
            <!-- IS A collection of maps -->
            <c:forEach items="${docElement[field.propertyName]}" var="mapInstance">
              <c:forEach items="${mapInstance}" var="fieldInstance">
                ${fieldInstance.key} / ${fieldInstance.value}<br />
              </c:forEach>
            </c:forEach>
          </c:when>

          <c:when test="${field.map}">
            <!-- IS A map -->
            <c:forEach items="${docElement[field.propertyName]}" var="fieldInstance">
              ${fieldInstance.key} / ${fieldInstance.value}<br />
            </c:forEach>
          </c:when>

          <c:when test="${field.collection}">
            <!-- IS A collection -->
            <c:choose>
              <c:when test="${fn:length(docElement[field.propertyName]) > 1}">
                <c:forEach items="${docElement[field.propertyName]}" var="fieldInstance">
                  ${fieldInstance}<br />
                </c:forEach>
              </c:when>
              <c:otherwise>${docElement[field.propertyName][0]}</c:otherwise>
            </c:choose>
          </c:when>

          <c:otherwise>
            <!-- IS A simple value -->
            ${docElement[field.propertyName]}
          </c:otherwise>
        </c:choose>
      </td>
    </tr>
  </c:if>
</c:forEach>
<!-- displayDocElement -->