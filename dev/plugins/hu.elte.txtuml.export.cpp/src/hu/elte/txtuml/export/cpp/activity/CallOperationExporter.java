package hu.elte.txtuml.export.cpp.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.TestIdentityAction;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.IDependencyCollector;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.OperatorTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.ObjectDeclDefTemplates;

class CallOperationExporter {

	private OutVariableExporter tempVariableExporter;
	private Map<CallOperationAction, OutputPin> returnOutputsToCallActions;
	private ActivityNodeResolver activityExportResolver;
	private Set<String> declaredTempVariables;
	private Optional<IDependencyCollector> exportUser;

	public CallOperationExporter(OutVariableExporter tempVariableExporter,
			Map<CallOperationAction, OutputPin> returnOutputsToCallActions, ActivityNodeResolver activityExportResolver,
			Optional<IDependencyCollector> exportUser) {
		declaredTempVariables = new HashSet<String>();

		this.tempVariableExporter = tempVariableExporter;
		this.returnOutputsToCallActions = returnOutputsToCallActions;
		this.activityExportResolver = activityExportResolver;
		this.exportUser = exportUser;
	}

	public String createTestIdentityActionCode(TestIdentityAction node) {
		InputPin firstArgument = node.getInputs().get(0);
		InputPin secondArgument = node.getInputs().get(1);
		tempVariableExporter.exportOutputPinToMap(node.getResult());

		String val = ActivityTemplates.isEqualTesting(activityExportResolver.getTargetFromInputPin(firstArgument),
				activityExportResolver.getTargetFromInputPin(secondArgument));

		return addValueToTemporalVariable(node.getResult().getType().getName(),
				tempVariableExporter.getRealVariableName(node.getResult()), val);
	}

	public String createCallOperationActionCode(CallOperationAction node) {
		StringBuilder source = new StringBuilder("");
		tempVariableExporter.exportAllOutputPinsToMap(node.getOutputs());
		OutputPin returnPin = searchReturnPin(node.getResults(), node.getOperation().outputParameters());
		if (returnPin != null)
			returnOutputsToCallActions.put(node, returnPin);

		if (CppExporterUtils.isConstructor(node.getOperation())) {

			/*
			 * In case of signal factory's constructor the first parameter is
			 * the signal type which is the target.
			 */
			InputPin target = node.getTarget() == null ? node.getArguments().get(0) : node.getTarget();
			EList<InputPin> arguments = node.getArguments();
			arguments.remove(target);
			return ActivityTemplates.blockStatement(createConstructorCallAction(target, arguments));
		}

		if (node.getOperation().getName().equals(ActivityTemplates.GetSignalFunctionName)) {
			return ActivityTemplates.getRealSignal(returnPin.getType().getName(),
					tempVariableExporter.getRealVariableName(returnPin));
		}
		String val = "";
		String returnTypeName = "";
		if (node.getOperation().getType() != null)
			returnTypeName = node.getOperation().getType().getName();

		if (isStdLibOperation(node)) {

			EList<OutputPin> outParamaterPins = node.getResults();
			outParamaterPins.remove(returnPin);
			source.append(declareAllOutTempParameters(outParamaterPins));
			List<String> parameterVariables = new ArrayList<String>(getParametersNames(node.getArguments()));
			addOutParametrsToList(parameterVariables, outParamaterPins);

			val = ActivityTemplates.stdLibCall(node.getOperation().getName(), parameterVariables);

			if (OperatorTemplates.isTimerStart(node.getOperation().getName())) {
				if (exportUser.isPresent()) {
					exportUser.get().addCppOnlyDependency(FileNames.TimerInterfaceHeader);
					exportUser.get().addCppOnlyDependency(FileNames.TimerHeader);
				}

			}

			if (node.getOperation().getType() != null) {
				if (node.getOutgoings().size() > 0) {
					returnTypeName = ((InputPin) node.getOutgoings().get(0).getTarget()).getType().getName();
				} else if (returnPin != null && returnPin.getOutgoings().size() > 0) {
					returnTypeName = ((InputPin) returnPin.getOutgoings().get(0).getTarget()).getType().getName();

				}

			}

		} else {

			Operation op = node.getOperation();

			Element opOwner = op.getOwner();
			if (opOwner instanceof NamedElement) {
				NamedElement namedOwner = (NamedElement) opOwner;
				if (exportUser.isPresent()) {
					exportUser.get().addCppOnlyDependency(namedOwner.getName());
				}
			}

			val = ActivityTemplates.operationCall(activityExportResolver.getTargetFromInputPin(node.getTarget(), false),
					ActivityTemplates.accesOperatoForType(activityExportResolver.getTypeFromInputPin(node.getTarget())),
					op.getName(), getParametersNames(node.getArguments()));

		}

		if (returnPin != null) {
			source.append(addValueToTemporalVariable(returnTypeName,
					tempVariableExporter.getRealVariableName(returnPin), val));
		} else {
			source.append(ActivityTemplates.blockStatement(val));
		}

		return source.toString();

	}

	private String createConstructorCallAction(InputPin target, EList<InputPin> arguments) {

		return ActivityTemplates.constructorCall(activityExportResolver.getTargetFromInputPin(target, false),
				target.getType().getName(), target.getType().eClass().equals(UMLPackage.Literals.SIGNAL)
						? ActivityTemplates.CreateObjectType.Signal : ActivityTemplates.CreateObjectType.Class,
				getParametersNames(arguments));
	}

	private void addOutParametrsToList(List<String> parameterVariables, EList<OutputPin> outParamaterPins) {
		for (OutputPin outPin : outParamaterPins) {
			parameterVariables.add(tempVariableExporter.getRealVariableName(outPin));
		}
	}

	private String declareAllOutTempParameters(EList<OutputPin> outParamaterPins) {
		StringBuilder declerations = new StringBuilder("");
		for (OutputPin outPin : outParamaterPins) {
			declerations.append(ObjectDeclDefTemplates.variableDecl(outPin.getType().getName(),
					tempVariableExporter.getRealVariableName(outPin), GenerationTemplates.VariableType.Default));
		}

		return declerations.toString();
	}

	private OutputPin searchReturnPin(EList<OutputPin> results, EList<Parameter> outputParameters) {
		for (int i = 0; i < Math.min(results.size(), outputParameters.size()); i++) {

			if (outputParameters.get(i).getDirection().equals(ParameterDirectionKind.RETURN_LITERAL)) {
				return results.get(i);
			}
		}
		return null;
	}

	private List<String> getParametersNames(List<InputPin> arguments_) {
		List<String> params = new ArrayList<String>();
		for (InputPin param : arguments_) {
			params.add(activityExportResolver.getTargetFromInputPin(param));
		}
		return params;
	}

	private String addValueToTemporalVariable(String type, String var, String value) {
		if (declaredTempVariables.contains(var)) {
			return ActivityTemplates.simpleSetValue(var, value);
		} else {
			declaredTempVariables.add(var);
			return ActivityTemplates.addVariableTemplate(type, var, value, GenerationTemplates.VariableType.Default);

		}
	}

	private boolean isStdLibOperation(CallOperationAction node) {
		return node.getTarget() == null;
	}

}
