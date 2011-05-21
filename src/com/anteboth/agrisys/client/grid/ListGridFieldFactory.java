package com.anteboth.agrisys.client.grid;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ListGridFieldFactory {

	public static ListGridField createFloatField(String name, String title) { 
		ListGridField field = new ListGridField(name, title);
		//add formatter which replaces '.' with ','
		field.setCellFormatter(new CellFormatter() {
			@Override
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
				if (value != null) {
					try {
						//parse number value
						Double d = Double.parseDouble(value.toString());
						//round to 3 decimals
						NumberFormat fmt = NumberFormat.getDecimalFormat();
						return fmt.format(d);
					} catch (Exception e) {
						e.printStackTrace();
						return value.toString();
					}
				}
				return null;
			}
		});
		return field;
	}

	public static ListGridField createFloatField(String name, String title, int width) {
		ListGridField field = createFloatField(name, title);
		field.setWidth(width);
		return field;
	}

	public static ListGridField createStringField(String name, String title) {
		ListGridField field = new ListGridField(name, title);
		return field;
	}

	public static ListGridField createStringField(String name, String title, int width) {
		ListGridField field = createStringField(name, title);
		field.setWidth(width);
		return field;
	}

	private static final DateTimeFormat dateFormatter = DateTimeFormat.getFormat("dd.MM.yyyy");

	public static ListGridField createDateField(String name, String title) {
		ListGridField field = new ListGridField(name, title);
		field.setCellFormatter(new CellFormatter() {
			public String format(Object value, ListGridRecord record, int rowNum, int colNum) {  
				if (value != null) {  
					try {  
						Date dateValue = (Date) value;  
						return dateFormatter.format(dateValue);  
					} catch (Exception e) {  
						return value.toString();  
					}  
				} else {  
					return "";  
				}  
			}  
		});
		return field;
	}
}
