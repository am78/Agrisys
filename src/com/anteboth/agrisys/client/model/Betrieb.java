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
	
	private String anschrift1;
	private String anschrift2;
	private String ort;
	private int plz;
	private String tel;
	
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

	public String getAnschrift1() {
		return anschrift1;
	}

	public void setAnschrift1(String anschrift1) {
		this.anschrift1 = anschrift1;
	}

	public String getAnschrift2() {
		return anschrift2;
	}

	public void setAnschrift2(String anschrift2) {
		this.anschrift2 = anschrift2;
	}

	public String getOrt() {
		return ort;
	}

	public void setOrt(String ort) {
		this.ort = ort;
	}

	public int getPlz() {
		return plz;
	}

	public void setPlz(int plz) {
		this.plz = plz;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
}
