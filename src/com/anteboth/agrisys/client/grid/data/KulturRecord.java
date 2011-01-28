package com.anteboth.agrisys.client.grid.data;

import java.util.Map;
import java.util.Map.Entry;

import com.anteboth.agrisys.client.model.ListRecord;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;

public class KulturRecord extends ListRecord<Kultur>{
	
	public KulturRecord(Kultur dto) {
		super(dto);
		updateAttributes();
	}

	@Override
	public void updateAttributes() {
		Long id = getDTO() != null ? getDTO().getId() : null;
		String name = getDTO() != null ? getDTO().getName() : "";
		String beschreibung = getDTO() != null ? getDTO().getBeschreibung() : "";
		
		setAttribute("id", id);
		setAttribute("name", name);
		setAttribute("beschreibung", beschreibung);
	}

	@Override
	public void updateDTO(Map<String, String> vals) {
		//update DTO values
		for (Entry<String, String> entry : vals.entrySet()) {
			String att = entry.getKey();
			String val = entry.getValue();
			
			Kultur dto = getDTO();
			if (att.equals("name")) {
				dto.setName(val);
			} 
			else if (att.equals("beschreibung")) {
				dto.setBeschreibung(val);
			}
		}
	}
}
