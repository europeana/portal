<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ include file="/WEB-INF/jsp/default/_common/variables/variables.jsp"%>

<!doctype html>

<html>
	<head>
		<link rel="stylesheet" href="/${branding}/css/europeana-font-face.css" />
		
		<script type="text/javascript" src="/${branding}/js/jquery/min/jquery-1.8.1.min.js"></script>
		<script type="text/javascript">
			window.js={};
		</script>
		<c:choose>
			<c:when test="${!empty model.debug && model.debug}">				
				<link rel="stylesheet" href="themes/default/css/common.css" />
				<link rel="stylesheet" href="themes/default/css/newsletter.css" />
			</c:when>
			<c:otherwise>				
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
	<body class="iframe-newsletter" id="e2ma_signup">
		<div class="e2ma_signup_form" id="e2ma_signup_form">
		
			<div class="e2ma_signup_message" id="e2ma_signup_message"></div>
			
			<div class="e2ma_signup_form_container" id="e2ma_signup_form_container">

				<form method="post" id="signup" name="signup" onSubmit="return checkForm()" action="https://app.e2ma.net/app2/audience/signup/1730327/1403149/?v=a" target="_new">

					<input type="email" name="email" id="id_email">
					         
					<div class="cb">
						<input type="checkbox" name="group_1293" id="id_group_1293"><label for="id_group_1293">English</label>
						<br/>
						<input type="checkbox" name="group_519437" id="id_group_519437"><label for="id_group_519437">French</label>
					</div>
					
					<input type="submit" name="Submit" value="Submit" id="e2ma_signup_submit_button">
				</form>
			</div>
		</div>

		<script type="text/javascript">
			window.emma = {
				"placeholder": "<spring:message code="news_letter_email_hint_t" />",
				"submitLabel": "<spring:message code="news_letter_signup_button_t" />"
			};

			var checkForm = function(form_obj) {

				//now handle required field validation
				var why = "";
				var email = $('#id_email');
				var filter=/^[a-z0-9_\-\.\+]+@[a-z0-9_\-\.]+\.[a-z]{2,4}$/i;

				if (email.val().length==0) {
					why += "Email must be specified\n";
				}
				else if (!filter.test(email.val())) {
					why += "Email must be a valid email address.\n";
				}
				
				if (!$('#id_group_1293').is(':checked') && !$('#id_group_519437').is(':checked')) {
					why += "A language must be specified.\n";
				}

				if (why != "") {
					alert(why);
					return false;
				} 
				$(".overlaid-content", parent.document).css('visibility', 'hidden');
				return true;	 
			};
			$(document).ready(function(){
				$('#e2ma_signup_submit_button').attr('title',	"<spring:message code="news_letter_description_t" />" );				
				$('#e2ma_signup_submit_button').attr('value',	window.emma.submitLabel);
				$('#id_email').attr('placeholder',				window.emma.placeholder);
				$('#id_email').attr('title',					window.emma.placeholder);				
				
				$(window).bind('keypress', function(e){
					if(e.ctrlKey || e.metaKey){
						return;
					}
					var key	=  e.keyCode ? e.keyCode : e.which;
					if(key==27){
						$(".overlaid-content", parent.document).css('visibility', 'hidden');
					}
				});
			});
		</script>
	</body>
</html>