package eu.europeana.portal2.services;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;

/**
 * A subclass of the services class which sets the cookie path to root
 * 
 * @author Gerald de Jong <geralddejong@gmail.com>
 */

public class RememberMeServices extends PersistentTokenBasedRememberMeServices {

	@SuppressWarnings("deprecation")
	public RememberMeServices() throws Exception {}

	@Override
	protected void cancelCookie(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("Cancelling cookie");
		Cookie cookie = new Cookie(SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	@Override
	public void setCookie(String[] tokens, int maxAge,
			HttpServletRequest request, HttpServletResponse response) {
		String cookieValue = encodeCookie(tokens);
		Cookie cookie = new Cookie(SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY, cookieValue);
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		cookie.setSecure(false);
		response.addCookie(cookie);
	}

	/**
	 * Tokens where becoming out of synch when requests where submitted to
	 * quickly. This caused users be be randomly logged out of MyEuropeana. The
	 * tokens where uses so it was not possible for someone to use an old cookie
	 * and log into myEuropeana at a later date without a valid signon. This
	 * level of security was overkill for MyEuropeana so we now always use the
	 * same token. The series is still used to provide a user with a identifier
	 * and maintain the logon.
	 */
	@Override
	protected String generateTokenData() {
		return "fixedTokenValue";
	}
}
