<%@page contentType="text/html; charset=ISO-8859-1"  language="java" %>

<%@page import="java.util.List"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="com.anteboth.agrisys.client.model.Betrieb"%>
<%@page import="com.anteboth.agrisys.client.model.Flurstueck"%>
<%@page import="com.googlecode.objectify.Objectify"%>
<%@page import="com.googlecode.objectify.ObjectifyService"%>
<%@page import="com.google.gwt.core.client.GWT"%>
<%@page import="com.google.appengine.api.users.User" %>
<%@page import="com.google.appengine.api.users.UserService" %>
<%@page import="com.google.appengine.api.users.UserServiceFactory" %>
<%
    response.setContentType( "application/json");

  	out.clear();
	UserService userService = UserServiceFactory.getUserService();
    User user = userService.getCurrentUser();
    if (user != null) {
  		//User logged in
  		ObjectifyService.register(Flurstueck.class);
  		ObjectifyService.register(Betrieb.class);
  		Objectify ofy = ObjectifyService.begin();
  		List<Flurstueck> data = ofy.query(Flurstueck.class).list();

  		Gson gson = new Gson();
  		String s = gson.toJson(data.toArray());
  		
  		out.append(s);
  		
  	}
  	else {
  		//User not logged in
  		response.sendRedirect(userService.createLoginURL(request.getRequestURI()));
  	}
%>