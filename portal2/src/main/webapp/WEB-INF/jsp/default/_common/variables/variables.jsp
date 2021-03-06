<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="view" value="" />
<c:set var="query" value="" />
<c:set var="langActionUrl" value="" />
<c:set var="MyEuropeanaCurrent" value="" />
<c:set var="CommunitiesCurrent" value="" />
<c:set var="PartnersCurrent" value="" />
<c:set var="AboutUsCurrent" value="" />
<c:set var="ThoughtLabCurrent" value="" />

<c:catch var="termsAndConditionsException"><c:set var="termsAndConditions">${model.tc}</c:set></c:catch>
<c:if test="${!empty termsAndConditionsException}">
	<c:set var="termsAndConditions">${false}</c:set>
</c:if>

 <%--
  The resulting array has count of at least 1 and we want to navigate backwards only if there is at least one slash. 
	if begin and end are the same the foreach will be called 
	--%>
<c:set var = "slashCountArray" value="${fn:split(model.pageName,'/')}"/>
<c:set var = "slashCount" value="${fn:length(slashCountArray)}"/>


<c:if  test="${slashCountArray[0] != 'myeuropeana'}">
	
  <c:forEach begin="2" end="${slashCount}">
    <c:set var="webRoot">${webRoot}../</c:set>
  </c:forEach>
    
  <c:if test="${model.pageName == 'full-doc.html'}">
    <c:set var="webRoot" value="../../" />
  </c:if>
  
  <c:if test="${model.pageName == 'staticpage.html'}">
    <c:if test="${termsAndConditions}">
      <c:set var="webRoot" value="../" />
    </c:if>
  </c:if>

  <c:if test="${model.pageName == 'exception.html'}">
    <c:set var="webRoot" value="" />
  </c:if>
    
</c:if>
 
 
<c:set var="req" value="${pageContext.request}" />

<%--
<c:set var="homeUrlNS" value="${fn:replace(req.requestURL, fn:substring(req.requestURI, 0, fn:length(req.requestURI)), req.contextPath)}" />
<c:set var="homeUrl"   value="${homeUrlNS}/" />
--%>


<c:set var="homeUrlNS"  value="${model.portalServer}" />
<c:set var="homeUrl"    value="${homeUrlNS}/" />

<c:set var="myEuropeanaUrl"	value="${homeUrl}myeuropeana" />
<c:set var="branding"		value="${homeUrl}themes/${model.theme}" />

<%--
<c:if test="${model.pageName == 'exception.html'}"><c:set var="branding" value="../../themes/${model.theme}" /></c:if>
 --%>


<c:if test="${!empty model.locale}"><c:set var="locale" value="${model.locale}" /></c:if>

<c:if test="${!empty RequestParameters.view}"><c:set var="view" value="${RequestParameters.view}" /></c:if>
<c:if test="${!empty RequestParameters.query}"><c:set var="query" value="${RequestParameters.query}" /></c:if>
<c:if test="${model.pageName == 'search.html'}"><c:set var="langActionUrl" value="${model.viewUrlTable}" /></c:if>

<%-- menus --%>
<c:set var="menu1_active" value="" />
<c:set var="menu2_active" value="" />
<c:set var="menu3_active" value="" />
<c:set var="menu4_active" value="" />
<c:set var="menu5_active" value="" />
<c:set var="menu6_active" value="" />

<c:if test="${'index.html' == model.pageName}"><c:set var="menu1_active" value="active " /></c:if>
<c:if test="${'timeline.html' == model.pageName || 'map.html' == model.pageName}"><c:set var="menu2_active" value="active " /></c:if>
<c:if test="${'login.html' == model.pageName || 'myeuropeana/index' == model.pageName}"><c:set var="menu6_active" value="active " /></c:if>

<%-- search navigation icons --%>
<c:set var="gridview_active" value="" />
<c:set var="timeline_active" value="" />
<c:set var="map_active" value="" />

<c:if test="${'search.html' == model.pageName}"><c:set var="gridview_active" value="active " /></c:if>
<c:if test="${'timeline.html' == model.pageName}"><c:set var="timeline_active" value="active " /></c:if>
<c:if test="${'map.html' == model.pageName}"><c:set var="map_active" value="active " /></c:if>

<%-- a css class holder for the <div id="query> so that it can accommodate for the spacing issue --%>
<c:set var="menu_user_exists" value="" />

<c:set var="query_action" value="/search.html" />
<c:if test="${'timeline.html' == model.pageName}"><c:set var="query_action" value="/timeline.html" /></c:if>
<c:if test="${'map.html' == model.pageName}"><c:set var="query_action" value="/map.html" /></c:if>

<c:set var="language_menu_action" value="${model.currentUrl}" />
<c:if test="${empty model.currentUrl}"><c:set var="language_menu_action" value="/search.html" /></c:if>

<c:set var="header_class" value=' class="notranslate"' />
<c:if test="${empty model.embedded && !empty model.user}"><c:set var="header_class" value=' class="notranslate user-bar-added"' /></c:if>

<c:set var="titleMaxLength" value="40" />
<c:set var="providerMaxLength" value="25" />
<c:set var="providerNameMaxLength" value="25" />
