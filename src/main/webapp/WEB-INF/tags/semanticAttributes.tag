<%@ tag trimDirectiveWhitespaces="true" %>

<%-- parameters --%>
<%-- type: SchemaOrgMapping --%>
<%@ attribute name="schemaOrgMapping" required="true" type="java.lang.Object" %>
<%-- type: String --%>
<%@ attribute name="field" required="true" %>
<%-- type: String --%>
<%@ attribute name="contextualEntity" required="false" %>

<%-- tag libs --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="semanticAttributes" value="${model.edmElements[field].fullQualifiedURI}" />

<c:set var="schemaKey" value="${field}" />
<c:if test="${contextualEntity != null && contextualEntity != ''}">
  <c:set var="schemaKey" value="${contextualEntity}/${field}" />
</c:if>

<c:if test="${schemaOrgMapping[schemaKey] != null}">
  <%-- type: SchemaOrgElement --%>
  <c:set var="elementMapping" value="${schemaOrgMapping[schemaKey]}" />
  <%-- type: Element --%>
  <c:set var="schemaOrgElement" value="${elementMapping.element}" />
  <%-- type: Element --%>
  <c:set var="edmElement" value="${elementMapping.edmElement}" />
  <c:set var="semanticAttributes">
    ${schemaOrgElement.elementName}${" "}${edmElement.fullQualifiedURI}
  </c:set>
</c:if>
property="${semanticAttributes}"