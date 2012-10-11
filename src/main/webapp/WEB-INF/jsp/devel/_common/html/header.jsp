<!-- header -->
<div id="header"${header_class}>
  <%@ include file="/WEB-INF/jsp/devel/_common/html/menus/main.jsp" %>
  <%@ include file="/WEB-INF/jsp/devel/_common/html/menus/user.jsp" %>
  <%@ include file="/WEB-INF/jsp/devel/_common/html/logo.jsp" %>
  <%@ include file="/WEB-INF/jsp/devel/_common/html/query.jsp" %>
</div>
<script type="text/javascript">
var completionTranslations = {};
completionTranslations['Title']     = "<spring:message code='FieldedSearchTitle_t' />";
completionTranslations['Place']     = "<spring:message code='FieldedSearchWhere_t' />";
completionTranslations['Time/Period'] = "<spring:message code='FieldedSearchWhen_t' />";
completionTranslations['Subject']   = "<spring:message code='FieldedSearchWhat_t' />";
completionTranslations['Creator']   = "<spring:message code='FieldedSearchWho_t' />";
</script>
<!-- /header -->