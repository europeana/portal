<%@ include file="/WEB-INF/jsp/vanilla/_common/navigation/navigation.jsp" %>

<%-- @todo : fix breadcrumb so that it works. currently getting exception error --%>
<%@ include file="/WEB-INF/jsp/vanilla/_common/navigation/breadcrumb.jsp" %>



<div about="${model.document.id}">
<%@ include file="/WEB-INF/jsp/vanilla/fulldoc/content/image.jsp" %>
<%-- <%@ include file="/WEB-INF/jsp/vanilla/fulldoc/content/original-context.jsp" %> --%>
<%-- <%@ include file="/WEB-INF/jsp/vanilla/fulldoc/content/additional-info.jsp" %> --%>
</div>
<%@ include file="/WEB-INF/jsp/vanilla/fulldoc/content/full-excerpt.jsp" %>
<%@ include file="/WEB-INF/jsp/vanilla/fulldoc/content/sidebar-right.jsp" %>