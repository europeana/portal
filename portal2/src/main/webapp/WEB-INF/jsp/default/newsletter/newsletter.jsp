<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@ include file="/WEB-INF/jsp/default/_common/variables/variables.jsp"%>

<!doctype html>

<html>
	<head>
		<link rel="stylesheet" href="${branding}/css/europeana-font-face.css" />
		
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
	<body class="iframe-newsletter" id="newsletter_signup">
	
	
	
	<!-- Begin MailChimp Signup Form -->
	<div id="mc_embed_signup">
		<form 	action="http://europeana.us3.list-manage.com/subscribe/post?u=ad318b7566f97eccc895e014e&amp;id=1d4f51a117" 
				method="post"
				id="mc-embedded-subscribe-form"
				name="mc-embedded-subscribe-form"
				class="validate"
				target="_blank"
				onSubmit="return checkForm()">
				
			<div class="mc-field-group">
				<label id="label-mce-EMAIL" for="mce-EMAIL">Email Address </label>
				<input type="email" value="" name="EMAIL" class="required email" id="mce-EMAIL">
			</div>
			<div class="mc-field-group input-group">
				<!-- strong>Which newsletter would you like to receive? </strong -->
				<ul>
					<li>
						<input type="radio" value="English" name="LANGUAGE" id="mce-LANGUAGE-0"><label for="mce-LANGUAGE-0">English</label>
					</li>
					<li>
						<input type="radio" value="French" name="LANGUAGE" id="mce-LANGUAGE-1"><label for="mce-LANGUAGE-1">French</label>
					</li>
				</ul>
			</div>
			<div id="mce-responses" class="clear">
			<div class="response" id="mce-error-response" style="display:none"></div>
			<div class="response" id="mce-success-response" style="display:none"></div>
			</div> <!-- real people should not fill this in and expect good things - do not remove this or risk form bot signups-->
			<div style="position: absolute; left: -5000px;"><input type="text" name="b_ad318b7566f97eccc895e014e_1d4f51a117" value=""></div>
			<div class="clear"><input type="submit" value="Subscribe" name="subscribe" id="mc-embedded-subscribe" class="button"></div>
		</form>
	</div>

	
	<!--End mc_embed_signup--> 	
	
		<%--div class="e2ma_signup_form" id="e2ma_signup_form">
		
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
		</div--%>

		<script type="text/javascript">
		
			window.newsletter = {
				"placeholder": "<spring:message code="news_letter_email_hint_t" />",
				"submitLabel": "<spring:message code="news_letter_signup_button_t" />"
			};			

			var checkForm = function(form_obj) {

				//now handle required field validation
				var why = "";
				var email = $('#mce-EMAIL');
				var filter=/^[a-z0-9_\-\.\+]+@[a-z0-9_\-\.]+\.[a-z]{2,4}$/i;

				if (email.val().length==0) {
					why += "Email must be specified\n";
				}
				else if (!filter.test(email.val())) {
					why += "Email must be a valid email address.\n";
				}
				
				if (!$('#mce-LANGUAGE-0').is(':checked') && !$('#mce-LANGUAGE-1').is(':checked')) {
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
				
				if('placeholder' in $('#mce-EMAIL')[0]){
					$('#label-mce-EMAIL').remove();
					$('#mce-EMAIL').attr('placeholder',		window.newsletter.placeholder);				
				}
				
				$('#mc-embedded-subscribe').attr('title',	"<spring:message code="news_letter_description_t" />" );
				$('#mc-embedded-subscribe').attr('value',	window.newsletter.submitLabel);
				$('#mce-EMAIL').attr('title',				window.newsletter.placeholder);
				
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