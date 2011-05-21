package com.anteboth.agrisys.client.model;

import java.io.Serializable;
import java.util.Date;

public class ImageResource implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String blobKey;
	private Date date;
	private String description;
	
	public ImageResource() {
	}
	
	public ImageResource(String blobKey, Date date, String description) {
		this.blobKey = blobKey;
		this.date = date;
		this.description = description;
	}
	
	public String getBlobKey() {
		return blobKey;
	}
	public void setBlobKey(String blobKey) {
		this.blobKey = blobKey;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
