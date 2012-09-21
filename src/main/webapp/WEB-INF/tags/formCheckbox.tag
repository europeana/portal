<%@ tag trimDirectiveWhitespaces="true" %>

<%-- parameters --%>
<%@ attribute name="path" required="true" rtexprvalue="true" type="java.lang.Object" %>
<%@ attribute name="attributes" required="false" rtexprvalue="true" %>

<%-- taglibs --%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>

<%--spring:bind path="${path}" / --%>
<c:set var="id" value="${spring.status.expression}" />
<c:set var="status" value="${spring.stringStatusValue}" />

<div id="agree">
  <input type="hidden" name="_${id}" value="false" />
  <sf:checkbox path="${path}" id="${id}" name="${id}" title="<spring:message code="IAgree_t" />" />
  <%-- <#if spring.status.value>checked="checked"</#if> --%>

  <label for="${id}"><spring:message code="IAgree_t" /></label>
</div>
