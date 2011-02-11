package com.anteboth.agrisys.service;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.anteboth.agrisys.client.model.res.IStammdatenResource;
import com.anteboth.agrisys.client.model.stammdaten.Stammdaten;
import com.anteboth.agrisys.server.ServiceManager;
import com.google.gson.Gson;

public class StammdatenResource extends ServerResource implements IStammdatenResource {

	@Get
	public Stammdaten retrieve() {
		Stammdaten sd = ServiceManager.getInstance().getStammdaten();
		return sd;
	}
	
	@Get("json")
	public String retrieveJson() {
		Stammdaten sd = retrieve();
		Gson gson = new Gson();
		String s = gson.toJson(sd);
		return s;
	}
	
}
