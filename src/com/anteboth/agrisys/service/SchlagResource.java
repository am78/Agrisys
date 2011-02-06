package com.anteboth.agrisys.service;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.model.res.ISchlagResource;
import com.anteboth.agrisys.server.ServiceManager;
import com.google.gson.Gson;

/**
 * 
 */
public class SchlagResource extends ServerResource implements ISchlagResource {

	
	@Get
	public Schlag retrieve() {
		if (getRequestAttributes().containsKey("id")) {
			Long id = Long.parseLong((String) getRequestAttributes().get("id"));
			Schlag s = ServiceManager.getInstance().getSchlag(id);
			return s;
		}
		return null;
	}
	
	@Get("json") 
	public String retrieveJson() {
		Schlag se = retrieve();
		Gson gson = new Gson();
		String s = gson.toJson(se);
		return s;
	}

}