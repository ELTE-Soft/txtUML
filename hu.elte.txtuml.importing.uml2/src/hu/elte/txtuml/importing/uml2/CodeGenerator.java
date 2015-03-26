package hu.elte.txtuml.importing.uml2;

import java.util.ArrayList;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UMLPackage;

public class CodeGenerator {
	public String AssociationInString(Association a) {
		return "class " + a.getName() + " extends Association {";
	}

	public String SignalInString(Signal s) {
		return "class " + s.getName() + " extends Signal {";
	}

	public String ClassInString(Class c) {
		return "class " + c.getName() + " extends ModelClass {";
	}

	public String PseudostateInString(Pseudostate ps) {
		return "class " + ps.getName() + " extends Initial {";
	}

	public String StateInString(State s) {
		if (s.isComposite()) {
			return "class " + s.getName() + " extends CompositeState {";
		}

		return "class " + s.getName() + " extends State {";
	}

	public String TransitionInString(Transition t, int index,
			JavaContentCustom jcc) {
		String ret = "";

		String triggerName = "";
		if (t.getTriggers().size() > 0) {
			triggerName = t.getTriggers().get(0).getName();

			if (jcc.isMultipleName(triggerName)) {
				triggerName = StringCalculator.fromQualifiedName(t
						.getTriggers().get(0).getQualifiedName());
			}
		}

		String source = t.getSource().getName();
		if (jcc.isMultipleName(source)) {
			source = StringCalculator.fromQualifiedName(t.getSource()
					.getQualifiedName());
		}
		ret += "@From(" + source + ".class) ";

		String target = t.getTarget().getName();
		if (jcc.isMultipleName(target)) {
			target = StringCalculator.fromQualifiedName(t.getTarget()
					.getQualifiedName());
		}
		ret += "@To(" + target + ".class) ";

		if (t.getTriggers().size() > 0) {
			ret += "@Trigger(" + triggerName + ".class) ";
		}

		ret += "\n";

		for (int i = 0; i < 4 * index; ++i) {
			ret += " ";
		}

		ret += "class " + t.getName() + " extends Transition {";

		return ret;
	}

	public String OperationInString(Operation o) {
		String ret = "";

		if (o.getType() != null) {
			ret += o.getType().getName();
		} else {
			ret += "void";
		}

		ret += " " + o.getName() + "(";
		ArrayList<Parameter> p = new ArrayList<Parameter>();
		p.addAll(o.getOwnedParameters());
		for (int i = 0; i < p.size() - 1; ++i) {
			if (i > 0) {
				ret += ", ";
			}
			ret += p.get(i).getType().getName() + " " + p.get(i).getLabel();
		}
		ret += ") {";

		return ret;
	}

	public String PropertyInString(Property p) {
		// TODO: refactor: associationEnd(ownedByTheClass, ownedByTheAssoc)
		if (p.getOwner().eClass().equals(UMLPackage.Literals.ASSOCIATION)) {
			String ret = "class " + p.getName() + " extends ";

			if (p.isMultivalued()) {
				ret += "Many";
			} else {
				ret += "One";
			}

			ret += "<" + p.getType().getName() + "> {}";

			return ret;
		} else if (p.getType().toString().contains("Integer")) {
			return "ModelInt " + p.getName() + ";";
		} else if (p.getType().toString().contains("Bool")) {
			return "ModelBool " + p.getName() + ";";
		} else if (p.getType().toString().contains("String")) {
			return "ModelString " + p.getName() + ";";
		}

		return "OtherProperty " + p.getName();
	}
}
