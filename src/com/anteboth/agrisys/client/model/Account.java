package com.anteboth.agrisys.client.model;

import java.io.Serializable;

import javax.persistence.Id;

@SuppressWarnings("serial")
public class Account implements Serializable, IDTO {
	
	private static final int DEFAULT_ERNTEJAHR = 2011;

	@Id Long id;

	private String username;
	private String password;
	private String email;
	private int currentErntejahr;
	
	public Account() {
	}
	
	public Account(String username, String pass, String email) {
		this.username = username;
		this.password = pass;
		this.email = email;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Long getId() {
		return id;
	}
	
	public int getCurrentErntejahr() {
		if (this.currentErntejahr < 1900) {
			this.currentErntejahr = DEFAULT_ERNTEJAHR;
		}
		return this.currentErntejahr;
	}
	
	public void setCurrentErntejahr(int currentErntejahr) {
		this.currentErntejahr = currentErntejahr;
	}
}