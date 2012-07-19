<%@ page session="false" language="java" contentType="application/json; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
{"total":${model.total},"start":${model.start},"rows":${model.rows},"items":[<c:forEach 
items="${model.carouselItems}" var="item" varStatus="itemStatus">
  <%@ include file="/WEB-INF/jsp/default/carousel/carousel.jspf" %><c:if test="${!itemStatus.last}">,</c:if>
</c:forEach>]}