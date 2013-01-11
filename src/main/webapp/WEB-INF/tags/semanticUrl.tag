<%@ tag trimDirectiveWhitespaces="true" %>

<%-- parameters --%>
<%@ attribute name="schemaOrgMapping" required="true" type="java.lang.Object" %>
<%@ attribute name="field" required="true"%>
<%@ attribute name="contextualEntity" required="false" %>

<%-- tag libs --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="schemaKey" value="${field}" />
<c:if test="${contextualEntity != null}">
  <c:set var="schemaKey" value="${contextualEntity}/${field}" />
</c:if>

<c:set var="semanticUrl" value="false" />
<c:if test="${schemaOrgMapping[schemaKey] != null}">
  <c:set var="elementMapping" value="${schemaOrgMapping[schemaKey]}" />
  <c:if test="${elementMapping.element.qualifiedName == 'schema:url'}">
    <c:set var="semanticUrl" value="true" />
  </c:if>
</c:if>
${semanticUrl}