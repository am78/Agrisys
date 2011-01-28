package com.anteboth.agrisys.client.model;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class Flurstueck implements Serializable, IDTO {
	
	@Id Long id;

	Key<Betrieb> betrieb;
	
	private String name;
	private String beschreibung;
	private double flaeche;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBeschreibung() {
		return beschreibung;
	}
	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}
	public double getFlaeche() {
		return flaeche;
	}
	public void setFlaeche(double flaeche) {
		this.flaeche = flaeche;
	}
	public Long getID() {
		return this.id;
	}
	
	public Key<Betrieb> getBetrieb() {
		return betrieb;
	}
	
	public void setBetrieb(Key<Betrieb> betrieb) {
		this.betrieb = betrieb;
	}

}
