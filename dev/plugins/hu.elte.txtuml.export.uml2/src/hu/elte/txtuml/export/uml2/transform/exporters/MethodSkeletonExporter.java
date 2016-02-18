package hu.elte.txtuml.export.uml2.transform.exporters;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;

import hu.elte.txtuml.export.uml2.transform.backend.ExporterConfiguration;
import hu.elte.txtuml.export.uml2.utils.ElementModifiersAssigner;

public class MethodSkeletonExporter {

	private final TypeExporter typeExporter;
	private final Class owner;

	/**
	 * Creates a <code>MethodExporter</code> instance.
	 * 
	 * @param typeExporter
	 *            The type exporter used.
	 * @param owner
	 *            The owning class.
	 */
	public MethodSkeletonExporter(TypeExporter typeExporter, Class owner) {
		this.typeExporter = typeExporter;
		this.owner = owner;
	}

	/**
	 * Obtains the parameters from specified method declaration.
	 * 
	 * @param methodDeclaration
	 *            The specified method declaration.
	 * @return A map entry: key - list of param names, value - list of param
	 *         types
	 */
	private Iterable<Parameter> obtainMethodParameters(
			MethodDeclaration methodDeclaration) {
		List<Parameter> parameters = new LinkedList<>();

		for (Object obj : methodDeclaration.parameters()) {
			if (obj instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration paramDeclaration = (SingleVariableDeclaration) obj;
				String paramName = paramDeclaration.getName()
						.getFullyQualifiedName();
				Type paramType = typeExporter.exportType(paramDeclaration
						.getType());

				Parameter param = UMLFactory.eINSTANCE.createParameter();
				param.setName(paramName);
				param.setType(paramType);
				param.setDirection(ParameterDirectionKind.IN_LITERAL);
				parameters.add(param);

				// TODO out, in/out parameters

			}
		}
		
		Type returnType = obtainReturnType(methodDeclaration);
		if (methodDeclaration.isConstructor()) {
			returnType = owner;
		}
		if (returnType != null) {
			Parameter returnParam = UMLFactory.eINSTANCE.createParameter();
			returnParam.setName(ExporterConfiguration.RETURN_PARAMETER_NAME);
			returnParam.setType(returnType);
			returnParam.setDirection(ParameterDirectionKind.RETURN_LITERAL);
			parameters.add(returnParam);
		}

		return parameters;
	}

	/**
	 * Obtains the return type of the specified method declaration.
	 * 
	 * @param methodDeclaration
	 *            The specified method declaration.
	 * @return The return type (UML2).
	 */
	private Type obtainReturnType(MethodDeclaration methodDeclaration) {
		return typeExporter.exportType(methodDeclaration.getReturnType2());
	}

	/**
	 * Exports a method skeleton from the specified method declaration.
	 * 
	 * @param methodDeclaration
	 *            The specified method declaration.
	 * @return The result UML2 operation.
	 */
	public Operation exportMethodSkeleton(MethodDeclaration methodDeclaration) {
		Operation operation = createMethodSkeleton(methodDeclaration);
		ElementModifiersAssigner.assignModifiersForElementBasedOnDeclaration(
				operation, methodDeclaration);
		return operation;
	}

	/**
	 * Creates the method skeleton based on the specified method declaration.
	 * 
	 * @param methodDeclaration
	 *            The specified method declaration.
	 * @return The created UML2 operation.
	 */
	private Operation createMethodSkeleton(MethodDeclaration methodDeclaration) {
		String methodName = methodDeclaration.getName().getFullyQualifiedName();

		Operation operation = owner.createOwnedOperation(methodName, null,
				null, null);

		obtainMethodParameters(methodDeclaration).forEach(
				operation.getOwnedParameters()::add);

		return operation;
	}

	/**
	 * Creates an opaque behavior for an operation.
	 * 
	 * @param specification
	 *            The opration.
	 * @param body
	 *            The body text.
	 * @return The behavior created.
	 */
	public Behavior createOwnedBehavior(Operation specification, String body) {
		String behaviorName = specification.getName() + "_opaqueBehavior";
		OpaqueBehavior behavior = (OpaqueBehavior) owner.createOwnedBehavior(
				behaviorName, UMLPackage.Literals.OPAQUE_BEHAVIOR);
		behavior.setSpecification(specification);
		behavior.getBodies().add(body);
		behavior.getLanguages().add("JtxtUML");
		return null;
	}
}
