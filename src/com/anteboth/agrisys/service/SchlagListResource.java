package com.anteboth.agrisys.service;
import java.util.List;

import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.anteboth.agrisys.client.model.Betrieb;
import com.anteboth.agrisys.client.model.Erntejahr;
import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.model.res.ISchlagListResource;
import com.anteboth.agrisys.server.ServiceManager;
import com.google.gson.Gson;

/**
 * Resource for a list of Schlag items.
 * Get retrieves the array of {@link Schlag} items
 * for the session user and the current Erntejahr.
 *  
 */
public class SchlagListResource extends ServerResource implements ISchlagListResource {

	@Get
	public Schlag[] retrieve() {
		
		ServiceManager mgr = ServiceManager.getInstance();
		
		Erntejahr erntejahr = mgr.getErntejahr(mgr.getCurrentErntejahr());
		Betrieb betrieb = mgr.getBetriebForAccount(mgr.getCurrentUserAccount());
		
		List<Schlag> data = mgr.loadSchlagData(erntejahr, betrieb);
		
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		
		return data.toArray(new Schlag[data.size()]);
	}
	
	@Get("json") 
	public String retrieveJson() {
		Schlag[] data = retrieve();
		
		Gson gson = new Gson();
		String s = gson.toJson(data);
		
		return s;
	}

}