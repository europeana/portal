<c:if test="${not empty model.metaCanonicalUrl}">
<link rel="canonical" href="${model.metaCanonicalUrl}"/></c:if>
<link rel="search" href="http://api.europeana.eu/api/opensearch.xml" type="application/opensearchdescription+xml" title="Europeana Search"/>
<link rel="shortcut icon" href="/${model.portalName}/favicon.ico"/>
<link rel="author" href="https://plus.google.com/u/0/115619270851872228337 ">
<link href="/humans.txt"/><%-- @todo make sure model.envAcceptance is available --%>
<c:if test='${model.pageName == "index.html"} && ${not model[envAcceptance]}'>
<link href="${model.googlePlusPublisherId}" rel="publisher"/></c:if>