<%--
  set og_image_url
--%>
<c:set var="og_image_url" value="${model.portalServer}${model.portalName}/sp/img/europeana-logo-en.png"/>

<c:if test="${model.pageName=='full-doc.html'}">
  <c:set var="og_image_url" value="${fn:replace(model.thumbnailUrl, '&amp;', '&')}"/>
  <%--
  <c:set var="og_image_url" value="http://europeanastatic.eu/api/image?type=IMAGE&uri=http%3A%2F%2Fresolver.kb.nl%2Fresolve%3Furn%3DBYVANCKB%3Amimi_74g27%3A069r_min%26role%3Dthumbnail&size=FULL_DOC&fbrefresh=1"/>
   --%>
   
   <c:set var="og_image_url" value="    <meta property="og:image" content="https://static-secure.guim.co.uk/sys-images/Guardian/Pix/pixies/2013/1/29/1359490853921/Pauline-Marois-presents-a-008.jpg"/>"/>
   
   
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