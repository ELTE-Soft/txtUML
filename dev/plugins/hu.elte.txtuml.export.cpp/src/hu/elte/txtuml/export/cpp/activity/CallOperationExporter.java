package hu.elte.txtuml.export.cpp.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.CallOperationAction;
import org.eclipse.uml2.uml.InputPin;
import org.eclipse.uml2.uml.OutputPin;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.TestIdentityAction;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.OperatorTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.VariableTemplates;

class CallOperationExporter {

	private boolean containsSignalAcces;
	private boolean containsTimerOperator;

	private OutVariableExporter tempVariableExporter;
	private Map<CallOperationAction, OutputPin> returnOutputsToCallActions;
	private ActivityNodeResolver activityExportResolver;
	private Set<String> declaredTempVariables;

	public CallOperationExporter(OutVariableExporter tempVariableExporter,
			Map<CallOperationAction, OutputPin> returnOutputsToCallActions,
			ActivityNodeResolver activityExportResolver) {
		containsSignalAcces = false;
		containsTimerOperator = false;
		declaredTempVariables = new HashSet<String>();

		this.tempVariableExporter = tempVariableExporter;
		this.returnOutputsToCallActions = returnOutputsToCallActions;
		this.activityExportResolver = activityExportResolver;
	}

	public boolean isUsedSignalParameter() {
		return containsSignalAcces;
	}

	public boolean isInvokedTimerOperation() {
		return containsTimerOperator;
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
		tempVariableExporter.exportAllOutputPinToMap(node.getOutputs());
		OutputPin returnPin = searchReturnPin(node.getResults(), node.getOperation().outputParameters());
		if (returnPin != null)
			returnOutputsToCallActions.put(node, returnPin);

		if (Shared.isConstructor(node.getOperation())) {

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
			containsSignalAcces = true;
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
			source.append(declareAllOutTempParameter(outParamaterPins));
			List<String> parameterVariables = new ArrayList<String>(getParamNames(node.getArguments()));
			addOutParametrsToList(parameterVariables, outParamaterPins);

			val = ActivityTemplates.stdLibCall(node.getOperation().getName(), parameterVariables);
			if (OperatorTemplates.isTimerStart(node.getOperation().getName())) {
				containsTimerOperator = true;
			}

			if (node.getOperation().getType() != null) {
				if (node.getOutgoings().size() > 0) {
					returnTypeName = ((InputPin) node.getOutgoings().get(0).getTarget()).getType().getName();
				} else if (returnPin != null && returnPin.getOutgoings().size() > 0) {
					returnTypeName = ((InputPin) returnPin.getOutgoings().get(0).getTarget()).getType().getName();

				}

			}

		} else {

			val = ActivityTemplates.operationCall(activityExportResolver.getTargetFromInputPin(node.getTarget(), false),
					ActivityTemplates.accesOperatoForType(activityExportResolver.getTypeFromInputPin(node.getTarget())),
					node.getOperation().getName(), getParamNames(node.getArguments()));

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
				getParamNames(arguments));
	}

	private void addOutParametrsToList(List<String> parameterVariables, EList<OutputPin> outParamaterPins) {
		for (OutputPin outPin : outParamaterPins) {
			parameterVariables.add(tempVariableExporter.getRealVariableName(outPin));
		}
	}

	private StringBuilder declareAllOutTempParameter(EList<OutputPin> outParamaterPins) {
		StringBuilder declerations = new StringBuilder("");
		for (OutputPin outPin : outParamaterPins) {
			declerations.append(VariableTemplates.variableDecl(outPin.getType().getName(),
					tempVariableExporter.getRealVariableName(outPin), false));
		}

		return declerations;
	}

	private OutputPin searchReturnPin(EList<OutputPin> results, EList<Parameter> outputParameters) {
		for (int i = 0; i < Math.min(results.size(), outputParameters.size()); i++) {

			if (outputParameters.get(i).getDirection().equals(ParameterDirectionKind.RETURN_LITERAL)) {
				return results.get(i);
			}
		}
		return null;
	}

	private List<String> getParamNames(List<InputPin> arguments_) {
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
			return ActivityTemplates.addVariableTemplate(type, var, value);

		}
	}

	private boolean isStdLibOperation(CallOperationAction node) {
		return node.getTarget() == null;
	}

}
