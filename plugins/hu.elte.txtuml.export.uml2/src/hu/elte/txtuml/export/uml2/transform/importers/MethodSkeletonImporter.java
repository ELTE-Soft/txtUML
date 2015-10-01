package hu.elte.txtuml.export.uml2.transform.importers;

import hu.elte.txtuml.export.uml2.utils.ElementModifiersAssigner;

import java.util.AbstractMap;
import java.util.Map;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.OpaqueBehavior;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;

public class MethodSkeletonImporter {

	private TypeImporter typeImporter;
	private org.eclipse.uml2.uml.Class owner;
	
	/**
	 * Creates a <code>MethodImporter</code> instance.
	 * 
	 * @param typeImporter
	 *            The type importer used.
	 * @param owner
	 *            The owning class.
	 */
	public MethodSkeletonImporter(TypeImporter typeImporter, org.eclipse.uml2.uml.Class owner) {
		this.typeImporter = typeImporter;
		this.owner = owner;
	}
	
	/**
	 * Obtains the parameters from specified method declaration.
	 * 
	 * @param methodDeclaration
	 *            The specified method declaration.
	 * @return A map entry: key - list of param names, value - list of param
	 *         types
	 *
	 * @author Ádám Ancsin
	 */
	private Map.Entry<EList<String>, EList<Type>> obtainMethodParameters(MethodDeclaration methodDeclaration) {
		EList<String> paramNames = new BasicEList<>();
		EList<Type> paramTypes = new BasicEList<>();
		for(Object param : methodDeclaration.parameters()) {
			if(param instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration paramDeclaration = (SingleVariableDeclaration) param;
				String paramName = paramDeclaration.getName().getFullyQualifiedName();
				paramNames.add(paramName);
				Type paramType = this.typeImporter.importType(paramDeclaration.getType());
				paramTypes.add(paramType);
			}
		}
		return new AbstractMap.SimpleEntry<>(paramNames, paramTypes);
	}
	
	/**
	 * Imports a method skeleton from the specified method declaration.
	 * 
	 * @param methodDeclaration
	 *            The specified method declaration.
	 * @return The result UML2 operation.
	 *
	 * @author Ádám Ancsin
	 */
	public Operation importMethodSkeleton(MethodDeclaration methodDeclaration) {
		Operation operation = createMethodSkeleton(methodDeclaration);
		ElementModifiersAssigner.assignModifiersForElementBasedOnDeclaration(operation, methodDeclaration);
		return operation;
	}
		
	/**
	 * Creates the method skeleton based on the specified method declaration.
	 * 
	 * @param methodDeclaration
	 *            The specified method declaration.
	 * @return The created UML2 operation.
	 *
	 * @author Ádám Ancsin
	 */
	private Operation createMethodSkeleton(MethodDeclaration methodDeclaration) {
		String methodName = methodDeclaration.getName().getFullyQualifiedName();
		
		Map.Entry<EList<String>, EList<Type>> params = obtainMethodParameters(methodDeclaration);
		EList<String> paramNames = params.getKey();
		EList<Type> paramTypes = params.getValue();
		Type returnType = obtainReturnType(methodDeclaration);
		return this.owner.createOwnedOperation(methodName, paramNames, paramTypes, returnType);
	}

	/**
	 * Obtains the return type of the specified method declaration.
	 * 
	 * @param methodDeclaration
	 *            The specified method declaration.
	 * @return The return type (UML2).
	 *
	 * @author Ádám Ancsin
	 */
	private Type obtainReturnType(MethodDeclaration methodDeclaration) {
		return this.typeImporter.importType(methodDeclaration.getReturnType2());
	}

	/**
	 * Creates an opaque behavior for an operation.
	 * 
	 * @param specification		The opration.
	 * @param body				The body text.
	 * @return					The behavior created.
	 */
	public Behavior createOwnedBehavior(Operation specification, String body) {
		String behaviorName = specification.getName() + "_opaqueBehavior";
		OpaqueBehavior behavior = (OpaqueBehavior)this.owner.createOwnedBehavior(behaviorName, UMLPackage.Literals.OPAQUE_BEHAVIOR);
		behavior.setSpecification(specification);
		behavior.getBodies().add(body);
		behavior.getLanguages().add("JtxtUML");
		return null;
	}
}
