<%@ include file="/WEB-INF/jsp/default/_common/include.jsp" %>
<%--
	http://www.w3.org/TR/xhtml-rdfa-primer/
	http://creativecommons.org/licenses/by-nc/3.0/
	cc is able to scrape the page's metadata it can also offer rights information to the browser
	nb: html element attributes about and resource are related to the rdfa metadata of the page
--%>
<%@ include file="/WEB-INF/jsp/default/_common/html/doctype.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/variables/variables.jsp" %>
<%-- @ include file="/WEB-INF/jsp/default/_common/macros/string-limiter.jsp" --%>

<%-- the next 3 are new ones! --%>
<%-- (Andy: removed display-ese-data-as-html) --%>

<%@ include file="/WEB-INF/jsp/default/fulldoc/variables/variables.jsp" %>
<%@ include file="/WEB-INF/jsp/default/fulldoc/macros/display-ese-data-as-meta.jsp" %>
<%@ include file="/WEB-INF/jsp/default/fulldoc/macros/rights.jsp" %>

<%-- begin page proper --%>

<%@ include file="/WEB-INF/jsp/default/_common/html/open-html.jsp" %>


<%-- begin responsive grid --%>
<div class="container">

	<%@ include file="/WEB-INF/jsp/default/_common/header.jsp" %>
	<%@ include file="/WEB-INF/jsp/default/fulldoc/content/content.jsp" %>
	<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/responsive-lightbox.jsp" %>
	<%@ include file="/WEB-INF/jsp/default/_common/footer.jsp" %>
	<%@ include file="/WEB-INF/jsp/default/_common/html/javascripts.jsp" %>
	
</div>
<%-- end responsive grid --%>

<%@ include file="/WEB-INF/jsp/default/_common/html/close-html.jsp" %>

