package com.anteboth.agrisys.client;

import java.util.List;

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
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.googlecode.objectify.Key;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("agrisys")
public interface AgrisysService extends RemoteService {
	
	/* User Management */
	
	/**
	 * Returns the current session name.
	 * @return
	 */
	String getSessionName();
	
	/**
	 * Tries to authenticate the user with the obtained credentials.
	 * @param username
	 * @param password
	 * @return {@link UserDataTO} if the user could be logged in, null otherwise
	 */
	UserDataTO authenticate(String username, String password);
	
	/**
	 * Returns the proper {@link UserDataTO} if the user is logged. Null otherwise.
	 * @return
	 */
	UserDataTO isUserAuthenticated();
	
	/* Stammdaten */
	
	/**
	 * Loads all {@link BodenbearbeitungTyp} items.
	 * @return
	 */
	List<BodenbearbeitungTyp> loadBodenbearbeitungTypen();
	
	/**
	 * Saves a {@link BodenbearbeitungTyp} item.
	 * @param typ
	 * @return
	 */
	BodenbearbeitungTyp save(BodenbearbeitungTyp typ);
	
	/**
	 * Deletes a {@link BodenbearbeitungTyp} item.
	 * @param typ
	 */
	void delete(BodenbearbeitungTyp typ);
	
	
	/**
	 * Returns all {@link PSMittel} items.
	 * @return
	 */
	List<PSMittel> loadPSMittel();
	
	/**
	 * Save a {@link PSMittel} item.
	 * @param dto
	 * @return
	 */
	PSMittel save(PSMittel dto);
	
	/**
	 * Deletes a {@link PSMittel} item.
	 * @param dto
	 */
	void delete(PSMittel dto);
	
	/**
	 * Loads all {@link Duengerart} items.
	 * @return
	 */
	List<Duengerart> loadDuengerart();
	
	/**
	 * Save a {@link Duengerart} item
	 * @param dto
	 * @return
	 */
	Duengerart save(Duengerart dto);
	
	/**
	 * Deletes a {@link Duengerart} item.
	 * @param dto
	 */
	void delete(Duengerart dto);
	
	/**
	 * Loads all {@link Erntejahr} entries.
	 * @return
	 */
	List<Erntejahr> loadErntejahrData();
	
	/* Schlagverwaltung */
	
	/**
	 * Loads all {@link Schlag} items for the specified {@link Erntejahr} and {@link Betrieb}.
	 * @param erntejahr
	 * @param betrieb
	 * @return
	 */
	List<Schlag> loadSchlagData(Erntejahr erntejahr, Betrieb betrieb);
	
	/**
	 * Saves a new {@link Schlag} item for the specified {@link Betrieb}.
	 * @param betrieb
	 * @param name
	 * @param flaeche
	 * @param bemerkung
	 * @param erntejahr
	 * @param anbau
	 * @param vorfrucht
	 * @return
	 */
	Schlag saveNewSchlag(Betrieb betrieb, String name, double flaeche, String bemerkung, int erntejahr, 
			Sorte anbau, Kultur vorfrucht);
	
	/**
	 * Updates a {@link Schlag} item with the specified values.
	 * @param s
	 * @param betrieb
	 * @param name
	 * @param flaeche
	 * @param bemerkung
	 * @param erntejahr
	 * @param anbau
	 * @param vorfrucht
	 * @return
	 */
	Schlag updateSchlag(Schlag s, Betrieb betrieb, String name, double flaeche,
			String bemerkung, int erntejahr, Sorte anbau, Kultur vorfrucht);

	
	/**
	 * Loads the {@link Erntejahr} item for the specified key.
	 * @param key
	 * @return
	 */
	Erntejahr loadErntejahr(Key<Erntejahr> key);

	/**
	 * Loads the assigned {@link Bodenbearbeitung} entries for the specified {@link SchlagErntejahr}.
	 * @param schlagErntejahr the {@link SchlagErntejahr} item
	 * @return the assigned {@link Bodenbearbeitung} values 
	 */
	List<Bodenbearbeitung> loadBodenbearbeitungData(SchlagErntejahr schlagErntejahr);

