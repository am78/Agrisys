package com.anteboth.agrisys.service;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.anteboth.agrisys.client.model.res.IStammdatenResource;
import com.anteboth.agrisys.client.model.stammdaten.Stammdaten;
import com.anteboth.agrisys.server.ServiceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StammdatenResource extends ServerResource implements IStammdatenResource {

	@Get
	public Stammdaten retrieve() {
		Stammdaten sd = ServiceManager.getInstance().getStammdaten();
		
		return sd;
	}
	
	@Get("json")
	public String retrieveJson() {
		Stammdaten sd = retrieve();
		GsonBuilder gb = new GsonBuilder();
		gb.setDateFormat("dd.MM.yyyy");
		gb.setPrettyPrinting();
		Gson gson = gb.create();
		String s = gson.toJson(sd);
		return s;
	}
	
}
