<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>

<!doctype html>
<html>
<head>
<title>AGRISYS</title>
<meta charset="utf-8" />
<meta http-equiv="content-type" content="text/html;charset=utf-8" />
<meta name="gwt:property" content="locale=de_DE">

<link rel="stylesheet" href="style.css" type="text/css" />

	<script>
    	var isomorphicDir = "agrisys/sc/";
    </script>
    
    <!--                                           -->
    <!-- This script loads your compiled module.   -->
    <!-- If you add any GWT meta tags, they must   -->
    <!-- be added before this line.                -->
    <!--                                           -->
    <script type="text/javascript" language="javascript" src="agrisys/agrisys.nocache.js"></script>
    
</head>
<body>

<%
    UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
  		//User logged in
%>

    <!-- OPTIONAL: include this if you want history support -->
    <iframe src="javascript:''" id="__gwt_historyFrame" tabIndex='-1' style="position:absolute;width:0;height:0;border:0"></iframe>
    
    <!-- RECOMMENDED if your web app will not function without JavaScript enabled -->
    <noscript>
      <div style="width: 22em; position: absolute; left: 50%; margin-left: -11em; color: red; background-color: white; border: 1px solid red; padding: 4px; font-family: sans-serif">
        Your web browser must have JavaScript enabled
        in order for this application to display correctly.
      </div>
    </noscript>


<table border="0" width="90%" align="center">
	<tr>
		<td align="left" width="230"> <!-- header -->
			<img src="/img/agrisys_logo.png" alt="logo"/>
		</td>
		<td width="70" align="left">
			<div id="buttonStammdatenPanel" /> 
		</td>
		<td>
			<a href="report" target="_blank">Report</a>
		</td>
		<td width="60" align="left">
			<a href="<%= userService.createLogoutURL(request.getRequestURI()) %>">Logout</a>
		</td>
		<td align="center">
			<div id="erntejahrSelectionPanel" />
		</td>
		<td align="left">
			<div id="userInfoPanel" />
		</td>
	</tr>
	<tr>
		<td colspan="6">	
			<p>
			<!-- mainPanel holds the application panel -->
			<div id="mainPanel" />
			</p>
		</td>				 
	</tr>		
	<tr>
		<td></td>
	</tr>	
</table>

<%  	
    } else {
    	//user not logged in
%>
<table border="0" width="90%" align="center">
	<tr>
		<td align="left" width="230"> <!-- header -->
			<img src="/img/agrisys_logo.png" alt="logo"/>
		</td>
	</tr>
	<tr>
		<td>	
			<p>
				Sie sind nicht angemeldet!
				Hier geht's zum <a href="<%= userService.createLoginURL(request.getRequestURI()) %>">Login</a>.
			</p>
		</td>				 
	</tr>		
	<tr>
		<td></td>
	</tr>	
</table>
<%
	response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
    }
%>

  </body>
</html>
