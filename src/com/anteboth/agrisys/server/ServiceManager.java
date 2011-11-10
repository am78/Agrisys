package com.anteboth.agrisys.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.anteboth.agrisys.client.model.Account;
import com.anteboth.agrisys.client.model.Aktivitaet;
import com.anteboth.agrisys.client.model.Aussaat;
import com.anteboth.agrisys.client.model.Betrieb;
import com.anteboth.agrisys.client.model.Bodenbearbeitung;
import com.anteboth.agrisys.client.model.Duengung;
import com.anteboth.agrisys.client.model.Ernte;
import com.anteboth.agrisys.client.model.Erntejahr;
import com.anteboth.agrisys.client.model.Flurstueck;
import com.anteboth.agrisys.client.model.ImageResource;
import com.anteboth.agrisys.client.model.Pflanzenschutz;
import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.Duengerart;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.PSMittel;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.anteboth.agrisys.client.model.stammdaten.Stammdaten;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

public class ServiceManager {
	
//	private static final int ACT_ERNTEJAHR = 2011;
	private static ServiceManager instance;
	
	
	public static ServiceManager getInstance() {
		if (instance == null) {
			instance = new ServiceManager();
		}
		return instance;
	}
	
	
	/**
	 * Construtor.
	 */
	private ServiceManager() {
		//register objectify entities
		ObjectifyService.register(Aussaat.class);
		ObjectifyService.register(Account.class);
		ObjectifyService.register(Betrieb.class);
		ObjectifyService.register(Bodenbearbeitung.class);
		ObjectifyService.register(Duengung.class);
		ObjectifyService.register(Pflanzenschutz.class);
		ObjectifyService.register(Erntejahr.class);
		ObjectifyService.register(Ernte.class);
		ObjectifyService.register(Flurstueck.class);
		ObjectifyService.register(SchlagErntejahr.class);
		
		ObjectifyService.register(BodenbearbeitungTyp.class);
		ObjectifyService.register(Duengerart.class);
		ObjectifyService.register(Kultur.class);
		ObjectifyService.register(PSMittel.class);
		ObjectifyService.register(Sorte.class);
	}
	
	
	
	/**
	 * Returns the current Erntejahr.
	 * @return
	 */
	public int getCurrentErntejahr() {
		//get selected current Erntejahr of current user
		Account currentUser = getCurrentUserAccount();
		if (currentUser != null) {
			return currentUser.getCurrentErntejahr();
		}
		else {
			throw new RuntimeException("Error: The current logged in user must not be null.");
		}
		
	}

	/**
	 * Gets the Account for the current user (session).
	 * @return
	 */
	public Account getCurrentUserAccount() {
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    
	    Account account = null;
	    if (user != null) {
			String email = user.getEmail();
			account = getOrCreateAccount(email, "", email, "Betrieb von " + email );
	    } else if (Constants.DEV_MODE) {
	    	//if we are in dev mode and the user is null, use the default user
	    	String email = "anteboth@gmail.com";
			account = getOrCreateAccount(email, "", email, "Betrieb von " + email );
	    }
	    return account;
	}
	
	/**
	 * Retrieves/Creates an account item in datastore if the email is not yet existing.
	 * Creates a Betrieb item as well for new accounts.
	 * 
	 * @param name the account user name
	 * @param pass the account password
	 * @param email the email address, must not be null, is unique for each user
	 */
	protected Account getOrCreateAccount(String name, String pass, String email, String betriebName) {
		Objectify ofy = ObjectifyService.begin();
		
		//get the account for the email address
		Query<Account> accountQuery = ofy.query(Account.class).filter("email", email);
		Account a = accountQuery.get();
		
		if (a == null) {
			//creat new account and store the item if it's not yet existing
			a = new Account(name, pass, email);
			Key<Account> accountKey = ofy.put(a);
			
			//create Betrieb instance
			Betrieb b = new Betrieb();
			b.setName(betriebName);
			b.setManagerKey(accountKey);
			
			ofy.put(b);
		}
		
		return a;
	}
	
