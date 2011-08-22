package com.rozlicz2.application.server;

import java.net.URL;
import java.net.URLEncoder;

import com.google.appengine.repackaged.org.json.JSONObject;
import com.visural.common.IOUtil;

class UserService {
	static class UserInformations {
		String id;
		String firstName;
		String lastName;
		String email;
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

	public static UserService get() {
		UserService userService = new UserService();
		return userService;
	}
}