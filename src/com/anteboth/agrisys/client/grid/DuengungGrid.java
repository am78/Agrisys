package com.anteboth.agrisys.client.grid;

import java.util.List;
import java.util.Map;

import com.anteboth.agrisys.client.ISchlagErntejahrSelectionListener;
import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.grid.data.DuengungRecord;
import com.anteboth.agrisys.client.model.Duengung;
import com.anteboth.agrisys.client.model.ListRecord;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.ui.DuengungDetailsWindow;
import com.googlecode.objectify.Key;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;

public class DuengungGrid 
extends AbstractListGrid<DuengungRecord> implements ISchlagErntejahrSelectionListener {
	
	private DataManager dataManager;
	/** The current SchlagErntejahr item. */
	private SchlagErntejahr schlagErntejahr;

	public DuengungGrid() {
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
	public void onSchlagErntejahrSelectionChanged(SchlagErntejahr se) {
		setSchlagErntejahr(se);
		reloadData();
	}
	
	public void setSchlagErntejahr(SchlagErntejahr schlagErntejahr) {
		this.schlagErntejahr = schlagErntejahr;
	}
	
	public SchlagErntejahr getSchlagErntejahr() {
		return schlagErntejahr;
	}
	
	@Override
	protected void initGridFields() {
		ListGridField datumField = ListGridFieldFactory.createDateField(
				DuengungRecord.DATUM, "Datum");
		ListGridField flaecheField = ListGridFieldFactory.createFloatField(
				DuengungRecord.FLAECHE, "Fl&auml;che");
		ListGridField bemField = ListGridFieldFactory.createStringField(
				DuengungRecord.BEMERKUNG, "Bemerkung");
		ListGridField duengerartField = ListGridFieldFactory.createStringField(
				DuengungRecord.DUENGERART, "D&uuml;ngerart");
		ListGridField ecField = ListGridFieldFactory.createFloatField(
				DuengungRecord.EC, "EC", 40);
		ListGridField kgProHaField = ListGridFieldFactory.createFloatField(
				DuengungRecord.KG_PRO_HA, "kg/ha");
		ListGridField attachmentsField = ListGridFieldFactory.createStringField(
				ListRecord.ATTACHMENTS, "FOTO", 35);
		ListGridField geoField = ListGridFieldFactory.createStringField(
				ListRecord.GEO_LOCATION, "GEO", 35);
		
		setFields(datumField, duengerartField, flaecheField, kgProHaField, ecField, 
				bemField, attachmentsField, geoField);
	}

	@Override
	protected void reloadData() {
		//load the data
		this.dataManager.loadDuengungData(this.schlagErntejahr, this);
	}

	@Override
	protected void deleteRecords(List<DuengungRecord> records) {
		//delete records
		this.dataManager.delete(records, this);
	}
	
	@Override
	public Boolean startEditing() {
		DuengungRecord r = (DuengungRecord) getSelectedRecord();
		if (r != null) {
			DuengungDetailsWindow window = new DuengungDetailsWindow(r, false, this);
			window.draw();
		}
		return false;
	}

	@Override
	public void startEditingNew() {
		if (schlagErntejahr == null) {
			return;
		}
		
		//create the data entry
		Duengung b = new Duengung();
		b.setSchlagErntejahr(new Key<SchlagErntejahr>(SchlagErntejahr.class, schlagErntejahr.getId()));

		//create new record
		DuengungRecord r = new DuengungRecord(b);
		
		DuengungDetailsWindow window = new DuengungDetailsWindow(r, true, this);
		window.draw();

	}

	@Override
	protected void udpateAndSaveRecord(DuengungRecord record, EditCompleteEvent event) {
		if (record != null) {
			Map<String, Object> vals = event.getNewValues();
			record.updateDTO(vals);
			
			//update data
			this.dataManager.update(record, this);
			
			super.saveAllEdits();
		}
	}
	
}
