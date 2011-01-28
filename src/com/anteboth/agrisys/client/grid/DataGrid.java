package com.anteboth.agrisys.client.grid;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public interface DataGrid {

	
	/**
	 * Adds a new table row entry to the table (data model).
	 * @param record to add
	 */
	void addNewRecord(ListGridRecord r);	
	
	/**
	 * Inform the table aboud a changed record.
	 * @param r the changed record.
	 */
	void recordChanged(ListGridRecord r);
	
}
