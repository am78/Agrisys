package com.anteboth.agrisys.client.grid.data;

import java.util.Map;
import java.util.Map.Entry;

import com.anteboth.agrisys.client.model.Flurstueck;
import com.anteboth.agrisys.client.model.ListRecord;
import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.model.SchlagErntejahr;

public class SchlagRecord extends ListRecord<Schlag> {

    public static final String FLAECHE 		= "flaeche";
	public static final String BEMERKUNG 	= "bemerkung";
	public static final String NAME 		= "name";
	public static final String NUMMER 		= "nummer";
	public static final String ERNTEJAHR 	= "erntejahr";
	public static final String SORTE 		= "sorte";
	public static final String VORFRUCHT 	= "vorfrucht";
	public static final String KULTUR 		= "kultur";
	public static final String SCHLAG_NR	= "schlagNr";	

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
		Integer schlagNr = f != null ? f.getSchlagNr() : 100;
		
		double flaeche = se != null ? se.getFlaeche() : 0; 
		
		setAttribute(NUMMER, id);
		setAttribute(NAME, name);
		setAttribute(BEMERKUNG, beschreibung);
		setAttribute(FLAECHE, flaeche);
		setAttribute(SCHLAG_NR, schlagNr);
//		setAttribute("sorte", se.getAnbau());
	}

    @Override
	public void updateDTO(Map<String, Object> vals) {
		Schlag s = getDTO();
		//update DTO values
		for (Entry<String, Object> entry : vals.entrySet()) {
			String att = entry.getKey();
			Object val = entry.getValue().toString();
			
			if (NAME.equals(att)) {
				s.getFlurstueck().setName((String) val);
			}
			else if (FLAECHE.equals(att)) {
				s.getSchlagErntejahr().setFlaeche(Double.parseDouble(val.toString()));
			}
			else if (BEMERKUNG.equals(att)) {
				s.getSchlagErntejahr().setBemerkung((String) val);
			}
			else if (SCHLAG_NR.equals(att)) {
				s.getFlurstueck().setSchlagNr((Integer) val);
			}
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