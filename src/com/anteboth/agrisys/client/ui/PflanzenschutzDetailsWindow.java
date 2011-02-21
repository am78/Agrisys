package com.anteboth.agrisys.client.ui;

import java.util.Date;

import com.anteboth.agrisys.client.grid.AbstractListGrid;
import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.grid.data.PflanzenschutzRecord;
import com.anteboth.agrisys.client.grid.data.StammdatenManager;
import com.anteboth.agrisys.client.model.PSMittelDataSource;
import com.anteboth.agrisys.client.model.Pflanzenschutz;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FloatItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Details panel for a {@link Pflanzenschutz} entry.
 * @author michael
 */
public class PflanzenschutzDetailsWindow extends Window {

	private AbstractListGrid grid;
	private StammdatenManager stammdatenManager;
	private DataManager dataManager;
	private PSMittelDataSource psDataSource;
	
	public PflanzenschutzDetailsWindow(final PflanzenschutzRecord record, final boolean addNewRecord, AbstractListGrid grid) {
		super();
		SelectItem pflanzenschutzItem = new SelectItem(
				PflanzenschutzRecord.PS_MITTEL, "Pflanzenschutzmittel");  
		pflanzenschutzItem.setRequired(true);
		this.psDataSource = new PSMittelDataSource(pflanzenschutzItem);

		this.stammdatenManager = new StammdatenManager();
		this.dataManager = new DataManager();
		
		this.grid = grid;
		setTitle("D&uuml;ngung");
		setAutoSize(true);
		setCanDragResize(true);
		setIsModal(true);  
        setShowModalMask(true);
        setAutoCenter(true);
        
		/* create the form */
		FloatItem flaecheItem = new FloatItem();
		flaecheItem.setName(PflanzenschutzRecord.FLAECHE);
		flaecheItem.setTitle("Fl&auml;che");
		flaecheItem.setRequired(true);
		
		DateItem datumItem = new DateItem(
				PflanzenschutzRecord.DATUM, "Datum");
		datumItem.setRequired(true);
		
		FloatItem kgProHaItem = new FloatItem();
		kgProHaItem.setName(PflanzenschutzRecord.KG_PRO_HA);
		kgProHaItem.setTitle("kg/ha");
		kgProHaItem.setRequired(false);
		
		FloatItem ecItem = new FloatItem();
		ecItem.setName(PflanzenschutzRecord.EC);
		ecItem.setTitle("EC");
		ecItem.setRequired(false);
        
		TextItem indItem = new TextItem(
        		PflanzenschutzRecord.INDIKATION, "Indikation");
		
        TextAreaItem bemItem = new TextAreaItem(
        		PflanzenschutzRecord.BEMERKUNG, "Bemerkung");

        final DynamicForm form = new DynamicForm();
        form.setItems(datumItem, pflanzenschutzItem, flaecheItem, kgProHaItem, ecItem, indItem, bemItem);
        
        //display values form an existing entry
        if (!addNewRecord && record != null) {
        	Pflanzenschutz ps = record.getDTO();
        	
        	bemItem.setValue(ps.getBemerkung() != null ? ps.getBemerkung() : "");
        	flaecheItem.setValue(ps.getFlaeche());
        	datumItem.setValue(ps.getDatum() != null ? ps.getDatum() : new Date());
        	ecItem.setValue(ps.getEc() != null ? ps.getEc() : Double.valueOf(0));
        	kgProHaItem.setValue(ps.getKgProHa() != null ? ps.getKgProHa() : Double.valueOf(0));
        	indItem.setValue(ps.getIndikation() != null ? ps.getIndikation() : "");
        	if (ps.getPsMittelKey() != null) {
        		psDataSource.setSelectedValue(ps.getPsMittelKey().getId());
        	}
        }

		/* create the save & cancel buttons */
        com.smartgwt.client.widgets.Button saveBtn = new com.smartgwt.client.widgets.Button("Speichern");
        saveBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				if(form.validate()) {
					//save changes
					onSavePressed(record, form, addNewRecord);
					//close dialog window
					PflanzenschutzDetailsWindow.this.destroy();
				}  
			}
		});
        
		com.smartgwt.client.widgets.Button cancelBtn = new com.smartgwt.client.widgets.Button("Abbrechen");
		cancelBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				//close dialog window
				PflanzenschutzDetailsWindow.this.destroy();
			}
		});
		


		/* create the layout */

		//panel for the buttons
		HLayout btnLayout = new HLayout();
		btnLayout.setMargin(10);
		//add the buttons
		btnLayout.addMember(saveBtn);
		btnLayout.addMember(cancelBtn);

		VLayout verticalPanel = new VLayout();
		verticalPanel.setMargin(10);
		verticalPanel.setPadding(5);
		
		//add the form 
		verticalPanel.addMember(form);
		//and the buttons
		verticalPanel.addMember(btnLayout);

		//add panel to component
		addItem(verticalPanel);
	}
	
	private void onSavePressed(PflanzenschutzRecord record, DynamicForm form, boolean addNewRecord) {
		//update the internal DTO values with the values stored in the form 
		record.updateDTO(form.getValues());

		if (addNewRecord) {
			//save data and add to table
			this.dataManager.save(record, this.grid);
		}
		else {
			//save data and update table
			this.dataManager.update(record, this.grid);
		}
		
		//redraw the grid
		this.grid.redraw();
	}
}