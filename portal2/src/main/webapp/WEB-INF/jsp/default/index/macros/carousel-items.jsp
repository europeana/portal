<%--

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


--%>

<%--
 * multilingo-menu-link.ftl
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
 * @version 2011-05-26 17.00 GMT+1
 --%>

<%-- #macro carouselitem ord1 --%>



<%--

<c:set var="url"><spring:message code="notranslate_carousel-item-${ord1}_a_url_t" /></c:set>

<c:if test='${!fn:contains(url, "http://"}'>
	<c:set var="url">/${url}</c:set>
</c:if>

<a href="${url}" title="<spring:message code="carousel-item-${ord1}_a_title_t" />" target="<spring:message code="notranslate_carousel-item-${ord1}_a_target_t" />">
	<img src="spring:message code="notranslate_carousel-item-${ord1}_img_url_t" />" width="<spring:message code="notranslate_carousel-item-${ord1}_img_width_t" />" alt="<spring:message code="carousel-item-${ord1}_img_alt_t" />"/>
</a>

 --%>