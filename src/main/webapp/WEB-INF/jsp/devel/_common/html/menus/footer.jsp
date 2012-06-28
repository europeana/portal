<!-- footer -->
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<ul id="menu-footer">
	<c:set var="menu" value="menu-footer" />
	<c:set var="ord2" value="0" />
	<c:set var="className" value="" />
	<li><c:set var="ord1" value="1" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
	<li><c:set var="ord1" value="2" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
	<li><c:set var="ord1" value="3" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
	<li><c:set var="ord1" value="4" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
	<li><c:set var="ord1" value="5" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
	<li><c:set var="ord1" value="6" /><%@ include file="/WEB-INF/jsp/_common/macros/menulinks.jsp" %></li>
 	<li class="separator">co-funded by the European Union</li>
	<li>
		<span title="" class="icons eu"></span>
	</li>
</ul>
<!-- /footer -->
