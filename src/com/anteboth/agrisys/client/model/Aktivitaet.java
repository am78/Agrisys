package com.anteboth.agrisys.client.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Embedded;

import com.googlecode.objectify.Key;

@SuppressWarnings("serial")
public abstract class Aktivitaet implements Serializable, IDTO {
	
	public static final int BODENBEARBEITUNG_TYPE = 0;
	public static final int AUSSAAT_TYPE = 1;
	public static final int DUENGUNG_TYPE = 2;
	public static final int ERNTE_TYPE = 3;
	public static final int PFLANZENSCHUTZ_TYPE = 4;
	
	Key<SchlagErntejahr> schlagErntejahr;
	
	private Date datum;
	private double flaeche;
	private String bemerkung;
	private Date lastModification;
	private boolean synchron = true;
	private boolean deleted = false;
	private int type;
	private Double longitude;
	private Double latitude;
	
	@Embedded
	private List<ImageResource> attachments;
	
	public Aktivitaet() {
		//set the lastModification date to if creating a new activity entry on server
		this.lastModification = new Date();
		if (this.attachments == null) {
			this.attachments = new ArrayList<ImageResource>();
		}
	}
	
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public double getFlaeche() {
		return flaeche;
	}
	public void setFlaeche(double flaeche) {
		this.flaeche = flaeche;
	}
	public String getBemerkung() {
		return bemerkung;
	}
	public void setBemerkung(String bemerkung) {
		this.bemerkung = bemerkung;
	}
	
	public Key<SchlagErntejahr> getSchlagErntejahr() {
		return schlagErntejahr;
	}
	
	public void setSchlagErntejahr(Key<SchlagErntejahr> schlagErntejahr) {
		this.schlagErntejahr = schlagErntejahr;
	}
	
	public Date getLastModification() {
		return lastModification;
	}
	
	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}
	
	public abstract Long getId();
	
	public boolean isSynchron() {
		return synchron;
	}
	
	public void setSynchron(boolean synchron) {
		this.synchron = synchron;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public Double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
	
	public Double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}
	

	public List<ImageResource> getAttachments() {
		if (attachments == null) {
			attachments = new ArrayList<ImageResource>();
		}
		return attachments;
	}
	
	public void setAttachments(List<ImageResource> attachments) {
		this.attachments = attachments;
	}

	public abstract void updateType();

}
