package com.anteboth.agrisys.client.grid;

import java.util.List;

import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.grid.data.SchlagRecord;
import com.anteboth.agrisys.client.model.Schlag;
import com.anteboth.agrisys.client.ui.SchlagdetailsWindow;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;

/**
 * This grid schows the lis of {@link Schlag} records for the current user.
 * 
 * @author michael
 */
public class SchlaglisteGrid extends AbstractListGrid<SchlagRecord> {
	
	private DataManager dataManager;

	public SchlaglisteGrid() {
		super();
		//disable default editing in table cells
		setEditEvent(ListGridEditEvent.NONE);

		//open editing dialog on 2xclick
		addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			@Override
			public void onRecordDoubleClick(RecordDoubleClickEvent event) {
				startEditing();
			}
		});
	}
	
	
	@Override
	protected void initialize() {
		this.dataManager = new DataManager();
	}
	
	@Override
	protected void initGridFields() {
		//ListGridField nrField = new ListGridField("nummer", "#");
		ListGridField nameField = new ListGridField(SchlagRecord.NAME, "Schlag");
		nameField.setWidth(130);
		ListGridField areaField = new ListGridField(SchlagRecord.FLAECHE, "Fl&auml;che");
		ListGridField schlagNrField = new ListGridField(SchlagRecord.SCHLAG_NR, "Schlagnr");		
		setFields(new ListGridField[] {nameField, areaField, schlagNrField}); 
		//default ordering (schlagNr)
		setSortField(SchlagRecord.SCHLAG_NR);
		
	}
	
	@Override
	protected void udpateAndSaveRecord(SchlagRecord record, EditCompleteEvent event) {
		this.redraw();
	}
	
	@Override
	protected void deleteRecords(List<SchlagRecord> records) {
		//delete records
		this.dataManager.delete(records, this);
	}
	
	@Override
	protected void reloadData() {
		this.dataManager.loadSchlagData(this);
	}
	
	@Override
	public void startEditingNew() {
		Schlag s = new Schlag();
		SchlagRecord r = new SchlagRecord(s);
		SchlagdetailsWindow window = new SchlagdetailsWindow(r, true, this);
		window.draw();
	}
	
	
	@Override
	public Boolean startEditing() {
		SchlagRecord r = (SchlagRecord) getSelectedRecord();
		if (r != null) {
			SchlagdetailsWindow window = new SchlagdetailsWindow(r, false, this);
			window.draw();
		}
		return false;
	}	
}