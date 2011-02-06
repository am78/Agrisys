package com.anteboth.agrisys.client.model.res;

import org.restlet.resource.Get;

import com.anteboth.agrisys.client.model.Stammdaten;

public interface IStammdatenResource {

	
	@Get
	public Stammdaten retrieve();
	
}
