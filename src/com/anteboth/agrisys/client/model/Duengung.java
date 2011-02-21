package com.anteboth.agrisys.client.model;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.anteboth.agrisys.client.model.stammdaten.Duengerart;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class Duengung extends Aktivitaet {
	
	@Id Long id;
	private Key<Duengerart> duengerartKey;
	
	private Double kgProHa;
	private Double ec;
	
	@Transient
	private Duengerart duengerart;
	
	@Override
	public Long getId() {
		return this.id;
	}

	public Key<Duengerart> getDuengerartKey() {
		return duengerartKey;
	}

	public void setDuengerartKey(Key<Duengerart> duengerartKey) {
		this.duengerartKey = duengerartKey;
	}

	public Double getKgProHa() {
		return kgProHa;
	}

	public void setKgProHa(Double kgProHa) {
		this.kgProHa = kgProHa;
	}

	public Double getEc() {
		return ec;
	}

	public void setEc(Double ec) {
		this.ec = ec;
	}

	public Duengerart getDuengerart() {
		return duengerart;
	}

	public void setDuengerart(Duengerart duengerart) {
		this.duengerart = duengerart;
	}
}
