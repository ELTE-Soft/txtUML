package hu.elte.txtuml.export.uml2.utils;

import java.lang.reflect.Field;

/**
 * Provides utilities for accessing (getting or setting) the field value of any field (with any visibility)
 * of an object.
 * 
 * @author Adam Ancsin
 */
public final class FieldValueAccessor {

	/**
	 * Sets the value of the field with the given field name of the specified object to the new value.
	 * @param object The specified object.
	 * @param fieldName The given field name.
	 * @param newVal The new value of the field.
	 *
	 * @author Adam Ancsin
	 */
	public static void setObjectFieldVal(Object object, String fieldName,Object newVal)
	{
		Field field = ElementFinder.findField(fieldName,object.getClass());
		
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
	
	/**
	 * Gets the value of the field with the given field name of the specified object.
	 * @param object The specified object.
	 * @param fieldName The given field name.
	 * @return The value.
	 *
	 * @author Adam Ancsin
	 */
	public static Object getObjectFieldVal(Object object,String fieldName)
	{	
		Field field = ElementFinder.findField(fieldName,object.getClass());
		
		return accessObjectFieldVal(object, field);	
	}
	
	/**
	 * Accesses the value of the specified object's given field.
	 * @param object The specified object.
	 * @param field The given field-
	 * @return The value.
	 *
	 * @author Adam Ancsin
	 */
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
