
<%--
  set og_image_url
--%>
<c:set var="og_image_url"		value="${model.portalServer}${model.portalName}/sp/img/europeana-logo-en.png"/>
<c:set var="og_title"			value="${model.pageTitle}"/>
<c:set var="og_description"		value="${model.pageTitle}"/>

<c:if test="${model.pageName=='full-doc.html'}">
  <c:set var="og_image_url" 	value="${fn:replace(model.thumbnailUrlUnescaped, '&amp;', '&')}"/>  
  <c:set var="og_description"	value="${fn:escapeXml( model.document.dcDescription[0] ) }"/>
</c:if>


<%--
  twitter card
--%>

<meta name="twitter:card"			content="summary"/>
<meta name="twitter:url"			content="${model.metaCanonicalUrl}"/>
<meta name="twitter:title"			content="${og_title}"/>
<meta name="twitter:description"	content="${og_description}"/>
<meta name="twitter:site"			content="@EuropeanaEU"/>


<%--
  add og metadata
  @link http://ogp.me/
  @link http://developers.facebook.com/docs/beta/opengraph/objects/
  @link http://developers.facebook.com/docs/beta/opengraph/internationalization/
  @link http://developers.facebook.com/tools/debug
--%>

<%-- required --%>

<meta property="og:title" content="${og_title}"/>
<meta property="og:type" content="website"/>
<meta property="og:image" content="${og_image_url}"/>
<meta property="og:url" content="${model.metaCanonicalUrl}"/>
<%-- optional --%>
<meta property="og:description" content="${og_description}"/>
<meta property="og:site_name" content="Europeana"/>
<%--<meta property="og:locale" content="%{model.locale}"/>--%>