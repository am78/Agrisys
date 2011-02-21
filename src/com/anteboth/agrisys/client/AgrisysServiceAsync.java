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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.googlecode.objectify.Key;

/**
 * The async counterpart of <code>AgrisysService</code>.
 */
public interface AgrisysServiceAsync {

	void getSessionName(AsyncCallback<String> callback);
	void authenticate(String username, String password, AsyncCallback<UserDataTO> callback);
	void isUserAuthenticated(AsyncCallback<UserDataTO> callback);


	void loadBodenbearbeitungTypen(AsyncCallback<List<BodenbearbeitungTyp>> callback);
	void save(BodenbearbeitungTyp typ, AsyncCallback<BodenbearbeitungTyp> callback);
	void delete(BodenbearbeitungTyp typ, AsyncCallback<Void> callback);

	void loadPSMittel(AsyncCallback<List<PSMittel>> callback);
	void save(PSMittel dto, AsyncCallback<PSMittel> callback);
	void delete(PSMittel dto, AsyncCallback<Void> callback);
	
	void loadDuengerart(AsyncCallback<List<Duengerart>> callback);
	void save(Duengerart dto, AsyncCallback<Duengerart> callback);
	void delete(Duengerart dto, AsyncCallback<Void> callback);
	
	void loadSchlagData(Erntejahr erntejahr, Betrieb betrieb, AsyncCallback<List<Schlag>> callback);
	void loadErntejahrData(AsyncCallback<List<Erntejahr>> callback);
	void saveNewSchlag(Betrieb betrieb, String name, double flaeche, String bemerkung,
			int erntejahr, Sorte anbau, Kultur vorfrucht, AsyncCallback<Schlag> callback);
	void updateSchlag(Schlag s, Betrieb betrieb, String name, double flaeche, String bemerkung,
			int erntejahr, Sorte anbau, Kultur vorfrucht, AsyncCallback<Schlag> callback);

	
	void loadErntejahr(Key<Erntejahr> key, AsyncCallback<Erntejahr> callback);
	
	void loadBodenbearbeitungData(SchlagErntejahr schlagErntejahr, AsyncCallback<List<Bodenbearbeitung>> asyncCallback);
	void save(Bodenbearbeitung b, AsyncCallback<Bodenbearbeitung> asyncCallback);
	void delete(Bodenbearbeitung b, AsyncCallback<Void> asyncCallback);
	
	void loadKulturData(AsyncCallback<List<Kultur>> asyncCallback);
	void save(Kultur dto, AsyncCallback<Kultur> callback);
	void delete(Kultur dto, AsyncCallback<Void> callback);

	void loadSorteData(Key<Kultur> kultur, AsyncCallback<List<Sorte>> asyncCallback);
	void save(Sorte dto, Kultur kultur, AsyncCallback<Sorte> callback);
	void delete(Sorte dto, AsyncCallback<Void> callback);
	
	
	void loadAussaatData(SchlagErntejahr schlagErntejahr, AsyncCallback<List<Aussaat>> asyncCallback);
	void delete(Aussaat dto, AsyncCallback<Void> callback);
	void save(Aussaat dto, AsyncCallback<Aussaat> asyncCallback);
	
	void loadErnteData(SchlagErntejahr schlagErntejahr, AsyncCallback<List<Ernte>> asyncCallback);
	void delete(Ernte dto, AsyncCallback<Void> callback);
	void save(Ernte dto, AsyncCallback<Ernte> asyncCallback);
	
	void save(Duengung b, AsyncCallback<Duengung> asyncCallback);
	void delete(Duengung b, AsyncCallback<Void> callback);
	void loadDuengungData(SchlagErntejahr schlagErntejahr, AsyncCallback<List<Duengung>> asyncCallback);
	void save(Pflanzenschutz d, AsyncCallback<Pflanzenschutz> callback);
	void delete(Pflanzenschutz d, AsyncCallback<Void> callback);
	void loadPflanzenschutzData(SchlagErntejahr schlagErntejahr,
			AsyncCallback<List<Pflanzenschutz>> callback);
}
