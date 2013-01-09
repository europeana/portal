<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<div class="row">
  <%@ include file="/WEB-INF/jsp/default/_common/content/messages.jsp"%>
  
  <c:choose>
  
    <c:when test="${!empty model.bodyContent}">

		<c:choose>
	    	<c:when test="${!empty model.leftContent}">
	    
	    		<div class="eight columns push-four">
	    			<div style="padding-left:1em;">
	    				${model.bodyContent}
	    			</div>
	    		</div>
	    	</c:when>
		    <c:otherwise>
				<div class="twelve columns">
					${model.bodyContent}
				</div>
		    </c:otherwise>
    	</c:choose>
    	
    </c:when>
    <c:otherwise>
    
    	<h2>Unable to find content for this page.</h2>
    	
    </c:otherwise>
  </c:choose>
  
   <%@ include file="/WEB-INF/jsp/default/staticpage/content/sidebar.jsp"%>
  
  
  
</div>