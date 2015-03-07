package hu.elte.txtuml.importing.uml2;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.uml2.uml.UMLPackage;

public class LiteralTypes {
	public static final EClass Class = UMLPackage.Literals.CLASS;
	public static final EClass Property = UMLPackage.Literals.PROPERTY;
	public static final EClass Pseudostate = UMLPackage.Literals.PSEUDOSTATE;
	public static final EClass State = UMLPackage.Literals.STATE;
	public static final EClass Association = UMLPackage.Literals.ASSOCIATION;
	public static final EClass Operation = UMLPackage.Literals.OPERATION;
	public static final EClass Transition = UMLPackage.Literals.TRANSITION;
	public static final EClass Signal = UMLPackage.Literals.SIGNAL;

	public static boolean isType(EClass eClass) {
		return eClass.equals(Class) || eClass.equals(Property)
				|| eClass.equals(Pseudostate) || eClass.equals(State)
				|| eClass.equals(Association) || eClass.equals(Operation)
				|| eClass.equals(Transition) || eClass.equals(Signal);
	}
}
