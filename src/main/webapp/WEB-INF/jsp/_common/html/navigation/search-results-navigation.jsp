<div class="search-results-navigation notranslate">
	<%@ include file="/WEB-INF/jsp/_common/html/navigation/icons.jsp" %>
	<%@ include file="/WEB-INF/jsp/_common/html/navigation/pagination.jsp" %>
	<script type="text/javascript">
		/* hide the link to the map if we're browsing with Internet Explorer */
		if( navigator.userAgent.indexOf("MSIE") > -1 ){
			hideElements = function(className) {
				var hasClassName = new RegExp("(?:^|\\s)" + className + "(?:$|\\s)");
				var allElements = document.getElementsByTagName("*");
				var element;
				for (var i = 0; (element = allElements[i]) != null; i++) {
					var elementClass = element.className;
					if (elementClass && elementClass.indexOf(className) != -1 && hasClassName.test(elementClass)){
						element.parentNode.style.display =  "none";
					}
				}
			}
			// MSIE - hide globe for map option
			// hideElements("mapview");
		}
	</script>
</div>
