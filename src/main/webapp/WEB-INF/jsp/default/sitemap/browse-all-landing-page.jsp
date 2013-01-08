<%@ page session="false" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" 
%><%@ include file="/WEB-INF/jsp/default/sitemap/incl_browse-all-header.jspf" %>
<h1>${model.title}</h1>
<c:forEach items="${model.results}" var="doc" varStatus="status">
<a href="${doc.getFullDocUrl()}" title="${doc.title[0]}"><img class="landing thumb" src="${doc.getThumbnail()}" alt="${doc.title[0]}"/></a>
</c:forEach>
</body>
