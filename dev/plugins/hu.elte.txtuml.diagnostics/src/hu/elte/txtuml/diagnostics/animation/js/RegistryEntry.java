package hu.elte.txtuml.diagnostics.animation.js;

public class RegistryEntry {
	private String modelClassName;
	private String modelClassInstanceID;
	private String modelClassInstanceName;
		
	RegistryEntry(String modelClassName, String modelClassInstanceID, String modelClassInstanceName){
		this.modelClassName = modelClassName;
		this.modelClassInstanceID = modelClassInstanceID;
		this.modelClassInstanceName = modelClassInstanceName;
	}

	public String getModelClassName(){
		return this.modelClassName;
	}
	
	public String getModelClassInstanceID(){
		return this.modelClassInstanceID;
	}
	
	public String getModelClassInstanceName(){
		return this.modelClassInstanceName;
	}
	
}
