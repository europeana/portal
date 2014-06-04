<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<% pageContext.setAttribute("newLineChar1", "\r"); %> 
<% pageContext.setAttribute("newLineChar2", "\n"); %> 

<div id="content" class="row">
	<div class="twelve columns">

		<div class="row">
			<%@ include file="/WEB-INF/jsp/default/fulldoc/content/navigation/navigation.jsp" %>
		</div>

		<div class="row" about="${model.document.cannonicalUrl}" vocab="http://schema.org/" typeof="CreativeWork">
			<div class="three-cols-fulldoc-sidebar">
				<%@ include file="/WEB-INF/jsp/default/fulldoc/content/sidebar-left/sidebar-left.jsp" %>
			</div>

			<div class="nine-cols-fulldoc" id="main-fulldoc-area">
				<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/full-excerpt.jsp" %>
				
				
				<c:if test="${model.showHierarchical}">
					<script type="text/javascript">
						var hierarchical = true;
					</script>

					<div class="fulldoc-cell">					
						<div class="hierarchy-objects">
							<div class="hierarchy-top-panel">
								<div class="hierarchy-prev"><a>view items above</a><span class="count"></span></div>
								
								<div class="hierarchy-title"><a></a><span class="count"></span></div>
							</div>
			
							<div class="hierarchy-container">
								<div id="hierarchy"></div>
							</div>		
							<div class="hierarchy-bottom-panel">
								<div class="hierarchy-next"><a>view items below</a><span class="count"></span></div>
								<div class="expand-collapse"><a class="expand-all">expand all items</a><a class="collapse-all">collapse all items</a></div>
							</div>
						</div>
					</div>					
				</c:if>

				<c:if test="${model.europeanaMlt != null && !empty model.europeanaMlt}">
					<div class="fulldoc-cell">
						<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/europeana-mlt.jspf" %>
					</div>
				</c:if>
				
			</div>
		</div>

		<c:if test="${model.europeanaMlt == null || empty model.europeanaMlt}">
			<c:if test="${!empty model.seeAlsoSuggestions && fn:length(model.seeAlsoSuggestions.fields) > 0}">
				<div class="row">
					<div class="sidebar-right show-on-x">
						<%@ include file="/WEB-INF/jsp/default/fulldoc/content/full-excerpt/see-also.jspf" %>
					</div>
				</div>
			</c:if>
		</c:if>

	</div>
</div>