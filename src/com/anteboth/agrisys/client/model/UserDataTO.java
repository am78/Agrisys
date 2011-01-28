package com.anteboth.agrisys.client.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserDataTO implements Serializable, IDTO {
	
	private Account account;
	private Betrieb betrieb;
	private Erntejahr erntejahr;
	

	public UserDataTO() {
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Betrieb getBetrieb() {
		return betrieb;
	}

	public void setBetrieb(Betrieb betrieb) {
		this.betrieb = betrieb;
	}

	public Erntejahr getErntejahr() {
		return erntejahr;
	}
	
	public void setErntejahr(Erntejahr erntejahr) {
		this.erntejahr = erntejahr;
	}
}