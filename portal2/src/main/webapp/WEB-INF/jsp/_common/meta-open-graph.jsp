<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--
  set og_image_url
--%>
<c:set var="og_image_url"		value="${model.portalServer}${model.portalName}/sp/img/europeana-logo-en.png"/>
<c:set var="og_title"			value="${fn:escapeXml(model.pageTitle)}"/>
<c:set var="og_description"		value="${fn:escapeXml(model.pageTitle)}"/>

<c:if test="${model.pageName=='full-doc.html'}">
  <c:set var="og_image_url" 	value="${fn:replace(model.thumbnailUrlUnescaped, '&amp;', '&')}"/>  
  <c:set var="og_description"	value="${fn:escapeXml( model.document.dcDescription[0] ) }"/>
  
	<%-- if title is empty use the description --%>
	
	<c:if test="${empty model.pageTitle}">
		<c:set var="og_title"	value="${fn:escapeXml(model.document.dcDescription[0])}"/>
	</c:if>
  
	<%-- if description is empty use the title --%>
	
	<c:if test="${empty model.document.dcDescription[0]}">
		<c:set var="og_description" 	value="${model.pageTitle}"/>
	</c:if>
  
</c:if>


<%--
  twitter card
--%>

<meta name="twitter:card"			content="summary"/>
<meta name="twitter:url"			content="${model.metaCanonicalUrl}"/>
<meta name="twitter:title"			content="${og_title}"/>
<meta name="twitter:description"	content="${og_description}"/>
<meta name="twitter:site"			content="@EuropeanaEU"/>
<meta name="twitter:image"			content="${og_image_url}"/>

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