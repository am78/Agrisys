package com.anteboth.agrisys.server;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;


/**
 * Initialization servlet.
 * 
 * @author michael
 */
@SuppressWarnings("serial")
public class StartupServlet extends GenericServlet {

	public void init() {
		String serverInfo = getServletContext().getServerInfo();
		/* ServletContext.getServerInfo() will return "Google App Engine Development/x.x.x"
		 * if will run locally, and "Google App Engine/x.x.x" if run on production envoirnment */
		if (serverInfo.contains("Development")) {
			Constants.DEV_MODE = true;
			System.out.println("DEV MODE = TRUE");
		}else {
			Constants.DEV_MODE = false;
		}
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1)
	throws ServletException, IOException {
	}
}