package com.anteboth.agrisys.client.model;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public abstract class Aktivitaet implements Serializable, IDTO {
	
	Key<SchlagErntejahr> schlagErntejahr;
	
	private Date datum;
	private double flaeche;
	private String bemerkung;
	
	
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
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
	
	public Key<SchlagErntejahr> getSchlagErntejahr() {
		return schlagErntejahr;
	}
	
	public void setSchlagErntejahr(Key<SchlagErntejahr> schlagErntejahr) {
		this.schlagErntejahr = schlagErntejahr;
	}

}
