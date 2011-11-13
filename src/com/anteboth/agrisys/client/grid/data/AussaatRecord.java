package com.anteboth.agrisys.client.grid.data;

import java.util.Map;
import java.util.Map.Entry;

import com.anteboth.agrisys.client.model.Aussaat;
import com.anteboth.agrisys.client.model.ListRecord;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.google.gwt.core.client.GWT;
import com.googlecode.objectify.Key;

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
    	
    	setAttribute(ATTACHMENTS, "...");
    	setAttribute(GEO_LOCATION, "...");
    }
    
    @Override
    public void updateDTO(Map<String, Object> vals) {
    	//update DTO values
    	Aussaat dto = getDTO();
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
			else if (att.equals(KG_PRO_HA)) {
				dto.setKgProHa(Double.parseDouble(val.toString()));
			}
			else if (att.equals(BEIZE)) {
				dto.setBeize((String) val);
			}
			else if (att.equals(SORTE)) {
				if (val instanceof String) {
					int id = Integer.parseInt((String) val);
					dto.setSorte(null);
					dto.setSorteKey(new Key<Sorte>(Sorte.class, id));
				} else if (val instanceof Sorte) {
					Sorte s = (Sorte) val;
					dto.setSorte(s);
					dto.setSorteKey(new Key<Sorte>(Sorte.class, s.getId()));
				}
			}
		}
    }
}
