package com.anteboth.agrisys.service;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.anteboth.agrisys.client.model.Aktivitaet;
import com.anteboth.agrisys.client.model.Aussaat;
import com.anteboth.agrisys.client.model.Bodenbearbeitung;
import com.anteboth.agrisys.client.model.Duengung;
import com.anteboth.agrisys.client.model.Ernte;
import com.anteboth.agrisys.client.model.Pflanzenschutz;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.model.res.IAktivitaetResource;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.Duengerart;
import com.anteboth.agrisys.client.model.stammdaten.PSMittel;
import com.anteboth.agrisys.server.ServiceManager;
import com.google.gson.Gson;
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
		
		Gson gson = new Gson();
		BaseTO bto = gson.fromJson(json, BaseTO.class);
		int type = bto.type;
		
		//Save Bodenbearbeitung
		if (type == Aktivitaet.BODENBEARBEITUNG_TYPE) {
			saveBodenbearbeitung(json, gson);
		}
		//save Pflanzenschutz
		else if (type == Aktivitaet.PFLANZENSCHUTZ_TYPE) {
			savePflanzenschutz(json, gson);
		}
		//save Dunegung
		else if (type == Aktivitaet.DUENGUNG_TYPE) {
			saveDuengung(json, gson);
		}
		//save Aussaat
		else if (type == Aktivitaet.AUSSAAT_TYPE) {
			saveAussaat(json, gson);
		}
		//save ernte
		else if (type == Aktivitaet.ERNTE_TYPE) {
			saveErnte(json, gson);
		}
		
	}
	
	/**
	 * Sets some Aktivitaet base valus from the given BaseTO.
	 * 
	 * @param baseTO source object
	 * @param aktivitaet destination object
	 */
	private void setBaseValues(BaseTO baseTO, Aktivitaet aktivitaet) {
		aktivitaet.setBemerkung(baseTO.bemerkung);
		aktivitaet.setDatum(toDate(baseTO.datum));
		aktivitaet.setLastModification(new Date());
		aktivitaet.setDeleted(false);
		
		if (baseTO.latitude != null && baseTO.latitude.length() > 0) {
			aktivitaet.setLatitude(doubleValue(baseTO.latitude));
		}
		if (baseTO.longitude != null && baseTO.longitude.length() > 0) {
			aktivitaet.setLongitude(doubleValue(baseTO.longitude));
		}

		if (baseTO.flaeche != null) {
			aktivitaet.setFlaeche(baseTO.flaeche);
		}

		Key<SchlagErntejahr> schlagErntejahrKey = new Key<SchlagErntejahr>(SchlagErntejahr.class, baseTO.schlagErntejahrId);
		aktivitaet.setSchlagErntejahr(schlagErntejahrKey);
	}
	
	
	private Double doubleValue(String s) {
		if (s != null && s.length() > 0) {
			return Double.valueOf(s);
		} 
		else {
			return null;
		}
	}
	
	private Integer intValue(String s) {
		if (s != null && s.length() > 0){
			return Integer.parseInt(s);
		} 
		else {
			return null;
		}
	}
	
	private void saveErnte(String json, Gson gson) {
		//transorm json input to TO object
		ErnteTO to = gson.fromJson(json, ErnteTO.class);
		
		//now create and fill the concrete BO
		Ernte e = new Ernte();
		setBaseValues(to, e);
		
		e.setDtProHa(doubleValue(to.dtProHa));
		e.setGesamtMenge(doubleValue(to.gesamtmenge));
		e.setAnlieferung(to.anlieferung);
		
		//and store it
		store(e);
	}

	private void saveAussaat(String json, Gson gson) {
		//transorm json input to TO object
		AussaatTO to = gson.fromJson(json, AussaatTO.class);
		
		//now create and fill the concrete BO
		Aussaat a = new Aussaat();
		setBaseValues(to, a);
		
		a.setKgProHa(doubleValue(to.kgProHa));
		a.setBeize(to.beize);
		
		//and store it
		store(a);
	}
	
	private void saveDuengung(String json, Gson gson) {
		//transorm json input to TO object
		DuengungTO to = gson.fromJson(json, DuengungTO.class);
		
		//now create and fill the concrete BO
		Duengung d = new Duengung();
		setBaseValues(to, d);
		
		d.setEc(doubleValue(to.ec));
		d.setKgProHa(doubleValue(to.kgProHa));
		
		Duengerart da = getDuengerart(intValue(to.duengerart));
		d.setDuengerart(da);
		d.setDuengerartKey(new Key<Duengerart>(Duengerart.class, da.getId()));
		
		//and store it
		store(d);
	}

	private void savePflanzenschutz(String json, Gson gson) {
		//transorm json input to TO object
		PflanzenschutzTO to = gson.fromJson(json, PflanzenschutzTO.class);
		
		//now create and fill the concrete BO
		Pflanzenschutz ps = new Pflanzenschutz();
		setBaseValues(to, ps);
		
		ps.setEc(doubleValue(to.ec));
		ps.setIndikation(to.indikation);
		ps.setKgProHa(doubleValue(to.kgProHa));
		
		PSMittel psm = getPSMittel(intValue(to.psMittel));
		ps.setPsMittel(psm);
		ps.setPsMittelKey(new Key<PSMittel>(PSMittel.class, psm.getId()));
		
		//and store it
		store(ps);
	}

	private void saveBodenbearbeitung(String json, Gson gson) {
		//transorm json input to TO object
		BodenbearbeitungTO to = gson.fromJson(json, BodenbearbeitungTO.class);
		
		//now create and fill the concrete BO
		Bodenbearbeitung b = new Bodenbearbeitung();
		setBaseValues(to, b);

		BodenbearbeitungTyp bt = getBodenbearbeitungTyp(intValue(to.bodenbearbeitungTyp));
		b.setBodenbearbeitungTyp(bt);
		b.setTypKey(new Key<BodenbearbeitungTyp>(BodenbearbeitungTyp.class, bt.getId()));
		
		//and store it
		store(b);
	}

	private BodenbearbeitungTyp getBodenbearbeitungTyp(int id) {
		BodenbearbeitungTyp t = ServiceManager.getInstance().getBodenbearbeitungTyp(id);
		return t;
	}
	
	private PSMittel getPSMittel(int id) {
		PSMittel psm = ServiceManager.getInstance().getPSMittel(id);
		return psm;
	}
	
	private Duengerart getDuengerart(int id) {
		Duengerart d = ServiceManager.getInstance().getDuengerart(id);
		return d;
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