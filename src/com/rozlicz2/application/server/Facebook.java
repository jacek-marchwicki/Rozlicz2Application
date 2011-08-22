package com.rozlicz2.application.server;

import com.visural.common.StringUtil;

public class Facebook {
    // get these from your FB Dev App
    private static final String api_key = "249870555046124";
    private static final String secret = "307bba0282982ea7459bc2689a6039c3";
    private static final String client_id = "249870555046124";  

    // set this to your servlet URL for the authentication servlet/filter
    private static final String redirect_uri = "http://127.0.0.1:8888/login";
    /// set this to the list of extended permissions you want
    private static final String[] perms = new String[] {"publish_stream", "email"};

    public static String getAPIKey() {
        return api_key;
    }

    public static String getSecret() {
        return secret;
    }

    public static String getLoginRedirectURL() {
        return "https://graph.facebook.com/oauth/authorize?client_id=" +
            client_id + "&display=page&redirect_uri=" +
            redirect_uri+"&scope="+StringUtil.delimitObjectsToString(",", perms);
    }

    public static String getAuthURL(String authCode) {
        return "https://graph.facebook.com/oauth/access_token?client_id=" +
            client_id+"&redirect_uri=" +
            redirect_uri+"&client_secret="+secret+"&code="+authCode;
    }
}