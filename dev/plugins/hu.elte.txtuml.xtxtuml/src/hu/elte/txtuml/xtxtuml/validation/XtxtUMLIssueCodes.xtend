package hu.elte.txtuml.xtxtuml.validation;

/**
 * Issue codes to be used in validators and quick fix providers.
 */
class XtxtUMLIssueCodes {

	protected static val ISSUE_CODE_PREFIX = "hu.elte.txtuml.xtxtuml.validation.IssueCodes.";

	public static val NOT_UNIQUE_NAME = ISSUE_CODE_PREFIX + "not_unique_name";
	public static val NOT_UNIQUE_CONSTRUCTOR = ISSUE_CODE_PREFIX + "not_unique_constructor";
	public static val NOT_UNIQUE_OPERATION = ISSUE_CODE_PREFIX + "not_unique_operation";
	public static val NOT_UNIQUE_INITIAL_STATE = ISSUE_CODE_PREFIX + "not_unique_initial_state";
	public static val NOT_UNIQUE_STATE_ACTIVITY = ISSUE_CODE_PREFIX + "not_unique_state_activity";
	public static val NOT_UNIQUE_INITIAL_TRANSITION = ISSUE_CODE_PREFIX + "not_unique_initial_transition";
	public static val NOT_UNIQUE_TRANSITION_MEMBER = ISSUE_CODE_PREFIX + "not_unique_transition_member";
	public static val NOT_UNIQUE_PORT_MEMBER = ISSUE_CODE_PREFIX + "not_unique_port_member";
	public static val NOT_UNIQUE_CONNECTOR_END = ISSUE_CODE_PREFIX + "not_unique_connector_end";

	public static val INVALID_TYPE = ISSUE_CODE_PREFIX + "invalid_type";
	public static val TYPE_MISMATCH = ISSUE_CODE_PREFIX + "type_mismatch";

	public static val MISSING_RETURN = ISSUE_CODE_PREFIX + "missing_return";
	public static val UNDEFINED_OPERATION = ISSUE_CODE_PREFIX + "undefined_operation";
	public static val MISSING_OPERATION_PARENTHESES = ISSUE_CODE_PREFIX + "missing_operation_parentheses";
	public static val INVALID_SIGNAL_ACCESS = ISSUE_CODE_PREFIX + "invalid_signal_access";
	public static val NOT_REQUIRED_SIGNAL = ISSUE_CODE_PREFIX + "not_required_signal";
	public static val QUERIED_PORT_IS_NOT_OWNED = ISSUE_CODE_PREFIX + "queried_port_is_not_owned";
	public static val NOT_ACCESSIBLE_PORT = ISSUE_CODE_PREFIX + "not_accessible_port";
	public static val NOT_ACCESSIBLE_ASSOCIATION_END = ISSUE_CODE_PREFIX + "not_accessible_association_end";
	public static val NOT_NAVIGABLE_ASSOCIATION_END = ISSUE_CODE_PREFIX + "not_navigable_association_end";

	public static val MISPLACED_MODEL_DECLARATION = ISSUE_CODE_PREFIX + "misplaced_model_declaration";
	public static val WRONG_PACKAGE = ISSUE_CODE_PREFIX + "wrong_package";

	public static val CLASS_HIERARCHY_CYCLE = ISSUE_CODE_PREFIX + "class_hierarchy_cycle";
	public static val INVALID_CONSTRUCTOR_NAME = ISSUE_CODE_PREFIX + "invalid_constructor_name";
	public static val MISSING_INITIAL_STATE = ISSUE_CODE_PREFIX + "missing_initial_state";
	public static val NOT_LEAVABLE_PSEUDOSTATE = ISSUE_CODE_PREFIX + "not_leavable_pseudostate";
	public static val UNREACHABLE_STATE = ISSUE_CODE_PREFIX + "unreachable_state";
	public static val INVALID_STATE_CONTAINER = ISSUE_CODE_PREFIX + "wrong_state_container";
	public static val ELEMENT_IN_PSEUDOSTATE = ISSUE_CODE_PREFIX + "element_in_pseudostate";
	public static val MISSING_MANDATORY_TRANSITION_MEMBER = ISSUE_CODE_PREFIX + "missing_mandatory_transition_member";
	public static val TARGET_IS_INITIAL_STATE = ISSUE_CODE_PREFIX + "target_is_initial_state";
	public static val INVALID_TRANSITION_MEMBER = ISSUE_CODE_PREFIX + "invalid_transition_member";
	public static val INVALID_ELSE_GUARD = ISSUE_CODE_PREFIX + "invalid_else_guard";
	public static val NOT_BEHAVIOR_TRIGGER_PORT = ISSUE_CODE_PREFIX + "not_behavior_trigger_port";
	public static val NOT_OWNED_TRIGGER_PORT = ISSUE_CODE_PREFIX + "not_owned_trigger_port";
	public static val VERTEX_LEVEL_MISMATCH = ISSUE_CODE_PREFIX + "vertex_level_mismatch";

	public static val ASSOCIATION_END_COUNT_MISMATCH = ISSUE_CODE_PREFIX + "association_end_count_mismatch";
	public static val ASSOCIATION_CONTAINS_CONTAINER_END = ISSUE_CODE_PREFIX + "association_contains_container_end";
	public static val CONTAINER_END_COUNT_MISMATCH = ISSUE_CODE_PREFIX + "container_end_count_mismatch";

	/**
	 * Used by XtxtUMLDerivedResourceMarkerCopier to propagate JtxtUML validation errors to XtxtUML source.
	 */
	public static val COPY_JTXTUML_PROBLEMS = ISSUE_CODE_PREFIX + "copyJtxtUMLProblems";

	public static val CONNECTOR_ASSEMBLY_CONTAINS_CONTAINER_ROLE = "hu.elte.txtuml.xtxtuml.issues.ConnectorAssemblyContainsContainerRole";
	public static val CONNECTOR_CONTAINER_ROLE_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorContainerRoleCountMismatch";
	public static val CONNECTOR_INCOMPATIBLE_PORTS = "hu.elte.txtuml.xtxtuml.ConnectorIncompatiblePorts";
	public static val CONNECTOR_ROLE_COMPOSITION_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorRoleCompositionMismatch";

	public static val CONNECTOR_END_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorEndCountMismatch";
	public static val CONNECTOR_END_PORT_OWNER_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorEndPortOwnerMismatch";

}
