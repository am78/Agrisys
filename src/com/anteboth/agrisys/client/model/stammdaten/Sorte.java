package com.anteboth.agrisys.client.model.stammdaten;

import java.io.Serializable;

import javax.persistence.Id;

import com.anteboth.agrisys.client.model.IDTO;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class Sorte implements Serializable, IDTO {
	
	@Id Long id;
	
	Key<Kultur> kultur;
	
	private String name;
	private String beschreibung;
	
	public Sorte() {
		this.name = "Name";
		this.beschreibung = "Beschreibung";
	}
	
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
	
	public Long getId() {
		return id;
	}
	
	public void setKultur(Key<Kultur> kultur) {
		this.kultur = kultur;
	}
	
	public Key<Kultur> getKultur() {
		return kultur;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
