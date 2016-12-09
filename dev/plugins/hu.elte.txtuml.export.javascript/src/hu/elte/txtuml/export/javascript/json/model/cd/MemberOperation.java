package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;

/**
 * 
 * Holds information about an operation of a class
 *
 */
public class MemberOperation extends ClassMember {
	@XmlAccessMethods(getMethodName = "getArgs")
	private List<Argument> args;
	@XmlAccessMethods(getMethodName = "getReturnType")
	private String returnType;

	/**
	 * No-arg constructor required for serialization
	 */
	protected MemberOperation() {
	}

	/**
	 * Creates a MemberOperation based on the EMF-UML model-element provided
	 * 
	 * @param op
	 *            the EMF-UML model-element which holds informations of this
	 *            diagram element
	 */
	public MemberOperation(Operation op) {
		super(op.getName(), op.getVisibility());
		returnType = null;
		args = new ArrayList<Argument>();
		for (Parameter arg : op.getOwnedParameters()) {
			// return type is a parameter in the EMF-UML model
			if (arg.getDirection() == ParameterDirectionKind.RETURN_LITERAL) {
				returnType = arg.getType().getName();
			} else {
				args.add(new Argument(arg));
			}
		}
	}

	/**
	 * 
	 * @return the arguments of the operation
	 */
	public List<Argument> getArgs() {
		return args;
	}

	/**
	 * 
	 * @return the return type of the operation (null if void)
	 */
	public String getReturnType() {
		return returnType;
	}

}
