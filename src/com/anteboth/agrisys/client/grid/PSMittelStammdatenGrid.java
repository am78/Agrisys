package com.anteboth.agrisys.client.grid;

import java.util.List;
import java.util.Map;

import com.anteboth.agrisys.client.grid.data.PSMittelRecord;
import com.anteboth.agrisys.client.grid.data.StammdatenManager;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.PSMittel;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;

/**
 * Provides a grid for {@link BodenbearbeitungTyp} stammdaten values.
 * @author michael
 */
public class PSMittelStammdatenGrid extends AbstractListGrid<PSMittelRecord> {
	
	private static final String BESCHREIBUNG_ATT = "beschreibung";
	private static final String NAME_ATT = "name";
	
	/** the {@link StammdatenManager} instance */
	private StammdatenManager stammdatenManager;

	/**
	 * Constructor.
	 */
	public PSMittelStammdatenGrid() {
		super();
	}
	
	@Override
	protected void initialize() {
		this.stammdatenManager = new StammdatenManager();
	}
	
	@Override
	protected void initGridFields() {
		ListGridField nameField = new ListGridField(NAME_ATT, "Name");
		ListGridField beschreibungField = new ListGridField(BESCHREIBUNG_ATT, "Beschreibung");
		setFields(new ListGridField[] {nameField, beschreibungField});  
	}

	@Override
	protected void reloadData() {
		this.stammdatenManager.loadPSMittelStammdatenRecords(this);
	}
	
	/**
	 * Adds a new table row entry to the table (data model).
	 * @param record to add
	 */
	public void addNewRecord(ListGridRecord r) {
		//save the record
		stammdatenManager.save((PSMittelRecord) r, this);
	}
	
	@Override
	public void startEditingNew() {		
		PSMittelRecord r = new PSMittelRecord(new PSMittel());
		//save the record
		stammdatenManager.save( r, this);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void udpateAndSaveRecord(PSMittelRecord record, EditCompleteEvent event) {
		if (record != null) {
			Map<String, String> vals = event.getNewValues();
			record.updateDTO(vals);
			
			//save changes
			stammdatenManager.update(record, this);
			
			super.saveAllEdits();
		}			
	}
	
	@Override
	protected void deleteRecords(List<PSMittelRecord> records) {
		stammdatenManager.delete(records, PSMittelStammdatenGrid.this);
	}  
}