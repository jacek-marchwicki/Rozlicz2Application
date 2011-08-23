<%@page import="java.net.URLEncoder"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.rozlicz2.application.server.Rozlicz2UserService.UserInfo" %>
<%@ page import="com.rozlicz2.application.server.Rozlicz2UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%
	Rozlicz2UserService userService = Rozlicz2UserService.get();
	UserInfo userInformations = userService.getCurrentUserInfo(request);
	
	String redirect_url = request.getParameter("redirect_url");
	
	if (redirect_url == null || redirect_url.isEmpty()) {
		redirect_url = "/loginPage.jsp";
	}
	
	if (userInformations != null)
	{
%>
		 you are logged as: <b><%= userInformations.email %></b>
		 <p>and of course you can logout here</p>
		 <a href="<%= userService.createLogoutURL(redirect_url) %>">sign out</a>.
		 <p> session id: <b><%= userService.getSessionId(request) %></b></p>
		
		 
		 <%
		//response.sendRedirect(redirect_url);
	} else {
		String googleUrl = userService.createGoogleLoginURL(redirect_url);
		String facebookUrl = userService.createFacebookLoginURL(redirect_url);
%>
		<html>
			
			<a href="<%= facebookUrl%>">Login via facebook</a>
			<a href="<%= googleUrl%>">Login via GoogleAccount</a>
		</html>
		<%
	}
%>