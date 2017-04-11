package hu.elte.txtuml.validation.model;

import hu.elte.txtuml.validation.common.MessageLoader;

public final class Messages {
	public static final MessageLoader LOADER;

	public static String ModelVisitor_association_label;
	public static String ModelVisitor_class_label;
	public static String ModelVisitor_signal_label;
	public static String VisitorBase_composite_state_label;
	public static String VisitorBase_initial_state_label;
	public static String VisitorBase_state_label;
	public static String VisitorBase_transition_label;

	static {
		// Create loader and load messages into fields.
		LOADER = new MessageLoader(Messages.class.getPackage().getName());
		LOADER.fillFields(Messages.class);
	}
}
