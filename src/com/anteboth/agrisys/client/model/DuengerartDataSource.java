/**
 * 
 */
package com.anteboth.agrisys.client.model;

import java.util.LinkedHashMap;
import java.util.List;

import com.anteboth.agrisys.client.Agrisys;
import com.anteboth.agrisys.client.model.stammdaten.Duengerart;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.form.fields.SelectItem;

/**
 * {@link Duengerart} data source. 
 * @author michael
 */
public class DuengerartDataSource  {
	
	private List<Duengerart> data;
	private SelectItem selectItem;

	public DuengerartDataSource(SelectItem selectItem) {
		this.selectItem = selectItem;
		loadData();
	}

	private void loadData() {
		//load the data
		Agrisys.getAgrisysservice().loadDuengerart(new AsyncCallback<List<Duengerart>>() {
			@Override
			public void onSuccess(List<Duengerart> result) {
				setData(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				GWT.getUncaughtExceptionHandler().onUncaughtException(caught);
			}
		});
	}

	protected void setData(List<Duengerart> result) {
		this.data = result;
		
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		for (Duengerart typ : result) {
			valueMap.put(typ.getId().toString(), typ.getName());
		}
		selectItem.setValueMap(valueMap);
	}

	public Duengerart getTypForId(String id) {
		for (Duengerart t : this.data) {
			if (t.getId().equals(Long.valueOf(id))) {
				return t;
			}
		}
		return null;
	}

	public void setSelectedValue(long id) {
		this.selectItem.setValue(Long.valueOf(id).toString());
	}
}
