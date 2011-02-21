package com.anteboth.agrisys.client.grid;

import java.util.List;
import java.util.Map;

import com.anteboth.agrisys.client.grid.data.SorteRecord;
import com.anteboth.agrisys.client.grid.data.StammdatenManager;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.anteboth.agrisys.client.model.stammdaten.Sorte;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;

/**
 * Provides a grid for {@link BodenbearbeitungTyp} stammdaten values.
 * @author michael
 */
public class SorteGrid extends AbstractListGrid<SorteRecord> {
	
//	private static final String BESCHREIBUNG_ATT = "beschreibung";
	private static final String NAME_ATT = "name";
	
	/** the {@link StammdatenManager} instance */
	private StammdatenManager stammdatenManager;
	private Kultur selectedKultur;

	/**
	 * Constructor.
	 */
	public SorteGrid() {
		super();
	}
	
	@Override
	protected void initialize() {
		this.stammdatenManager = new StammdatenManager();
	}
	
	@Override
	protected void initGridFields() {
		ListGridField nameField = new ListGridField(NAME_ATT, "Sorte");
//		ListGridField beschreibungField = new ListGridField(BESCHREIBUNG_ATT, "Beschreibung");
		setFields(new ListGridField[] {nameField});  
	}

	@Override
	protected void reloadData() {
		this.stammdatenManager.loadSorteStammdatenRecords(this.selectedKultur, this);
	}
	
	/**
	 * Adds a new table row entry to the table (data model).
	 * @param record to add
	 */
	public void addNewRecord(ListGridRecord r) {
		//save the record
		stammdatenManager.save((SorteRecord) r, this.selectedKultur, this);
	}
	
	@Override
	public void startEditingNew() {		
		Sorte s = new Sorte();
		SorteRecord r = new SorteRecord(s);
		//save the record
		stammdatenManager.save(r, this.selectedKultur, this);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected void udpateAndSaveRecord(SorteRecord record, EditCompleteEvent event) {
		if (record != null) {
			Map<String, Object> vals = event.getNewValues();
			record.updateDTO(vals);
			
			//save changes
			stammdatenManager.update(record, this.selectedKultur, this);
			
			super.saveAllEdits();
		}			
	}
	
	@Override
	protected void deleteRecords(List<SorteRecord> records) {
		stammdatenManager.delete(records, SorteGrid.this);
	}

	/**
	 * Sets the selected Kultur item.
	 * @param selected
	 */
	public void setSelectedKultur(Kultur selected) {
		this.selectedKultur = selected;
		//reload the data
		reloadData();
	}  
}