package com.anteboth.agrisys.client.grid.data;

import java.util.Map;
import java.util.Map.Entry;

import com.anteboth.agrisys.client.model.ListRecord;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;

public class BodenbearbeitungTypRecord extends ListRecord<BodenbearbeitungTyp>{
	
	public BodenbearbeitungTypRecord(BodenbearbeitungTyp typ) {
		super(typ);
		updateAttributes();
	}

	@Override
	public void updateAttributes() {
		String name = getDTO() != null ? getDTO().getName() : "";
		String beschreibung = getDTO() != null ? getDTO().getBeschreibung() : "";
		
		setAttribute("name", name);
		setAttribute("beschreibung", beschreibung);
	}

	@Override
	public void updateDTO(Map<String, Object> vals) {
		//update DTO values
		for (Entry<String, Object> entry : vals.entrySet()) {
			String att = entry.getKey();
			Object val = entry.getValue();
			
			BodenbearbeitungTyp typ = getDTO();
			if (att.equals("name")) {
				typ.setName((String) val);
			} 
			else if (att.equals("beschreibung")) {
				typ.setBeschreibung((String) val);
			}
		}
	}
}
