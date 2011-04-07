package com.anteboth.agrisys.client.model.res;

import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

import com.anteboth.agrisys.client.model.Aktivitaet;

public interface IAktivitaetResource {
	
	@Get
	public Aktivitaet retrieve();
	
	@Put
	public void store(Aktivitaet aktivitaet);
	
	@Delete
	public void delete(Long id);

}
