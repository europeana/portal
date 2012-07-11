<%@ include file="/WEB-INF/jsp/devel/_common/include.jsp" %>
<%--
	http://www.w3.org/TR/xhtml-rdfa-primer/
	http://creativecommons.org/licenses/by-nc/3.0/
	cc is able to scrape the page's metadata it can also offer rights information to the browser
	nb: html element attributes about and resource are related to the rdfa metadata of the page
--%>
<%@ include file="/WEB-INF/jsp/devel/_common/html/doctype.jsp" %>
<%@ include file="/WEB-INF/jsp/devel/_common/variables/variables.jsp" %>

<%-- next 4 are new ones! --%>
<%@ include file="/WEB-INF/jsp/devel/fulldoc/variables/variables.jsp" %>
<%-- @ include file="/WEB-INF/jsp/devel/fulldoc/macros/display-ese-data-as-meta.jsp" --%>
<%-- @ include file="/WEB-INF/jsp/devel/fulldoc/macros/display-ese-data-as-html.jsp" --%>
<%-- @ include file="/WEB-INF/jsp/devel/fulldoc/macros/rights.jsp" --%>

<%@ include file="/WEB-INF/jsp/devel/_common/html/open-html.jsp" %>
	<%@ include file="/WEB-INF/jsp/devel/_common/html/header.jsp" %>
	<%@ include file="/WEB-INF/jsp/devel/fulldoc/content/content.jsp" %>
	<%@ include file="/WEB-INF/jsp/devel/_common/html/footer.jsp" %>
	<%@ include file="/WEB-INF/jsp/devel/_common/html/javascripts.jsp" %>
	<%--
	<%@ include file="/WEB-INF/jsp/devel/fulldoc/content/full-excerpt/lightbox.jsp" %>
	 --%>
<%@ include file="/WEB-INF/jsp/devel/_common/html/close-html.jsp" %>
