package hu.elte.txtuml.export.cpp.description;


import hu.elte.txtuml.api.model.ModelClass;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThreadDescriptionExporter {
	
	private Map<String, ThreadPoolConfiguration > configMap;
	private boolean descriptionExported = false;
	private boolean isMultiThreading = false;
	private boolean containsMulthreadingAnnotaion = false;
	
	List<String> warningList;
	List<String> errorList;
	
	
	int numberOfConfigurations;
	
	Set<String> exportedClasses;
	
	
	public ThreadDescriptionExporter(){
		configMap = new HashMap<String, ThreadPoolConfiguration >();
		exportedClasses = new HashSet<String>();
		numberOfConfigurations = 0;
		
		warningList = new ArrayList<String>();
		errorList = new ArrayList<String>();
	}
	
	
	
	public Map<String, ThreadPoolConfiguration > getConfigMap(){
		return configMap;
	}
	
	public void exportDescription(Class<? extends Configuration> description) {
		
		if(descriptionExported) return;
		
		for(Annotation annotaion: description.getAnnotations() ){
			if(annotaion instanceof GroupContainer){
				
				for(Annotation group: ((GroupContainer) annotaion).value()){
					
					exportGroup( (Group) group);
				}
			}
			else if (annotaion instanceof Group) {
				
				exportGroup( (Group) annotaion);

			}
			else if(annotaion instanceof Multithreading){
				containsMulthreadingAnnotaion = true;
				Multithreading mlt = (Multithreading) annotaion;
				if(mlt.value()){
					isMultiThreading = true;
				}
				else{
					isMultiThreading = false;
				}
			}
			else{
				warningList.add("Unknown annotation detected!");
			}
		}
		
		if (!containsMulthreadingAnnotaion) {
			warningList.add("Missing Multithreading option from the description!");
			
			if (!configMap.isEmpty()) {
				isMultiThreading = true;
			}
		}
		
		descriptionExported = true;
		
	}
	
	public boolean isSuccessfulExportation(){
		if(!descriptionExported){
			return false;
		}
		else{
			return errorList.isEmpty();
		}
	}
	
	public boolean warningListIsEmpty() {
		return !warningList.isEmpty();
	}
	
	public List<String> getErrors(){
		return errorList;
	}
	
	public List<String> getWarnings(){
		return warningList;
	}
	
	public boolean isMultiThreading() {
		return isMultiThreading;
	}
	
	private void exportGroup(Group group) {
		numberOfConfigurations = numberOfConfigurations + 1;
		
		ThreadPoolConfiguration config = new ThreadPoolConfiguration(numberOfConfigurations, group.gradient(), group.constant());
		config.setMaxThreads(group.max());

		
		checkEmptyGroup(group.contains());
		
		for(Class<? extends ModelClass> cls: group.contains()){
			if(!exportedClasses.contains(cls.getSimpleName())){
				exportedClasses.add(cls.getSimpleName());
				configMap.put(cls.getSimpleName(), config);
			}
			else{
				warningList.add(cls.getSimpleName() + " configured more times!");
			}
			
		}
	}

	private void checkEmptyGroup(Class<? extends ModelClass>[] classes) {
		if(classes.length == 0){
			warningList.add("Empty Group annotation");
		}
		
	}
}
