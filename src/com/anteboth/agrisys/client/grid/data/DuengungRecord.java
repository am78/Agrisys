package com.anteboth.agrisys.client.grid.data;

import java.util.Map;
import java.util.Map.Entry;

import com.anteboth.agrisys.client.model.Duengung;
import com.anteboth.agrisys.client.model.ListRecord;
import com.anteboth.agrisys.client.model.stammdaten.Duengerart;
import com.google.gwt.core.client.GWT;
import com.googlecode.objectify.Key;

public class DuengungRecord extends ListRecord<Duengung> {
	
    public static final String EC = "ec";
	public static final String KG_PRO_HA = "kgProHa";
	public static final String BEMERKUNG = "bemerkung";
	public static final String FLAECHE = "flaeche";
	public static final String DUENGERART = "duengerart";
	public static final String DATUM = "datum";

	public DuengungRecord(Duengung b) {
    	super(b);
    	updateAttributes();
    }
    
    public DuengungRecord() {
    }
    
    @Override
    public void updateAttributes() {
    	setAttribute(DATUM, 	getDTO() != null ? getDTO().getDatum() : "");
    	setAttribute(DUENGERART, (getDTO() != null && getDTO().getDuengerart() != null) ? getDTO().getDuengerart() : "");
    	setAttribute(FLAECHE, 	getDTO() != null ? getDTO().getFlaeche(): "");
    	setAttribute(BEMERKUNG, getDTO() != null ? getDTO().getBemerkung() : "");
    	setAttribute(KG_PRO_HA, getDTO() != null ? getDTO().getKgProHa() : "");
    	setAttribute(EC, 		getDTO() != null ? getDTO().getEc() : "");
    	
    	setAttribute(ATTACHMENTS, "...");
    }
    
    @Override
    public void updateDTO(Map<String, Object> vals) {
    	//update DTO values
    	Duengung dto = getDTO();
		for (Entry<String, Object> entry : vals.entrySet()) {
			String att = entry.getKey();
			Object val = entry.getValue();
			
			GWT.log("Name: " + att + " Value: " + val);
			if (val == null) {
				GWT.log("Warning, value is null");
			}
			
			if (att.equals(FLAECHE)) {
				if (val != null) {
					dto.setFlaeche(Double.parseDouble(val.toString()));
				} else {
					dto.setFlaeche(-1);
				}
			} 
			else if (att.equals(BEMERKUNG)) {
				dto.setBemerkung((String) val);
			}
			else if (att.equals(DATUM)) {
				dto.setDatum((java.util.Date) val);
			}
			else if (att.equals(EC)) {
				if (val != null) {
					dto.setEc(Double.parseDouble(val.toString()));
				} else {
					dto.setEc(Double.valueOf(-1));
				}
			}
			else if (att.equals(KG_PRO_HA)) {
				if (val != null) {
					dto.setKgProHa(Double.parseDouble(val.toString()));
				} else {
					dto.setKgProHa(Double.valueOf(-1));
				}
			}
			else if (att.equals(DUENGERART)) {
				if (val != null) {
					Key<Duengerart> key = new Key<Duengerart>(
							Duengerart.class, Long.parseLong((String) val));
					dto.setDuengerartKey(key);
					dto.setDuengerart(null);
				} else {
					dto.setDuengerart(null);
					dto.setDuengerartKey(null);
				}
			}
		}
    }
}
