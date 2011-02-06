package com.anteboth.agrisys.client.model.stammdaten;

import java.io.Serializable;

import javax.persistence.Id;

import com.anteboth.agrisys.client.model.IDTO;

@SuppressWarnings("serial")
public class PSMittel implements Serializable, IDTO {
	
	@Id Long id;
	
	private String name;
	private String beschreibung;
	
	public PSMittel() {
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
	
	@Override
	public String toString() {
		return name;
	}
}
