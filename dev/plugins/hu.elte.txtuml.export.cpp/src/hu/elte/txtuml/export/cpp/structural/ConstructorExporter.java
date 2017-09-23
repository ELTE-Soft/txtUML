package hu.elte.txtuml.export.cpp.structural;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Operation;

import hu.elte.txtuml.export.cpp.ActivityExportResult;
import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.structual.ConstructorTemplates;

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

	String exportConstructorsDefinitions(String className, boolean ownStateMachine) {
		StringBuilder source = new StringBuilder();
		for (Operation operation : constructors) {
			ActivityExportResult activityResult = activityExporter.createFunctionBody(CppExporterUtils.getOperationActivity(operation));
			source.append(
					ConstructorTemplates.constructorDef(className, CppExporterUtils.getOperationParamNames(operation),
							CppExporterUtils.getOperationParams(operation)));
			source.append(ConstructorTemplates.initDef(className, activityResult.getActivitySource(), CppExporterUtils.getOperationParams(operation),
					ownStateMachine));

		}
		return source.toString();
	}

	String exportConstructorDeclarations(String className) {
		StringBuilder source = new StringBuilder();
		if (ownConstructor) {
			for (Operation operation : constructors) {
				source.append(ConstructorTemplates.constructorDecl(className,
						CppExporterUtils.getOperationParamTypes(operation)));
				source.append(
						ConstructorTemplates.initDecl(className, CppExporterUtils.getOperationParamTypes(operation)));

			}
		} else {
			source.append(ConstructorTemplates.defaultConstructorDecl(className));
		}

		return source.toString();

	}

	private void importConstructors(List<Operation> allOperation) {
		for (Operation op : allOperation) {
			if (CppExporterUtils.isConstructor(op)) {
				constructors.add(op);
				ownConstructor = true;
			}
		}
	}

}
