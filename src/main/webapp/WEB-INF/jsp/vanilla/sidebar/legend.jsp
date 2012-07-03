<!-- legend.jsp -->
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<h2 id="legend"><spring:message code="Legend_t" />:</h2>
<div id="legend-icons">
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:IMAGE"	title="<spring:message code="Image_t" />"><spring:message code="Image_t" /></a>
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:VIDEO"	title="<spring:message code="Video_t" />"><spring:message code="Video_t" /></a>
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:TEXT"	title="<spring:message code="Text_t" />"><spring:message code="Text_t" /></a>
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:SOUND"	title="<spring:message code="Sound_t" />"><spring:message code="Sound_t" /></a>
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:3D"	title="<spring:message code="3D_t" />"><spring:message code="3D_t" /></a>
</div>
<!-- /legend.jsp -->
