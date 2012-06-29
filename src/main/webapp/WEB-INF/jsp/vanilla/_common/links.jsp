<link rel="search" href="http://api.europeana.eu/api/opensearch.xml" type="application/opensearchdescription+xml" title="Europeana Search"/>
<link rel="shortcut icon" href="/${model.portalName}/favicon.ico"/>
<link rel="author" href="/humans.txt"/>
<!-- @todo make sure model.envAcceptance is available --> 
<%-- <c:if test='${model.pageName == "index.html" && model.envAcceptance}'> --%>
<c:if test='${model.pageName != "index.html"}'>
  <link href="${model.googlePlusPublisherId}" rel="publisher">
</c:if>