package com.anteboth.agrisys.client.grid;

import java.util.List;
import java.util.Map;

import com.anteboth.agrisys.client.ISchlagErntejahrSelectionListener;
import com.anteboth.agrisys.client.grid.data.AussaatRecord;
import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.model.Aussaat;
import com.anteboth.agrisys.client.model.ListRecord;
import com.anteboth.agrisys.client.model.SchlagErntejahr;
import com.anteboth.agrisys.client.ui.AussaatDetailsWindow;
import com.googlecode.objectify.Key;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;

public class AussaatGrid 
extends AbstractListGrid<AussaatRecord> implements ISchlagErntejahrSelectionListener {
	
	private DataManager dataManager;
	
	/** The current SchlagErntejahr item. */
	private SchlagErntejahr schlagErntejahr;

	public AussaatGrid() {
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
				AussaatRecord.DATUM, "Datum");
		ListGridField flaecheField = ListGridFieldFactory.createFloatField(
				AussaatRecord.FLAECHE, "Fl&auml;che");
		ListGridField bemField = ListGridFieldFactory.createStringField(
				AussaatRecord.BEMERKUNG, "Bemerkung");
		ListGridField sorteField = ListGridFieldFactory.createStringField(
				AussaatRecord.SORTE, "Sorte");
		ListGridField kulturField = ListGridFieldFactory.createStringField(
				AussaatRecord.KULTUR, "Kultur");
		ListGridField kgProHaField = ListGridFieldFactory.createFloatField(
				AussaatRecord.KG_PRO_HA, "kg/ha");
		ListGridField beizeField = ListGridFieldFactory.createStringField(
				AussaatRecord.BEIZE, "Beize");
		ListGridField attachmentsField = ListGridFieldFactory.createStringField(
				ListRecord.ATTACHMENTS, "FOTO", 35);
		ListGridField geoField = ListGridFieldFactory.createStringField(
				ListRecord.GEO_LOCATION, "GEO", 35);
		
		setFields(datumField, kulturField, sorteField, flaecheField, 
				kgProHaField, beizeField, bemField, attachmentsField, geoField);
	}

	@Override
	protected void reloadData() {
		//load the data
		this.dataManager.loadAussaatData(this.schlagErntejahr, this);
	}

	@Override
	protected void deleteRecords(List<AussaatRecord> records) {
		//delete records
		this.dataManager.delete(records, this);
	}
	
	@Override
	public Boolean startEditing() {
		AussaatRecord r = (AussaatRecord) getSelectedRecord();
		if (r != null) {
			AussaatDetailsWindow window = new AussaatDetailsWindow(r, false, this);
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
		Aussaat a = new Aussaat();
		a.setSchlagErntejahr(new Key<SchlagErntejahr>(SchlagErntejahr.class, schlagErntejahr.getId()));
		a.setKulturKey(schlagErntejahr.getAnbauKultur());
		a.setSorteKey(schlagErntejahr.getAnbauSorte());

		//create new record
		AussaatRecord r = new AussaatRecord(a);
		
		AussaatDetailsWindow window = new AussaatDetailsWindow(r, true, this);
		window.draw();

	}

	@Override
	protected void udpateAndSaveRecord(AussaatRecord record, EditCompleteEvent event) {
		if (record != null) {
			Map<String, Object> vals = event.getNewValues();
			record.updateDTO(vals);
			
			//update data
			this.dataManager.update(record, this);
			
			super.saveAllEdits();
		}
	}
}