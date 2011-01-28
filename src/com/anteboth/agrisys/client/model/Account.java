package com.anteboth.agrisys.client.model;

import java.io.Serializable;

import javax.persistence.Id;

@SuppressWarnings("serial")
public class Account implements Serializable, IDTO {

	
	@Id Long id;

	private String username;
	private String password;
	
	public Account() {
	}

	public Account(String username, String pass) {
		this.username = username;
		this.password = pass;
	}

	public String getUsername() {
		return this.username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
