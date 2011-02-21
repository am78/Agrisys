package com.anteboth.agrisys.client.grid.data;

import java.util.Map;
import java.util.Map.Entry;

import com.anteboth.agrisys.client.model.Bodenbearbeitung;
import com.anteboth.agrisys.client.model.ListRecord;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.google.gwt.core.client.GWT;
import com.googlecode.objectify.Key;

public class BodenbearbeitungRecord extends ListRecord<Bodenbearbeitung> {

	
    public BodenbearbeitungRecord(Bodenbearbeitung b) {
    	super(b);
    	updateAttributes();
    }
    
    public BodenbearbeitungRecord() {
    }
    
    @Override
    public void updateAttributes() {
    	setAttribute("datum", getDTO() != null ? getDTO().getDatum() : "");
    	setAttribute("typ", (getDTO() != null && getDTO().getBodenbearbeitungTyp() != null) ? getDTO().getBodenbearbeitungTyp() : "");
    	setAttribute("flaeche", getDTO() != null ? getDTO().getFlaeche(): "");
    	setAttribute("bemerkung", getDTO() != null ? getDTO().getBemerkung() : "");
    }
    
    @Override
    public void updateDTO(Map<String, Object> vals) {
    	//update DTO values
    	Bodenbearbeitung dto = getDTO();
		for (Entry<String, Object> entry : vals.entrySet()) {
			String att = entry.getKey();
			Object val = entry.getValue();
			GWT.log("Name: " + att + " Value: " + val);
			if (val == null) {
				GWT.log("Warning, value is null");
			}
			
			if (att.equals("flaeche")) {
				if (val != null) {
					dto.setFlaeche(Double.parseDouble(val.toString()));
				} else {
					dto.setFlaeche(-1);
				}
			} 
			else if (att.equals("bemerkung")) {
				dto.setBemerkung((String) val);
			}
			else if (att.equals("datum")) {
				dto.setDatum((java.util.Date) val);
			}
			else if (att.equals("typ")) {
				if (val != null) {
					Key<BodenbearbeitungTyp> key = new Key<BodenbearbeitungTyp>(
							BodenbearbeitungTyp.class, Long.parseLong((String) val));
					dto.setTypKey(key);
				} else {
					dto.setBodenbearbeitungTyp(null);
					dto.setTypKey(null);
				}
			}
		}
    }
    
    
}
