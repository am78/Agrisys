package com.anteboth.agrisys.server;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Provides the authentivation check.
 * @author michael
 */
public class AuthServlet implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
	throws IOException, ServletException {
		
		String thisURL = ((HttpServletRequest) request).getRequestURI();			
		UserService userService = UserServiceFactory.getUserService();
		boolean loggedIn = userService.isUserLoggedIn();
		
		if (loggedIn) {
			chain.doFilter(request, response);
		} 
		else {
			String url = userService.createLoginURL(thisURL); 
			((HttpServletResponse) response).sendRedirect(url);
		}
		
	}

	@Override
	public void init(FilterConfig cfg) throws ServletException {
	}
	
	@Override
	public void destroy() {
	}
}