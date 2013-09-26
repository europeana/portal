package eu.europeana.portal2.web.controllers;

import java.text.MessageFormat;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import eu.europeana.corelib.web.service.EuropeanaUrlService;
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
	private EuropeanaUrlService urlService;

	@Resource
	private ClickStreamLogService clickStreamLogger;

	@RequestMapping("/full-doc.html")
	public void fullDocHtml(@RequestParam(value = "uri", required = false) String uri, HttpServletResponse response)
			throws Exception {

		if (!StringUtils.isEmpty(uri) && !uri.contains("full-doc")) {
				response.sendRedirect(urlService.getCanonicalPortalRecord(urlService.extractEuropeanaId(uri)).toString());
		} else {
			response.sendRedirect(urlService.getPortalHome(false).toString());
		}
	}

	/*
	 * The page where you are redirected to the isShownAt and isShownBy links
	 */
	@RequestMapping("/redirect.html")
	public String handleRedirectFromFullView(HttpServletRequest request) throws Exception {

		String isShownAt = request.getParameter(EuropeanaUrlService.PARAM_REDIRECT_SHOWNAT);
		String isShownBy = request.getParameter(EuropeanaUrlService.PARAM_REDIRECT_SHOWNBY);
		String provider = request.getParameter(EuropeanaUrlService.PARAM_REDIRECT_PROVIDER);
		String europeanaId = request.getParameter(EuropeanaUrlService.PARAM_REDIRECT_EUROPEANAID);
		String redirect;
		if (isShownAt != null) {
			redirect = isShownAt;
		} else if (isShownBy != null) {
			redirect = isShownBy;
		} else {
			throw new IllegalArgumentException(MessageFormat.format(
					"Expected to find '{0}' or '{1}' in the request URL", EuropeanaUrlService.PARAM_REDIRECT_SHOWNAT, EuropeanaUrlService.PARAM_REDIRECT_SHOWNBY));
		}
		String logString = MessageFormat.format("outlink={0}, provider={2}, europeana_id={1}", redirect, europeanaId,
				provider);
		clickStreamLogger.logCustomUserAction(request, ClickStreamLogService.UserAction.REDIRECT_OUTLINK, logString);
		return "redirect:" + redirect;
	}
}
