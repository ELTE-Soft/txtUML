package hu.elte.txtuml.export.cpp.activity;

import java.util.Map;
import java.util.Optional;

import org.eclipse.uml2.uml.CreateObjectAction;
import org.eclipse.uml2.uml.DestroyObjectAction;
import org.eclipse.uml2.uml.SendObjectAction;
import org.eclipse.uml2.uml.StartClassifierBehaviorAction;
import org.eclipse.uml2.uml.StartObjectBehaviorAction;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.IDependencyCollector;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates.CreateObjectType;

class ObjectActionExporter {

	private Map<CreateObjectAction, String> objectMap;
	private OutVariableExporter tempVariableExporter;
	private ActivityNodeResolver activityExportResolver;
	private Optional<IDependencyCollector> exportUser;

	ObjectActionExporter(OutVariableExporter tempVariableExporter, Map<CreateObjectAction, String> objectMap,
			ActivityNodeResolver activityExportResolver, Optional<IDependencyCollector> exportUser) {

		this.tempVariableExporter = tempVariableExporter;
		this.activityExportResolver = activityExportResolver;
		this.objectMap = objectMap;
		this.exportUser = exportUser;
	}

	public String createCreateObjectActionCode(CreateObjectAction createObjectActionNode) {
		String type = createObjectActionNode.getClassifier().getName();

		ActivityTemplates.CreateObjectType objectType;
		if (createObjectActionNode.getClassifier().eClass().equals(UMLPackage.Literals.SIGNAL)) {
			objectType = ActivityTemplates.CreateObjectType.Signal;
		} else {
			objectType = CreateObjectType.Class;
			if(exportUser.isPresent()) {
				exportUser.get().addCppOnlyDependency(type);
			}
		}

		tempVariableExporter.exportOutputPinToMap(createObjectActionNode.getResult());
		String name = tempVariableExporter.getRealVariableName(createObjectActionNode.getResult());
		objectMap.put(createObjectActionNode, name);
		
		return ActivityTemplates.createObject(type, name, objectType);
	}

	public String createDestroyObjectActionCode(DestroyObjectAction node) {
			return ActivityTemplates.deleteObject(activityExportResolver.getTargetFromInputPin(node.getTarget()));
	}

	public String createStartObjectActionCode(StartClassifierBehaviorAction node) {
		return ActivityTemplates.startObject(activityExportResolver.getTargetFromInputPin(node.getObject()));
	}

	public String createStartObjectActionCode(StartObjectBehaviorAction node) {
		return ActivityTemplates.startObject(activityExportResolver.getTargetFromInputPin(node.getObject()));
	}

	public String createSendSignalActionCode(SendObjectAction sendObjectAction) {

		String target = activityExportResolver.getTargetFromInputPin(sendObjectAction.getTarget());
		String singal = activityExportResolver.getTargetFromInputPin(sendObjectAction.getRequest());

		return ActivityTemplates.signalSend(target, singal);

	}
}
