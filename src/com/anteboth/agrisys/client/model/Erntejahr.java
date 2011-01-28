package com.anteboth.agrisys.client.model;

import java.io.Serializable;

import javax.persistence.Id;

@SuppressWarnings("serial")
public class Erntejahr implements Serializable, IDTO {
	
	@Id
	Long id;
	
	private int erntejahr;
	
	public Erntejahr() {
	}

	public int getErntejahr() {
		return erntejahr;
	}

	public void setErntejahr(int erntejahr) {
		this.erntejahr = erntejahr;
	}
	
	public Long getId() {
		return id;
	}

}
