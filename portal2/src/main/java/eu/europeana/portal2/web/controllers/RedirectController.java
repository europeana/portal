package eu.europeana.portal2.web.controllers;

import java.text.MessageFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.services.ClickStreamLogService;

/**
 * Deprecated views get redirected here.
 * 
 * @author Borys Omelayenko
 */

@Controller
public class RedirectController {

	@Resource
	private Configuration config;

	@Resource
	private ClickStreamLogService clickStreamLogger;

	static private final String SHOWN_AT = "shownAt";
	static private final String SHOWN_BY = "shownBy";
	static private final String PROVIDER = "provider";
	static private final String EUROPEANA_ID = "id";

	@RequestMapping("/full-doc.html")
	public void fullDocHtml(@RequestParam(value = "uri", required = false) String uri, HttpServletResponse response)
			throws Exception {

		if (!StringUtils.isEmpty(uri) && !uri.contains("full-doc")) {
			String urilc = SitemapController.convertEuropeanaUriToCanonicalUrl(uri);
			if (urilc.startsWith("http://europeana.eu") || urilc.startsWith("http://www.europeana.eu")
					|| !urilc.endsWith(".html")) {
				response.sendRedirect(urilc);
			}
		} else {
			response.sendRedirect(config.getPortalUrl());
		}
	}

	/*
	 * The page where you are redirected to the isShownAt and isShownBy links
	 */
	@RequestMapping("/redirect.html")
	public String handleRedirectFromFullView(HttpServletRequest request) throws Exception {

		String isShownAt = request.getParameter(SHOWN_AT);
		String isShownBy = request.getParameter(SHOWN_BY);
		String provider = request.getParameter(PROVIDER);
		String europeanaId = request.getParameter(EUROPEANA_ID);
		String redirect;
		if (isShownAt != null) {
			redirect = isShownAt;
		} else if (isShownBy != null) {
			redirect = isShownBy;
		} else {
			throw new IllegalArgumentException(MessageFormat.format(
					"Expected to find '{0}' or '{1}' in the request URL", SHOWN_AT, SHOWN_BY));
		}
		String logString = MessageFormat.format("outlink={0}, provider={2}, europeana_id={1}", redirect, europeanaId,
				provider);
		clickStreamLogger.logCustomUserAction(request, ClickStreamLogService.UserAction.REDIRECT_OUTLINK, logString);
		return "redirect:" + redirect;
	}
}
