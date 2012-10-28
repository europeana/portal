<div class="row footer" role="contentinfo">
<div class="twelve columns">
<div class="footer-wrapper">
<div class="row">
<div class="six columns">
<h3><spring:message code="follow_us_t" /></h3>
<div id="social-links">
<span></span><!-- Andy: empty span needed to stop 1st item appearing smaller in firefox -->
<a class="social-link" title="<spring:message code="europeana-facebook-title_t" />" href="<spring:message code="europeana-facebook-url" />" target="<spring:message code="europeana-facebook-target" />" rel="me"><span class="social-link icon-facebook"><span class="text"><spring:message code="europeana-facebook_t" /></span></span></a>
<a class="social-link" title="<spring:message code="europeana-twitter-title_t" />" href="<spring:message code="europeana-twitter-url" />" target="<spring:message code="europeana-twitter-target" />" rel="me"><span class="social-link icon-twitter"><span class="text"><spring:message code="europeana-twitter_t" /></span></span></a>
<a class="social-link" title="<spring:message code="europeana-pinterest-title_t" />" href="<spring:message code="europeana-pinterest-url" />" target="<spring:message code="europeana-pinterest-target" />" rel="me"><span class="social-link icon-pinterest-2"><span class="text"><spring:message code="europeana-pinterest_t" /></span></span></a>
<a class="social-link" title="<spring:message code="europeana-google-title_t" />" href="<spring:message code="europeana-google-url" />" target="<spring:message code="europeana-google-target" />"><span class="social-link icon-google"><span class="text"><spring:message code="europeana-google_t" /></span></span></a><span class="stretch"></span></div>
</div>
<div id="newsletter-wrapper" class="six columns">
<h3><spring:message code="news_letter_signup_t" /></h3>
<!--  <iframe style="margin:0; width:100%;" id="footer-iframe" src="/${model.portalName}/newsletter.html"></iframe>  -->
</div>
</div>
<div class="row" id="footer-links-all">
<div class="four columns">
<%-- Explore --%>
<h3><a class="europeana" href="<spring:message code="notranslate_main_menu_explore_a_url_t"/>" target="<spring:message code="notranslate_main_menu_explore_a_target_t"/>" title="<spring:message code="main_menu_explore_a_title_t"/>"><spring:message code="main_menu_explore_a_text_t"/></a></h3>
<ul class="footer-links with-icons">
<li><span class="icon-logo" id="link-logo-1"></span><a class="europeana" href="<spring:message code="notranslate_main_menu_explore_1_a_url_t"/>" target="<spring:message code="notranslate_main_menu_explore_1_a_target_t"/>" title="<spring:message code="main_menu_explore_1_a_title_t"/>"><spring:message code="main_menu_explore_1_a_text_t"/></a></li>
<li><span class="icon-logo" id="link-logo-2"></span><a class="europeana" href="<spring:message code="notranslate_main_menu_explore_2_a_url_t"/>" target="<spring:message code="notranslate_main_menu_explore_2_a_target_t"/>" title="<spring:message code="main_menu_explore_2_a_title_t"/>"><spring:message code="main_menu_explore_2_a_text_t"/></a></li>
<li><span class="icon-logo" id="link-logo-3"></span><a class="europeana" href="<spring:message code="notranslate_main_menu_explore_3_a_url_t"/>" target="<spring:message code="notranslate_main_menu_explore_3_a_target_t"/>" title="<spring:message code="main_menu_explore_3_a_title_t"/>"><spring:message code="main_menu_explore_3_a_text_t"/></a></li>
<li><span class="icon-logo" id="link-logo-4"></span><a class="europeana" href="<spring:message code="notranslate_main_menu_explore_4_a_url_t"/>" target="<spring:message code="notranslate_main_menu_explore_4_a_target_t"/>" title="<spring:message code="main_menu_explore_4_a_title_t"/>"><spring:message code="main_menu_explore_4_a_text_t"/></a></li>
<li><span class="icon-logo" id="link-logo-5"></span><a class="europeana" href="<spring:message code="notranslate_main_menu_explore_5_a_url_t"/>" target="<spring:message code="notranslate_main_menu_explore_5_a_target_t"/>" title="<spring:message code="main_menu_explore_5_a_title_t"/>"><spring:message code="main_menu_explore_5_a_text_t"/></a></li>
</ul>
</div>
<div class="four columns">
<%-- Help --%>
<h3><a class="europeana" href="/${model.portalName}/<spring:message code="notranslate_main_menu_help_a_url_t"/>" target="<spring:message code="notranslate_main_menu_help_a_target_t"/>" title="<spring:message code="main_menu_help_a_title_t"/>"><spring:message code="main_menu_help_a_text_t"/></a></h3>
<ul class="footer-links">
<li><a class="europeana" href="/${model.portalName}/<spring:message code="notranslate_main_menu_help_1_a_url_t"/>" target="<spring:message code="notranslate_main_menu_help_1_a_target_t"/>" title="<spring:message code="main_menu_help_1_a_title_t"/>"><spring:message code="main_menu_help_1_a_text_t"/></a></li>
<li><a class="europeana" href="/${model.portalName}/<spring:message code="notranslate_main_menu_help_2_a_url_t"/>" target="<spring:message code="notranslate_main_menu_help_2_a_target_t"/>" title="<spring:message code="main_menu_help_2_a_title_t"/>"><spring:message code="main_menu_help_2_a_text_t"/></a></li>
<li><a class="europeana" href="/${model.portalName}/<spring:message code="notranslate_main_menu_help_3_a_url_t"/>" target="<spring:message code="notranslate_main_menu_help_3_a_target_t"/>" title="<spring:message code="main_menu_help_3_a_title_t"/>"><spring:message code="main_menu_help_3_a_text_t"/></a></li>
<li><a class="europeana" href="/${model.portalName}/<spring:message code="notranslate_main_menu_help_4_a_url_t"/>" target="<spring:message code="notranslate_main_menu_help_4_a_target_t"/>" title="<spring:message code="main_menu_help_4_a_title_t"/>"><spring:message code="main_menu_help_4_a_text_t"/></a></li>
<li><a class="europeana" href="/${model.portalName}/<spring:message code="notranslate_main_menu_help_5_a_url_t"/>" target="<spring:message code="notranslate_main_menu_help_5_a_target_t"/>" title="<spring:message code="main_menu_help_5_a_title_t"/>"><spring:message code="main_menu_help_5_a_text_t"/></a></li>
</ul>
</div>
<div class="four columns">
<h3><a class="europeana" href="/${model.portalName}/<spring:message code="notranslate_main_menu_api_a_url_t"/>" target="<spring:message code="notranslate_main_menu_api_a_target_t"/>" title="<spring:message code="main_menu_api_a_title_t"/>"><spring:message code="main_menu_api_a_text_t"/></a></h3>
<ul class="footer-links">			
<li><a class="europeana" href="<spring:message code="notranslate_main_menu_api_1_a_url_t"/>" target="<spring:message code="notranslate_main_menu_api_1_a_target_t"/>" title="<spring:message code="main_menu_api_1_a_title_t"/>"><spring:message code="main_menu_api_1_a_text_t"/></a></li>
<li><a class="europeana" href="<spring:message code="notranslate_main_menu_api_2_a_url_t"/>" target="<spring:message code="notranslate_main_menu_api_2_a_target_t"/>" title="<spring:message code="main_menu_api_2_a_title_t"/>"><spring:message code="main_menu_api_2_a_text_t"/></a></li>
<li><a class="europeana" href="<spring:message code="notranslate_main_menu_api_3_a_url_t"/>" target="<spring:message code="notranslate_main_menu_api_3_a_target_t"/>" title="<spring:message code="main_menu_api_3_a_title_t"/>"><spring:message code="main_menu_api_3_a_text_t"/></a></li>
<li><a class="europeana icon-external-right" href="<spring:message code="notranslate_main_menu_api_4_a_url_t"/>" target="<spring:message code="notranslate_main_menu_api_4_a_target_t"/>" title="<spring:message code="main_menu_api_4_a_title_t"/>"><spring:message code="main_menu_api_4_a_text_t"/></a></li>
</ul>
</div>
</div>
</div>
</div>
</div>
<div class="row footer-bottom-links">
<div class="twelve columns">
<a class="europeana footer-bottom-link" href="/${model.portalName}/<spring:message code="footer_menu_about_us_url_t"/>" target="<spring:message code="footer_menu_about_us_target_t"/>" title="<spring:message code="footer_menu_about_us_title_t"/>"> <spring:message code="footer_menu_about_us_text_t"/></a>
<a class="europeana footer-bottom-link" href="/${model.portalName}/<spring:message code="footer_menu_tou_url_t"/>" target="<spring:message code="footer_menu_tou_target_t"/>" title="<spring:message code="footer_menu_tou_title_t"/>"><spring:message code="footer_menu_tou_text_t"/></a>
<a class="europeana footer-bottom-link" href="/${model.portalName}/<spring:message code="footer_menu_contact_us_url_t"/>" target="<spring:message code="footer_menu_contact_us_target_t"/>" title="<spring:message code="footer_menu_contact_us_title_t"/>"><spring:message code="footer_menu_contact_us_text_t"/></a>
<a class="europeana footer-bottom-link" href="/${model.portalName}/<spring:message code="footer_menu_sitemap_url_t"/>" target="<spring:message code="footer_menu_sitemap_target_t"/>" title="<spring:message code="footer_menu_sitemap_title_t"/>"> <spring:message code="footer_menu_sitemap_text_t"/></a>
<span class="footer-funded-by"><spring:message code="FundedBy_t"/><span class="icon-euflag"></span>
</div>
</div>