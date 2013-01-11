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
            var json_data = {"content": "<div class=\"e2ma_signup_form\" id=\"e2ma_signup_form\">\n    \n    <div class=\"e2ma_signup_message\" id=\"e2ma_signup_message\">    \n        \n    <\/div>\n    <div class=\"e2ma_signup_form_container\" id=\"e2ma_signup_form_container\">\n    <form target=\"_new\" method=\"post\" id=\"e2ma_signup\" onSubmit=\"return signupFormObj.checkForm(this)\" action=\"https:\/\/app.e2ma.net\/app2\/audience\/signup\/1722088\/1403149\/?v=a\" >\n    \n    <input type=\"hidden\" name=\"prev_member_email\" id=\"id_prev_member_email\" \/>\n    \n    <input type=\"hidden\" name=\"source\" id=\"id_source\" \/>\n    \n    \n    \n    <input type=\"hidden\" name=\"group_1293\" value=\"1293\" id=\"id_group_1293\" \/>\n    \n      <input type=\"hidden\" name=\"private_set\" value=\"{num_private}\">\n\n    \n    \n    \n    \n    \n    <div class=\"e2ma_signup_form_row\">\n      <div class=\"e2ma_signup_form_label\">\n        \n        <span class=\"e2ma_signup_form_required_asterix\">*<\/span>\n        \n        Email\n      <\/div>\n      <div class=\"e2ma_signup_form_element\"><input type=\"text\" name=\"email\" id=\"id_email\" \/><\/div>\n    <\/div>\n    \n    \n    \n    <div class=\"e2ma_signup_form_required_footnote\"><span class=\"e2ma_signup_form_required_asterix\">*<\/span> required<\/div>\n    <div class=\"e2ma_signup_form_button_row\" id=\"e2ma_signup_form_button_row\">\n    <input id=\"e2ma_signup_submit_button\" class=\"e2ma_signup_form_button\" type=\"submit\" name=\"Submit\" value=\"Submit\" {disabled}>\n     \n      <input id=\"e2ma_signup_reset_button\" class=\"e2ma_signup_form_button\" type=\"reset\" value=\"Clear\" {disabled}>\n    <\/div>\n  <\/form>\n  <\/div>\n<\/div>\n<script type=\"text\/javascript\">\nif (false && document.getElementById) { \/\/if everything loads cool hide the link to emma\n    document.getElementById('load_check').style.display = 'none';\n}\n<\/script>\n"};
            
            //print form
            $('body').html(json_data.content);
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
