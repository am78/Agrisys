package com.anteboth.agrisys.client.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Schlag implements Serializable, IDTO {
	
	private Flurstueck flurstueck;
	private SchlagErntejahr schlagErntejahr;
	
	public Schlag(){
	}

	public Flurstueck getFlurstueck() {
		return flurstueck;
	}

	public void setFlurstueck(Flurstueck flurstueck) {
		this.flurstueck = flurstueck;
	}

	public SchlagErntejahr getSchlagErntejahr() {
		return schlagErntejahr;
	}

	public void setSchlagErntejahr(SchlagErntejahr schlagErntejahr) {
		this.schlagErntejahr = schlagErntejahr;
	}
}