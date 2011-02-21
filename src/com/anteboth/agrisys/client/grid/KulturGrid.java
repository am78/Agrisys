package com.anteboth.agrisys.client.grid;

import java.util.List;
import java.util.Map;

import com.anteboth.agrisys.client.grid.data.KulturRecord;
import com.anteboth.agrisys.client.grid.data.StammdatenManager;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;

/**
 * Provides a grid for {@link BodenbearbeitungTyp} stammdaten values.
 * @author michael
 */
public class KulturGrid extends AbstractListGrid<KulturRecord> {
	
	private static final String BESCHREIBUNG_ATT = "beschreibung";
	private static final String NAME_ATT = "name";
	
	/** the {@link StammdatenManager} instance */
	private StammdatenManager stammdatenManager;

	/**
	 * Constructor.
	 */
	public KulturGrid() {
		super();
	}
	
	@Override
	protected void initialize() {
		this.stammdatenManager = new StammdatenManager();
	}
	
	@Override
	protected void initGridFields() {
		ListGridField nameField = new ListGridField(NAME_ATT, "Kultur");
//		ListGridField beschreibungField = new ListGridField(BESCHREIBUNG_ATT, "Beschreibung");
		setFields(new ListGridField[] {nameField});  
	}

	@Override
	protected void reloadData() {
		this.stammdatenManager.loadKulturStammdatenRecords(this);
	}
	
	/**
	 * Adds a new table row entry to the table (data model).
	 * @param record to add
	 */
	public void addNewRecord(ListGridRecord r) {
		//save the record
		stammdatenManager.save((KulturRecord) r, this);
	}
	
	@Override
	public void startEditingNew() {		
		KulturRecord r = new KulturRecord(new Kultur());
		//save the record
		stammdatenManager.save( r, this);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void udpateAndSaveRecord(KulturRecord record, EditCompleteEvent event) {
		if (record != null) {
			Map<String, Object> vals = event.getNewValues();
			record.updateDTO(vals);
			
			//save changes
			stammdatenManager.update(record, this);
			
			super.saveAllEdits();
		}			
	}
	
	@Override
	protected void deleteRecords(List<KulturRecord> records) {
		stammdatenManager.delete(records, KulturGrid.this);
	}  
}