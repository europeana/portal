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
							<span class="social-link icon-facebook"></span>
						</a>
							
						<a class	="social-link"
							title	="<spring:message code="europeana-pinterest-title_t" />"
							href	="<spring:message code="europeana-pinterest-url" />"
							target	="<spring:message code="europeana-pinterest-target" />"
							rel		="me"
							>
							<span class="social-link icon-pinterest-2"></span>
						</a>
						
						<a class	="social-link"
							title	="<spring:message code="europeana-twitter-title_t" />"
							href	="<spring:message code="europeana-twitter-url" />"
							target	="<spring:message code="europeana-twitter-target" />"
							rel		="me"
							>
							<span class="social-link icon-twitter"></span>
						</a>
						
						<a class	="social-link"
							title	="<spring:message code="europeana-blog-title_t" />"
							href	="<spring:message code="europeana-blog-url" />"
							target	="<spring:message code="europeana-blog-target" />"
							>
							<span class="social-link icon-blog"			></span>
						</a>
						
						
						<span class="stretch"></span>
					</div>
				</div>
				
				<div class="six columns">				
					<div id="footer-subscribe">
						<form>
							<%--
							<input name="name"	type="text"		placeholder="<spring:message code="news_letter_name_hint_t" />"/>
							 --%>
							
							<input name="email"	type="email"	placeholder="<spring:message code="news_letter_email_hint_t" />"/>
							<input type="submit" class="europeana-button-1" value="<spring:message code="news_letter_signup_button_t" />"/>
						</form>
					</div>
				</div>
			</div>
			
			
			<%-- About Us --%>
			<h3>
				<a href="<spring:message code="notranslate_main_menu_about_a_url_t"/>" target="<spring:message code="notranslate_main_menu_about_a_target_t"/>" title="<spring:message code="main_menu_about_a_title_t"/>"><spring:message code="main_menu_about_a_text_t"/></a>
			</h3>
			<ul class="footer-links">			
				<li><a href="<spring:message code="notranslate_main_menu_about_1_a_url_t"/>" target="<spring:message code="notranslate_main_menu_about_1_a_target_t"/>" title="<spring:message code="main_menu_about_1_a_title_t"/>"><spring:message code="main_menu_about_1_a_text_t"/></a></li>
				<li><a href="<spring:message code="notranslate_main_menu_about_2_a_url_t"/>" target="<spring:message code="notranslate_main_menu_about_2_a_target_t"/>" title="<spring:message code="main_menu_about_2_a_title_t"/>"><spring:message code="main_menu_about_2_a_text_t"/></a></li>
				<li><a href="<spring:message code="notranslate_main_menu_about_3_a_url_t"/>" target="<spring:message code="notranslate_main_menu_about_3_a_target_t"/>" title="<spring:message code="main_menu_about_3_a_title_t"/>"><spring:message code="main_menu_about_3_a_text_t"/></a></li>
				<li><a href="<spring:message code="notranslate_main_menu_about_4_a_url_t"/>" target="<spring:message code="notranslate_main_menu_about_4_a_target_t"/>" title="<spring:message code="main_menu_about_4_a_title_t"/>"><spring:message code="main_menu_about_4_a_text_t"/></a></li>
			</ul>
			
			<%-- Help --%>
			<h3>
				<a href="<spring:message code="notranslate_main_menu_help_a_url_t"/>" target="<spring:message code="notranslate_main_menu_help_a_target_t"/>" title="<spring:message code="main_menu_help_a_title_t"/>"><spring:message code="main_menu_help_a_text_t"/></a>
			</h3>
			<ul class="footer-links">
				<li><a href="<spring:message code="notranslate_main_menu_help_1_a_url_t"/>" target="<spring:message code="notranslate_main_menu_help_1_a_target_t"/>" title="<spring:message code="main_menu_help_1_a_title_t"/>"><spring:message code="main_menu_help_1_a_text_t"/></a></li>
				<li><a href="<spring:message code="notranslate_main_menu_help_2_a_url_t"/>" target="<spring:message code="notranslate_main_menu_help_2_a_target_t"/>" title="<spring:message code="main_menu_help_2_a_title_t"/>"><spring:message code="main_menu_help_2_a_text_t"/></a></li>
				<li><a href="<spring:message code="notranslate_main_menu_help_3_a_url_t"/>" target="<spring:message code="notranslate_main_menu_help_3_a_target_t"/>" title="<spring:message code="main_menu_help_3_a_title_t"/>"><spring:message code="main_menu_help_3_a_text_t"/></a></li>
				<li><a href="<spring:message code="notranslate_main_menu_help_4_a_url_t"/>" target="<spring:message code="notranslate_main_menu_help_4_a_target_t"/>" title="<spring:message code="main_menu_help_4_a_title_t"/>"><spring:message code="main_menu_help_4_a_text_t"/></a></li>
				<li><a href="<spring:message code="notranslate_main_menu_help_5_a_url_t"/>" target="<spring:message code="notranslate_main_menu_help_5_a_target_t"/>" title="<spring:message code="main_menu_help_5_a_title_t"/>"><spring:message code="main_menu_help_5_a_text_t"/></a></li>
			</ul>
			
			
			<%-- Explore --%>
			<h3>
				<a href="<spring:message code="notranslate_main_menu_explore_a_url_t"/>" target="<spring:message code="notranslate_main_menu_explore_a_target_t"/>" title="<spring:message code="main_menu_explore_a_title_t"/>"><spring:message code="main_menu_explore_a_text_t"/></a>
			</h3>
			<ul class="footer-links">
				<li><a href="<spring:message code="notranslate_main_menu_explore_1_a_url_t"/>" target="<spring:message code="notranslate_main_menu_explore_1_a_target_t"/>" title="<spring:message code="main_menu_explore_1_a_title_t"/>"><spring:message code="main_menu_explore_1_a_text_t"/></a></li>
				<li><a href="<spring:message code="notranslate_main_menu_explore_2_a_url_t"/>" target="<spring:message code="notranslate_main_menu_explore_2_a_target_t"/>" title="<spring:message code="main_menu_explore_2_a_title_t"/>"><spring:message code="main_menu_explore_2_a_text_t"/></a></li>
				<li><a href="<spring:message code="notranslate_main_menu_explore_3_a_url_t"/>" target="<spring:message code="notranslate_main_menu_explore_3_a_target_t"/>" title="<spring:message code="main_menu_explore_3_a_title_t"/>"><spring:message code="main_menu_explore_3_a_text_t"/></a></li>
			</ul>
			
			
