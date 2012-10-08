<div class="row footer" role="contentinfo">
	<div class="twelve columns">
		<div class="footer-wrapper">
			<div class="row">
			
				<div class="six columns">
					<h3><spring:message code="follow_us_t" /></h3>
					
					<div id="social-links">
						<span></span><!-- Andy: empty span needed to stop 1st item appearing smaller in firefox -->
						
						<a class	="social-link"
							title	="<spring:message code="europeana-facebook-title_t" />"
							href	="<spring:message code="europeana-facebook-url" />"
							target	="<spring:message code="europeana-facebook-target" />"
							rel		="me"
							>
							<span class="social-link icon-facebook"><span class="text xhide-on-phones"><spring:message code="europeana-facebook_t" /></span></span>
						</a>
							
						<a class	="social-link"
							title	="<spring:message code="europeana-twitter-title_t" />"
							href	="<spring:message code="europeana-twitter-url" />"
							target	="<spring:message code="europeana-twitter-target" />"
							rel		="me"
							>
							<span class="social-link icon-twitter"><span class="text xhide-on-phones"><spring:message code="europeana-twitter_t" /></span></span>
						</a>

						<a class	="social-link"
							title	="<spring:message code="europeana-pinterest-title_t" />"
							href	="<spring:message code="europeana-pinterest-url" />"
							target	="<spring:message code="europeana-pinterest-target" />"
							rel		="me"
							>
							<span class="social-link icon-pinterest-2"><span class="text xhide-on-phones"><spring:message code="europeana-pinterest_t" /></span></span>
						</a>
						
						
						<a class	="social-link"
							title	="<spring:message code="europeana-google-title_t" />"
							href	="<spring:message code="europeana-google-url" />"
							target	="<spring:message code="europeana-google-target" />"
							>
							<span class="social-link icon-blog"><span class="text xhide-on-phones"><spring:message code="europeana-google_t" /></span></span>
						</a>
						
						
						<span class="stretch"></span>
					</div>
				</div>
				
				<div class="six columns">		
				
						
					<div id="footer-subscribe">
					
						<h3><spring:message code="news_letter_signup_t" /></h3>
					
						<form action="https://app.e2ma.net/app2/audience/signup/1415029/1403149/?v=a">
							<%--
							<input name="name"	type="text"		placeholder="<spring:message code="news_letter_name_hint_t" />"/>
							 --%>
							
							<input id="id_email" name="email" type="email" placeholder="<spring:message code="news_letter_email_hint_t" />"/>
							<input type="submit" class="europeana-button-1 deans-button-1" value="<spring:message code="news_letter_signup_button_t" />"/>
							
							<span style="xdisplay:block; xmax-width:50%; xfloat:right; ">
								<spring:message code="news_letter_description_t" />
							</span>
							
							
						</form>
						
<%-- 
						<div id="load_check" class="signup_form_message" >This form needs Javascript to display, which your browser doesn't support.
								<a href="https://app.e2ma.net/app2/audience/signup/1722088/1403149/?v=a"> Sign up here</a> instead 
						</div>
