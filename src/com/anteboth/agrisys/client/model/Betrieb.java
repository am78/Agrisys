/**
 * 
 */
package com.anteboth.agrisys.client.model;

import java.io.Serializable;

import javax.persistence.Id;

import com.googlecode.objectify.Key;

/**
 * @author michael
 *
 */
@SuppressWarnings("serial")
public class Betrieb implements Serializable, IDTO {

	@Id Long id;
	Key<Account> manager;

	private String name;
	
	public Betrieb() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Key<Account> getManagerKey() {
		return manager;
	}
	
	public void setManagerKey(Key<Account> manager) {
		this.manager = manager;
	}
	
	public Long getId() {
		return id;
	}
}
