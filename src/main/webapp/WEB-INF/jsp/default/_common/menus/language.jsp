
<c:choose>
  <c:when test="${not empty model.currentUrl}">
    <c:set var="language_menu_action" value="${model.currentUrl}"/>
  </c:when>
  <c:otherwise>
    <c:set var="language_menu_action" value="${model.portalName}/search.html"/>
  </c:otherwise>
</c:choose>

<form action="${language_menu_action}" method="post" id="language-selector" style="display:table-cell; vertical-align:middle;">
	<div class="eu-menu no-highlight white hide-ilb-on-phones" id="lang-menu">
		<span class="menu-label"></span>
		<span class="icon-arrow-3 open-menu"></span>
	    <ul title="<spring:message code='ChooseLanguage_t' />">
			<li class="item">
				<a class="choose"><spring:message code='ChooseLanguage_t'/></a>
			</li>
			<c:forEach items="${model.portalLanguages}" var="language">
				<li class="item">
					<a class="${language.languageCode}">${language.languageName}</a>
				</li>
			</c:forEach>
		</ul>
		<input type="hidden" name="embeddedlang" value="" style="margin:0;padding:0;"/>
	</div>
</form>
  