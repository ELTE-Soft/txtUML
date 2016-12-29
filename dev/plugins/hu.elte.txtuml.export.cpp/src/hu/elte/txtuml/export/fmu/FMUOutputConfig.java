package hu.elte.txtuml.export.fmu;

import java.util.Map;

public class FMUOutputConfig {

	public FMUOutputConfig(String outputSignalName, Map<String, Object> initialValues) {
		this.outputSignalName = outputSignalName;
		this.initialValues = initialValues;
	}
	
	String outputSignalName;
	Map<String, Object> initialValues;	
}
