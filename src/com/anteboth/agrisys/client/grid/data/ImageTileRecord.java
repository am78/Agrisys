package com.anteboth.agrisys.client.grid.data;

import java.util.Date;

import com.smartgwt.client.widgets.tile.TileRecord;

public class ImageTileRecord extends TileRecord {  
	  
    public ImageTileRecord() {  
    	this(new Date(), "", "");
    }  
  
    public ImageTileRecord(Date date, String picture, String description) {
		setDate(date);
    	setPicture(picture);
    	if (description != null) {
    		setDescription(description);
    	} else {
    		setDescription("");
    	}
    }
    
    public void setDate(Date date) {
    	setAttribute("date", date);
    }
    
    public Date getDate() {
    	return getAttributeAsDate("date");
    }
  
    /** 
     * Set the picture. 
     * 
     * @param picture the picture 
     */  
    public void setPicture(String picture) {  
        setAttribute("picture", picture);  
    }  
  
    /** 
     * Return the picture. 
     * 
     * @return the picture 
     */  
    public String getPicture() {  
        return getAttribute("picture");  
    }  
  
    /** 
     * Set the description. 
     * 
     * @param description the description 
     */  
    public void setDescription(String description) {  
        setAttribute("description", description);  
    }  
  
    /** 
     * Return the description. 
     * 
     * @return the description 
     */  
    public String getDescription() {  
        return getAttribute("description");  
    }  
}  