package com.anteboth.agrisys.client.grid;

import java.util.List;
import java.util.Map;

import com.anteboth.agrisys.client.ISchlagErntejahrSelectionListener;
import com.anteboth.agrisys.client.grid.data.BodenbearbeitungRecord;
import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.model.Bodenbearbeitung;
import com.anteboth.agrisys.client.model.ListRecord;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.ui.BodenbearbeitungDetailsWindow;
import com.googlecode.objectify.Key;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;

public class BodenbearbeitungGrid 
extends AbstractListGrid<BodenbearbeitungRecord> implements ISchlagErntejahrSelectionListener {
	
	private DataManager dataManager;
	/** The current SchlagErntejahr item. */
	private SchlagErntejahr schlagErntejahr;

	public BodenbearbeitungGrid() {
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
				BodenbearbeitungRecord.DATUM, "Datum");
		ListGridField flaecheField = ListGridFieldFactory.createFloatField(
				BodenbearbeitungRecord.FLAECHE, "Fl&auml;che");
		ListGridField bemField = ListGridFieldFactory.createStringField(
				BodenbearbeitungRecord.BEMERKUNG, "Bemerkung");
		ListGridField typField = ListGridFieldFactory.createStringField( 
				BodenbearbeitungRecord.TYP, "Typ");
		ListGridField attachmentsField = ListGridFieldFactory.createStringField(
				ListRecord.ATTACHMENTS, "FOTO", 35);
		ListGridField geoField = ListGridFieldFactory.createStringField(
				ListRecord.GEO_LOCATION, "GEO", 35);
		
		setFields(datumField, typField, flaecheField, bemField, attachmentsField, geoField);
	}

	@Override
	protected void reloadData() {
		//load the data
		this.dataManager.loadBodenbearbeitungData(this.schlagErntejahr, this);
	}

	@Override
	protected void deleteRecords(List<BodenbearbeitungRecord> records) {
		//delete records
		this.dataManager.delete(records, this);
	}
	
	@Override
	public Boolean startEditing() {
		BodenbearbeitungRecord r = (BodenbearbeitungRecord) getSelectedRecord();
		if (r != null) {
			BodenbearbeitungDetailsWindow window = new BodenbearbeitungDetailsWindow(r, false, this);
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
		Bodenbearbeitung b = new Bodenbearbeitung();
		b.setSchlagErntejahr(new Key<SchlagErntejahr>(SchlagErntejahr.class, schlagErntejahr.getId()));

		//create new record
		BodenbearbeitungRecord r = new BodenbearbeitungRecord(b);
		
		BodenbearbeitungDetailsWindow window = new BodenbearbeitungDetailsWindow(r, true, this);
		window.draw();

	}

	@Override
	protected void udpateAndSaveRecord(BodenbearbeitungRecord record, EditCompleteEvent event) {
		if (record != null) {
			Map<String, Object> vals = event.getNewValues();
			record.updateDTO(vals);
			
			//update data
			this.dataManager.update(record, this);
			
			super.saveAllEdits();
		}
	}
	
}
