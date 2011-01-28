package com.anteboth.agrisys.client.model;

import javax.persistence.Id;
import javax.persistence.Transient;

import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;

@SuppressWarnings("serial")
public class Ernte extends Aktivitaet {
	
	@Id Long id;
	
	@Transient
	private Sorte sorte;
	
	@Transient
	private Kultur kultur;
	
	private Double dtProHa;
	private String anlieferung;
	private Double gesamtMenge;

	public Long getId() {
		return id;
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

	public Double getDtProHa() {
		return dtProHa;
	}

	public void setDtProHa(Double dtProHa) {
		this.dtProHa = dtProHa;
	}

	public String getAnlieferung() {
		return anlieferung;
	}
	
	public void setAnlieferung(String anlieferung) {
		this.anlieferung = anlieferung;
	}

	public Double getGesamtMenge() {
		return gesamtMenge;
	}

	public void setGesamtMenge(Double gesamtMenge) {
		this.gesamtMenge = gesamtMenge;
	}
}
