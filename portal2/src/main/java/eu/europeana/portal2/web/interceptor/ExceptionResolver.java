package eu.europeana.portal2.web.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import eu.europeana.corelib.definitions.exception.ProblemType;
import eu.europeana.corelib.logging.Log;
import eu.europeana.corelib.logging.Logger;
import eu.europeana.corelib.solr.exceptions.EuropeanaQueryException;
import eu.europeana.corelib.web.service.EmailService;
import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.web.presentation.PortalPageInfo;
import eu.europeana.portal2.web.presentation.model.ExceptionPage;
import eu.europeana.portal2.web.util.ControllerUtil;

public class ExceptionResolver implements HandlerExceptionResolver {

	@Resource
	private Configuration config;

	@Resource
	private EmailService emailService;

	@Log
	private Logger log;

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
			Exception exception) {

		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		ProblemType problem = ProblemType.NONE;
		if (exception instanceof EuropeanaQueryException) {
			problem = ((EuropeanaQueryException) exception).getProblem();
		}

		String stackTrace = ExceptionUtils.getFullStackTrace(exception);
		if (problem != ProblemType.RECORD_NOT_FOUND) {
			log.error("problem.name: " + problem.name());
			log.error(stackTrace);
		}

		switch (problem.getAction()) {
		case MAIL:
			try {
				StringBuilder message = new StringBuilder();
				message.append("hostName: ").append(request.getServerName()).append("\n");
				message.append("portal: ").append(config.getPortalUrl()).append("\n");
				message.append("request: ").append(ControllerUtil.formatFullRequestUrl(request)).append("\n");
				message.append("stackTrace: ").append(stackTrace).append("\n");
				// model.put("cacheUrl", imageCacheUrl);
				message.append("portalName: ").append("\n");
				message.append("agent: ").append(request.getHeader("User-Agent")).append("\n");
				message.append("referer: ").append(request.getHeader("referer")).append("\n");
				message.append("date: ").append(new DateTime()).append("\n");

				if (!config.getDebugMode()) {
					emailService.sendException(problem.getMessage(), message.toString());
				} else {
					log.error(stackTrace);
				}
			} catch (Exception e) {
				log.warn("Unable to send exception email to system admin. " + e);
			}
			break;
		case LOG:
			log.warn(problem.getMessage() + "\n" + stackTrace);
			break;
		// ignore
		case IGNORE:
			break;
		}

		ExceptionPage model = new ExceptionPage();
		model.setException(exception);
		model.setProblem(problem);
		model.setStackTrace(stackTrace);
		// set all needed config as interceptors don't work on exception handlers
		model.setTheme(ControllerUtil.getSessionManagedTheme(request, config.getDefaultTheme()));
		model.setLocale(RequestContextUtils.getLocaleResolver(request).resolveLocale(request));
		model.setPortalUrl(config.getPortalUrl());
		model.setPortalName("");
		model.setDebug(config.getDebugMode());

		return ControllerUtil.createModelAndViewPage(model, PortalPageInfo.EXCEPTION);
	}
}
