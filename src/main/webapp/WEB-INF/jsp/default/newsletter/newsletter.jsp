<html>
	<head>

		<script type="text/javascript" src="themes/default/js/jquery/min/jquery-1.8.1.min.js"></script>
	
		<link rel="stylesheet" href="themes/default/css/html.css" />
		<link rel="stylesheet" href="themes/default/css/common.css" />
	
		<link rel="stylesheet" href="themes/default/css/header.css" />
		<link rel="stylesheet" href="themes/default/css/menu-main.css" />

		<link rel="stylesheet" href="themes/default/css/europeana-font-face.css" />
		
		<link rel="stylesheet" href="themes/default/css/menu-styling.css" />
		
		
		<style type="text/css">
			#e2ma_signup{
			}
			
			.e2ma_signup_form_button_row,
			.e2ma_signup_form_row{
				width:		100%;
				position:	relative;
			}
			
			#footer-subscribe input[type=text],
			#footer-subscribe input[type=email]{
				position:			absolute;
				width:				46%;
				left:				0;
				margin-top:			1em;
				padding:			0.3em;
			}

			#footer-subscribe input[type=submit]{
				xmargin-top:			1em;
				width:					46%;
				
				position:				absolute;
				right:					0;
				
				padding:			0.3em;
			}
			
			
			body{
				background-image: url("themes/default/images/dust.png");
				margin:0;
				padding:0;
				margin-right:1em;  /* allow the shadow to show */
				width:	100%;
				max-width:100%;
			}
			
			
	#footer-subscribe.phone input[type=text],
	#footer-subscribe.phone input[type=email],
	#footer-subscribe.phone input[type=submit]{
		width:				100%;
	}

	#footer-subscribe.phone input[type=submit]{
		top:				4em;
	}			
			
			
<!--[if IE 8]>

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
			
<![endif]-->
			
			
		</style>
	</head>
	<body>

		<div id="footer-subscribe">	
			
			<div id="load_check" class="signup_form_message" >This form needs Javascript to display, which your browser doesn't support.
				<a href="https://app.e2ma.net/app2/audience/signup/1722088/1403149/?v=a"> Sign up here</a> instead 
			</div>
								
			<script type="text/javascript" src="https://app.e2ma.net/app2/audience/tts_signup/1722088/1c99e86abb6bc30f8e24592a87471334/1403149/?v=a"></script>
			<script type="text/javascript">
				window.emma = window.parent.emma;
				signupFormObj.drawForm();
				document.getElementById('e2ma_signup_submit_button').setAttribute('value',		window.emma.submitLabel);
				document.getElementById('id_email').setAttribute('placeholder',	window.emma.placeholder);
				document.getElementById('id_email').setAttribute('title',		window.emma.placeholder);
			</script>
		</div>

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

	</body>
</html>	