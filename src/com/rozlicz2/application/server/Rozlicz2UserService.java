package com.rozlicz2.application.server;

import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.repackaged.org.json.JSONObject;
import com.visural.common.IOUtil;

public class Rozlicz2UserService {
	
	com.google.appengine.api.users.UserService userService;
	private DatastoreService datastore;
	
	public Rozlicz2UserService() {
		datastore = DatastoreServiceFactory.getDatastoreService();
		userService =
			com.google.appengine.api.users.UserServiceFactory.getUserService();
	}
	static public class UserInformations {
		String id;
		String firstName;
		String lastName;
		String email;
	}
	
	
	public UserInformations getCurrentUser(HttpServletRequest request) {
		// TODO if logged - if in request is cookie and it's corresponding to database value
		// TODO if logged via google
		if (logged_via_google) {
			// check if in database
			
			// else
			
			Entity user = new Entity("User");
			user.setProperty("google",some_data);
			user.setProperty("facebook",null);
			datastore.put(user);
			
			// create cookie
			Entity cookie = new Entity("Cookie");
			cookie.setProperty("cookie", null);
			cookie.setProperty("user_key", user.getKey());
			datastore.put(cookie);
			
			// send cookie
			
		}
		// TODO if received information via facebook
		
		
		return null;
	}
	
	public String createGoogleLoginURL(String current_url) {
		return userService.createLoginURL(current_url);
	}
	
	public String createFacebookLoginURL(String current_url) {
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
            userInformations.id = resp.getString("id");
            userInformations.firstName = resp.getString("first_name");
            userInformations.lastName = resp.getString("last_name");
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