package com.anteboth.agrisys.client.ui;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemValueParser;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FloatItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class FormItemFactory {

	/**
	 * Creates a FloatItem
	 * @param name the internal name
	 * @param title the displayed title
	 * @param required mandatory or not
	 * @return the created FloatItem
	 */
	public static FloatItem createFloatItem(String name, String title, boolean required) {
		FloatItem item = new FloatItem();
		item.setName(name);
		item.setTitle(title);
		item.setRequired(required);
		item.setEditorValueParser(new FormItemValueParser() {
			@Override
			public Object parseValue(String value, DynamicForm form, FormItem item) {
				try {
					if (value != null) {
						String v = value.replace(',', '.');
						return Float.valueOf(v);
					}
				} catch (Throwable t) {
					t.printStackTrace();
				}
				return value;
			}
		});
		return item;
	}
	
	public static DateItem createDateItem(String name, String title, boolean required) {
		DateItem item = new DateItem(name, title);
		item.setRequired(required);
		return item;
	}

	public static TextAreaItem createTextAreaItem(String name, String title, boolean required) {
		TextAreaItem item = new TextAreaItem(name, title);
		item.setRequired(required);
		return item;
	}

	public static TextItem createTextItem(String name, String title, boolean required) {
		TextItem item = new TextItem(name, title);
		item.setRequired(required);
		return item;
	}
	
}
