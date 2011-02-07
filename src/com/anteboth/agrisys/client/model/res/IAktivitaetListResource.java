package com.anteboth.agrisys.client.model.res;

import org.restlet.resource.Get;

import com.anteboth.agrisys.client.model.Aktivitaet;

public interface IAktivitaetListResource {
	
	@Get
	public Aktivitaet[] retrieve();
	

}
