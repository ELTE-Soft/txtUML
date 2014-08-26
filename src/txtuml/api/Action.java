package txtuml.api;

import txtuml.importer.MethodImporter;
import txtuml.importer.Importer;
import txtuml.utils.InstanceCreator;

public class Action implements ModelElement {
	public static <T extends ModelClass> T create(Class<T> classType) {
		return InstanceCreator.createInstance(classType, 1);
	}

	public static void link(Class<? extends Association> assocClass,
    		String leftPhrase,  ModelClass leftObj,
    		String rightPhrase, ModelClass rightObj) {
		if(MethodImporter.instructionImport())
		{
			MethodImporter.link(assocClass,leftPhrase,leftObj,rightPhrase,rightObj);		
		}
		else
		{
			Runtime.link(assocClass, leftPhrase, leftObj, rightPhrase, rightObj);
		}
        
    }

    public static void unLink(Class<? extends Association> assocClass,
    		String leftPhrase,  ModelClass leftObj,
    		String rightPhrase, ModelClass rightObj) {
    	if(MethodImporter.instructionImport())
		{
    		MethodImporter.unLink(assocClass, leftPhrase, leftObj, rightPhrase, rightObj);
			
		}
    	else
    	{
    		Runtime.unLink(assocClass, leftPhrase, leftObj, rightPhrase, rightObj);
    	}
        
    }

	@SuppressWarnings("unchecked") // unchecked cast from ModelClass to T
	public static <T extends ModelClass> T selectOne(ModelClass startObj, Class<? extends Association> assocClass, String phrase) {
		if(MethodImporter.instructionImport())
		{
			return (T) MethodImporter.selectOne(startObj, assocClass, phrase);
		}
		else
		{
			return (T) Runtime.selectOne(startObj, assocClass, phrase);
		}
		
	}
	
	public static void send(ModelClass receiverObj, Signal event) {
		if(MethodImporter.instructionImport())
		{
			MethodImporter.send(receiverObj, event);
		}
		else
		{
			Runtime.send(receiverObj, event);
		}
		
	}

	public static void delete(ModelClass obj) {
		if(MethodImporter.instructionImport())
		{
			MethodImporter.delete(obj);
		}
		else
		{
			Runtime.delete(obj);
		}
    	
    }

	public static void log(String message) { // user log
		if(MethodImporter.instructionImport())
		{
			
		}
		else
		{
			Runtime.log(message);
		}
		
	}
    	
	public static void logError(String message) { // user log
		if(MethodImporter.instructionImport())
		{
			
		}
		else
		{
			Runtime.logError(message);
		}
		
	}
	
	static void runtimeLog(String message) { // api log
		if(MethodImporter.instructionImport())
		{
			
		}
		else
		{
			Runtime.runtimeLog(message);
		}
		
	}

	static void runtimeFormattedLog(String format, Object... args) { // api log
		if(MethodImporter.instructionImport())
		{
			
		}
		else
		{
			Runtime.runtimeFormattedLog(format, args);
		}
		
	}
	
	static void runtimeErrorLog(String message) { // api log
		if(Importer.instructionImport())
		{
			
		}	
		else
		{
			Runtime.runtimeErrorLog(message);
		}
		
	}
}