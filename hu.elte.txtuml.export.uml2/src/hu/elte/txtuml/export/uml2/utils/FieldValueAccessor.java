package hu.elte.txtuml.export.uml2.utils;

import java.lang.reflect.Field;

public final class FieldValueAccessor {

	public static void setObjectFieldVal(Object object, String fieldName,Object newVal)
	{
		Field field = ElementFinder.findField(object.getClass(),fieldName);
		
		if(field!=null)
		{
			field.setAccessible(true);
			try {
				field.set(object,newVal);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			field.setAccessible(false);
		}
	}
	public static Object getObjectFieldVal(Object object,String fieldName)
	{	
		Field field = ElementFinder.findField(object.getClass(),fieldName);
		
		return accessObjectFieldVal(object, field);	
	}
	
	private static Object accessObjectFieldVal(Object object, Field field)
	{
		Object val=null;
		
		if(field!=null)
		{
			field.setAccessible(true);
			try {
				val = field.get(object);
				return val;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			field.setAccessible(false);
		}
		
		
		return val;
	}

}
