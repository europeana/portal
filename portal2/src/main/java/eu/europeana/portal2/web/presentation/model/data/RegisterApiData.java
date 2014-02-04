package eu.europeana.portal2.web.presentation.model.data;

import eu.europeana.portal2.web.presentation.model.abstracts.SearchPageData;

public class RegisterApiData extends SearchPageData {

	protected boolean failureFormat = false;
	protected boolean failureExists = false;
	protected boolean success = false;

	private String[] fieldOfWorks = new String[] { "Gallery", "Library", "Archive", "Museum", "Research/Education",
			"Individual developer", "Commercial company", "Non profit organization/Government" };

	private String token;
	private String email;
	private String userName;
	private String apiKey;
	private String privateKey;
	private String applicationName;
	private Boolean disclaimer;
	private String firstName;
	private String lastName;
	private String company;
	private String country;
	private String phone;
	private String address;
	private String website;
	private String requestedAction;
	private String fieldOfWork;

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

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public Boolean getDisclaimer() {
		return disclaimer;
	}

	public void setDisclaimer(Boolean disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getRequestedAction() {
		return requestedAction;
	}

	public void setRequestedAction(String requestedAction) {
		this.requestedAction = requestedAction;
	}

	public boolean isFailureFormat() {
		return failureFormat;
	}

	public void setFailureFormat(boolean failureFormat) {
		this.failureFormat = failureFormat;
	}

	public boolean isFailureExists() {
		return failureExists;
	}

	public void setFailureExists(boolean failureExists) {
		this.failureExists = failureExists;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getFieldOfWork() {
		return fieldOfWork;
	}

	public void setFieldOfWork(String fieldOfWork) {
		this.fieldOfWork = fieldOfWork;
	}

	public String[] getFieldOfWorks() {
		return fieldOfWorks;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
}
