package hu.elte.txtuml.importing.uml2;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.NamedElement;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Pseudostate;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.State;
import org.eclipse.uml2.uml.Transition;
import org.eclipse.uml2.uml.UMLPackage;

public class CodeRecognizer {
	public ArrayList<String> getAllOwnedElements(Element element) {
		ArrayList<String> elements = new ArrayList<>();

		for (Element e : element.allOwnedElements()) {
			if (LiteralTypes.isType(e.eClass())) {
				elements.add(((NamedElement) e).getName());
			}
		}

		return elements;
	}

	public ArrayList<Element> getOwnedElements(Element element) {
		ArrayList<Element> elements = new ArrayList<>();

		for (Element e : element.getOwnedElements()) {
			if (e.eClass().equals(UMLPackage.Literals.STATE_MACHINE)) {
				elements.addAll(getOwnedElements(e));
			} else if (e.eClass().equals(UMLPackage.Literals.REGION)) {
				elements.addAll(getOwnedElements(e));
			} else if (LiteralTypes.isType(e.eClass())) {
				elements.add(e);
			}
		}

		return elements;
	}

	public String getCode(JavaContentCustom jcc, Element element, int index,
			EClass eclass) {
		String code = "";

		for (Element e : element.getOwnedElements()) {
			if (e.eClass().equals(UMLPackage.Literals.STATE_MACHINE)) {
				code += getCode(jcc, e, index, eclass);
			} else if (e.eClass().equals(UMLPackage.Literals.REGION)) {
				code += getCode(jcc, e, index, eclass);
			} else if (eclass.equals(e.eClass())) {
				code += generate(e, index, jcc) + '\n';

				code += jcc.getContent(e);

				if (!e.eClass().equals(LiteralTypes.Property)) {
					for (int i = 0; i < 4 * index; ++i) {
						code += " ";
					}
					code += "}\n\n";
				}
			}
		}

		return code;
	}

	public String generate(Element e, int index, JavaContentCustom jcc) {
		CodeGenerator cg = new CodeGenerator();

		String result = "";
		for (int i = 0; i < 4 * index; ++i) {
			result += " ";
		}

		if (e.eClass().equals(LiteralTypes.Association)) {
			result += cg.AssociationInString((Association) e);
		} else if (e.eClass().equals(LiteralTypes.Class)) {
			result += cg.ClassInString((Class) e);
		} else if (e.eClass().equals(LiteralTypes.Pseudostate)) {
			result += cg.PseudostateInString((Pseudostate) e);
		} else if (e.eClass().equals(LiteralTypes.State)) {
			result += cg.StateInString((State) e);
		} else if (e.eClass().equals(LiteralTypes.Signal)) {
			result += cg.SignalInString((Signal) e);
		} else if (e.eClass().equals(LiteralTypes.Transition)) {
			result += cg.TransitionInString((Transition) e, index, jcc);
		} else if (e.eClass().equals(LiteralTypes.Operation)) {
			result += cg.OperationInString((Operation) e);
		} else if (e.eClass().equals(LiteralTypes.Property)) {
			result += cg.PropertyInString((Property) e);
		} else {
			result += "Error: element '" + ((NamedElement) e).getName()
					+ "' is not recognized.";
		}

		return result;
	}
}
