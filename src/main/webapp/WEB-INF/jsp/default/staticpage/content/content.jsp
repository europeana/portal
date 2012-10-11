<div class="row">


  <%@ include file="/WEB-INF/jsp/default/_common/content/messages.jsp"%>
  <%@ include file="/WEB-INF/jsp/default/_common/menus/sidebar.jsp"%>
  <c:choose>
  
  
    <c:when test="${!empty model.bodyContent}">
    
    
		<c:choose>
	    	<c:when test="${!empty model.leftContent}">
	    
	    		<div class="eight columns">
	    			<div style="padding-left:1em;">
	    				${model.bodyContent}
	    			</div>
	    		</div>
	    	</c:when>
		    <c:otherwise>
			    
				${model.bodyContent}
	
		    </c:otherwise>
    	</c:choose>
    	
    </c:when>
    
    
    <c:otherwise>
    	<h2>Unable to find content for this page.</h2>
    </c:otherwise>
  </c:choose>
  
  
</div>