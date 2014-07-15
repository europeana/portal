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
				
				
				<!-- HIERARCHICAL OBJECTS -->
				
			    <c:set var="api_root_url" value="${model.apiUrl}/v2/record${model.document.about}"></c:set>  
			    
			    <script type="text/javascript">
					var hierarchyTestUrl =  '${api_root_url}/self.json?wskey=api2demo';
					
					<!-- DEMO -->				
					<c:if test="${model.debug && model.showHierarchical}">
						var hierarchical = true;
					</c:if>
			    </script>
				
				<!-- END HIERARCHICAL OBJECTS -->
				

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