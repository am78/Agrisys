package com.anteboth.agrisys.client.ui;

import com.anteboth.agrisys.client.grid.AbstractListGrid;
import com.anteboth.agrisys.client.grid.data.BodenbearbeitungRecord;
import com.anteboth.agrisys.client.grid.data.DataManager;
import com.anteboth.agrisys.client.grid.data.StammdatenManager;
import com.anteboth.agrisys.client.model.Bodenbearbeitung;
import com.anteboth.agrisys.client.model.BodenbearbeitungTypDataSource;
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
public class BodenbearbeitungDetailsWindow extends Window {

	private AbstractListGrid grid;
	private StammdatenManager stammdatenManager;
	private DataManager dataManager;
	private BodenbearbeitungTypDataSource btypDataSource;
	
	public BodenbearbeitungDetailsWindow(final BodenbearbeitungRecord record, final boolean addNewRecord, AbstractListGrid grid) {
		super();
		SelectItem typItem = new SelectItem("typ", "Typ");  
		typItem.setRequired(true);
		this.btypDataSource = new BodenbearbeitungTypDataSource(typItem);

		this.stammdatenManager = new StammdatenManager();
		this.dataManager = new DataManager();
		
		this.grid = grid;
		setTitle("Bodenbearbeitung");
		setAutoSize(true);
		setCanDragResize(true);
		setIsModal(true);  
        setShowModalMask(true);
        setAutoCenter(true);
        
		/* create the form */
		FloatItem flaecheItem = new FloatItem();
		flaecheItem.setName("flaeche");
		flaecheItem.setTitle("Fl&auml;che");
		flaecheItem.setRequired(true);
		
		DateItem datumItem = new DateItem("datum", "Datum");
		datumItem.setRequired(true);
		
        
        TextAreaItem bemItem = new TextAreaItem("bemerkung", "Bemerkung");

        final DynamicForm form = new DynamicForm();
        form.setItems(datumItem, typItem, flaecheItem, bemItem);
        
        //display values form an existing entry
        if (!addNewRecord && record != null) {
        	Bodenbearbeitung b = record.getDTO();
        	
        	bemItem.setValue(b.getBemerkung());
        	flaecheItem.setValue(b.getFlaeche());
        	datumItem.setValue(b.getDatum());
        	btypDataSource.setSelectedValue(b.getTypKey().getId());
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
					BodenbearbeitungDetailsWindow.this.destroy();
				}  
			}
		});
        
		com.smartgwt.client.widgets.Button cancelBtn = new com.smartgwt.client.widgets.Button("Abbrechen");
		cancelBtn.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
			@Override
			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
				//close dialog window
				BodenbearbeitungDetailsWindow.this.destroy();
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
	
	private void onSavePressed(BodenbearbeitungRecord record, DynamicForm form, boolean addNewRecord) {
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