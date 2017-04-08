package hu.elte.txtuml.validation;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "hu.elte.txtuml.validation.messages"; //$NON-NLS-1$
	public static String InvalidChildrenElement_message;
	public static String InvalidDataTypeField_message;
	public static String InvalidModelClassElement_message;
	public static String InvalidModifier_message;
	public static String InvalidSignalContent_message;
	public static String InvalidTemplate_message;
	public static String InvalidTypeInModel_message;
	public static String InvalidParameterType_message;
	public static String InvalidAttributeType_message;
	public static String MissingTransitionSource_message;
	public static String MissingTransitionTarget_message;
	public static String MissingTransitionTrigger_message;
	public static String ModelVisitor_association_label;
	public static String ModelVisitor_class_label;
	public static String ModelVisitor_signal_label;
	public static String MutableDataTypeField_message;
	public static String StateMethodParameters_message;
	public static String TransitionFromOutside_message;
	public static String TransitionMethodNonVoidReturn_message;
	public static String TransitionMethodParameters_message;
	public static String TransitionToOutside_message;
	public static String TriggerOnInitialTransition_message;
	public static String UnknownClassInState_message;
	public static String UnknownStateMethod_message;
	public static String UnknownTransitionMethod_message;
	public static String VisitorBase_composite_state_label;
	public static String VisitorBase_initial_state_label;
	public static String VisitorBase_state_label;
	public static String VisitorBase_transition_label;
	public static String WrongCompositionEnds_message;
	public static String WrongNumberOfAssociationEnds_message;
	public static String WrongTypeInAssociation_message;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