	/**
	 * Updates (persisting) the existing account value.
	 * @param account the account value to store
	 */
	public void save(Account account) {
		if (account == null) {
			return;
		}
		
		//store data
		Objectify ofy = ObjectifyService.begin();
		ofy.put(account);
		
	}


	/**
	 * Get berieb for account.
	 * 
	 * @param account the manager account 
	 * @return Betrieb
	 */
	public Betrieb getBetriebForAccount(Account account) {
		if (account == null) {
			return null;
		}
		
		Objectify ofy = ObjectifyService.begin();
		//get betrieb for account
		Betrieb b = ofy.query(Betrieb.class).filter("manager", account).get();
		return b;
	}
	
	
	/**
	 * Returns the {@link Erntejahr} item for the specified jahr.
	 * @param jahr get {@link Erntejahr} for this value
	 * @return {@link Erntejahr} item
	 */
	public Erntejahr getErntejahr(int jahr) {
		Objectify ofy = ObjectifyService.begin();
		Erntejahr erntejahr = ofy.query(Erntejahr.class).filter("erntejahr", jahr).get();
		return erntejahr;
	}


	/* Stammdaten */
	
	/**
	 * Loads all master data entries.
	 * 
	 * @return
	 */
	public Stammdaten getStammdaten() {
		Stammdaten sd = new Stammdaten();
		sd.setKulturList(loadKulturData());
		sd.setBodenbearbeitungTypList(loadBodenbearbeitungTypen());
		sd.setDuengerartList(loadDuengerart());
		sd.setPsMittelList(loadPSMittel());
		sd.setSorteList(loadSorten());
		sd.setCurrentErntejahr(getCurrentErntejahr());
		return sd;
	}

	public List<BodenbearbeitungTyp> loadBodenbearbeitungTypen() {

		//load the data
		Objectify ofy = ObjectifyService.begin();
		List<BodenbearbeitungTyp> result = ofy.query(BodenbearbeitungTyp.class).order("name").list();

		return result;
	}
	
	public BodenbearbeitungTyp getBodenbearbeitungTyp(long id) {
		Objectify ofy = ObjectifyService.begin();
		BodenbearbeitungTyp t = ofy.find(new Key<BodenbearbeitungTyp>(BodenbearbeitungTyp.class, id));
		return t;
	}
	
	public BodenbearbeitungTyp save(BodenbearbeitungTyp typ) {
		if (typ != null) {
			Objectify ofy = ObjectifyService.begin();
			Key<BodenbearbeitungTyp> key = ofy.put(typ);
			typ = ofy.get(key);
		}
		return typ;
	}
	
	
	public void delete(BodenbearbeitungTyp typ) {
		if (typ != null) {
			Objectify ofy = ObjectifyService.begin();
			ofy.delete(typ);
		}
	}
	
	
	public List<PSMittel> loadPSMittel() {
		//load the data
		Objectify ofy = ObjectifyService.begin();
		List<PSMittel> result = ofy.query(PSMittel.class).order("name").list();
		return result;
	}
	
	public PSMittel save(PSMittel dto) {
		if (dto != null) {
			Objectify ofy = ObjectifyService.begin();
			Key<PSMittel> key = ofy.put(dto);
			dto = ofy.get(key);
		}
		return dto;
	}
	
	public PSMittel getPSMittel(long id) {
		Objectify ofy = ObjectifyService.begin();
		PSMittel psm = ofy.find(new Key<PSMittel>(PSMittel.class, id));
		return psm;
	}
	
	
	public void delete(PSMittel dto) {
		if (dto != null) {
			Objectify ofy = ObjectifyService.begin();
			ofy.delete(dto);
		}
	}
	
