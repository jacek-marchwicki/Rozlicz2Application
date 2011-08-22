<%@page import="java.net.URLEncoder"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.rozlicz2.application.server.Rozlicz2UserService.UserInformations" %>
<%@ page import="com.rozlicz2.application.server.Rozlicz2UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%
	Rozlicz2UserService userService = Rozlicz2UserService.get();
	UserInformations userInformations = userService.getCurrentUser(request);
	
	String redirect_url = request.getParameter("redirect_url");
	
	if (redirect_url == null || redirect_url.isEmpty()) {
		redirect_url = "/loginPage.jsp";
	}
	
	if (userInformations != null)
	{
		%>
		<b> you are logged as: <%= userInformations.email %></b>
		 
		//response.sendRedirect(redirect_url);
		 
		 <%
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