<!-- 
		<li><a href="<spring:message code="notranslate_main_menu_terms_1_a_url_t"/>" target="<spring:message code="notranslate_main_menu_terms_1_a_target_t"/>" title="<spring:message code="main_menu_terms_1_a_title_t"/>"><spring:message code="main_menu_terms_1_a_text_t"/></a></li>
		<li><a href="<spring:message code="notranslate_main_menu_terms_2_a_url_t"/>" target="<spring:message code="notranslate_main_menu_terms_2_a_target_t"/>" title="<spring:message code="main_menu_terms_2_a_title_t"/>"><spring:message code="main_menu_terms_2_a_text_t"/></a></li>
		<li><a href="<spring:message code="notranslate_main_menu_terms_3_a_url_t"/>" target="<spring:message code="notranslate_main_menu_terms_3_a_target_t"/>" title="<spring:message code="main_menu_terms_3_a_title_t"/>"><spring:message code="main_menu_terms_3_a_text_t"/></a></li>
		<li><a href="<spring:message code="notranslate_main_menu_terms_4_a_url_t"/>" target="<spring:message code="notranslate_main_menu_terms_4_a_target_t"/>" title="<spring:message code="main_menu_terms_4_a_title_t"/>"><spring:message code="main_menu_terms_4_a_text_t"/></a></li>
 -->			
			

		</div>
	</div>
</div>