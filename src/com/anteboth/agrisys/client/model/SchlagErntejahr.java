package com.anteboth.agrisys.client.model;

import java.io.Serializable;

import javax.persistence.Id;

import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class SchlagErntejahr implements Serializable, IDTO {
	
	@Id Long id;
	
	Key<Erntejahr> erntejahr;
	Key<Flurstueck> flurstueck;
	
	Key<Kultur> vorfrucht;
	
	Key<Sorte> anbauSorte;
	Key<Kultur> anbauKultur;;

	private double flaeche;
	private String bemerkung;
	private boolean deleted = false;
	
	public Long getId() {
		return id;
	}
	
	public double getFlaeche() {
		return flaeche;
	}
	public void setFlaeche(double flaeche) {
		this.flaeche = flaeche;
	}
	public String getBemerkung() {
		return bemerkung;
	}
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	public Key<Erntejahr> getErntejahr() {		
		return erntejahr;
	}	
	public Key<Flurstueck> getFlurstueck() {
		return flurstueck;
	}
	public Key<Kultur> getVorfrucht() {
		return vorfrucht;
	}
	public void setErntejahr(Key<Erntejahr> erntejahr) {
		this.erntejahr = erntejahr;
	}
	public void setFlurstueck(Key<Flurstueck> flurstueck) {
		this.flurstueck = flurstueck;
	}
	public void setVorfrucht(Key<Kultur> vorfrucht) {
		this.vorfrucht = vorfrucht;
	}
	
	public Key<Kultur> getAnbauKultur() {
		return anbauKultur;
	}
	
	public void setAnbauKultur(Key<Kultur> anbauKultur) {
		this.anbauKultur = anbauKultur;
	}
	
	public Key<Sorte> getAnbauSorte() {
		return anbauSorte;
	}
	
	public void setAnbauSorte(Key<Sorte> anbauSorte) {
		this.anbauSorte = anbauSorte;
	}
	
	public boolean isDeleted() {
		return deleted;
	}
	
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
