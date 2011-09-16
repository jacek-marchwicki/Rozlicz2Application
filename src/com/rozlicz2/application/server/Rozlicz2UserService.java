package com.rozlicz2.application.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.NotFoundException;
import com.rozlicz2.application.server.service.AppUserDao;
import com.rozlicz2.application.server.service.AppUserSessionDao;
import com.rozlicz2.application.shared.entity.AppUser;
import com.rozlicz2.application.shared.entity.AppUserSession;
import com.visural.common.IOUtil;
import com.visural.common.StringUtil;

public class Rozlicz2UserService {

	private static final String facebook_client_id = "249870555046124";

	// / set this to the list of extended permissions you want
	private static final String[] facebook_perms = new String[] { "email" };

	// set this to your servlet URL for the authentication servlet/filter
	private static final String facebook_redirect_url = "http://localhost:8888/login";

	private static final String facebook_secret = "307bba0282982ea7459bc2689a6039c3";

	private static final SecureRandom random = new SecureRandom();

	private static final String SESSION_ID = "user_id";

	public static Rozlicz2UserService get() {
		Rozlicz2UserService userService = new Rozlicz2UserService();
		return userService;
	}

	public static String getFacebookAuthURL(String authCode,
			String redirect2_url) throws UnsupportedEncodingException {

		String redirect2_url_safe = URLEncoder.encode(redirect2_url, "utf-8");
		String redirect_url_safe = URLEncoder.encode(facebook_redirect_url
				+ "?redirect_url=" + redirect2_url_safe, "utf-8");
		return "https://graph.facebook.com/oauth/access_token?client_id="
				+ facebook_client_id + "&redirect_uri=" + redirect_url_safe
				+ "&client_secret=" + facebook_secret + "&code="
				+ URLEncoder.encode(authCode, "utf-8");
	}

	private final UserService googleUserService;

	private final AppUserSessionDao sessionDao = new AppUserSessionDao();

	private final AppUserDao userDao = new AppUserDao();

	public Rozlicz2UserService() {
		googleUserService = UserServiceFactory.getUserService();
	}

	public void authFacebookLogin(String accessToken, int expires,
			HttpServletRequest request) {
		try {
			JSONObject resp = new JSONObject(IOUtil.urlToString(new URL(
					"https://graph.facebook.com/me?access_token="
							+ URLEncoder.encode(accessToken, "utf-8"))));

			String email = resp.getString("email");
			String nickname = resp.getString("username");
			if (nickname == null || nickname.isEmpty()) {
				nickname = resp.getString("name");
			}

			String facebookId = resp.getString("id");

			AppUser user = userDao.getByProperty("facebookId", facebookId);
			if (user == null) {
				user = userDao.getByProperty("email", email);

				if (user == null) {
					user = new AppUser();
					user.setEmail(email);
					user.setNickname(nickname);
				}
				user.setFacebookId(facebookId);

				userDao.put(user);
			}

			AppUserSession session = new AppUserSession();
			session.setUserKey(user);
			session.setSessionId(generateSessionId());
			sessionDao.put(session);
			assert session.getSessionId() != null;
			setSessionId(request, session.getSessionId());

		} catch (Throwable ex) {
			throw new RuntimeException("failed login", ex);
		}
	}

	public String createFacebookLoginURL(String redirectUrl) {

		String redirect2_url_safe;
		try {
			redirect2_url_safe = URLEncoder.encode(redirectUrl, "utf-8");
			String redirect_url_safe = URLEncoder.encode(facebook_redirect_url
					+ "?redirect_url=" + redirect2_url_safe, "utf-8");
			return "https://graph.facebook.com/oauth/authorize?client_id="
					+ facebook_client_id + "&display=page&redirect_uri="
					+ redirect_url_safe + "&scope="
					+ StringUtil.delimitObjectsToString(",", facebook_perms);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}

	public String createGoogleLoginURL(String redirectUrl) {
		return googleUserService.createLoginURL(redirectUrl);

	}

	public String createLogoutURL(String redirectUrl) {

		return "/logout?redirect_url=" + redirectUrl;
	}

	public String generateSessionId() {
		return new BigInteger(130, random).toString(32);
	}

	public AppUser getCurrentUserInfo(HttpServletRequest request) {
		assert (request != null);
		String sessionId = getSessionId(request);

		if (sessionId != null) {
			Key<AppUserSession> sessionKey = new Key<AppUserSession>(
					AppUserSession.class, sessionId);
			AppUserSession appUserSession;
			try {
				appUserSession = sessionDao.get(sessionKey);
				AppUser appUser = userDao.get(appUserSession.getUserKey());
				return appUser;
			} catch (NotFoundException e) {
				// not found entity or user in DAO
			}

		}

		User googleUser = googleUserService.getCurrentUser();

		if (googleUser != null) {
			// google user found
			// check if it is stored in database
			AppUser user = userDao.getByProperty("googleId",
					googleUser.getUserId());
			if (user == null) {
				user = userDao.getByProperty("email", googleUser.getEmail());

				if (user == null) {
					user = new AppUser();
					user.setEmail(googleUser.getEmail());
					user.setNickname(googleUser.getNickname());
				}
				user.setGoogleId(googleUser.getUserId());

				userDao.put(user);
			}

			AppUserSession session = new AppUserSession();
			session.setUserKey(user);
			session.setSessionId(generateSessionId());
			sessionDao.put(session);
			assert session.getSessionId() != null;

			setSessionId(request, session.getSessionId());

			return user;
		}
		// TODO if received information via facebook

		return null;
	}

	public String getSessionId(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return (String) session.getAttribute(SESSION_ID);
	}

	public void logoutUser(HttpServletRequest request,
			HttpServletResponse response, String redirect_url)
			throws IOException {
		String sessionId = getSessionId(request);
		if (sessionId != null) {
			Key<AppUserSession> session = new Key<AppUserSession>(
					AppUserSession.class, sessionId);
			sessionDao.deleteKey(session);
		}
		User googleUser = googleUserService.getCurrentUser();

		if (googleUser != null) {
			response.sendRedirect(googleUserService
					.createLogoutURL(redirect_url));
		}

		// TODO if (facebook)
		response.sendRedirect(redirect_url);

	}

	public void setSessionId(HttpServletRequest request, String id) {
		HttpSession session = request.getSession();
		session.setAttribute(SESSION_ID, id);
	}
}