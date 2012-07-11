<%@ taglib prefix="europeana" tagdir="/WEB-INF/tags"%>
<div id="additional-info" class="sidebar" about="${model.document.id}">
  <%@ include file="/WEB-INF/jsp/devel/fulldoc/content/sidebar-left/image.jsp"%>
  <europeana:displayRights inLightbox="false" />
  <%@ include file="/WEB-INF/jsp/devel/fulldoc/content/sidebar-left/original-context.jsp"%>
  <%@ include file="/WEB-INF/jsp/devel/fulldoc/content/sidebar-left/additional-info.jsp"%>
</div>