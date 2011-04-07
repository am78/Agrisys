package com.anteboth.agrisys.client.model;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;

@SuppressWarnings("serial")
public class Aussaat extends Aktivitaet {
	
	@Id Long id;
	
	@Transient
	private Sorte sorte;
	
	@Transient
	private Kultur kultur;
	
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
	
	@Override
	public void updateType() {
		setType(AUSSAAT_TYPE);
	}
}
