package hu.elte.txtuml.export.cpp.activity;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.OutputPin;

import hu.elte.txtuml.export.cpp.templates.ActivityTemplates;

class OutVariableExporter {
	
	private Map<OutputPin, String> outTempVariables;
	private int tempVariableCounter;
	
	public OutVariableExporter() {
		outTempVariables = new HashMap<OutputPin, String>();
		tempVariableCounter = 0;
	}
	
	
	public void exportOutputPinToMap(OutputPin out) {
		if (!outTempVariables.containsKey(out)) {
			outTempVariables.put(out, ActivityTemplates.TempVar + tempVariableCounter);			
			tempVariableCounter++;
		}

	}
	
	public void exportAllOutputPinToMap(EList<OutputPin> outputs) {
		for (OutputPin outPin : outputs) {
			exportOutputPinToMap(outPin);
		}

	}
	
	public Boolean outIsExported(OutputPin out) {
		return outTempVariables.containsKey(out);
	}
	
	public String getRealVariableName(OutputPin out) {
		assert(outTempVariables.containsKey(out));
		
		return outTempVariables.get(out);
	}

}
