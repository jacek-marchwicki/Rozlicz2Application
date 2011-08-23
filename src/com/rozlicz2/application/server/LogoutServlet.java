package com.rozlicz2.application.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LogoutServlet extends HttpServlet {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		Rozlicz2UserService rozlicz2UserService = Rozlicz2UserService.get();
		
		// logout
		rozlicz2UserService.logoutUser(req,resp, "/loginPage.jsp");

	}

}
