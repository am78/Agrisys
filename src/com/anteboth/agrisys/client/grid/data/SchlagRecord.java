package com.anteboth.agrisys.client.grid.data;

import java.util.Map;
import java.util.Map.Entry;

import com.anteboth.agrisys.client.model.Flurstueck;
import com.anteboth.agrisys.client.model.ListRecord;
import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.model.SchlagErntejahr;

public class SchlagRecord extends ListRecord<Schlag> {

    public SchlagRecord(Schlag s) {
    	super(s);
    	updateAttributes();
    }

    @Override
	public void updateAttributes() {
    	Schlag schlag = getDTO();
    	if (schlag == null) {
    		return;
    	}
    	
    	Flurstueck f = schlag.getFlurstueck();
    	SchlagErntejahr se = schlag.getSchlagErntejahr();
    	
		String name = f != null ? f.getName() : "";
		String beschreibung = f != null ? f.getBeschreibung() : "";
		Long id = f != null ? f.getID() : null;
		
		double flaeche = se != null ? se.getFlaeche() : 0; 
		
		setAttribute("nummer", id);
		setAttribute("name", name);
		setAttribute("beschreibung", beschreibung);
		setAttribute("flaeche", flaeche);
//		setAttribute("sorte", se.getAnbau());
	}

	@Override
	public void updateDTO(Map<String, String> vals) {
		//update DTO values
		for (Entry<String, String> entry : vals.entrySet()) {
			String att = entry.getKey();
			String val = entry.getValue();
			
//			Schlag s = getDTO();
//			if (att.equals("name")) {
//				s.setName(val);
//			} 
//			else if (att.equals("beschreibung")) {
//				s.setBeschreibung(val);
//			}
//			else if (att.equals("flaeche")) {
//				s.setFlaeche(Double.valueOf(val));
//			}
		}
	}

	public void updateDTO() {
//		Schlag s = getDTO();
//		if (s != null) {
//			s.setBeschreibung(getAttribute("beschreibung"));
//			s.setName(getAttribute("name"));
//			s.setFlaeche(Double.valueOf(getAttribute("flaeche")));
//		}
	}
}