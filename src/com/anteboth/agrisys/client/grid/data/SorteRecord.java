package com.anteboth.agrisys.client.grid.data;

import java.util.Map;
import java.util.Map.Entry;

import com.anteboth.agrisys.client.model.ListRecord;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;

public class SorteRecord extends ListRecord<Sorte>{
	
	public SorteRecord(Sorte dto) {
		super(dto);
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
	public void updateDTO(Map<String, String> vals) {
		//update DTO values
		for (Entry<String, String> entry : vals.entrySet()) {
			String att = entry.getKey();
			String val = entry.getValue();
			
			Sorte dto = getDTO();
			if (att.equals("name")) {
				dto.setName(val);
			} 
			else if (att.equals("beschreibung")) {
				dto.setBeschreibung(val);
			}
		}
	}
}