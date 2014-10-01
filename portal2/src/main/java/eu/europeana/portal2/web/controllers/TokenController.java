package eu.europeana.portal2.web.controllers;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import eu.europeana.corelib.web.support.Configuration;
import eu.europeana.portal2.services.BingTokenService;



@Controller
public class TokenController {

	private final BingTokenService tokenService = new BingTokenService();

	@Resource
	private Configuration config;

	@RequestMapping(value = "/token.json", method = RequestMethod.GET, headers="Accept=*/*")
	public @ResponseBody String getToken(){
		String token = 	tokenService.getToken(config.getBingTranslateClientId(), config.getBingTranslateClientSecret());
		return token;
	}

	
}
