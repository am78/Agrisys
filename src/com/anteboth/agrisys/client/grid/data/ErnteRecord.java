package com.anteboth.agrisys.client.grid.data;

import java.util.Map;
import java.util.Map.Entry;

import com.anteboth.agrisys.client.model.Ernte;
import com.anteboth.agrisys.client.model.ListRecord;
import com.google.gwt.core.client.GWT;

public class ErnteRecord extends ListRecord<Ernte> {

    public static final String BEMERKUNG = "bemerkung";
	public static final String FLAECHE = "flaeche";
	public static final String DT_PRO_HA = "dtProHa";
	public static final String KULTUR = "kultur";
	public static final String SORTE = "sorte";
	public static final String DATUM = "datum";
	public static final String ANLIEFERUNG = "anlieferung";
	public static final String GESAMTMENGE = "gesamtmenge";

	public ErnteRecord(Ernte dta) {
    	super(dta);
    	updateAttributes();
    }
    
    public ErnteRecord() {
    }

    @Override
    public void updateAttributes() {
    	setAttribute(DATUM, getDTO() != null ? getDTO().getDatum() : "");
    	setAttribute(SORTE, getDTO() != null ? getDTO().getSorte() : "");
    	setAttribute(KULTUR, getDTO() != null ? getDTO().getKultur() : "");
    	setAttribute(DT_PRO_HA, getDTO() != null ? getDTO().getDtProHa() : "");
    	setAttribute(ANLIEFERUNG, getDTO() != null ? getDTO().getAnlieferung() : "");
    	setAttribute(GESAMTMENGE, getDTO() != null ? getDTO().getGesamtMenge() : "");
    	setAttribute(FLAECHE, getDTO() != null ? getDTO().getFlaeche(): "");
    	setAttribute(BEMERKUNG, getDTO() != null ? getDTO().getBemerkung() : "");
    	
    	setAttribute(ATTACHMENTS, "...");
    	setAttribute(GEO_LOCATION, "...");
    }
    
    @Override
    public void updateDTO(Map<String, Object> vals) {
    	//update DTO values
    	Ernte dto = getDTO();
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
			else if (att.equals(DT_PRO_HA)) {
				dto.setDtProHa(Double.parseDouble(val.toString()));
			}
			else if (att.equals(ANLIEFERUNG)) {
				dto.setAnlieferung((String) val);
			}
			else if (att.equals(GESAMTMENGE)) {
				dto.setGesamtMenge(Double.parseDouble(val.toString()));
			}
		}
    }
}
