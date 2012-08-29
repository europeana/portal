
<c:set var="xmlns" value='' />
<c:set var="lang" value='${model.locale}' />


<c:choose>
	<c:when test='${model.pageName == "fulldoc.html"}'>
	
		<c:set var="xmlns" value='
			xmlns="http://www.w3.org/1999/xhtml"
			xmlns:og="http://ogp.me/ns#"
			xmlns:fb="http://www.facebook.com/2008/fbml"
			xmlns:dcterms="http://purl.org/dc/terms/"
			xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
			xmlns:europeana="http://www.europeana.eu/schemas/ese/"
			xmlns:cc="http://creativecommons.org/ns#"
			xmlns:marcrel="http://id.loc.gov/vocabulary/relators/"
			xmlns:foaf="http://xmlns.com/foaf/0.1/"
			xmlns:enrichment="http://www.europeana.eu/schemas/ese/enrichment/"
			xmlns:dc="http://purl.org/dc/elements/1.1/"
			' />
			
	</c:when>
	<c:when test='${model.pageName == "index.html"}'>
		<c:set var="xmlns" value='
			xmlns="http://www.w3.org/1999/xhtml"
			xmlns:og="http://ogp.me/ns#"
			xmlns:fb="http://www.facebook.com/2008/fbml"
		'/>
	</c:when>

	<c:when test='${model.pageName == "map.html"}'>
		<c:set var="xmlns" value='
			xmlns="http://www.w3.org/1999/xhtml"
			xmlns:og="http://ogp.me/ns#"
			xmlns:fb="http://www.facebook.com/2008/fbml"
		'/>
	</c:when>

	<c:when test='${model.pageName == "search.html"}'>
		<c:set var="xmlns" value='xmlns="http://www.w3.org/1999/xhtml"
			xmlns:og="http://ogp.me/ns#"
			xmlns:fb="http://www.facebook.com/2008/fbml"
		'/>
	</c:when>

	<c:when test='${model.pageName == "timeline.html"}'>
		<c:set var="xmlns" value='
			xmlns="http://www.w3.org/1999/xhtml"
			xmlns:og="http://ogp.me/ns#"
			xmlns:fb="http://www.facebook.com/2008/fbml"
		'/>
	</c:when>
</c:choose>


<!--[if lt IE 7 ]>
<html ${xmlns} lang="${lang}"	class="ie ie6">
<![endif]-->
<!--[if IE 7 ]>
<html ${xmlns} lang="${lang}"	class="ie ie7">
<![endif]-->
<!--[if IE 8 ]>
<html ${xmlns} lang="${lang}"	class="ie ie8">
<![endif]-->
<!--[if IE 9 ]>
<html ${xmlns} lang="${lang}"	class="ie ie9">
<![endif]-->
<!--[if gt IE 9]>
<html ${xmlns} lang="${lang}"	class="ie">
<![endif]-->
<!--[if !IE]><!-->
<html ${xmlns} lang="${lang}" >
<!--<![endif]-->






