<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ include file="/WEB-INF/jsp/default/_common/variables/variables.jsp"%>

<!doctype html>

<html>
	<head>
		<script type="text/javascript" src="/${branding}/js/jquery/min/jquery-1.8.1.min.js"></script>
		<script type="text/javascript">
			window.js={};
		</script>
		<c:choose>
			<c:when test="${!empty model.debug && model.debug}">
				<script type="text/javascript" src="/${branding}/js/js/loader.js"></script>
				
				<link rel="stylesheet" href="themes/default/css/common.css" />
				<link rel="stylesheet" href="themes/default/css/newsletter.css" />
				
			</c:when>
			<c:otherwise>
				<script type="text/javascript" src="/${branding}/js/js/min/loader.min.js"></script>
				
				<link rel="stylesheet" href="themes/default/css/min/common.min.css" />
				<link rel="stylesheet" href="themes/default/css/min/newsletter.min.css" />
				
			</c:otherwise>
		</c:choose>
		
		<!--[if IE 8]>
		<style type="text/css">
			#e2ma_signup_submit_button{
				filter:					progid:DXImageTransform.Microsoft.gradient(startColorstr=#FF0048ff,endColorstr=#FF4C7ECF);
				color:					white !important;
			}
		</style>
		<![endif]-->
			
		
	</head>
	<body class="iframe-newsletter">
	
		<div id="load_check" class="signup_form_message" >This form needs Javascript to display, which your browser doesn't support.
			<a href="https://app.e2ma.net/app2/audience/signup/1722088/1403149/?v=a"> Sign up here</a> instead 
		</div>
		
		<div id="footer-subscribe">
			<script type="text/javascript">
				window.emma = {
					"placeholder"	: "<spring:message code='news_letter_email_hint_t' />",
					"submitLabel"	: "<spring:message code='news_letter_signup_button_t' />"
				};
			</script>
											
			<script type="text/javascript">
			
				$(document).ready(function(){

					js.loader.loadScripts([{
						name: 'emma',
						file : '?v=a',
						xxxpath : 'https://app.e2ma.net/app2/audience/tts_signup/1722088/1c99e86abb6bc30f8e24592a87471334/1403149/',
						xxxpath : '/${branding}/js/com/e2ma/emma.js',
						path : '/${branding}/js/com/e2ma/min/emma.min.js',
						
						callback : function() {

							signupFormObj.drawForm();				

							   
							//window.emma = window.parent.emma;
							document.getElementById('e2ma_signup_submit_button').setAttribute('title',	"<spring:message code="news_letter_description_t" />" );
							
							document.getElementById('e2ma_signup_submit_button').setAttribute('value',	window.emma.submitLabel);
							document.getElementById('id_email').setAttribute('placeholder',				window.emma.placeholder);
							document.getElementById('id_email').setAttribute('title',					window.emma.placeholder);


				            var heightVal = $('body').height() + 'px';
				            
				            $('.e2ma_signup_form, html, body').css('height',  heightVal);
            				$("#footer-iframe, .iframe-wrap", window.parent.document).css('height', heightVal);
				            
						}
					}]);

				});
			
			</script>
			
		</div>

	</body>
</html>