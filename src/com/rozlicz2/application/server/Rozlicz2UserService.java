package com.rozlicz2.application.server;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.visural.common.IOUtil;

public class Rozlicz2UserService {

	private UserService googleUserService;

	private DatastoreService datastore;

	public Rozlicz2UserService() {
		datastore = DatastoreServiceFactory.getDatastoreService();
		googleUserService = UserServiceFactory.getUserService();
	}

	static public class UserInfo {
		public String nickname;
		public String email;
	}

	public String getSessionId(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (String) session.getAttribute("user_id");
	}

	public void setSessionId(HttpServletRequest request, String id) {
		HttpSession session = request.getSession();
		session.setAttribute("user_id", id);
	}

	public UserInfo getCurrentUserInfo(HttpServletRequest request) {
		String sessionId = getSessionId(request);

		if (sessionId != null) {
			try {
				Key sessionKey = KeyFactory.stringToKey(sessionId);

				Entity sessionEntity;

				sessionEntity = datastore.get(sessionKey);

				Entity userEntity = datastore.get((Key) sessionEntity.getProperty("userKey"));
				return getUserInformationsFromEntity(userEntity);
			} catch (EntityNotFoundException e1) {
				// session timeout
			} catch (java.lang.IllegalArgumentException e2) {
				// wrong session format
			}
		}

		User googleUser = googleUserService.getCurrentUser();

		if (googleUser != null) {
			// google user found
			// check if it is stored in database
			Query userQuerry = new Query("User");
			userQuerry.addFilter("googleId", Query.FilterOperator.EQUAL, googleUser.getUserId());
			PreparedQuery pq = datastore.prepare(userQuerry);
			Entity user = pq.asSingleEntity();
			if (user == null) { 			
				// Create user's entity in database

				user = new Entity("User");
				user.setProperty("googleId", googleUser.getUserId());
				user.setProperty("facebookId", null);
				user.setProperty("email", googleUser.getEmail());
				user.setProperty("nickname", googleUser.getNickname());

				datastore.put(user);
			}

			Entity session = new Entity("Session");
			session.setProperty("userKey", user.getKey());
			datastore.put(session);

			setSessionId(request, KeyFactory.keyToString(session.getKey()));

			UserInfo userInformations = getUserInformationsFromEntity(user);

			return userInformations;
		}
		// TODO if received information via facebook

		return null;
	}

	private UserInfo getUserInformationsFromEntity(Entity user) {
		UserInfo userInformations = new UserInfo();
		userInformations.email = (String) user.getProperty("email");
		userInformations.nickname = (String) user.getProperty("nickname");
		return userInformations;
	}

	public String createGoogleLoginURL(String redirectUrl) {
		return googleUserService.createLoginURL(redirectUrl);

	}

	public String createFacebookLoginURL(String redirectUrl) {
		// TODO merge current_url with servlet url

		// TODO return facebook url

		// TODO in servlet redirect to property site
		return null;
	}

	public String createLogoutURL(String redirectUrl) {

		return "/logout?redirect_url=" + redirectUrl;			
	}

	public void logoutUser(HttpServletRequest request, HttpServletResponse response, String redirect_url) throws IOException {
		String sessionId = getSessionId(request);
		try {
			Key sessionKey = KeyFactory.stringToKey(sessionId);

			datastore.delete(sessionKey);
		} catch (java.lang.IllegalArgumentException e2) {
			// wrong session format
		}
		
		User googleUser = googleUserService.getCurrentUser();

		if (googleUser != null) {
			response.sendRedirect(
					googleUserService.createLogoutURL(redirect_url));
		}

		// TODO if (facebook)
		response.sendRedirect(redirect_url);


	}
	// ....

	public UserInfo authFacebookLogin(String accessToken, int expires) {
		try {
			JSONObject resp = new JSONObject(
					IOUtil.urlToString(new URL("https://graph.facebook.com/me?access_token=" + URLEncoder.encode(accessToken, "utf-8"))));
			UserInfo userInformations = new UserInfo();
			//            userInformations.id = resp.getString("id");
			//            userInformations.firstName = resp.getString("first_name");
			//            userInformations.lastName = resp.getString("last_name");
			userInformations.email = resp.getString("email");

			// ...
			// create and authorise the user in your current system w/ data above
			// ...
			return userInformations;

		} catch (Throwable ex) {
			throw new RuntimeException("failed login", ex);
		}
	}

	public static Rozlicz2UserService get() {
		Rozlicz2UserService userService = new Rozlicz2UserService();
		return userService;
	}
}