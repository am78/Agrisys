package com.anteboth.agrisys.server;

import java.util.List;

import com.anteboth.agrisys.client.AgrisysService;
import com.anteboth.agrisys.client.model.Account;
import com.anteboth.agrisys.client.model.Aktivitaet;
import com.anteboth.agrisys.client.model.Aussaat;
import com.anteboth.agrisys.client.model.Betrieb;
import com.anteboth.agrisys.client.model.Bodenbearbeitung;
import com.anteboth.agrisys.client.model.Duengung;
import com.anteboth.agrisys.client.model.Ernte;
import com.anteboth.agrisys.client.model.Erntejahr;
import com.anteboth.agrisys.client.model.Pflanzenschutz;
import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.model.UserDataTO;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.Duengerart;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.PSMittel;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
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
	
	private ServiceManager serviceManager;
	
	/**
	 * Construtor.
	 */
	public AgrisysServiceImpl() {

		this.serviceManager = ServiceManager.getInstance();

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
				to.setErntejahr(this.serviceManager.getErntejahr(this.serviceManager.getCurrentErntejahr()));
				to.setBetrieb(betrieb);				
				
				return to;
			} 
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return null;
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
			account = this.serviceManager.getOrCreateAccount(email, "", email, "Betrieb von " + email );
		}
		else {
			return null;
		}
		
		
		Betrieb betrieb = getBetriebForAccount(account); 
		
		UserDataTO to = new UserDataTO();
		to.setAccount(account);
		to.setErntejahr(this.serviceManager.getErntejahr(this.serviceManager.getCurrentErntejahr()));
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


	public void delete(Aussaat a) {
		serviceManager.delete(a);
	}


	public void delete(Bodenbearbeitung b) {
		serviceManager.delete(b);
	}


	public void delete(BodenbearbeitungTyp typ) {
		serviceManager.delete(typ);
	}


	public void delete(Duengerart dto) {
		serviceManager.delete(dto);
	}


	public void delete(Ernte ernte) {
		serviceManager.delete(ernte);
	}


	public void delete(Kultur dto) {
		serviceManager.delete(dto);
	}


	public void delete(PSMittel dto) {
		serviceManager.delete(dto);
	}


	public void delete(Sorte dto) {
		serviceManager.delete(dto);
	}


	public int getCurrentErntejahr() {
		return serviceManager.getCurrentErntejahr();
	}


	public List<Aussaat> loadAussaatData(SchlagErntejahr schlagErntejahr) {
		return serviceManager.loadAussaatData(schlagErntejahr);
	}


	public List<Bodenbearbeitung> loadBodenbearbeitungData(
			SchlagErntejahr schlagErntejahr) {
		return serviceManager.loadBodenbearbeitungData(schlagErntejahr);
	}


	public List<BodenbearbeitungTyp> loadBodenbearbeitungTypen() {
		return serviceManager.loadBodenbearbeitungTypen();
	}


	public List<Duengerart> loadDuengerart() {
		return serviceManager.loadDuengerart();
	}


	public List<Ernte> loadErnteData(SchlagErntejahr schlagErntejahr) {
		return serviceManager.loadErnteData(schlagErntejahr);
	}


	public Erntejahr loadErntejahr(Key<Erntejahr> key) {
		return serviceManager.loadErntejahr(key);
	}


	public List<Erntejahr> loadErntejahrData() {
		return serviceManager.loadErntejahrData();
	}
	
	@Override
	public void selectCurrentErntejahr(int erntejahr) {
		serviceManager.selectCurrentErntejahr(erntejahr);
	}


	public List<Kultur> loadKulturData() {
		return serviceManager.loadKulturData();
	}


	public List<PSMittel> loadPSMittel() {
		return serviceManager.loadPSMittel();
	}


	public List<Schlag> loadSchlagData(Erntejahr erntejahr, Betrieb betrieb) {
		return serviceManager.loadSchlagData(erntejahr, betrieb);
	}


	public List<Sorte> loadSorteData(Key<Kultur> kulturKey) {
		return serviceManager.loadSorteData(kulturKey);
	}


	public Aussaat save(Aussaat a) {
		return serviceManager.save(a);
	}


	public Bodenbearbeitung save(Bodenbearbeitung b) {
		return serviceManager.save(b);
	}


	public BodenbearbeitungTyp save(BodenbearbeitungTyp typ) {
		return serviceManager.save(typ);
	}


	public Duengerart save(Duengerart dto) {
		return serviceManager.save(dto);
	}


	public Ernte save(Ernte ernte) {
		return serviceManager.save(ernte);
	}


	public Kultur save(Kultur dto) {
		return serviceManager.save(dto);
	}


	public PSMittel save(PSMittel dto) {
		return serviceManager.save(dto);
	}


	public Sorte save(Sorte dto, Kultur kultur) {
		return serviceManager.save(dto, kultur);
	}


	public Schlag saveNewSchlag(Betrieb betrieb, String name, double flaeche,
			String bemerkung, int jahr, Sorte anbau, Kultur vorfrucht) {
		return serviceManager.saveNewSchlag(betrieb, name, flaeche, bemerkung,
				jahr, anbau, vorfrucht);
	}
	
	
	@Override
	public void delete(Schlag s) {
		serviceManager.delete(s);
	}


	public Schlag updateSchlag(Schlag s, Betrieb betrieb, String name,
			double flaeche, String bemerkung, int jahr, Sorte anbau,
			Kultur vorfrucht) {
		return serviceManager.updateSchlag(s, betrieb, name, flaeche,
				bemerkung, jahr, anbau, vorfrucht);
	}


	@Override
	public List<Duengung> loadDuengungData(SchlagErntejahr schlagErntejahr) {
		return this.serviceManager.loadDuengungData(schlagErntejahr);
	}
	
	@Override
	public Duengung save(Duengung d) {		
		return this.serviceManager.save(d);
	}
	
	@Override
	public void delete(Duengung d) {
		this.serviceManager.delete(d);
	}
	
	@Override
	public List<Pflanzenschutz> loadPflanzenschutzData(SchlagErntejahr schlagErntejahr) {
		return this.serviceManager.loadPflanzenschutzData(schlagErntejahr);
	}
	
	@Override
	public Pflanzenschutz save(Pflanzenschutz ps) {		
		return this.serviceManager.save(ps);
	}
	
	@Override
	public void delete(Pflanzenschutz ps) {
		this.serviceManager.delete(ps);
	}
	
	@Override
	public void deleteResource(Long id, String resKey) {
		this.serviceManager.deleteResource(id, resKey);
	}
	
	@Override
	public Aktivitaet loadAktivitaet(Long id) {
		return this.serviceManager.getAktivitaet(id);
	}

	@Override
	public String getBlobstoreUploadUrl() {
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		return blobstoreService.createUploadUrl("/upload");
	}
}
