package eu.europeana.corelib.web.service.impl;

import java.io.Serializable;
import java.util.List;

import eu.europeana.corelib.db.entity.relational.custom.TagCloudItem;
import eu.europeana.corelib.db.exception.DatabaseException;
import eu.europeana.corelib.db.service.UserService;
import eu.europeana.corelib.definitions.db.entity.relational.SavedItem;
import eu.europeana.corelib.definitions.db.entity.relational.SocialTag;
import eu.europeana.corelib.definitions.db.entity.relational.User;

public class UserServiceImplMock implements UserService {

	@Override
	public User store(User object) throws DatabaseException {
		return null;
	}

	@Override
	public void remove(User object) throws DatabaseException {
	}

	@Override
	public long count() {
		return 0;
	}

	@Override
	public List<User> findAll() {
		return null;
	}

	@Override
	public User findByID(Serializable id) throws DatabaseException {
		return null;
	}

	@Override
	public User create(String tokenString, String username, String password)
			throws DatabaseException {
		return null;
	}

	@Override
	public User create(String tokenString, String username, String password,
			boolean isApiRegistration, String company, String country,
			String firstName, String lastName, String website, String address,
			String phone, String fieldOfWork) throws DatabaseException {
		return null;
	}

	@Override
	public User findByEmail(String email) {
		return null;
	}

	@Override
	public User findByApiKey(String apiKey) {
		return null;
	}

	@Override
	public User findByName(String userName) {
		return null;
	}

	@Override
	public User authenticateUser(String email, String password) {
		return null;
	}

	@Override
	public User changePassword(Long userId, String oldPassword,
			String newPassword) throws DatabaseException {
		return null;
	}

	@Override
	public User createSavedSearch(Long userId, String query, String queryString)
			throws DatabaseException {
		return null;
	}

	@Override
	public User createSavedItem(Long userId, String europeanaObjectId)
			throws DatabaseException {
		return null;
	}

	@Override
	public User createSocialTag(Long userId, String europeanaObjectId,
			String tag) throws DatabaseException {
		return null;
	}

	@Override
	public void removeSavedSearch(Long userId, Long savedSearchId)
			throws DatabaseException {
	}

	@Override
	public void removeSavedItem(Long userId, Long savedItemId)
			throws DatabaseException {
	}

	@Override
	public void removeSavedItem(Long userId, String objectId)
			throws DatabaseException {
	}

	@Override
	public void removeSocialTag(Long userId, Long socialTagId)
			throws DatabaseException {
	}

	@Override
	public void removeSocialTag(Long userId, String objectId, String tag)
			throws DatabaseException {
	}

	@Override
	public User registerApiUserForMyEuropeana(Long userId, String userName,
			String password) throws DatabaseException {
		return null;
	}

	@Override
	public List<TagCloudItem> createSocialTagCloud(Long userId)
			throws DatabaseException {
		return null;
	}

	@Override
	public List<SocialTag> findSocialTagsByTag(Long userId, String tag)
			throws DatabaseException {
		return null;
	}

	@Override
	public List<SocialTag> findSocialTagsByEuropeanaId(Long userId,
			String europeanaId) throws DatabaseException {
		return null;
	}

	@Override
	public SavedItem findSavedItemByEuropeanaId(Long userId, String europeanaId)
			throws DatabaseException {
		return null;
	}

	@Override
	public User updateUserLanguagePortal(Long userId, String languageCode)
			throws DatabaseException {
		return null;
	}

	@Override
	public User updateUserLanguageItem(Long userId, String languageCode)
			throws DatabaseException {
		return null;
	}

	@Override
	public User updateUserLanguageSearch(Long userId, String... languageCodes)
			throws DatabaseException {
		return null;
	}

}
