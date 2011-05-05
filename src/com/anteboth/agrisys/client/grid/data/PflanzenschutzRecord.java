package com.anteboth.agrisys.client.grid.data;

import java.util.Map;
import java.util.Map.Entry;

import com.anteboth.agrisys.client.model.ListRecord;
import com.anteboth.agrisys.client.model.Pflanzenschutz;
import com.anteboth.agrisys.client.model.stammdaten.PSMittel;
import com.google.gwt.core.client.GWT;
import com.googlecode.objectify.Key;

public class PflanzenschutzRecord extends ListRecord<Pflanzenschutz> {

    public static final String EC 			= "ec";
	public static final String KG_PRO_HA 	= "kgProHa";
	public static final String BEMERKUNG 	= "bemerkung";
	public static final String FLAECHE 		= "flaeche";
	public static final String PS_MITTEL 	= "psMittel";
	public static final String DATUM 		= "datum";
	public static final String INDIKATION 	= "indikation";
	
    public PflanzenschutzRecord(Pflanzenschutz b) {
    	super(b);
    	updateAttributes();
    }
    
    public PflanzenschutzRecord() {
    }
    
    @Override
    public void updateAttributes() {
    	setAttribute(DATUM, 	getDTO() != null ? getDTO().getDatum() : "");
    	setAttribute(PS_MITTEL, (getDTO() != null && getDTO().getPsMittel() != null) ? getDTO().getPsMittel() : "");
    	setAttribute(FLAECHE, 	getDTO() != null ? getDTO().getFlaeche(): "");
    	setAttribute(BEMERKUNG, getDTO() != null ? getDTO().getBemerkung() : "");
    	setAttribute(KG_PRO_HA, getDTO() != null ? getDTO().getKgProHa() : "");
    	setAttribute(EC, 		getDTO() != null ? getDTO().getEc() : "");
    	setAttribute(INDIKATION,getDTO() != null ? getDTO().getIndikation() : "");
    	
    	setAttribute(ATTACHMENTS, "...");
    	setAttribute(GEO_LOCATION, "...");
    }
    
    @Override
    public void updateDTO(Map<String, Object> vals) {
    	//update DTO values
    	Pflanzenschutz dto = getDTO();
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
			else if (att.equals(INDIKATION)) {
				dto.setIndikation((String) val);
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
			else if (att.equals(PS_MITTEL)) {
				if (val != null) {
					Key<PSMittel> key = new Key<PSMittel>(
							PSMittel.class, Long.parseLong((String) val));
					dto.setPsMittelKey(key);
					dto.setPsMittel(null);
				} else {
					dto.setPsMittel(null);
					dto.setPsMittelKey(null);
				}
			}
		}
    }
}
