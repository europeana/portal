<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>
<compress:html enabled="true" compressJavaScript="false"  yuiJsDisableOptimizations="true">


<%@ include file="/WEB-INF/jsp/_common/tag-libraries.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/include.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/html/doctype.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/variables/variables.jsp" %>
<%@ include file="/WEB-INF/jsp/default/_common/html/open-html.jsp" %>


	<div class="container">
		<%@ include file="/WEB-INF/jsp/default/_common/header.jsp" %>
		<%@ include file="/WEB-INF/jsp/default/index/content.jsp" %>
		<%@ include file="/WEB-INF/jsp/default/_common/footer.jsp" %>
		<%@ include file="/WEB-INF/jsp/default/_common/html/javascripts.jsp" %>
	</div>


<%@ include file="/WEB-INF/jsp/default/_common/html/close-html.jsp" %>
</compress:html>
