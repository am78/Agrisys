package com.anteboth.agrisys.client.model;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class Bodenbearbeitung extends Aktivitaet {
	
	@Id Long id;
	private Key<BodenbearbeitungTyp> typ;
	
	@Transient
	private BodenbearbeitungTyp bodenbearbeitungTyp;
	
	public Key<BodenbearbeitungTyp> getTypKey() {
		return typ;
	}
	
	public void setTypKey(Key<BodenbearbeitungTyp> typ) {
		this.typ = typ;
	}
	
	public BodenbearbeitungTyp getBodenbearbeitungTyp() {
		return bodenbearbeitungTyp;
	}

	public void setBodenbearbeitungTyp(BodenbearbeitungTyp bodenbearbeitungTyp) {
		this.bodenbearbeitungTyp = bodenbearbeitungTyp;
	}
	
	@Override
	public Long getId() {
		return this.id;
	}
}