	public List<Duengerart> loadDuengerart() {
		//load the data
		Objectify ofy = ObjectifyService.begin();
		List<Duengerart> result = ofy.query(Duengerart.class).order("name").list();
		return result;
	}
	
	public Duengerart getDuengerart(long id) {
		Objectify ofy = ObjectifyService.begin();
		Duengerart psm = ofy.find(new Key<Duengerart>(Duengerart.class, id));
		return psm;
	}
	
	public Duengerart save(Duengerart dto) {
		if (dto != null) {
			Objectify ofy = ObjectifyService.begin();
			Key<Duengerart> key = ofy.put(dto);
			dto = ofy.get(key);
		}
		return dto;
	}
	
	
	public void delete(Duengerart dto) {
		if (dto != null) {
			Objectify ofy = ObjectifyService.begin();
			ofy.delete(dto);
		}
	}
	
	public List<Erntejahr> loadErntejahrData() {
		Objectify ofy = ObjectifyService.begin();
		List<Erntejahr> data = ofy.query(Erntejahr.class).order("erntejahr").list();
		return data;
	}
	
	/* Schlagverwaltung */
	
	public List<Schlag> loadSchlagData(Erntejahr erntejahr, Betrieb betrieb) {
		if (erntejahr == null) {
			return null;
		}
		
		Objectify ofy = ObjectifyService.begin();
		List<Schlag> result = new ArrayList<Schlag>();
		
		//get SchlagErntejahr entries for requested erntejahr
		List<SchlagErntejahr> schlagErntejahrList = 
			ofy.query(SchlagErntejahr.class).filter("erntejahr", erntejahr).list();

		for (SchlagErntejahr se : schlagErntejahrList) {
			//get Flurstueck
			Flurstueck flurstueck = ofy.get(se.getFlurstueck());

			//only get Flurstueck for current users Betrieb
			Betrieb fsBetrieb = flurstueck.getBetrieb() != null ? ofy.find(flurstueck.getBetrieb()) : null;
			if (fsBetrieb != null && betrieb.getId().equals(fsBetrieb.getId())) {
				//create Schlag entry for SchlagErntejahr and Flurstueck 
				Schlag schlag = new Schlag();
				schlag.setSchlagErntejahr(se);
				schlag.setFlurstueck(flurstueck);
				result.add(schlag);
			}
		}
		return result;
	}
	
	
	/**
	 * Loads the {@link Schlag} item for the specified {@link SchlagErntejahr} id.
	 * @param id the {@link SchlagErntejahr} id
	 * @return the {@link Schlag} item
	 */
	public Schlag getSchlag(long id) {
		//TODO auf Scope des aktuellen Benutzers einschrŠnken, sonst Sicherheitsrisiko
		
		if (id < 0) {
			return null;
		}
		
		Objectify ofy = ObjectifyService.begin();
		
		//get SchlagErntejahr for id
		SchlagErntejahr se = ofy.query(SchlagErntejahr.class).filter("id", id).get();
		if (se == null) {
			return null;
		}
		Flurstueck f = ofy.get(se.getFlurstueck());
		
		Schlag s = new Schlag();
		s.setSchlagErntejahr(se);
		s.setFlurstueck(f);

		return s;
	}
	
	public Schlag saveNewSchlag(Betrieb betrieb, String name, double flaeche, String bemerkung, 
			int jahr, Sorte anbau, Kultur vorfrucht) 
	{
		Objectify ofy = ObjectifyService.begin();

		Erntejahr erntejahr = getErntejahr(jahr);
		Flurstueck f = getOrCreateFlurstueck(name, flaeche, betrieb);		
		
		SchlagErntejahr se = new SchlagErntejahr();
		se.setFlurstueck(new Key<Flurstueck>(Flurstueck.class, f.getID()));
		se.setErntejahr(new Key<Erntejahr>(Erntejahr.class, erntejahr.getId()));
		se.setBemerkung(bemerkung);
		se.setFlaeche(flaeche);
		if (vorfrucht != null) {
			se.setVorfrucht(new Key<Kultur>(Kultur.class, vorfrucht.getId()));
		}
		if (anbau != null) {
			se.setAnbauSorte(new Key<Sorte>(Sorte.class, anbau.getId()));
			se.setAnbauKultur(anbau.getKultur());
		}
		
		ofy.put(se);
		
		Schlag s = new Schlag();
		s.setFlurstueck(f);
		s.setSchlagErntejahr(se);
		
		return s;
	}
	
