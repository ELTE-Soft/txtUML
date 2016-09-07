package hu.elte.txtuml.export.cpp.structural;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Operation;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

class ConstructorExporter {
	
	boolean ownConstructor;
	List<Operation> constructors;
	ActivityExporter activityExporter;
	
	ConstructorExporter(List<Operation> allOperation) {
		constructors = new ArrayList<Operation>();
		ownConstructor = false;
		
		activityExporter = new ActivityExporter();
		
		importConstructors(allOperation);
	}
	
	String exportConstructorsDefinitions(String className,boolean ownStateMachine) {
		StringBuilder source = new StringBuilder();
		for(Operation operation : constructors) {
			String body = activityExporter.createfunctionBody(Shared.getOperationActivity(operation));
			//TODO invoke base class
			source.append(GenerationTemplates.constructorDef(className, null, body,Shared.getOperationParams(operation), null, ownStateMachine));
			
		}
		return source.toString();
	}
	

	String exportConstructorDeclareations(String className) {
		StringBuilder source = new StringBuilder();
		if(ownConstructor) {
			for(Operation operation : constructors) {
				source.append(GenerationTemplates.constructorDecl(className, Shared.getOperationParamTypes(operation)));
				
			}
		}
		else {
			source.append(GenerationTemplates.defaultConstructorDecl(className));
		}
		
		return source.toString();
		
	}
	
	private void importConstructors(List<Operation> allOperation) {
		for(Operation op : allOperation) {
			if(Shared.isConstructor(op)) {
				constructors.add(op);
				ownConstructor = true;
			}
		}
	}

	
}
