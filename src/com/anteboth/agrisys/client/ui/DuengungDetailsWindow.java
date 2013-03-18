package com.anteboth.agrisys.client.ui;

import java.util.Date;

import com.anteboth.agrisys.client.grid.AbstractListGrid;
import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.grid.data.DuengungRecord;
import com.anteboth.agrisys.client.grid.data.StammdatenManager;
import com.anteboth.agrisys.client.model.Bodenbearbeitung;
import com.anteboth.agrisys.client.model.DuengerartDataSource;
import com.anteboth.agrisys.client.model.Duengung;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FloatItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Details panel for a {@link Bodenbearbeitung} entry.
 * @author michael
 */
public class DuengungDetailsWindow extends Window {

	private AbstractListGrid grid;
	private StammdatenManager stammdatenManager;
	private DataManager dataManager;
	private DuengerartDataSource duengerartDataSource;
	
	public DuengungDetailsWindow(final DuengungRecord record, final boolean addNewRecord, AbstractListGrid grid) {
		super();
		SelectItem duengerartItem = new SelectItem(
				DuengungRecord.DUENGERART, "D&uuml;ungerart");  
		duengerartItem.setRequired(true);
		this.duengerartDataSource = new DuengerartDataSource(duengerartItem);

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
		FloatItem flaecheItem = FormItemFactory.createFloatItem(
				DuengungRecord.FLAECHE, "Fl&auml;che", true);
		DateItem datumItem = FormItemFactory.createDateItem(
				DuengungRecord.DATUM, "Datum", true);
		FloatItem kgProHaItem = FormItemFactory.createFloatItem(
				DuengungRecord.KG_PRO_HA, "kg/ha", false);
		FloatItem ecItem = FormItemFactory.createFloatItem(
				DuengungRecord.EC, "EC", false);
        TextAreaItem bemItem = FormItemFactory.createTextAreaItem(
        		DuengungRecord.BEMERKUNG, "Bemerkung", false);

        final DynamicForm form = new DynamicForm();
        form.setItems(datumItem, duengerartItem, flaecheItem, kgProHaItem, ecItem, bemItem);
        
        //display values form an existing entry
        if (!addNewRecord && record != null) {
        	Duengung d = record.getDTO();
        	
        	bemItem.setValue(d.getBemerkung() != null ? d.getBemerkung() : "");
        	flaecheItem.setValue(d.getFlaeche());
        	datumItem.setValue(d.getDatum() != null ? d.getDatum() : new Date());
        	ecItem.setValue(d.getEc() != null ? d.getEc() : Double.valueOf(0));
        	kgProHaItem.setValue(d.getKgProHa() != null ? d.getKgProHa() : Double.valueOf(0));
        	if (d.getDuengerartKey() != null) {
        		duengerartDataSource.setSelectedValue(d.getDuengerartKey().getId());
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
					DuengungDetailsWindow.this.destroy();
				}  
			}
		});
        
		com.smartgwt.client.widgets.Button cancelBtn = new com.smartgwt.client.widgets.Button("Abbrechen");
		cancelBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				//close dialog window
				DuengungDetailsWindow.this.destroy();
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
	
	private void onSavePressed(DuengungRecord record, DynamicForm form, boolean addNewRecord) {
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