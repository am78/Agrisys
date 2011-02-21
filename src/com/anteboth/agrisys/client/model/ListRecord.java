/**
 * 
 */
package com.anteboth.agrisys.client.model;

import java.util.Map;

import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * @author michael
 *
 */
public class ListRecord <T extends IDTO> extends ListGridRecord {
	
	private T dto;
	
	public ListRecord() {
		this(null);
	}

	/**
	 * Constructor.
	 * @param dto {@link IDTO} to set
	 */
	public ListRecord(T dto) {
		setDTO(dto);
	}

	/**
	 * @return returns the {@link IDTO}.
	 */
	public T getDTO() {
		return this.dto;
	}
	
	/**
	 * Sets the {@link IDTO}.
	 * @param to
	 */
	public void setDTO(T to) {
		this.dto = to;
		updateAttributes();
	}
	
	/**
	 * Updates the record attributes with the {@link IDTO} values.
	 */
	public void updateAttributes() {
	}

	/**
	 * Updates the {@link IDTO} attributes with the values of the obtained map. 
	 * @param vals the values map
	 */
	public void updateDTO(Map<String, Object> vals) {
	}
}
