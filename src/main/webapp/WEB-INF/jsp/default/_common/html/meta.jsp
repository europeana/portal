<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="eu" tagdir="/WEB-INF/tags" %>
<meta name="description" content="${model.pageTitle}"/>
<link rel="author" href="/humans.txt" />

<c:if test="${not model.indexable}">
  <c:choose>
    <c:when test="${model.indexingBlocked}">
      <meta name="robots" content="noindex,nofollow"/>
    </c:when>
    <c:otherwise>
       <meta name="robots" content="noindex,follow"/>
    </c:otherwise>
  </c:choose>
</c:if>

<meta name="viewport" content="width=device-width, initial-scale=1" />

<c:if test='${model.pageName == "fulldoc.html" || model.pageName == "full-doc.html"}'>
  <%@ include file="/WEB-INF/jsp/_common/meta-facebook.jsp" %>
  <%@ include file="/WEB-INF/jsp/_common/meta-open-graph.jsp" %>
  <%-- @todo make sure model.formatLabels exists and that esemeta fields are displayed --%>
  <c:if test="${not model.formatLabels}">
    <%-- eu:displayEseDataAsMeta metaDataFields="${model.metaDataFields}" showFieldName="false" /--%>
  </c:if>
</c:if>
<c:if test='${model.pageName == "index.html"}'>
  <%@ include file="/WEB-INF/jsp/_common/meta-facebook.jsp" %>
  <%@ include file="/WEB-INF/jsp/_common/meta-open-graph.jsp" %>
</c:if>
<c:if test='${model.pageName == "map.html"}'>
  <%@ include file="/WEB-INF/jsp/_common/meta-facebook.jsp" %>
  <%@ include file="/WEB-INF/jsp/_common/meta-open-graph.jsp" %>
</c:if>
<c:if test='${model.pageName == "search.html"}'>
  <%@ include file="/WEB-INF/jsp/_common/meta-facebook.jsp" %>
  <%@ include file="/WEB-INF/jsp/_common/meta-open-graph.jsp" %>
</c:if>
<c:if test='${model.pageName == "timeline.html"}'>
  <%@ include file="/WEB-INF/jsp/_common/meta-facebook.jsp" %>
  <%@ include file="/WEB-INF/jsp/_common/meta-open-graph.jsp" %>
</c:if>

<meta name="application-name" content="Europeana"/>
<meta name="msapplication-TileColor" content="#8C0095"/>
<meta name="msapplication-TileImage" content="${branding}/images/3af68e97-900f-4170-9a03-86ea73d68dcd.png"/>

