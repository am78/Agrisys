package com.anteboth.agrisys.client.grid.data;

import java.util.Map;
import java.util.Map.Entry;

import com.anteboth.agrisys.client.model.Aussaat;
import com.anteboth.agrisys.client.model.ListRecord;
import com.google.gwt.core.client.GWT;

public class AussaatRecord extends ListRecord<Aussaat> {

    public static final String BEMERKUNG = "bemerkung";
	public static final String FLAECHE = "flaeche";
	public static final String BEIZE = "beize";
	public static final String KG_PRO_HA = "kgProHa";
	public static final String KULTUR = "kultur";
	public static final String SORTE = "sorte";
	public static final String DATUM = "datum";

	public AussaatRecord(Aussaat a) {
    	super(a);
    	updateAttributes();
    }
    
    public AussaatRecord() {
    }

    @Override
    public void updateAttributes() {
    	setAttribute(DATUM, getDTO() != null ? getDTO().getDatum() : "");
    	setAttribute(SORTE, getDTO() != null ? getDTO().getSorte() : "");
    	setAttribute(KULTUR, getDTO() != null ? getDTO().getKultur() : "");
    	setAttribute(KG_PRO_HA, getDTO() != null ? getDTO().getKgProHa() : "");
    	setAttribute(BEIZE, getDTO() != null ? getDTO().getBeize() : "");
    	setAttribute(FLAECHE, getDTO() != null ? getDTO().getFlaeche(): "");
    	setAttribute(BEMERKUNG, getDTO() != null ? getDTO().getBemerkung() : "");
    }
    
    @Override
    public void updateDTO(Map<String, String> vals) {
    	//update DTO values
    	Aussaat dto = getDTO();
		for (Entry<String, String> entry : vals.entrySet()) {
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
			else if (att.equals(KG_PRO_HA)) {
				dto.setKgProHa(Double.parseDouble(val.toString()));
			}
			else if (att.equals(BEIZE)) {
				dto.setBeize((String) val);
			}
		}
    }
}