package com.anteboth.agrisys.client.model;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class Aussaat extends Aktivitaet {
	
	@Id Long id;
	
	@Transient
	private Sorte sorte;
	private Key<Sorte> sorteKey;
	
	@Transient
	private Kultur kultur;
	private Key<Kultur> kulturKey;
	
	private Double kgProHa;
	private String beize;

	public Aussaat() {
		setType(AUSSAAT_TYPE);
	}

	public Long getId() {
		return id;
	}
	
	public Double getKgProHa() {
		return kgProHa;
	}

	public void setKgProHa(Double kgProHa) {
		this.kgProHa = kgProHa;
	}

	public String getBeize() {
		return beize;
	}

	public void setBeize(String beize) {
		this.beize = beize;
	}

	public Sorte getSorte() {
		return sorte;
	}

	public void setSorte(Sorte sorte) {
		this.sorte = sorte;
	}
	
	public Kultur getKultur() {
		return kultur;
	}
	
	public void setKultur(Kultur kultur) {
		this.kultur = kultur;
	}
	
	public Key<Sorte> getSorteKey() {
		return sorteKey;
	}

	public void setSorteKey(Key<Sorte> sorteKey) {
		this.sorteKey = sorteKey;
	}

	public Key<Kultur> getKulturKey() {
		return kulturKey;
	}

	public void setKulturKey(Key<Kultur> kulturKey) {
		this.kulturKey = kulturKey;
	}

	@Override
	public void updateType() {
		setType(AUSSAAT_TYPE);
	}
}
