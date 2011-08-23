package com.rozlicz2.application.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rozlicz2.application.server.Rozlicz2UserService.UserInformations;
import com.visural.common.StringUtil;


public class LoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
        String code = req.getParameter("code");
        if (StringUtil.isNotBlankStr(code)) {
            String authURL = Facebook.getAuthURL(code);
            URL url = new URL(authURL);
            try {
                String result = readURL(url);
                String accessToken = null;
                Integer expires = null;
                String[] pairs = result.split("&");
                for (String pair : pairs) {
                    String[] kv = pair.split("=");
                    if (kv.length != 2) {
                        throw new RuntimeException("Unexpected auth response");
                    } else {
                        if (kv[0].equals("access_token")) {
                            accessToken = kv[1];
                        }
                        if (kv[0].equals("expires")) {
                            expires = Integer.valueOf(kv[1]);
                        }
                    }
                }
                if (accessToken != null && expires != null) {
                    Rozlicz2UserService us = Rozlicz2UserService.get();
                    UserInformations authFacebookLogin = us.authFacebookLogin(accessToken, expires);
                    resp.getWriter().println("Hello, " + authFacebookLogin.email);
                } else {
                    throw new RuntimeException("Access token and expires not found");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
        	resp.sendRedirect(Facebook.getLoginRedirectURL());
        }
    }

	private String readURL(URL url) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InputStream is = url.openStream();
        int r;
        while ((r = is.read()) != -1) {
            baos.write(r);
        }
        return new String(baos.toByteArray());
    }
	


}

