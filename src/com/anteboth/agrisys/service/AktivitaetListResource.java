package com.anteboth.agrisys.service;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.anteboth.agrisys.client.model.Aktivitaet;
import com.anteboth.agrisys.client.model.res.IAktivitaetListResource;
import com.anteboth.agrisys.server.ServiceManager;
import com.google.gson.Gson;

/**
 * Resource for a list of {@link Aktivitaet} items.
 * 
 */
public class AktivitaetListResource extends ServerResource implements IAktivitaetListResource {

	@Get
	public Aktivitaet[] retrieve() {
		Aktivitaet[] data = null;
		if (getRequestAttributes().containsKey("id")) {
			Long id = Long.parseLong((String) getRequestAttributes().get("id"));
			List<Aktivitaet> l = ServiceManager.getInstance().loadAktivitaetData(id);
			if (l != null) {
				data = l.toArray(new Aktivitaet[l.size()]);
			}
		}	
		
		return data;
	}
	
	@Get("json") 
	public String retrieveJson() {
		Aktivitaet[] data = retrieve();
		
		Gson gson = new Gson();
		String s = gson.toJson(data);
		
		return s;
	}

}