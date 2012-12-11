<%@ tag trimDirectiveWhitespaces="true" %>

<%-- parameters --%>
<%@ attribute name="schemaOrgMapping" required="true" type="java.lang.Object" %>
<%@ attribute name="field" required="true"%>

<%-- tag libs --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<c:set var="semanticAttributes" value="${model.edmElements[field].fullQualifiedURI}" />
<c:if test="${schemaOrgMapping[field] != null}">
  <c:set var="elementMapping" value="${schemaOrgMapping[field]}" />
  <c:set var="schemaOrgElement" value="${elementMapping.element}" />
  <c:set var="edmElement" value="${elementMapping.edmElement}" />
  <c:set var="semanticAttributes">
    ${schemaOrgElement.elementName}${" "}${edmElement.fullQualifiedURI}
  </c:set>
</c:if>
property="${semanticAttributes}"