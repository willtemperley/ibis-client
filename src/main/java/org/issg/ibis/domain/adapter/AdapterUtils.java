package org.issg.ibis.domain.adapter;

public class AdapterUtils {
			
	private static String EMPTY_STRING = "";
	
	public static String coalesce(Object obj) {
		if (obj != null) {
			return obj.toString();
		} 
		return EMPTY_STRING;
	}

}
