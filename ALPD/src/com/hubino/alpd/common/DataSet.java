package com.hubino.alpd.common;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

/**
* The Class DataSet.
* 
* @author Hubino
* @version 1.0.0 - The Class DataSet Created
* 
*/

public class DataSet implements KvmSerializable{

	public byte[] data = null;
	public String userId = null;
	
	public Object getProperty(int arg0) {
		switch (arg0){
		case 0:
            return data;
		case 1:
            return userId;
		 default:
             return null;
		}
	}

	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@SuppressWarnings("rawtypes")
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch(index)
        {
        case 0:
            info.type = byte[].class;
            info.name = "data";
            break;        
        case 1:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "userId";
        default:
        	break;
        }
		
	}

	public void setProperty(int index, Object value) {
		switch(index)
        {
        case 0:
        	data = (byte[])value;
            break;
        case 1:
        	userId = value.toString();
            break;
        default:
            break;
        }
		
	}

}
