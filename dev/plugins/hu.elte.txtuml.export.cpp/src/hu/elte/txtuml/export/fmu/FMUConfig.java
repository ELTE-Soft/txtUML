package hu.elte.txtuml.export.fmu;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FMUConfig {

	String umlClassName;
	Optional<String> outputSignalConfig;
	List<VariableDefinition> outputVariables;
	
	Optional<String> inputSignalConfig;
	List<VariableDefinition> inputVariables;
	
	Map<String, Object> initialValues;
	
	String guid;
	
}
