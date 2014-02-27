<%@ page session="false" language="java" contentType="text/plain; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="LN" value="\n"/>
<c:set var="R" value="\r"/>
<c:if test="${model.hasResults}"
><c:forEach items="${model.results}" var="provider" varStatus="provStatus"
><c:set var="pp1"><c:choose><c:when test="${provStatus.last}">o</c:when><c:otherwise>+</c:otherwise></c:choose></c:set
><c:set var="po">.${pp1}-- ${provider.name} (${provider.count}<c:if test="${provider.totalDatasetCount > 0 && provider.count != provider.totalDatasetCount}">, error: ${provider.totalDatasetCount}</c:if>)</c:set
><c:out value="${po}" />
<c:if test="${fn:length(provider.dataProviders) > 0}"
><c:forEach items="${provider.dataProviders}" var="dataProvider" varStatus="dprovStatus"
><c:set var="dpp1"><c:choose><c:when test="${provStatus.last}">.</c:when><c:otherwise>|</c:otherwise></c:choose></c:set
><c:set var="dpp2"><c:choose><c:when test="${dprovStatus.last}">o</c:when><c:otherwise>+</c:otherwise></c:choose></c:set
><c:set var="dpo">.${dpp1}..${dpp2}-- ${fn:replace(fn:replace(dataProvider.name, LN, ' '), R, ' ')} (${dataProvider.count}<c:if test="${dataProvider.count != dataProvider.totalDatasetCount}">, error: ${dataProvider.totalDatasetCount}</c:if>)</c:set
><c:out value="${dpo}" />
<c:if test="${fn:length(dataProvider.datasets) > 0}"
><c:forEach items="${dataProvider.datasets}" var="dataset" varStatus="dsStatus"
><c:set var="dsp1"><c:choose><c:when test="${provStatus.last}">.</c:when><c:otherwise>|</c:otherwise></c:choose></c:set
><c:set var="dsp2"><c:choose><c:when test="${dprovStatus.last}">.</c:when><c:otherwise>|</c:otherwise></c:choose></c:set
><c:set var="dsp3"><c:choose><c:when test="${dsStatus.last}">o</c:when><c:otherwise>+</c:otherwise></c:choose></c:set
><c:set var="dso">.${dsp1}..${dsp2}..${dsp3}-- ${dataset.name} (providers=${dataset.providerCount} dataProviders=${dataset.dataProviderCount} count=${dataset.count})</c:set
><c:out value="${dso}" />
</c:forEach
></c:if
></c:forEach
></c:if
><c:forEach items="${provider.datasets}" var="dataset" varStatus="dsStatus"
><c:set var="dsp1"><c:choose><c:when test="${provStatus.last}">.</c:when><c:otherwise>|</c:otherwise></c:choose></c:set
><c:set var="dsp3"><c:choose><c:when test="${dsStatus.last}">o</c:when><c:otherwise>+</c:otherwise></c:choose></c:set
><c:set var="dso">.${dsp1}..${dsp3}-- ${dataset.name} (providers=${dataset.providerCount} dataProviders=${dataset.dataProviderCount} count=${dataset.count})</c:set
><c:out value="${dso}" />
</c:forEach
></c:forEach
></c:if>
