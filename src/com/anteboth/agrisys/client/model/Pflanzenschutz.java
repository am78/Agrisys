package com.anteboth.agrisys.client.model;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.anteboth.agrisys.client.model.stammdaten.PSMittel;
import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public class Pflanzenschutz extends Aktivitaet {
	
	@Id Long id;
	private Key<PSMittel> psMittelKey;
	
	private Double kgProHa;
	private Double ec;
	private String indikation;
	
	@Transient
	private PSMittel psMittel;
	
	@Override
	public Long getId() {
		return this.id;
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

	public Key<PSMittel> getPsMittelKey() {
		return psMittelKey;
	}

	public void setPsMittelKey(Key<PSMittel> psMittelKey) {
		this.psMittelKey = psMittelKey;
	}

	public String getIndikation() {
		return indikation;
	}

	public void setIndikation(String indication) {
		this.indikation = indication;
	}

	public PSMittel getPsMittel() {
		return psMittel;
	}

	public void setPsMittel(PSMittel psMittel) {
		this.psMittel = psMittel;
	}

}
