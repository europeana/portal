package eu.europeana.portal2.web.presentation.model.data;

import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;

public class RegisterData extends SearchPageData {

	private String token;
	private String email;
	private String userName;
	private String password;
	private String password2;
	private Boolean disclaimer;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public Boolean getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(Boolean disclaimer) {
		this.disclaimer = disclaimer;
	}
}
