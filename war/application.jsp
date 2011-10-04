<%@page import="com.rozlicz2.application.shared.entity.AppUser"%>
<%@page import="java.net.URLEncoder"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.rozlicz2.application.server.Rozlicz2UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%
	Rozlicz2UserService userService = Rozlicz2UserService.get();
	AppUser userInformations = userService.getCurrentUserInfo(request);
	
%>
<?xml version="1.0" encoding="utf-8"?>
<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pl" lang="pl">
<head>
<meta name="Description" content="Application for making forms easier." />
<meta name="Keywords" content="form, application, hospital" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="Distribution" content="Global" />
<meta name="Author" content="Jacek Marchwicki - jacek@3made.pl" />
<meta name="Robots" content="index,follow" />
<link rel="stylesheet" href="/stylesheets/loading.css" type="text/css" />
<title>SZAS-form application</title>
<!--[if IE]>
		<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
		<![endif]-->
<%
	if (userInformations != null)
	{
%>		
		<script type="text/javascript">
			<!--
				var userEmail = "<%= userInformations.getEmail() %>";
			//-->
		</script>
		<script type="text/javascript"
			src="rozlicz2application/rozlicz2application.nocache.js"></script>
<%
	}
	else
%>
		<link rel="stylesheet" href="/stylesheets/main.css" type="text/css" />
</head>

<body>
<%
	if (userInformations != null)
	{
%>		
		<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1'
		style="position: absolute; width: 0; height: 0; border: 0"></iframe>
	<div id="application"></div>
	<div id="loading"></div>
	<noscript>
		<div
			style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
			Your web browser must have JavaScript enabled in order for this
			application to display correctly.</div>
	</noscript>
<%
	} else {
		String redirect_url = "/application.jsp";
		String googleUrl = userService.createGoogleLoginURL(redirect_url);
		String facebookUrl = userService.createFacebookLoginURL(redirect_url);
%>
		<div id="h1h1">
		<div id="h1h2">
		</div>
	</div>
	<div id="h2h1">
		<h1 id="h2h2">
			<div id="logo">
				rozlicz<span class="green"><span class="two">2</span>.pl</span>
			</div>
			<div id="content-right">
				rozliczaj się ze wszystkiego* <span>*za darmo</span>
			</div>
		</h1>
	</div>
	<div id="content">

		<div id="body">
			<a href="<%= facebookUrl%>"><img alt="Login via Facebook" src="images/facebook.png"></img></a>
			<a href="<%= googleUrl%>"><img alt="Login via Google Account" src="images/google.png"></img></a>
		</div>
	</div>

<%
	}
%>
</body>
</html>