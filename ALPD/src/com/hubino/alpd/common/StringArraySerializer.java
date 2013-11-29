package com.hubino.alpd.common;

import java.util.Hashtable;
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

/**
* The Class StringArraySerializer.
* 
* @author Hubino
* @version 1.0.0 - The Class StringArraySerializer Created
* 
*/

public class StringArraySerializer extends Vector<String> implements KvmSerializable {

	private static final long serialVersionUID = -3210602378177773980L;
	public String n1 = "http://services.hubino.omar.com/";

      public Object getProperty(int arg0) {
              return this.get(arg0);
      }

      public int getPropertyCount() {
              return this.size();
      }

      public void setProperty(int arg0, Object arg1) {
              this.add(arg1.toString());
      }
      
      @SuppressWarnings("rawtypes")
      public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
              arg2.name = "strarray";
              arg2.type = PropertyInfo.STRING_CLASS;
              arg2.namespace = n1;
      }

}