	/**
	 * Saves a new {@link Bodenbearbeitung} item.
	 * @param b the {@link Bodenbearbeitung} item to save
	 * @return the saved {@link Bodenbearbeitung} item
	 */
	Bodenbearbeitung save(Bodenbearbeitung b);

	/**
	 * Deletes a {@link Bodenbearbeitung} item.
	 * @param b the value to delete
	 */
	void delete(Bodenbearbeitung b);

	/**
	 * Loads all existing Kultur data items.
	 * @return
	 */
	List<Kultur> loadKulturData();

	/**
	 * Saves the obtained Kultur item.
	 * @param dto
	 * @return
	 */
	Kultur save(Kultur dto);

	/**
	 * Deletes the obtained Kultur item.
	 * @param dto
	 */
	void delete(Kultur dto);

	/**
	 * Loads the Sorte-Stammdaten entries.
	 * @return
	 */
	List<Sorte> loadSorteData(Key<Kultur> kultur);

	/**
	 * Saves the specified Sorte-Stammdaten entire for the given kultur item.
	 * @param dto
	 * @param kultur
	 * @return
	 */
	Sorte save(Sorte dto, Kultur kultur);

	/**
	 * Deletes the specified Sorte-Stammdaten entry.
	 * @param dto the item to delete
	 */
	void delete(Sorte dto);

	
	/**
	 * Loads the {@link Aussaat} entries for the obtained {@link SchlagErntejahr} value.
	 * @param schlagErntejahr
	 * @return list of Aussaat items
	 */
	List<Aussaat> loadAussaatData(SchlagErntejahr schlagErntejahr);

	/**
	 * Deletes the obtained {@link Aussaat} item.
	 * @param dto the item to delete
	 */
	void delete(Aussaat dto);

	/**
	 * Saves/Updates the obtained {@link Aussaat} item.
	 * @param dto item to save
	 * @return the reloaded Aussaat item
	 */
	Aussaat save(Aussaat dto);

	
	/**
	 * Loads the {@link Ernte} entries for the obtained {@link SchlagErntejahr} item.
	 * @param schlagErntejahr the {@link SchlagErntejahr} item
	 * @return the list of {@link Ernte} items
	 */
	List<Ernte> loadErnteData(SchlagErntejahr schlagErntejahr);

	/**
	 * Deletes the obtained {@link Ernte} item.
	 * @param dto value to delete
	 */
	void delete(Ernte dto);

	/**
	 * Saves the obtained {@link Ernte} item.
	 * @param dto the {@link Ernte} item
	 * @return the saved item
	 */
	Ernte save(Ernte dto);

	/**
	 * Saves/updates the specified {@link Duengung} item.
	 * @param d the item to save
	 * @return the saved item
	 */
	Duengung save(Duengung d);

	/**
	 * Deletes the specified {@link Duengung} from the data store.
	 * @param d the item to delete
	 */
	void delete(Duengung d);

	/**
	 * Loads the list of {@link Duengung} items for the obtained {@link SchlagErntejahr} item.
	 * 
	 * @param schlagErntejahr load {@link Duengung} items for this entry
	 * @return a list of {@link Duengung}
	 */
	List<Duengung> loadDuengungData(SchlagErntejahr schlagErntejahr);
	
	/**
	 * Saves/updates the specified {@link Pflanzenschutz} item.
	 * @param d the item to save
	 * @return the saved item
	 */
	Pflanzenschutz save(Pflanzenschutz d);

	/**
	 * Deletes the specified {@link Pflanzenschutz} from the data store.
	 * @param d the item to delete
	 */
	void delete(Pflanzenschutz d);

	/**
	 * Loads the list of {@link Pflanzenschutz} items for the obtained {@link SchlagErntejahr} item.
	 * 
	 * @param schlagErntejahr load {@link Pflanzenschutz} items for this entry
	 * @return a list of {@link Duengung}
	 */
	List<Pflanzenschutz> loadPflanzenschutzData(SchlagErntejahr schlagErntejahr);
	
}
