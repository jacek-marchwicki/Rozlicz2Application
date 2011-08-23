package com.rozlicz2.application.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import com.visural.common.StringUtil;

public class Rozlicz2UserService {

	// ....
	

	// set this to your servlet URL for the authentication servlet/filter
	private static final String facebook_redirect_url = "http://localhost:8888/login";

	private static final String facebook_client_id = "249870555046124";

	/// set this to the list of extended permissions you want
	private static final String[] facebook_perms = new String[] {"email"};

	private static final String facebook_secret = "307bba0282982ea7459bc2689a6039c3";

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

				userQuerry = new Query("User");
				userQuerry.addFilter("email", Query.FilterOperator.EQUAL, googleUser.getEmail());
				pq = datastore.prepare(userQuerry);
				user = pq.asSingleEntity();

				if (user == null) {
					user = new Entity("User");
					user.setProperty("email", googleUser.getEmail());
					user.setProperty("nickname", googleUser.getNickname());
				}
				user.setProperty("googleId", googleUser.getUserId());

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

    	String redirect2_url_safe;
		try {
			redirect2_url_safe = URLEncoder.encode(redirectUrl, "utf-8");
			String redirect_url_safe = URLEncoder.encode(facebook_redirect_url + "?redirect_url=" + redirect2_url_safe, "utf-8");
	        return "https://graph.facebook.com/oauth/authorize?client_id=" +
	        facebook_client_id + "&display=page&redirect_uri=" +
	            redirect_url_safe+"&scope="+StringUtil.delimitObjectsToString(",", facebook_perms);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "";
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



    public static String getFacebookAuthURL(String authCode, String redirect2_url) throws UnsupportedEncodingException {

		String redirect2_url_safe = URLEncoder.encode(redirect2_url, "utf-8");
		String redirect_url_safe = URLEncoder.encode(facebook_redirect_url + "?redirect_url=" + redirect2_url_safe, "utf-8");
        return "https://graph.facebook.com/oauth/access_token?client_id=" +
            facebook_client_id+"&redirect_uri=" +
            redirect_url_safe+"&client_secret="+facebook_secret+"&code="+URLEncoder.encode(authCode,"utf-8");
    }
	
	public void authFacebookLogin(String accessToken, int expires, HttpServletRequest request) {
		try {
			JSONObject resp = new JSONObject(
					IOUtil.urlToString(new URL("https://graph.facebook.com/me?access_token=" + URLEncoder.encode(accessToken, "utf-8"))));

			String email = resp.getString("email");
			String nickname = resp.getString("username");
			if (nickname == null || nickname.isEmpty())  {
				nickname = resp.getString("name");
			}

			String facebookId = resp.getString("id");

			// check if it is stored in database
			Query userQuerry = new Query("User");
			userQuerry.addFilter("facebookId", Query.FilterOperator.EQUAL, facebookId);
			PreparedQuery pq = datastore.prepare(userQuerry);
			Entity user = pq.asSingleEntity();
			if (user == null) { 			
				// Create user's entity in database
				
				userQuerry = new Query("User");
				userQuerry.addFilter("email", Query.FilterOperator.EQUAL, email);
				pq = datastore.prepare(userQuerry);
				user = pq.asSingleEntity();
				
				if (user == null) { 
					user = new Entity("User");
					user.setProperty("email", email);
					user.setProperty("nickname", nickname);
				}
				user.setProperty("facebookId", facebookId);
				

				datastore.put(user);
			}

			Entity session = new Entity("Session");
			session.setProperty("userKey", user.getKey());
			datastore.put(session);

			setSessionId(request, KeyFactory.keyToString(session.getKey()));


		} catch (Throwable ex) {
			throw new RuntimeException("failed login", ex);
		}
	}

	public static Rozlicz2UserService get() {
		Rozlicz2UserService userService = new Rozlicz2UserService();
		return userService;
	}
}