<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
 * menu-links.ftl
 *
 * This macro was built in order to populate menu link attributes
 * using the language keys and values found in the file
 * /trunk/portal-full/src/main/message_keys/message.properties
 *
 * These language keys can go through the translation process if needed
 * by submitting them through the multilingo service. If this is done
 * then the translations are pulled in from the appropriate message_lc.properties
 * file.
 *
 * Assumptions :
 * language key format - [optional prefix_]menu-ord1[optional -ord2][optional _suffix]
 * menus are no more than 1 level deep, thus ord2 is the deepest level
 * ${model.portalName} - will always represent the root level of the portal
 * notranslate_${menu}-${ordinal}_a_url - always exists
 * notranslate_${menu}-${ordinal}_a_target
 * ${menu}-${ordinal}_a_title - always exists
 * ${menu}-${ordinal}_a_text - always exists
 *
 * @param menu string - the menu name used in the language key, e.g., menu-main
 * @param ord1 int - the ordinal position of the menu in the root of the menu
 * @param ord2 int - the ordinal position of the menu one level down from the root
 * @param class string - any class name you wish to assign to the <a> tag, can be empty
 * 
 * @author Dan Entous
 * @version 2011-05-26 15.40 GMT+1
 --%>
<c:choose>
	<c:when test='${ord2 == "0"}'>
		<c:set var="ordinal" value="${ord1}" />
		<c:set var="htag" value="h1" />
	</c:when>
	<c:otherwise>
		<c:set var="ordinal" value="${ord1}-${ord2}" />
		<c:set var="htag" value="h2" />
	</c:otherwise>
</c:choose>
<c:set var="msg_notranslate"><spring:message code="notranslate_${menu}-${ordinal}_a_target_t" /></c:set>
<c:set var="msg_title"><spring:message code="${menu}-${ordinal}_a_title_t" /></c:set>
<c:set var="msg_text"><spring:message code="${menu}-${ordinal}_a_text_t" /></c:set>

<c:set var="url"><spring:message code="notranslate_${menu}-${ordinal}_a_url_t" /></c:set>
contains
<c:if test='${!fn:contains(url, "http://")}'>
	<c:set var="url">/${model.portalName}/${url}</c:set>
</c:if>

<a href="${url}" target="${msg_notranslate}" title="${msg_title}" <c:if test="${fn:length(className) > 0}">class="${className}"</c:if>>${msg_text}</a>
