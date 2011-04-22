package com.anteboth.agrisys.client.model;

import java.util.LinkedHashMap;
import java.util.List;

import com.anteboth.agrisys.client.Agrisys;
import com.anteboth.agrisys.client.model.stammdaten.PSMittel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.form.fields.SelectItem;

/**
 * {@link PSMittel} data source. 
 * @author michael
 */
public class PSMittelDataSource  {
	
	private List<PSMittel> data;
	private SelectItem selectItem;

	public PSMittelDataSource(SelectItem selectItem) {
		this.selectItem = selectItem;
		loadData();
	}

	private void loadData() {
		//load the data
		Agrisys.getAgrisysservice().loadPSMittel(new AsyncCallback<List<PSMittel>>() {
			@Override
			public void onSuccess(List<PSMittel> result) {
				setData(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				GWT.getUncaughtExceptionHandler().onUncaughtException(caught);
			}
		});
	}

	protected void setData(List<PSMittel> result) {
		this.data = result;
		
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		for (PSMittel ps : result) {
			valueMap.put(ps.getId().toString(), ps.getName());
		}
		selectItem.setValueMap(valueMap);
	}

	public PSMittel getTypForId(String id) {
		for (PSMittel t : this.data) {
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
