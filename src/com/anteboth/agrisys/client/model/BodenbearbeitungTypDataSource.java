/**
 * 
 */
package com.anteboth.agrisys.client.model;

import java.util.LinkedHashMap;
import java.util.List;

import com.anteboth.agrisys.client.Agrisys;
import com.anteboth.agrisys.client.model.stammdaten.BodenbearbeitungTyp;
import com.anteboth.agrisys.client.model.stammdaten.Kultur;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.widgets.form.fields.SelectItem;

/**
 * @author michael
 *
 */
public class BodenbearbeitungTypDataSource  {
	
	private List<BodenbearbeitungTyp> data;
	private SelectItem selectItem;

	public BodenbearbeitungTypDataSource(SelectItem selectItem) {
		this.selectItem = selectItem;
		loadData();
	}

	private void loadData() {
		//load the data
		Agrisys.getAgrisysservice().loadBodenbearbeitungTypen(new AsyncCallback<List<BodenbearbeitungTyp>>() {
			@Override
			public void onSuccess(List<BodenbearbeitungTyp> result) {
				setData(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				GWT.getUncaughtExceptionHandler().onUncaughtException(caught);
			}
		});
	}

	protected void setData(List<BodenbearbeitungTyp> result) {
		this.data = result;
		
		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		for (BodenbearbeitungTyp typ : result) {
			valueMap.put(typ.getId().toString(), typ.getName());
		}
		selectItem.setValueMap(valueMap);
	}

	public BodenbearbeitungTyp getTypForId(String id) {
		for (BodenbearbeitungTyp t : this.data) {
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
