<%@ page session="false" language="java" contentType="text/xml; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9" xmlns:image="http://www.google.com/schemas/sitemap-image/1.1">
  <c:if test="${model.hasResults}">
    <c:forEach items="${model.results}" var="record">
      <url>
        <loc>${record.loc}</loc>
        <c:if test="${model.showImages}">
          <image:image>
            <image:loc>${model.cacheUrl}uri=${record.image}&amp;size=FULL_DOC</image:loc>
            <image:title>${record.title?xhtml}</image:title>
          </image:image>
        </c:if>
        <priority>${record.priority}</priority>
      </url>
    </c:forEach>
  </c:if>
</urlset>