--%>
						
					</div>
				</div>
			</div>


			<div class="row" id="footer-links-all">
			
				<div class="four columns">
				
					<%-- Explore --%>
					<h3>
						<a class="europeana" href="<spring:message code="notranslate_main_menu_explore_a_url_t"/>" target="<spring:message code="notranslate_main_menu_explore_a_target_t"/>" title="<spring:message code="main_menu_explore_a_title_t"/>"><spring:message code="main_menu_explore_a_text_t"/></a>
					</h3>
					

					<ul class="footer-links">
						<li><span class="icon-logo" id="link-logo-1"></span><a class="europeana" href="<spring:message code="notranslate_main_menu_explore_1_a_url"/>" target="<spring:message code="notranslate_main_menu_explore_1_a_target_t"/>" title="<spring:message code="main_menu_explore_1_a_title_t"/>"><spring:message code="main_menu_explore_1_a_text_t"/></a></li>
						<li><span class="icon-logo" id="link-logo-2"></span><a class="europeana" href="<spring:message code="notranslate_main_menu_explore_2_a_url"/>" target="<spring:message code="notranslate_main_menu_explore_2_a_target_t"/>" title="<spring:message code="main_menu_explore_2_a_title_t"/>"><spring:message code="main_menu_explore_2_a_text_t"/></a></li>
						<li><span class="icon-logo" id="link-logo-3"></span><a class="europeana" href="<spring:message code="notranslate_main_menu_explore_3_a_url"/>" target="<spring:message code="notranslate_main_menu_explore_3_a_target_t"/>" title="<spring:message code="main_menu_explore_3_a_title_t"/>"><spring:message code="main_menu_explore_3_a_text_t"/></a></li>
						<li><span class="icon-logo" id="link-logo-4"></span><a class="europeana" href="<spring:message code="notranslate_main_menu_explore_4_a_url"/>" target="<spring:message code="notranslate_main_menu_explore_4_a_target_t"/>" title="<spring:message code="main_menu_explore_4_a_title_t"/>"><spring:message code="main_menu_explore_4_a_text_t"/></a></li>
						<li><span class="icon-logo" id="link-logo-5"></span><a class="europeana" href="<spring:message code="notranslate_main_menu_explore_5_a_url"/>" target="<spring:message code="notranslate_main_menu_explore_5_a_target_t"/>" title="<spring:message code="main_menu_explore_5_a_title_t"/>"><spring:message code="main_menu_explore_5_a_text_t"/></a></li>
					</ul>

				</div>
			
				<div class="four columns">
					<%-- Help --%>
					<h3>
						<a class="europeana" href="<spring:message code="notranslate_main_menu_help_a_url"/>" target="<spring:message code="notranslate_main_menu_help_a_target_t"/>" title="<spring:message code="main_menu_help_a_title_t"/>"><spring:message code="main_menu_help_a_text_t"/></a>
					</h3>
					<ul class="footer-links">
						<li><a class="europeana" href="<spring:message code="notranslate_main_menu_help_1_a_url"/>" target="<spring:message code="notranslate_main_menu_help_1_a_target_t"/>" title="<spring:message code="main_menu_help_1_a_title_t"/>"><spring:message code="main_menu_help_1_a_text_t"/></a></li>
						<li><a class="europeana" href="<spring:message code="notranslate_main_menu_help_2_a_url"/>" target="<spring:message code="notranslate_main_menu_help_2_a_target_t"/>" title="<spring:message code="main_menu_help_2_a_title_t"/>"><spring:message code="main_menu_help_2_a_text_t"/></a></li>
						<li><a class="europeana" href="<spring:message code="notranslate_main_menu_help_3_a_url"/>" target="<spring:message code="notranslate_main_menu_help_3_a_target_t"/>" title="<spring:message code="main_menu_help_3_a_title_t"/>"><spring:message code="main_menu_help_3_a_text_t"/></a></li>
						<li><a class="europeana" href="<spring:message code="notranslate_main_menu_help_4_a_url"/>" target="<spring:message code="notranslate_main_menu_help_4_a_target_t"/>" title="<spring:message code="main_menu_help_4_a_title_t"/>"><spring:message code="main_menu_help_4_a_text_t"/></a></li>
						<li><a class="europeana" href="<spring:message code="notranslate_main_menu_help_5_a_url"/>" target="<spring:message code="notranslate_main_menu_help_5_a_target_t"/>" title="<spring:message code="main_menu_help_5_a_title_t"/>"><spring:message code="main_menu_help_5_a_text_t"/></a></li>
					</ul>
				</div>
			
				<div class="four columns">
				
					<h3>
						<a class="europeana" href="<spring:message code="notranslate_main_menu_api_a_url"/>" target="<spring:message code="notranslate_main_menu_api_a_target_t"/>" title="<spring:message code="main_menu_api_a_title_t"/>"><spring:message code="main_menu_api_a_text_t"/></a>
					</h3>
					<ul class="footer-links">			
						<li><a class="europeana" href="<spring:message code="notranslate_main_menu_api_1_a_url"/>" target="<spring:message code="notranslate_main_menu_api_1_a_target_t"/>" title="<spring:message code="main_menu_api_1_a_title_t"/>"><spring:message code="main_menu_api_1_a_text_t"/></a></li>
						<li><a class="europeana" href="<spring:message code="notranslate_main_menu_api_2_a_url"/>" target="<spring:message code="notranslate_main_menu_api_2_a_target_t"/>" title="<spring:message code="main_menu_api_2_a_title_t"/>"><spring:message code="main_menu_api_2_a_text_t"/></a></li>
						<li><a class="europeana" href="<spring:message code="notranslate_main_menu_api_3_a_url"/>" target="<spring:message code="notranslate_main_menu_api_3_a_target_t"/>" title="<spring:message code="main_menu_api_3_a_title_t"/>"><spring:message code="main_menu_api_3_a_text_t"/></a></li>
						<li><a class="europeana icon-external-right" href="<spring:message code="notranslate_main_menu_api_4_a_url"/>" target="<spring:message code="notranslate_main_menu_api_4_a_target_t"/>" title="<spring:message code="main_menu_api_4_a_title_t"/>"><spring:message code="main_menu_api_4_a_text_t"/></a></li>
					</ul>

				</div>
			
			</div>


			
			
			
			
			
			
<!-- 
		<li><a href="<spring:message code="notranslate_main_menu_terms_1_a_url_t"/>" target="<spring:message code="notranslate_main_menu_terms_1_a_target_t"/>" title="<spring:message code="main_menu_terms_1_a_title_t"/>"><spring:message code="main_menu_terms_1_a_text_t"/></a></li>
		<li><a href="<spring:message code="notranslate_main_menu_terms_2_a_url_t"/>" target="<spring:message code="notranslate_main_menu_terms_2_a_target_t"/>" title="<spring:message code="main_menu_terms_2_a_title_t"/>"><spring:message code="main_menu_terms_2_a_text_t"/></a></li>
		<li><a href="<spring:message code="notranslate_main_menu_terms_3_a_url_t"/>" target="<spring:message code="notranslate_main_menu_terms_3_a_target_t"/>" title="<spring:message code="main_menu_terms_3_a_title_t"/>"><spring:message code="main_menu_terms_3_a_text_t"/></a></li>
		<li><a href="<spring:message code="notranslate_main_menu_terms_4_a_url_t"/>" target="<spring:message code="notranslate_main_menu_terms_4_a_target_t"/>" title="<spring:message code="main_menu_terms_4_a_title_t"/>"><spring:message code="main_menu_terms_4_a_text_t"/></a></li>
 -->			
			

		</div>
	</div>
</div>