	public Schlag updateSchlag(Schlag s, Betrieb betrieb, String name, double flaeche,
			String bemerkung, int jahr, Sorte anbau, Kultur vorfrucht) 
	{
		Objectify ofy = ObjectifyService.begin();

		Flurstueck f = s.getFlurstueck();
		f.setName(name);
		
		//save Flurstueck entry
		ofy.put(f);
		
		//update values
		SchlagErntejahr se = s.getSchlagErntejahr();
		se.setBemerkung(bemerkung);
		se.setFlaeche(flaeche);

		if (vorfrucht != null) {
			se.setVorfrucht(new Key<Kultur>(Kultur.class, vorfrucht.getId()));
		}
		if (anbau != null) {
			se.setAnbauSorte(new Key<Sorte>(Sorte.class, anbau.getId()));
			se.setAnbauKultur(anbau.getKultur());
		}
		
		//save SchlagErnetejahr entry
		ofy.put(se);
		
		return s;
	}


	/**
	 * Queries the {@link Flurstueck} for the specified name and {@link Betrieb}, if it doesn't exist jet 
	 * a new one is create using the name and flaeche values.
	 * @param name
	 * @param flaeche
	 * @param betrieb 
	 * @return {@link Flurstueck} value
	 */
	private Flurstueck getOrCreateFlurstueck(String name, double flaeche, Betrieb betrieb) {
		Objectify ofy = ObjectifyService.begin();
		Flurstueck f = ofy.query(Flurstueck.class).filter("name", name).filter("betrieb", betrieb).get();
		if (f == null) {
			f = new Flurstueck();
			f.setName(name);
			f.setFlaeche(flaeche);
			f.setBetrieb(new Key<Betrieb>(Betrieb.class, betrieb.getId()));
			ofy.put(f);
		}
		return f;
	}
	
	public Erntejahr loadErntejahr(Key<Erntejahr> key) {
		if (key != null) {
			Objectify ofy = ObjectifyService.begin();
			return ofy.get(key);
		}
		return null;
	}
	
	
	
	/* Aktivitäten */
	
	/**
	 * Loads the list of {@link Aktivitaet} entries for the specified {@link SchlagErntejahr} id.
	 * @param id the {@link SchlagErntejahr} id
	 * @return the list of {@link Aktivitaet} entries
	 */
	public List<Aktivitaet> loadAktivitaetData(Long id) {
		List<Aktivitaet> data = new ArrayList<Aktivitaet>();
		if (id != null) {
			Objectify ofy = ObjectifyService.begin();
			//get SchlagErntejahr for id
			SchlagErntejahr se = ofy.query(SchlagErntejahr.class).filter("id", id).get();
			if (se != null) {
				List<Bodenbearbeitung> bd = loadBodenbearbeitungData(se);
				if (bd != null) {
					data.addAll(bd);
				}
				List<Aussaat> ad = loadAussaatData(se);
				if (ad != null) {
					data.addAll(ad);
				}
				List<Ernte> ed = loadErnteData(se);
				if (ed != null) {
					data.addAll(ed);
				}
				List<Duengung> dd = loadDuengungData(se);
				if (dd != null) {
					data.addAll(dd);
				}
				List<Pflanzenschutz> pd = loadPflanzenschutzData(se);
				if (pd != null) {
					data.addAll(pd);
				}
			}
			
			//ensure that LastModification date is set
			for (Aktivitaet a : data) {
				if (a.getLastModification() == null) {
					//set to now if value not set
					a.setLastModification(new Date());
				}
				
				//is always true if the data comes from the server
				a.setSynchron(true);
			}
		}
		
		//sort by time 
		Collections.sort(data, new Comparator<Aktivitaet>() {
			@Override
			public int compare(Aktivitaet a1, Aktivitaet a2) {
				if (a1 != null && a2 != null) {
					Date d1 = a1.getDatum();
					Date d2 = a2.getDatum();
					if (d1 != null && d2 != null) {
						return d2.compareTo(d1);
					}
				}
				return 0;
			}
		});
		
		
		return data;
	}
	
