<%@ include file="/WEB-INF/jsp/devel/_common/variables/variables.jsp"%>

<html>
	<head>
		<script type="text/javascript" src="themes/default/js/jquery/min/jquery-1.8.1.min.js"></script>
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
			
		</style>
		
		
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
	<body>
		<div id="footer-subscribe">	
			
			<div id="load_check" class="signup_form_message" >This form needs Javascript to display, which your browser doesn't support.
				<a href="https://app.e2ma.net/app2/audience/signup/1722088/1403149/?v=a"> Sign up here</a> instead 
			</div>
						
			<!-- script type="text/javascript" src="https://app.e2ma.net/app2/audience/tts_signup/1722088/1c99e86abb6bc30f8e24592a87471334/1403149/?v=a"></script-->
			<script type="text/javascript">
				var signupFormObj = {

				    error_string: "",
				    element_count: "1",

				    drawForm: function() {
				        if(this.error_string != '') {
				            document.write(this.error_string);
				        }
				        else {
				            //All old forms will be calling a drawForm method on signupFormObj
				            //with no paramaters passed in.
				            var json_data = {"content": "<div class=\"e2ma_signup_form\" id=\"e2ma_signup_form\">\n    \n    <div class=\"e2ma_signup_message\" id=\"e2ma_signup_message\">    \n        \n    <\/div>\n    <div class=\"e2ma_signup_form_container\" id=\"e2ma_signup_form_container\">\n    <form target=\"_new\" method=\"post\" id=\"e2ma_signup\" onSubmit=\"return signupFormObj.checkForm(this)\" action=\"https:\/\/app.e2ma.net\/app2\/audience\/signup\/1722088\/1403149\/?v=a\" >\n    \n    <input type=\"hidden\" name=\"prev_member_email\" id=\"id_prev_member_email\" \/>\n    \n    <input type=\"hidden\" name=\"source\" id=\"id_source\" \/>\n    \n    \n    \n    <input type=\"hidden\" name=\"group_1293\" value=\"1293\" id=\"id_group_1293\" \/>\n    \n      <input type=\"hidden\" name=\"private_set\" value=\"{num_private}\">\n\n    \n    \n    \n    \n    \n    <div class=\"e2ma_signup_form_row\">\n      <div class=\"e2ma_signup_form_label\">\n        \n        <span class=\"e2ma_signup_form_required_asterix\">*<\/span>\n        \n        Email\n      <\/div>\n      <div class=\"e2ma_signup_form_element\"><input type=\"text\" name=\"email\" id=\"id_email\" \/><\/div>\n    <\/div>\n    \n    \n    \n    <div class=\"e2ma_signup_form_required_footnote\"><span class=\"e2ma_signup_form_required_asterix\">*<\/span> required<\/div>\n    <div class=\"e2ma_signup_form_button_row\" id=\"e2ma_signup_form_button_row\">\n    <input id=\"e2ma_signup_submit_button\" class=\"e2ma_signup_form_button\" type=\"submit\" name=\"Submit\" value=\"Submit\" {disabled}>\n      &nbsp;\n      <input id=\"e2ma_signup_reset_button\" class=\"e2ma_signup_form_button\" type=\"reset\" value=\"Clear\" {disabled}>\n    <\/div>\n  <\/form>\n  <\/div>\n<\/div>\n<script type=\"text\/javascript\">\nif (document.getElementById) { \/\/if everything loads cool hide the link to emma\n    document.getElementById('load_check').style.display = 'none';\n}\n<\/script>\n"};
				            
				            //print form
				            document.write(json_data.content);
				            
				        }
				    },
				    
				    checkForm: function(form_obj) {
				        //now handle required field validation
				        json_fields = {"data": [{"widget_type": "text", "field_type": "text", "required": true, "name": "Email", "short_name": "email"}]};
				        var element_array = json_fields.data;
				        var why = "";
				        for (var loop = 0; loop < element_array.length; loop++) 
				        {
				            if(element_array[loop].widget_type == 'text' || element_array[loop].widget_type == 'long')
				            {   
				                if(form_obj[element_array[loop].short_name].value == "")
				                {
				                    why += element_array[loop].name + " is a required field.\n"
				                }
				                else if(element_array[loop].short_name == 'email')
				                {
				                    var filter=/^[a-z0-9_\-\.\+]+@[a-z0-9_\-\.]+\.[a-z]{2,4}$/i;

				                    if (!filter.test(form_obj[element_array[loop].short_name].value))
				                    {
				                        why += element_array[loop].name + " must be a valid email address.\n";
				                    }
				                }
				                continue;
				            }
				            else if (element_array[loop].widget_type == 'check_multiple')
				            {
				                var element = form_obj[element_array[loop].short_name];
				                if(signupFormObj.checkMulti(element))
				                {
				                    continue;
				                }
				                why += element_array[loop].name + " is a required field.\n";
				            }
				            else if (element_array[loop].widget_type == 'radio')
				            {
				                var flag = 'false';
				                var element = form_obj[element_array[loop].short_name];
				                if (signupFormObj.checkMulti(element))
				                {
				                    continue;
				                }
				                why += element_array[loop].name + " is a required field.\n";
				            } 
				            else if (element_array[loop].widget_type == 'select one')
				            {
				                var index = form_obj[element_array[loop].short_name].selectedIndex;
				                if(form_obj[element_array[loop].short_name].options[index].value == "")
				                {
				                    why += element_array[loop].name + " is a required field.\n";
				                }
				            }
				            else if (element_array[loop].widget_type == 'select multiple')
				            {
				                var element = form_obj[element_array[loop].short_name];
				                if(!signupFormObj.checkSelMulti(element)) {
				                    why += element_array[loop].name + " is a required field.\n";
				                }
				            }
				            else if (element_array[loop].widget_type == 'date')
				            {
				                var str_month = element_array[loop].widget_type + "_month";
				                var str_day = element_array[loop].widget_type + "_day";
				                var str_year = element_array[loop].widget_type + "_year";

				                if (form_obj[str_month].selectedIndex < 1 || form_obj[str_day].selectedIndex < 1 || form_obj[str_year].selectedIndex < 1) 
				                {
				                    why += element_array[loop].name + " is a required field.\n";
				                }
				            }
				        }
				        if (why != "") {
				            alert(why);
				            return false;
				        } 

				        return true;   
				        
				    },
				    
				    checkSelMulti: function (element) {
				        for(var i = 0; i < element.length; i++) {
				            if(element[i].selected) {
				                return true;
				            }
				        }
				        return false;
				    },

				    checkMulti: function (element) {
				        for (var i = 0; i < element.length; i++) {
				            if (element[i].checked)
				            {
				                return true;
				            }
				        }
				        return false;
				    }
				};	
			</script>
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