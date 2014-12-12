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
				
			    <script type="text/javascript">
			    	window.waitMessages = [
           	   		    {
           	   		    	"time" : 3,
           	   		    	"msg"  : '<spring:message code="load_wait_message_1" />'
           	   		    },
           	   		    {
           	   		    	"time" : 8,
           	   		    	"msg"  : '<spring:message code="load_wait_message_2" />'
           	   		    },
           	   		    {
           	   		    	"time" : 60,
           	   		    	"msg"  : '<spring:message code="load_wait_message_3" />'
           	   		    }
           	   		];
			    </script>


				<c:if test="${model.showHierarchical}">
				    <script type="text/javascript">
				    
				    	var showHierarchical	 = ${model.showHierarchical};
						
						window.apiKey            =   'api2demo';
						window.apiServerRoot     =  '${model.apiUrl}/v2/record';
						var hierarchyOriginalUrl = '${model.document.about}';
						var hierarchyTestUrl     =  window.apiServerRoot + '${model.document.about}/ancestor-self-siblings.json?wskey=' + window.apiKey;
						var hierarchyHeader      = '<spring:message code="hierarchy_header_t" />';
						
	    			</script>
    			</c:if>
							    
				
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