	/* Bodenbearbeitung */
	
	public List<Bodenbearbeitung> loadBodenbearbeitungData(SchlagErntejahr schlagErntejahr) {
		Objectify ofy = ObjectifyService.begin();
		List<Bodenbearbeitung> data = 
			ofy.query(Bodenbearbeitung.class).filter("schlagErntejahr", schlagErntejahr).order("datum").list();
		
		for (Bodenbearbeitung bodenbearbeitung : data) {
			Key<BodenbearbeitungTyp> k = bodenbearbeitung.getTypKey();
			if (k != null) {
				BodenbearbeitungTyp typ = ofy.find(k);
				bodenbearbeitung.setBodenbearbeitungTyp(typ);
			}
		}
		
		return data;
	}
	
	public Bodenbearbeitung save(Bodenbearbeitung b) {
		Objectify ofy = ObjectifyService.begin();
		Key<Bodenbearbeitung> key = ofy.put(b);
		Bodenbearbeitung result = ofy.find(key);
		
		BodenbearbeitungTyp typ = ofy.find(result.getTypKey());
		result.setBodenbearbeitungTyp(typ);
		
		return result;
	}

	public void delete(Bodenbearbeitung b) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(b);
	}
	
	
	/* Duengung */
	
	public List<Duengung> loadDuengungData(SchlagErntejahr schlagErntejahr) {
		Objectify ofy = ObjectifyService.begin();
		List<Duengung> data = 
			ofy.query(Duengung.class).filter("schlagErntejahr", schlagErntejahr).order("datum").list();
		
		for (Duengung duengung : data) {
			Key<Duengerart> k = duengung.getDuengerartKey();
			if (k != null) {
				Duengerart duengerart = ofy.find(k);
				duengung.setDuengerart(duengerart);
			}
		}
		
		return data;
	}
	
	public Duengung save(Duengung d) {
		Objectify ofy = ObjectifyService.begin();
		Key<Duengung> key = ofy.put(d);
		Duengung result = ofy.find(key);
		
		Duengerart duengerart = ofy.find(result.getDuengerartKey());
		result.setDuengerart(duengerart);
		
		return result;
	}

	public void delete(Duengung d) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(d);
	}
	
	/* Pflanzenschutz */
	
	public List<Pflanzenschutz> loadPflanzenschutzData(SchlagErntejahr schlagErntejahr) {
		Objectify ofy = ObjectifyService.begin();
		List<Pflanzenschutz> data = 
			ofy.query(Pflanzenschutz.class).filter("schlagErntejahr", schlagErntejahr).order("datum").list();
		
		for (Pflanzenschutz ps : data) {
			Key<PSMittel> k = ps.getPsMittelKey();
			if (k != null) {
				PSMittel psMittel = ofy.find(k);
				ps.setPsMittel(psMittel);
			}
		}
		
		return data;
	}
	
	public Pflanzenschutz save(Pflanzenschutz d) {
		Objectify ofy = ObjectifyService.begin();
		Key<Pflanzenschutz> key = ofy.put(d);
		Pflanzenschutz result = ofy.find(key);
		
		PSMittel psMittel = ofy.find(result.getPsMittelKey());
		result.setPsMittel(psMittel);
		
		return result;
	}

	public void delete(Pflanzenschutz d) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(d);
	}
	
	/* Kultur */
	
	public List<Kultur> loadKulturData() {
		Objectify ofy = ObjectifyService.begin();
		List<Kultur> data =	ofy.query(Kultur.class).order("name").list();
		return data;
	}
	
	public Kultur save(Kultur dto) {
		Objectify ofy = ObjectifyService.begin();
		Key<Kultur> key = ofy.put(dto);
		Kultur result = ofy.find(key);
		return result;
	}
	
	public void delete(Kultur dto) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(dto);
	}
	

	/* Sorte */
	
	public List<Sorte> loadSorten() {
		Objectify ofy = ObjectifyService.begin();
		List<Sorte> result = ofy.query(Sorte.class).order("name").list();
		return result;
	}
	
	public List<Sorte> loadSorteData(Key<Kultur> kulturKey) {
		Objectify ofy = ObjectifyService.begin();
		List<Sorte> result = ofy.query(Sorte.class).filter("kultur", kulturKey).list();
		return result;
	}
	
	public Sorte save(Sorte dto, Kultur kultur) {
		Objectify ofy = ObjectifyService.begin();
		
		//set the kultur key
		Key<Kultur> kulturKey = new Key<Kultur>(Kultur.class, kultur.getId());
		dto.setKultur(kulturKey);
		
		Key<Sorte> key = ofy.put(dto);
		Sorte result = ofy.find(key);
		return result;
	}
	
	public void delete(Sorte dto) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(dto);
	}
	
	/* Aussaat */
	
	public List<Aussaat> loadAussaatData(SchlagErntejahr schlagErntejahr) {
		Objectify ofy = ObjectifyService.begin();
		List<Aussaat> data = 
			ofy.query(Aussaat.class).filter("schlagErntejahr", schlagErntejahr).order("datum").list();
		if (schlagErntejahr != null) {
			Key<Sorte> sk = schlagErntejahr.getAnbauSorte();
			Sorte sorte = ofy.find(sk);
			Key<Kultur> kk = schlagErntejahr.getAnbauKultur();
			Kultur kultur = ofy.find(kk);
			
			for (Aussaat dto : data) {
				dto.setSorte(sorte);
				dto.setKultur(kultur);
			}
		}
		
		return data;
	}
	
	public Aussaat save(Aussaat a) {
		Objectify ofy = ObjectifyService.begin();
		Key<Aussaat> key = ofy.put(a);
		Aussaat dto = ofy.find(key);
		
		Key<SchlagErntejahr> sek = a.getSchlagErntejahr();
		SchlagErntejahr schlagErntejahr = ofy.find(sek);
		
		if (schlagErntejahr != null) {
			Key<Sorte> sk = schlagErntejahr.getAnbauSorte();
			Sorte sorte = ofy.find(sk);
			Key<Kultur> kk = schlagErntejahr.getAnbauKultur();
			Kultur kultur = ofy.find(kk);
			dto.setSorte(sorte);
			dto.setKultur(kultur);
		}
		
		
		return dto;
	}
	
	public void delete(Aussaat a) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(a);
	}
	
	
	/* Ernte */
	
	public List<Ernte> loadErnteData(SchlagErntejahr schlagErntejahr) {
		Objectify ofy = ObjectifyService.begin();
		List<Ernte> data = 
			ofy.query(Ernte.class).filter("schlagErntejahr", schlagErntejahr).order("datum").list();
		
		if (schlagErntejahr != null) {
			Key<Sorte> sk = schlagErntejahr.getAnbauSorte();
			Sorte sorte = ofy.find(sk);
			Key<Kultur> kk = schlagErntejahr.getAnbauKultur();
			Kultur kultur = ofy.find(kk);
			
			for (Ernte dto : data) {
				dto.setSorte(sorte);
				dto.setKultur(kultur);
			}
		}
		
		return data;
	}
	
	public Ernte save(Ernte ernte) {
		Objectify ofy = ObjectifyService.begin();
		Key<Ernte> key = ofy.put(ernte);
		Ernte dto = ofy.find(key);
		
		Key<SchlagErntejahr> sek = ernte.getSchlagErntejahr();
		SchlagErntejahr schlagErntejahr = ofy.find(sek);
		
		if (schlagErntejahr != null) {
			Key<Sorte> sk = schlagErntejahr.getAnbauSorte();
			Sorte sorte = ofy.find(sk);
			Key<Kultur> kk = schlagErntejahr.getAnbauKultur();
			Kultur kultur = ofy.find(kk);
			dto.setSorte(sorte);
			dto.setKultur(kultur);
		}
		
		
		return dto;
	}
	
	public void delete(Ernte ernte) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(ernte);
	}


	/**
	 * Get the Kultur entry for the specified key.
	 * @param key the kultur key, must not be null
	 * @return the found Kultur entry, null if nothing found
	 */
	public Kultur getKultur(Key<Kultur> key) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.find(key);
	}


	/**
	 * Get the Sorte entry for the specified key.
	 * @param key the sorte key, must not be null
	 * @return the sound sorte entry, null of nothing found
	 */
	public Sorte getSorte(Key<Sorte> key) {
		Objectify ofy = ObjectifyService.begin();
		return ofy.find(key);
	}


	public Aktivitaet getAktivitaet(Long id) {
		Objectify ofy = ObjectifyService.begin();
		
		//unfortunately we need to request the concrete aktivitaet subtype to get it loaded
		//so load the particular subtypes till we found the entry 
		
		Key<Aussaat> keya = new Key<Aussaat>(Aussaat.class, id);
		Aktivitaet akt = ofy.find(keya);
		if (akt != null) return akt;
		
		Key<Bodenbearbeitung> keyb = new Key<Bodenbearbeitung>(Bodenbearbeitung.class, id);
		akt = ofy.find(keyb);
		if (akt != null) return akt;
		
		Key<Duengung> keyd = new Key<Duengung>(Duengung.class, id);
		akt = ofy.find(keyd);
		if (akt != null) return akt;
		
		Key<Pflanzenschutz> keyp = new Key<Pflanzenschutz>(Pflanzenschutz.class, id);
		akt = ofy.find(keyp);
		if (akt != null) return akt;
		
		Key<Ernte> keye = new Key<Ernte>(Ernte.class, id);
		akt = ofy.find(keye);
		if (akt != null) return akt;
		
		return null;
	}


	/**
	 * Delete {@link Aktivitaet} for specified ID.
	 * @param id the id of the item to delete
	 */
	public void deleteAktivitaet(Long id) {
		Objectify ofy = ObjectifyService.begin();
		
		//TODO use concrete Aktivitaet type
		ofy.delete(Aktivitaet.class, id);
	}


	/**
	 * Store {@link Aktivitaet} using synchronization mechanism.
	 * @param aktivitaet item to update
	 */
	public void store(Aktivitaet aktivitaet) {
		System.out.println("store aktivitaet entry:" + aktivitaet);

		// TODO sync mechanismus umsetzen fallse benötigt
		//momentan ist verhalten so dass das letzte update gewinnt
		//if ID is set (entry already exists) ensure that the modification date of the 
		//changed item is newer than the last modification date of the persisted entry
		
//		if (aktivitaet.getId() == null) {
			if (aktivitaet instanceof Bodenbearbeitung) {
				save((Bodenbearbeitung) aktivitaet);
			}
			else if (aktivitaet instanceof Aussaat) {
				save((Aussaat) aktivitaet);
			}
			else if (aktivitaet instanceof Duengung) {
				save((Duengung) aktivitaet);
			}
			else if (aktivitaet instanceof Ernte) {
				save((Ernte) aktivitaet);
			}
			else if (aktivitaet instanceof Pflanzenschutz) {
				save((Pflanzenschutz) aktivitaet);
			}
//		} 
	}


	/**
	 * Assignes the specified blobKey to the given refid.
	 * Multiple blob keys can be assigned to one reference id.
	 * 
	 * @param refId the reference id, must not be null
	 * @param blobKey the blob key, must not be null
	 */
	public void assignBlob(String refId, String blobKey) {
		if (refId == null || blobKey == null) {
			return;
		}
		
		//get aktivitaet entry for the reference id
		Aktivitaet a = getAktivitaet(Long.parseLong(refId));
		//add the blob key
		String desc = "";
		a.getAttachments().add(new ImageResource(blobKey, new Date(), desc));
		//save aktivitaet entry
		store(a);
	}


	/**
	 * Deletes the resource for the specified key which is 
	 * assigned to the aktivitaet with the specified id.
	 *  
	 * @param id if of aktivitaet entry
	 * @param resKey resource key
	 */
	public void deleteResource(Long id, String resKey) {
		if (id != null && resKey != null) {
			//get aktivitaet entry for the reference id
			Aktivitaet a = getAktivitaet(id);
			
			//now find the resource item and remove it from list
			List<ImageResource> atts = a.getAttachments();
			ImageResource toRemove = null;
			for (ImageResource att : atts) {
				if (att != null && resKey.equals(att.getBlobKey())) {
					toRemove = att;
					break;
				}
			}
			if (toRemove != null) {
				//remove the found attachment
				a.getAttachments().remove(toRemove);
				
				//save aktivitaet entry
				store(a);
				
				//finally remove the blob
				BlobKey blobKey = new BlobKey(resKey);
				BlobstoreServiceFactory.getBlobstoreService().delete(blobKey);
			}			
		}
	}


	/**
	 * Sets the current Erntejahr of the current logged in user to the specified value.
	 * 
	 * @param erntejahr the new current erntejahr
	 */
	public void selectCurrentErntejahr(int erntejahr) {
		Account user = getCurrentUserAccount();
		if (user != null) {
			int oldEJ = user.getCurrentErntejahr();
			//update erntejahr value
			user.setCurrentErntejahr(erntejahr);
			//and persist changes
			save(user);
			
			//only if new erntejahr == current erntejahr+1
			//for convinience copy the schlag entries (whithout any activity payload of course)
			//from the previous erntejahr. But only if the new current erntejahr do not contain any schlag entries.
			if (erntejahr == oldEJ+1) {
				Betrieb betrieb = getBetriebForAccount(user);
				Erntejahr ej = getErntejahr(erntejahr);
				List<Schlag> schlagData = loadSchlagData(ej, betrieb);
				if (schlagData == null || schlagData.size() < 1) {
					List<Schlag> oldSchlagData = loadSchlagData(getErntejahr(oldEJ), betrieb);
					for (Schlag oldSchlag : oldSchlagData) {
						SchlagErntejahr oldSE = oldSchlag.getSchlagErntejahr(); 
						
						Sorte anbau = getSorte(oldSE.getAnbauSorte());
						Kultur vorfrucht = getKultur(oldSE.getVorfrucht());
						//save new schlag using the values from the old schlag
						saveNewSchlag(betrieb, 
								oldSchlag.getFlurstueck().getName(), 
								oldSE.getFlaeche(), 
								oldSE.getBemerkung(), 
								erntejahr, 
								anbau, 
								vorfrucht);
					}
				}
			}
			
		} else {
			throw new RuntimeException("Error: The current User must not be null.");
		}
	}


	/**
	 * Deletes the specified {@link Schlag}.
	 * @param s the schlag to delete
	 */
	public void delete(Schlag s) {
		//TODO
	}
}
