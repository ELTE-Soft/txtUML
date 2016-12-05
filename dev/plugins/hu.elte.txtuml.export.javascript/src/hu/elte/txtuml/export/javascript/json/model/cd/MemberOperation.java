package hu.elte.txtuml.export.javascript.json.model.cd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.ParameterDirectionKind;

import hu.elte.txtuml.layout.visualizer.model.Direction;

public class MemberOperation extends ClassMember {
	@XmlAccessMethods(getMethodName = "getArgs")
	private List<Argument> args;
	@XmlAccessMethods(getMethodName = "getReturnType")
	private String returnType;

	protected MemberOperation() {
	}

	public MemberOperation(Operation op) {
		super(op);
		returnType = null;
		args = new ArrayList<Argument>();
		for (Parameter arg : op.getOwnedParameters()) {
			if (arg.getDirection() == ParameterDirectionKind.RETURN_LITERAL) {
				returnType = arg.getType().getName();
			} else {
				args.add(new Argument(arg));
			}
		}
	}

	public List<Argument> getArgs() {
		return args;
	}

	public String getReturnType() {
		return returnType;
	}

}
