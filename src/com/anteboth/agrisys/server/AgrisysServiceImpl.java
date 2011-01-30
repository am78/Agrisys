package com.anteboth.agrisys.server;

import java.util.ArrayList;
import java.util.List;

import com.anteboth.agrisys.client.AgrisysService;
import com.anteboth.agrisys.client.model.Account;
import com.anteboth.agrisys.client.model.Aussaat;
import com.anteboth.agrisys.client.model.Betrieb;
import com.anteboth.agrisys.client.model.Bodenbearbeitung;
import com.anteboth.agrisys.client.model.Ernte;
import com.anteboth.agrisys.client.model.Erntejahr;
import com.anteboth.agrisys.client.model.Flurstueck;
import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.model.UserDataTO;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.Duengerart;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.PSMittel;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.Query;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class AgrisysServiceImpl extends RemoteServiceServlet implements AgrisysService {
	
	private static final int ACT_ERNTEJAHR = 2011;
	
	
	/**
	 * Construtor.
	 */
	public AgrisysServiceImpl() {
		//register objectify entities
		ObjectifyService.register(Aussaat.class);
		ObjectifyService.register(Account.class);
		ObjectifyService.register(Betrieb.class);
		ObjectifyService.register(Bodenbearbeitung.class);
		ObjectifyService.register(Erntejahr.class);
		ObjectifyService.register(Ernte.class);
		ObjectifyService.register(Flurstueck.class);
		ObjectifyService.register(SchlagErntejahr.class);
		
		ObjectifyService.register(BodenbearbeitungTyp.class);
		ObjectifyService.register(Duengerart.class);
		ObjectifyService.register(Kultur.class);
		ObjectifyService.register(PSMittel.class);
		ObjectifyService.register(Sorte.class);

		//ensure that the basic data exists
		createBaseData();
	}
	

	private void createBaseData() {
		//insert testuser if not exists
//		createAccount("user", "pass");
//		createAccount("admin", "agrisysadmin");
//		createAccount("user2", "pass");
		
		Objectify ofy = ObjectifyService.begin();

		/* create Erntjahr entries if needed */		
		Query<Erntejahr> query = ofy.query(Erntejahr.class);
		if (query.count() < 1){
			for (int i=2005; i < 2021; i++) {
				Erntejahr erntejahr = new Erntejahr();
				erntejahr.setErntejahr(i);
				ofy.put(erntejahr);
			}
		}
	}


	/**
	 * Retrieves/Creates an account item in datastore if the email is not yet existing.
	 * @param name the account user name
	 * @param pass the account password
	 * @param email the email address, must not be null, is unique for each user
	 */
	private Account getOrCreateAccount(String name, String pass, String email, String betriebName) {
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


	/* (non-Javadoc)
	 * @see com.anteboth.agrisys.client.AgrisysService#getSessionName()
	 */
	public String getSessionName() {
		Account currentAccount = (Account) super.getThreadLocalRequest().getSession().getAttribute(WebConstants.CURRENT_ACCOUNT); 
		return currentAccount.getUsername();
	}

	/* (non-Javadoc)
	 * @see com.anteboth.agrisys.client.AgrisysService#authenticate(java.lang.String, java.lang.String)
	 */
	public UserDataTO authenticate(String username, String password) {
		try {
			Account account = findByAuthenticatinon(username, password);
				//ServiceFactory().getAccountService().findByAuthenticatinon(username, password);
			if (account != null) {
				this.getThreadLocalRequest().getSession().setAttribute(WebConstants.CURRENT_ACCOUNT,account);
				
				Betrieb betrieb = getBetriebForAccount(account); 
				
				UserDataTO to = new UserDataTO();
				to.setAccount(account);
				to.setErntejahr(getErntejahr(ACT_ERNTEJAHR));
				to.setBetrieb(betrieb);				
				
				return to;
			} 
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
	}

	
	/**
	 * Returns the {@link Erntejahr} item for the specified jahr.
	 * @param jahr get {@link Erntejahr} for this value
	 * @return {@link Erntejahr} item
	 */
	private Erntejahr getErntejahr(int jahr) {
		Objectify ofy = ObjectifyService.begin();
		Erntejahr erntejahr = ofy.query(Erntejahr.class).filter("erntejahr", jahr).get();
		return erntejahr;
	}


	/**
	 * Lookup user in data store and check the obtained password.
	 * @param username the username to lookup
	 * @param password password to check
	 * @return Account entity for the user if the password was correct
	 */
	private Account findByAuthenticatinon(String username, String password) {
		
		//username and password required
		if (username == null || password == null) return null; 
		
		Objectify ofy = ObjectifyService.begin();
		
		//lookup for account entry for the given username
		Account a = ofy.query(Account.class).filter("username", username) .get();
		if (a != null) {
			//check the password
			if (password.equals(a.getPassword())) {
				return a; //return account item if password is correct
			} else {
				//return null if passwd isn't correct
				return null;
			}
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.anteboth.agrisys.client.AgrisysService#isUserAuthenticated()
	 */
	public UserDataTO isUserAuthenticated() {
		
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		
		Account account = null;
		if (user != null) {
			String email = user.getEmail();
			account = getOrCreateAccount(email, "", email, "Betrieb von " + email );
		}
		else {
			return null;
		}
		
		
		Betrieb betrieb = getBetriebForAccount(account); 
		
		UserDataTO to = new UserDataTO();
		to.setAccount(account);
		to.setErntejahr(getErntejahr(ACT_ERNTEJAHR));
		to.setBetrieb(betrieb);
		return to;
	}

	/**
	 * Get berieb for account.
	 * 
	 * @param account the manager account 
	 * @return Betrieb
	 */
	private Betrieb getBetriebForAccount(Account account) {
		if (account == null) {
			return null;
		}
		
		Objectify ofy = ObjectifyService.begin();
		//get betrieb for account
		Betrieb b = ofy.query(Betrieb.class).filter("manager", account).get();
		return b;
	}


	/* Stammdaten */

	@Override
	public List<BodenbearbeitungTyp> loadBodenbearbeitungTypen() {
		
		List<BodenbearbeitungTyp> result = new ArrayList<BodenbearbeitungTyp>();
		//load the data
		Objectify ofy = ObjectifyService.begin();
		Query<BodenbearbeitungTyp> query = ofy.query(BodenbearbeitungTyp.class);
		
		//add loaded data to result
		for (BodenbearbeitungTyp t : query) {
			result.add(t); 
		}
		
		return result;
	}
	
	@Override
	public BodenbearbeitungTyp save(BodenbearbeitungTyp typ) {
		if (typ != null) {
			Objectify ofy = ObjectifyService.begin();
			Key<BodenbearbeitungTyp> key = ofy.put(typ);
			typ = ofy.get(key);
		}
		return typ;
	}
	
	
	@Override
	public void delete(BodenbearbeitungTyp typ) {
		if (typ != null) {
			Objectify ofy = ObjectifyService.begin();
			ofy.delete(typ);
		}
	}
	
	
	@Override
	public List<PSMittel> loadPSMittel() {
		
		List<PSMittel> result = new ArrayList<PSMittel>();
		//load the data
		Objectify ofy = ObjectifyService.begin();
		Query<PSMittel> query = ofy.query(PSMittel.class);
		
		//add loaded data to result
		for (PSMittel dto : query) {
			result.add(dto); 
		}
		
		return result;
	}
	
	@Override
	public PSMittel save(PSMittel dto) {
		if (dto != null) {
			Objectify ofy = ObjectifyService.begin();
			Key<PSMittel> key = ofy.put(dto);
			dto = ofy.get(key);
		}
		return dto;
	}
	
	
	@Override
	public void delete(PSMittel dto) {
		if (dto != null) {
			Objectify ofy = ObjectifyService.begin();
			ofy.delete(dto);
		}
	}
	
	@Override
	public List<Duengerart> loadDuengerart() {
		
		List<Duengerart> result = new ArrayList<Duengerart>();
		//load the data
		Objectify ofy = ObjectifyService.begin();
		Query<Duengerart> query = ofy.query(Duengerart.class);
		
		//add loaded data to result
		for (Duengerart dto : query) {
			result.add(dto); 
		}
		
		return result;
	}
	
	@Override
	public Duengerart save(Duengerart dto) {
		if (dto != null) {
			Objectify ofy = ObjectifyService.begin();
			Key<Duengerart> key = ofy.put(dto);
			dto = ofy.get(key);
		}
		return dto;
	}
	
	
	@Override
	public void delete(Duengerart dto) {
		if (dto != null) {
			Objectify ofy = ObjectifyService.begin();
			ofy.delete(dto);
		}
	}
	
	@Override
	public List<Erntejahr> loadErntejahrData() {
		Objectify ofy = ObjectifyService.begin();
		List<Erntejahr> data = ofy.query(Erntejahr.class).order("erntejahr").list();
		return data;
	}
	
	/* Schlagverwaltung */
	
	@Override
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
	
	@Override
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
	
	@Override
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
	
	@Override
	public Erntejahr loadErntejahr(Key<Erntejahr> key) {
		if (key != null) {
			Objectify ofy = ObjectifyService.begin();
			return ofy.get(key);
		}
		return null;
	}
	
	/* Bodenbearbeitung */
	
	@Override
	public List<Bodenbearbeitung> loadBodenbearbeitungData(SchlagErntejahr schlagErntejahr) {
		Objectify ofy = ObjectifyService.begin();
		List<Bodenbearbeitung> data = 
			ofy.query(Bodenbearbeitung.class).filter("schlagErntejahr", schlagErntejahr).list();
		
		for (Bodenbearbeitung bodenbearbeitung : data) {
			Key<BodenbearbeitungTyp> k = bodenbearbeitung.getTypKey();
			if (k != null) {
				BodenbearbeitungTyp typ = ofy.find(k);
				bodenbearbeitung.setBodenbearbeitungTyp(typ);
			}
		}
		
		return data;
	}
	
	@Override
	public Bodenbearbeitung save(Bodenbearbeitung b) {
		Objectify ofy = ObjectifyService.begin();
		Key<Bodenbearbeitung> key = ofy.put(b);
		Bodenbearbeitung result = ofy.find(key);
		
		BodenbearbeitungTyp typ = ofy.find(result.getTypKey());
		result.setBodenbearbeitungTyp(typ);
		
		return result;
	}
	
	@Override
	public void delete(Bodenbearbeitung b) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(b);
	}
	
	/* Kultur */
	
	@Override
	public List<Kultur> loadKulturData() {
		Objectify ofy = ObjectifyService.begin();
		List<Kultur> data =	ofy.query(Kultur.class).list();
		return data;
	}
	
	@Override
	public Kultur save(Kultur dto) {
		Objectify ofy = ObjectifyService.begin();
		Key<Kultur> key = ofy.put(dto);
		Kultur result = ofy.find(key);
		return result;
	}
	
	@Override
	public void delete(Kultur dto) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(dto);
	}
	

	/* Sorte */
	
	@Override
	public List<Sorte> loadSorteData(Key<Kultur> kulturKey) {
		Objectify ofy = ObjectifyService.begin();
		List<Sorte> result = ofy.query(Sorte.class).filter("kultur", kulturKey).list();
		return result;
	}
	
	@Override
	public Sorte save(Sorte dto, Kultur kultur) {
		Objectify ofy = ObjectifyService.begin();
		
		//set the kultur key
		Key<Kultur> kulturKey = new Key<Kultur>(Kultur.class, kultur.getId());
		dto.setKultur(kulturKey);
		
		Key<Sorte> key = ofy.put(dto);
		Sorte result = ofy.find(key);
		return result;
	}
	
	@Override
	public void delete(Sorte dto) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(dto);
	}
	
	/* Aussaat */
	
	@Override
	public List<Aussaat> loadAussaatData(SchlagErntejahr schlagErntejahr) {
		Objectify ofy = ObjectifyService.begin();
		List<Aussaat> data = 
			ofy.query(Aussaat.class).filter("schlagErntejahr", schlagErntejahr).list();
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
	
	@Override
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
	
	@Override
	public void delete(Aussaat a) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(a);
	}
	
	
	/* Ernte */
	
	@Override
	public List<Ernte> loadErnteData(SchlagErntejahr schlagErntejahr) {
		Objectify ofy = ObjectifyService.begin();
		List<Ernte> data = 
			ofy.query(Ernte.class).filter("schlagErntejahr", schlagErntejahr).list();
		
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
	
	@Override
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
	
	@Override
	public void delete(Ernte ernte) {
		Objectify ofy = ObjectifyService.begin();
		ofy.delete(ernte);
	}
	
}
