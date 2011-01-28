package com.anteboth.agrisys.client.ui;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourcePasswordField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;

public class LoginScreen extends DialogBox {

	public LoginScreen() {
		super();
		setTitle("Anmeldung");
		
//		setIsModal(true);
//		centerInPage();

		/* create the form data source */
		DataSourceTextField nameField = new DataSourceTextField("name", "Benutzername", 50, true);  
		DataSourcePasswordField passwdField = new DataSourcePasswordField("password", "Passwort", 50, true);  

		DataSource dataSource = new DataSource();  
		dataSource.setDataFormat(DSDataFormat.JSON);  
		dataSource.setFields(nameField, passwdField);  

		final DynamicForm form = new DynamicForm();  
		form.setDataSource(dataSource);  
		form.setUseAllDataSourceFields(true);  
		
		/* create the buttons */
		Button saveButton = new Button("Anmelden");  
		saveButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				onLoginBtnPressed(form);
			}
		});  

		form.getSaveOnEnter();
		
		/* create the layout */
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSpacing(20);
		verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

		//add the form 
		verticalPanel.add(form);

		//panel for the buttons
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);

		//add the buttons
		horizontalPanel.add(saveButton);

		//add panel to component
		add(verticalPanel);
	}
	
	private void onLoginBtnPressed(final DynamicForm form) {
		if(form.validate()) {  
			//get properties
			String name = form.getValueAsString("name");
			String pass = form.getValueAsString("password");
	
			performLogin(name, pass);
		}
	}

	/**
	 * Performs the login operation.
	 * Override in subclasses.
	 * 
	 * @param name the user name
	 * @param pass the password
	 */
	public void performLogin(String name, String pass) {
	}
}