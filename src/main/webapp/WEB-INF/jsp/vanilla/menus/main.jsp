<h1>main menu</h1>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="menu" value="menu-main" />
<c:set var="ord1" value="1" />
<c:set var="ord2" value="0" />
<c:set var="className" value="${menu1_active}menu-tab" />
<ul>
  
  <%-- Home --%>
  <li><%@ include file="/WEB-INF/jsp/vanilla/macros/menulinks.jsp" %></li>
  
  
  <%-- Explore --%>
  <li>
    
    <c:set var="ord1" value="2" />
    <c:set var="className" value="${menu2_active}dropdown menu-tab" />
    <%@ include file="/WEB-INF/jsp/vanilla/macros/menulinks.jsp" %>
      
      <ul>

        <c:set var="className" value="menu-sub-tab" />
        <li><c:set var="ord1" value="1" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
        <li><c:set var="ord1" value="2" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
        <li><c:set var="ord1" value="3" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
        <li><c:set var="ord1" value="4" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
        <li><c:set var="ord1" value="5" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
        <li><c:set var="ord1" value="6" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>

      </ul>
      
  </li>
  
  
  <%-- Help --%>
  <li>
    
    <c:set var="ord1" value="3" />
    <c:set var="ord2" value="0" />
    <c:set var="className" value="${menu3_active}dropdown menu-tab" />
    <%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %>
    
    <ul>
      <c:set var="className" value="menu-sub-tab" />
      <li><c:set var="ord2" value="1" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
      <li><c:set var="ord2" value="2" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
      <li><c:set var="ord2" value="3" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
      <li><c:set var="ord2" value="4" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
      <li><c:set var="ord2" value="5" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
      
    </ul>
    
  </li>
  
  
  <%-- About Us --%>
  <li>
    <c:set var="ord1" value="4" />
    <c:set var="ord2" value="0" />
    <c:set var="className" value="${menu4_active}dropdown menu-tab" />
    <%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %>
    
    <ul>
      <c:set var="className" value="menu-sub-tab" />
      <li><c:set var="ord2" value="1" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
      <li><c:set var="ord2" value="2" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
      <li><c:set var="ord2" value="3" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
      <li><c:set var="ord2" value="4" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
      
    </ul>
    
  </li>
  
  
  <%-- Follow Us --%>
  <li>
    <c:set var="ord1" value="5" />
    <c:set var="ord2" value="0" />
    <c:set var="className" value="${menu5_active}dropdown menu-tab" />
    <%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %>
    
    <ul>
      
      <c:set var="className" value="menu-sub-tab" />
      <li><c:set var="ord2" value="1" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
      <li><c:set var="ord2" value="2" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
      <c:set var="className" value="menu-sub-tab external" />
      <li><c:set var="ord2" value="3" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
      <li><c:set var="ord2" value="4" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
      
    </ul>
    
  </li>


  <%-- My Europeana --%>
  <c:set var="ord1" value="6" />
  <c:set var="ord2" value="0" />
  <c:set var="className" value="${menu6_active}menu-tab" />
  <li><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
  
  
  <%-- Choose a language --%>
  <li>
    
    <#include '/_common/html/menus/language.ftl'/>
    
  </li>
  
</ul>