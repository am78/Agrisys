/**
 * 
 */
package com.anteboth.agrisys.client.model;

import java.util.LinkedHashMap;
import java.util.List;

import com.anteboth.agrisys.client.Agrisys;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.form.fields.SelectItem;

/**
 * @author michael
 *
 */
public class KulturDataSource  {
	
	private List<Kultur> data;
	private SelectItem selectItem;

	public KulturDataSource(SelectItem selectItem) {
		this.selectItem = selectItem;
		loadData();
	}

	private void loadData() {
		//load the data
		Agrisys.getAgrisysservice().loadKulturData(new AsyncCallback<List<Kultur>>() {
			@Override
			public void onSuccess(List<Kultur> result) {
				setData(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				GWT.getUncaughtExceptionHandler().onUncaughtException(caught);
			}
		});
	}

	protected void setData(List<Kultur> result) {
		this.data = result;
		
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		for (Kultur kultur : result) {
			valueMap.put(kultur.getId().toString(), kultur.getName());
		}
		selectItem.setValueMap(valueMap);
		
		
	}

	public Kultur getKulturForId(String id) {
		for (Kultur k : this.data) {
			if (k.getId().equals(Long.valueOf(id))) {
				return k;
			}
		}
		return null;
	}

	public void setSelectedValue(long id) {
		this.selectItem.setValue(Long.valueOf(id).toString());
	}
}
