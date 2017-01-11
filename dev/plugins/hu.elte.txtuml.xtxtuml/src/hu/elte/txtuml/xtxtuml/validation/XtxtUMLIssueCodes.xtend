package hu.elte.txtuml.xtxtuml.validation;

/**
 * Issue codes to be used in validators and quick fix providers.
 */
class XtxtUMLIssueCodes {

	protected static val ISSUE_CODE_PREFIX = "hu.elte.txtuml.xtxtuml.validation.IssueCodes.";

	// Name-related issues

	public static val RESERVED_NAME = ISSUE_CODE_PREFIX + "reserved_name";

	// Uniqueness-related issues

	public static val NOT_UNIQUE_NAME = ISSUE_CODE_PREFIX + "not_unique_name";
	public static val NOT_UNIQUE_CONSTRUCTOR = ISSUE_CODE_PREFIX + "not_unique_constructor";
	public static val NOT_UNIQUE_OPERATION = ISSUE_CODE_PREFIX + "not_unique_operation";
	public static val NOT_UNIQUE_INITIAL_STATE = ISSUE_CODE_PREFIX + "not_unique_initial_state";
	public static val NOT_UNIQUE_STATE_ACTIVITY = ISSUE_CODE_PREFIX + "not_unique_state_activity";
	public static val NOT_UNIQUE_INITIAL_TRANSITION = ISSUE_CODE_PREFIX + "not_unique_initial_transition";
	public static val NOT_UNIQUE_TRANSITION_MEMBER = ISSUE_CODE_PREFIX + "not_unique_transition_member";
	public static val NOT_UNIQUE_PORT_MEMBER = ISSUE_CODE_PREFIX + "not_unique_port_member";
	public static val NOT_UNIQUE_RECEPTION = ISSUE_CODE_PREFIX + "not_unique_reception";

	// Typing-related issues

	public static val INVALID_TYPE = ISSUE_CODE_PREFIX + "invalid_type";
	public static val TYPE_MISMATCH = ISSUE_CODE_PREFIX + "type_mismatch";

	// Expression-related issues

	public static val MISSING_RETURN = ISSUE_CODE_PREFIX + "missing_return";
	public static val UNDEFINED_OPERATION = ISSUE_CODE_PREFIX + "undefined_operation";
	public static val MISSING_OPERATION_PARENTHESES = ISSUE_CODE_PREFIX + "missing_operation_parentheses";
	public static val INVALID_SIGNAL_ACCESS = ISSUE_CODE_PREFIX + "invalid_signal_access";
	public static val NOT_REQUIRED_SIGNAL = ISSUE_CODE_PREFIX + "not_required_signal";
	public static val QUERIED_PORT_IS_NOT_OWNED = ISSUE_CODE_PREFIX + "queried_port_is_not_owned";
	public static val MISSING_CLASS_PROPERTY = ISSUE_CODE_PREFIX + "missing_class_property";
	public static val NOT_ACCESSIBLE_PORT = ISSUE_CODE_PREFIX + "not_accessible_port";
	public static val NOT_ACCESSIBLE_ASSOCIATION_END = ISSUE_CODE_PREFIX + "not_accessible_association_end";
	public static val NOT_NAVIGABLE_ASSOCIATION_END = ISSUE_CODE_PREFIX + "not_navigable_association_end";

	// File-related issues

	public static val MISPLACED_MODEL_DECLARATION = ISSUE_CODE_PREFIX + "misplaced_model_declaration";
	public static val WRONG_MODEL_INFO = ISSUE_CODE_PREFIX + "wrong_model_info";

	// Class-related issues

	public static val CLASS_HIERARCHY_CYCLE = ISSUE_CODE_PREFIX + "class_hierarchy_cycle";
	public static val INVALID_CONSTRUCTOR_NAME = ISSUE_CODE_PREFIX + "invalid_constructor_name";
	public static val MISSING_INITIAL_STATE = ISSUE_CODE_PREFIX + "missing_initial_state";
	public static val NOT_LEAVABLE_PSEUDOSTATE = ISSUE_CODE_PREFIX + "not_leavable_pseudostate";
	public static val UNREACHABLE_STATE = ISSUE_CODE_PREFIX + "unreachable_state";
	public static val STATE_OR_TRANSITION_IN_NOT_COMPOSITE_STATE = ISSUE_CODE_PREFIX + "state_or_transition_in_not_composite_state";
	public static val ACTIVITY_IN_PSEUDOSTATE = ISSUE_CODE_PREFIX + "activity_in_pseudostate";
	public static val MISSING_MANDATORY_TRANSITION_MEMBER = ISSUE_CODE_PREFIX + "missing_mandatory_transition_member";
	public static val TARGET_IS_INITIAL_STATE = ISSUE_CODE_PREFIX + "target_is_initial_state";
	public static val INVALID_TRANSITION_MEMBER = ISSUE_CODE_PREFIX + "invalid_transition_member";
	public static val INVALID_ELSE_GUARD = ISSUE_CODE_PREFIX + "invalid_else_guard";
	public static val NOT_BEHAVIOR_TRIGGER_PORT = ISSUE_CODE_PREFIX + "not_behavior_trigger_port";
	public static val NOT_OWNED_TRIGGER_PORT = ISSUE_CODE_PREFIX + "not_owned_trigger_port";
	public static val VERTEX_LEVEL_MISMATCH = ISSUE_CODE_PREFIX + "vertex_level_mismatch";

	// Association-related issues

	public static val ASSOCIATION_END_COUNT_MISMATCH = ISSUE_CODE_PREFIX + "association_end_count_mismatch";
	public static val CONTAINER_END_IN_ASSOCIATION = ISSUE_CODE_PREFIX + "container_end_in_association";
	public static val CONTAINER_END_COUNT_MISMATCH = ISSUE_CODE_PREFIX + "container_end_count_mismatch";
	public static val WRONG_ASSOCIATION_END_MULTIPLICITY = ISSUE_CODE_PREFIX + "wrong_association_end_multiplicity";

	// Connector-related issues

	public static val CONNECTOR_END_COUNT_MISMATCH = ISSUE_CODE_PREFIX + "connector_end_count_mismatch";
	public static val CONTAINER_ROLE_COUNT_MISMATCH = ISSUE_CODE_PREFIX + "container_role_count_mismatch";
	public static val CONTAINER_ROLE_IN_ASSSEMBLY_CONNECTOR = ISSUE_CODE_PREFIX + "container_role_in_assembly_connector";
	public static val COMPOSITION_MISMATCH_IN_DELEGATION_CONNECTOR = ISSUE_CODE_PREFIX + "composition_mismatch_in_delegation_connector";
	public static val COMPOSITION_MISMATCH_IN_ASSEMBLY_CONNECTOR = ISSUE_CODE_PREFIX + "composition_mismatch_in_assembly_connector";
	public static val INCOMPATIBLE_PORTS = ISSUE_CODE_PREFIX + "incompatible_ports";
	public static val NOT_OWNED_PORT = ISSUE_CODE_PREFIX + "not_owned_port";

	// UI-related issues
	
	public static val WRONG_PACKAGE = ISSUE_CODE_PREFIX + "wrong_package";

	/**
	 * Used by {@link XtxtUMLDerivedResourceMarkerCopier} to propagate JtxtUML validation errors to XtxtUML source.
	 */
	public static val COPY_JTXTUML_PROBLEMS = ISSUE_CODE_PREFIX + "copy_jtxtuml_problems";

}
