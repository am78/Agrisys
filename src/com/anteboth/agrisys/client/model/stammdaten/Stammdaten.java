package com.anteboth.agrisys.client.model.stammdaten;

import java.io.Serializable;
import java.util.List;


@SuppressWarnings("serial")
public class Stammdaten implements Serializable {

	private List<Kultur> kulturList;
	private List<Sorte> sorteList;
	private List<Duengerart> duengerartList;
	private List<PSMittel> psMittelList;
	private List<BodenbearbeitungTyp> bodenbearbeitungTypList;

	public List<Kultur> getKulturList() {
		return kulturList;
	}

	public void setKulturList(List<Kultur> kulturList) {
		this.kulturList = kulturList;
	}

	public List<Sorte> getSorteList() {
		return sorteList;
	}

	public void setSorteList(List<Sorte> sorteList) {
		this.sorteList = sorteList;
	}

	public List<Duengerart> getDuengerartList() {
		return duengerartList;
	}

	public void setDuengerartList(List<Duengerart> duengerartList) {
		this.duengerartList = duengerartList;
	}

	public List<PSMittel> getPsMittelList() {
		return psMittelList;
	}

	public void setPsMittelList(List<PSMittel> psMittelList) {
		this.psMittelList = psMittelList;
	}

	public List<BodenbearbeitungTyp> getBodenbearbeitungTypList() {
		return bodenbearbeitungTypList;
	}

	public void setBodenbearbeitungTypList(
			List<BodenbearbeitungTyp> bodenbearbeitungTypList) {
		this.bodenbearbeitungTypList = bodenbearbeitungTypList;
	}
	
}
