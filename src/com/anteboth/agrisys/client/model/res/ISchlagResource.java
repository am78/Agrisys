package com.anteboth.agrisys.client.model.res;

import org.restlet.resource.Get;

import com.anteboth.agrisys.client.model.Schlag;

public interface ISchlagResource {
	
	@Get
    public Schlag retrieve();
	
}
