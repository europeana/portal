<%@ page session="false" language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<?xml version="1.0" encoding="UTF-8"?>
<c:if test="${model.hasResults}">
  <providers>
  <c:forEach items="${model.results}" var="provider">
    <provider>
      <name>${fn:escapeXml(provider.name)}</name>
      <count>${provider.count}</count>
      <c:if test="${fn:length(provider.dataProviders) > 0}">
        <dataProviders>
        <c:forEach items="${provider.dataProviders}" var="dataProvider">
          <name>${fn:escapeXml(dataProvider.name)}</name>
          <count>${dataProvider.count}</count>
          <c:if test="${fn:length(dataProvider.datasets) > 0}">
            <datasets>
            <c:forEach items="${dataProvider.datasets}" var="dataset">
              <dataset>
                <name>${fn:escapeXml(dataset.name)}</name>
                <count providers="${dataset.providerCount}" dataProviders="${dataset.dataProviderCount}">${dataset.count}</count>
              </dataset>
            </c:forEach>
            </datasets>
          </c:if>
        </c:forEach>
        </dataProviders>
      </c:if>
    </provider>
  </c:forEach>
  </providers>
</c:if>
