<!-- legend.jsp -->
<h2 id="legend"><spring:message code="Legend_t"/>:</h2>
<div id="legend-icons">
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:IMAGE" class="image" title="<spring:message code="Image_t"/>"><spring:message code="Image_t"/></a>
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:VIDEO" class="video" title="<spring:message code="Video_t"/>"><spring:message code="Video_t"/></a>
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:TEXT" class="text" title="<spring:message code="Text_t"/>"><spring:message code="Text_t"/></a>
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:SOUND" class="sound" title="<spring:message code="Sound_t"/>"><spring:message code="Sound_t"/></a>
	<a href="/${model.portalName}/${model.pageName}?query=${model.query}&amp;qf=TYPE:3D" class="threeD" title="<spring:message code="3D_t"/>"><spring:message code="3D_t"/></a>
</div>
<!-- /legend.jsp -->
