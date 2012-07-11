<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>

<ul id="menu-main" class="notranslate">
  <%-- Home --%>
  <li><europeana:menulinks menu="menu-main" ord1="1" ord2="0" className="${menu1_active}menu-tab" /></li>

  <%-- Explore --%>
  <li><europeana:menulinks menu="menu-main" ord1="2" ord2="0" className="${menu2_active}dropdown menu-tab" />
    <ul>
      <li><europeana:menulinks menu="menu-main" ord1="2" ord2="1" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="2" ord2="2" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="2" ord2="3" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="2" ord2="4" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="2" ord2="5" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="2" ord2="6" className="menu-sub-tab" /></li>
    </ul>
  </li>

  <%-- Help --%>
  <li><europeana:menulinks menu="menu-main" ord1="3" ord2="0" className="${menu3_active}dropdown menu-tab" />
    <ul>
      <c:set var="className" value="menu-sub-tab" />
      <li><europeana:menulinks menu="menu-main" ord1="3" ord2="1" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="3" ord2="2" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="3" ord2="3" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="3" ord2="4" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="3" ord2="5" className="menu-sub-tab" /></li>
    </ul>
  </li>

  <%-- About Us --%>
  <li>
    <europeana:menulinks menu="menu-main" ord1="4" ord2="0" className="${menu4_active}dropdown menu-tab" />
    <ul>
      <c:set var="className" value="menu-sub-tab" />
      <li><europeana:menulinks menu="menu-main" ord1="4" ord2="1" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="4" ord2="2" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="4" ord2="3" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="4" ord2="4" className="menu-sub-tab" /></li>
    </ul>
   </li>

  <%-- Follow Us --%>
  <li>
    <europeana:menulinks menu="menu-main" ord1="5" ord2="0" className="${menu5_active}dropdown menu-tab" />
    <ul>
      <li><europeana:menulinks menu="menu-main" ord1="5" ord2="1" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="5" ord2="2" className="menu-sub-tab" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="5" ord2="3" className="menu-sub-tab external" /></li>
      <li><europeana:menulinks menu="menu-main" ord1="5" ord2="4" className="menu-sub-tab external" /></li>
    </ul>
  </li>

  <%-- My Europeana --%>
  <li><europeana:menulinks menu="menu-main" ord1="6" ord2="0" className="${menu6_active}menu-tab" /></li>

  <%-- Choose a language --%>
  <li><%@ include file="/WEB-INF/jsp/devel/_common/html/menus/language.jsp"%></li>
</ul>
