<%@ include file="/WEB-INF/jsp/default/_common/variables/variables.jsp"%>

<%--

FOR THE CSS TIDY - DELETED STUFF:


	<body class="iframe-newsletter">
		<div id="footer-subscribe">



 --%>

 
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
				
				
				<link rel="stylesheet" href="themes/default/css/html.css" />
				<link rel="stylesheet" href="themes/default/css/common.css" />
				<link rel="stylesheet" href="themes/default/css/header.css" />
				<link rel="stylesheet" href="themes/default/css/menu-main.css" />
				<link rel="stylesheet" href="themes/default/css/europeana-font-face.css" />
				
				
				<link rel="stylesheet" href="themes/default/css/min/newsletter.min.css" />		
			</c:otherwise>
		</c:choose>


	<%--
		<c:choose>
			<c:when test="${!empty model.debug && !model.debug}">
				<link rel="stylesheet" href="themes/default/css/min/common.min.css" />
			</c:when>
			<c:otherwise>
				<link rel="stylesheet" href="themes/default/css/html.css" />
				<link rel="stylesheet" href="themes/default/css/common.css" />
				<link rel="stylesheet" href="themes/default/css/header.css" />
				<link rel="stylesheet" href="themes/default/css/menu-main.css" />
				<link rel="stylesheet" href="themes/default/css/europeana-font-face.css" />
			</c:otherwise>
		</c:choose>
	 --%>



	
		
		<!--[if IE 8]>
		<style type="text/css">

			#footer-subscribe.phone input[type=text],
			#footer-subscribe.phone input[type=email],
			#footer-subscribe.phone input[type=submit]{
				display:	none;
			}
		
			#footer-subscribe #id_email{
				position:			absolute;
				width:				46% !important;
				left:				0;
				margin-top:			1em;
				padding:			0.3em;
				margin-top:			1.5em;				
			}

			#footer-subscribe input[type=submit]{
				width:					46% !important;
				position:				absolute;
				right:					0;
				padding:				0.3em;
				background-color:		#0048FF;
				color:					white;
			}
			
			#e2ma_signup_submit_button{
				width:					46% !important;
				right:					2em;
				position:				absolute;
				background-color:		#0048FF;
				margin-top				1em;
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
			
				$(document).ready(function(){

					document._oldGetElementById = document.getElementById;
					document.getElementById = function(id){
						if(id=='load_check'){
							return {"style":""};
						}
						return document._oldGetElementById(id);
					};
					
					
					js.loader.loadScripts([{
						name: 'emma',
						file : '?v=a',
						path : 'https://app.e2ma.net/app2/audience/tts_signup/1722088/1c99e86abb6bc30f8e24592a87471334/1403149/',
						callback : function() {
							
							var cssImports = [];
							$('head').find('link').each(function(i, ob){
								cssImports[cssImports.length] = ob;
							});

							signupFormObj.drawForm();
							
							$("body").html($("body").html().replace(/[&]nbsp[;]/gi, '') );

							document.close(); // stop page loading wheel of firefox

							$(cssImports).each(function(i, ob){
								$('<link rel="stylesheet" href="' + $(ob).attr('href') + '" />').appendTo( $('head') );
							});

							
				            $('.e2ma_signup_form  form')[0].onsubmit = function(){
					            return signupFormObj.checkForm(this);			            	
				            };

							window.emma = window.parent.emma;
							document.getElementById('e2ma_signup_submit_button').setAttribute('value',	window.emma.submitLabel);
							document.getElementById('id_email').setAttribute('placeholder',				window.emma.placeholder);
							document.getElementById('id_email').setAttribute('title',					window.emma.placeholder);

				            var heightVal = $('.e2ma_signup_form_container').height() + 'px';
				            
				            $('.e2ma_signup_form, html, body').css('height',  heightVal);
            				$("#footer-iframe, .iframe-wrap", window.parent.document).css('height', heightVal);
				            
						}
					}]);

					return;
					
				});
			
				/*
				window.emma = window.parent.emma;
				signupFormObj.drawForm();
				document.getElementById('e2ma_signup_submit_button').setAttribute('value',		window.emma.submitLabel);
				document.getElementById('id_email').setAttribute('placeholder',	window.emma.placeholder);
				document.getElementById('id_email').setAttribute('title',		window.emma.placeholder);
				*/
			</script>
			
		</div>

		<%--
		<script type="text/javascript">
			var toggleFn = function(){
				if(  $("#mobile-menu", window.parent.document).is(":visible")  ){
					$("#footer-subscribe").addClass("phone");
				}
				else{					
					$("#footer-subscribe").removeClass("phone");
				}
			};
		
			$(document).ready(function(){
				toggleFn();
			});
			
			$(window).resize( function() {
				toggleFn();
		     });
		</script>
		--%>

	</body>
</html>