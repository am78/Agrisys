<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>    

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Agrisys Mobile</title>
<script type="text/javascript" src="js/phonegap.0.9.4.js"></script>
</head>
<body>

<%
UserService userService = UserServiceFactory.getUserService();
User user = userService.getCurrentUser();
if (user != null) {
	//User logged in		
%>
	<h1>logged in</h1>
	
	<script type="text/javascript">
		window.AGRISYS.loginStatus("logged_in");
	</script>	
<%
}
else {
//user not logged in
%>
	<h1>not logged in</h1>
	
	<script type="text/javascript">
		window.AGRISYS.loginStatus("logged_out");
	</script>	
<%
}
%>

</body>
</html>