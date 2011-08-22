package com.rozlicz2.application.server;

import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
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
	static public class UserInformations {
		public String nickname;
		public String email;
	}
	
	public UserInformations getCurrentUser(HttpServletRequest request) {
		
		Query sessionQuerry = new Query("Session");
		sessionQuerry.addFilter("id", Query.FilterOperator.EQUAL, request.getSession().getId());
		PreparedQuery preparedSessionQuerry = datastore.prepare(sessionQuerry);
		Entity sessionEntity = preparedSessionQuerry.asSingleEntity();
		
		try {
			if (sessionEntity != null) {
				Entity userEntity = datastore.get((Key) sessionEntity.getProperty("userKey"));
				return getUserInformationsFromEntity(userEntity);
				 
			}
		} catch (EntityNotFoundException e) {
			// not found
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
			session.setProperty("id", request.getSession().getId());
			session.setProperty("userKey", user.getKey());
			datastore.put(session);
			
			UserInformations userInformations = getUserInformationsFromEntity(user);
			
			return userInformations;
		}
		// TODO if received information via facebook
		
		
		return null;
	}

	private UserInformations getUserInformationsFromEntity(Entity user) {
		UserInformations userInformations = new UserInformations();
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

    // ....

    public UserInformations authFacebookLogin(String accessToken, int expires) {
        try {
            JSONObject resp = new JSONObject(
                IOUtil.urlToString(new URL("https://graph.facebook.com/me?access_token=" + URLEncoder.encode(accessToken, "utf-8"))));
            UserInformations userInformations = new UserInformations();
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