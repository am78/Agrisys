package com.anteboth.agrisys.service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.anteboth.agrisys.client.grid.data.StammdatenManager;
import com.anteboth.agrisys.client.model.Aktivitaet;
import com.anteboth.agrisys.client.model.Bodenbearbeitung;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.model.res.IAktivitaetResource;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.server.ServiceManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.googlecode.objectify.Key;

/**
 * Resource for a list of {@link Aktivitaet} items.
 * 
 */
public class AktivitaetResource extends ServerResource implements IAktivitaetResource {

	@Get
	public Aktivitaet retrieve() {
		Aktivitaet data = null;
		if (getRequestAttributes().containsKey("id")) {
			Long id = Long.parseLong((String) getRequestAttributes().get("id"));
			data = ServiceManager.getInstance().getAktivitaet(id);
		}	
		
		return data;
	}
	
	@Get("json") 
	public String retrieveJson() {
		Aktivitaet data = retrieve();
		
		Gson gson = new Gson();
		String s = gson.toJson(data);
		
		return s;
	}

	@Delete
	public void delete(Long id) {
		if (id != null) {
			ServiceManager.getInstance().deleteAktivitaet(id);
		}
	}

	@Put
	public void store(Aktivitaet aktivitaet) {
		if (aktivitaet != null) {
			ServiceManager.getInstance().store(aktivitaet);
		}
	}
	
	@Put ("json")
	public void store(String json) {
		System.out.println(json);
		
		//TODO get type
		int type = 0;
		
		if (type == Aktivitaet.BODENBEARBEITUNG_TYPE) {
			//transorm json input to TO object
			Gson gson = new Gson();
			BodenbearbeitungTO to = gson.fromJson(json, BodenbearbeitungTO.class);
			
			//now create and fill the concrete BO
			Bodenbearbeitung b = new Bodenbearbeitung();
			b.setBemerkung(to.bemerkung);
			b.setDatum(toDate(to.datum));
			b.setFlaeche(to.flaeche);
			b.setLastModification(new Date());
			b.setDeleted(false);

			BodenbearbeitungTyp bt = getBodenbearbeitungTyp(to.bodenbearbeitungTyp);
			b.setBodenbearbeitungTyp(bt);
			b.setTypKey(new Key<BodenbearbeitungTyp>(BodenbearbeitungTyp.class, bt.getId()));
			
			Key<SchlagErntejahr> schlagErntejahrKey = new Key<SchlagErntejahr>(SchlagErntejahr.class, to.schlagErntejahrId);
			b.setSchlagErntejahr(schlagErntejahrKey);
			
			//and store it
			store(b);
		}
	}

	private BodenbearbeitungTyp getBodenbearbeitungTyp(int id) {
		BodenbearbeitungTyp t = ServiceManager.getInstance().getBodenbearbeitungTyp(id);
		return t;
	}

	private Date toDate(String datum) {
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		try {
			return df.parse(datum);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

}