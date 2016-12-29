package hu.elte.txtuml.export.fmu;

import java.util.List;
import java.util.Optional;

public class FMUConfig {

	String umlClassName;
	Optional<FMUOutputConfig> outputSignalConfig;
	List<VariableDefinition> outputVariables;
	
	Optional<String> inputSignalConfig;
	List<VariableDefinition> inputVariables;
	
	String guid;
	
}
