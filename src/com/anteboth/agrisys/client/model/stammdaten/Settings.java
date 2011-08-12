package com.anteboth.agrisys.client.model.stammdaten;

import java.io.Serializable;

public class Settings implements Serializable {

	private int erntejahr = 0;
	
	public int getErntejahr() {
		return erntejahr;
	}
	
	public void setErntejahr(int erntejahr) {
		this.erntejahr = erntejahr;
	}
	
}
