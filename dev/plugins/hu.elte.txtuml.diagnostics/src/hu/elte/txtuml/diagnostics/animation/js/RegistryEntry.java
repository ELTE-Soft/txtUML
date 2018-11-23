package hu.elte.txtuml.diagnostics.animation.js;

public class RegistryEntry {
	private String modelClassName;
	private String modelClassInstanceName;
	private String locationName;

	RegistryEntry(String modelClassName, String modelClassInstanceName, String locationName){
		this.modelClassName = modelClassName;
		this.modelClassInstanceName = modelClassInstanceName;
		this.locationName = locationName;
	}

	public String getModelClassName(){
		return modelClassName;
	}

	public String getModelClassInstanceName(){
		return modelClassInstanceName;
	}

	public String getLocationName() {
		return locationName;
	}
}
