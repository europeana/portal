<%--
  set og_image_url
--%>
<c:set var="og_image_url" value="${model.portalServer}${model.portalName}/sp/img/europeana-logo-en.png"/>
<c:if test="fulldoc.html' == ${model.pageName}">
  <c:set var="og_image_url" value="${model.thumbnailUrl}"/>
</c:if>
<%--
  add og metadata
  @link http://ogp.me/
  @link http://developers.facebook.com/docs/beta/opengraph/objects/
  @link http://developers.facebook.com/docs/beta/opengraph/internationalization/
  @link http://developers.facebook.com/tools/debug
--%>
<%-- required --%>
<meta property="og:title" content="${model.pageTitle}"/>
<meta property="og:type" content="website"/>
<meta property="og:image" content="${og_image_url}"/>
<meta property="og:url" content="${model.metaCanonicalUrl}"/>
<%-- optional --%>
<meta property="og:description" content="${model.pageTitle}"/>
<meta property="og:site_name" content="Europeana"/>
<%--<meta property="og:locale" content="%{model.locale}"/>--%>