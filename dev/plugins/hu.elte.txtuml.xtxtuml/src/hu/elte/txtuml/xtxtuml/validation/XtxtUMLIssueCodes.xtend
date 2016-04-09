package hu.elte.txtuml.xtxtuml.validation;

/**
 * Issue codes to be used in validators and quick fix providers.
 */
class XtxtUMLIssueCodes {

	protected static val ISSUE_CODE_PREFIX = "hu.elte.txtuml.xtxtuml.validation.IssueCodes.";

	public static val NOT_UNIQUE_NAME = ISSUE_CODE_PREFIX + "not_unique_name";
	public static val NOT_UNIQUE_OPERATION = ISSUE_CODE_PREFIX + "not_unique_operation";
	public static val NOT_UNIQUE_CONNECTOR_END = ISSUE_CODE_PREFIX + "not_unique_connector_end";

	public static val INVALID_TYPE = ISSUE_CODE_PREFIX + "invalid_type";
	public static val TYPE_MISMATCH = ISSUE_CODE_PREFIX + "type_mistmatch";

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

	/**
	 * Used by XtxtUMLDerivedResourceMarkerCopier to propagate JtxtUML validation errors to XtxtUML source.
	 */
	public static val COPY_JTXTUML_PROBLEMS = ISSUE_CODE_PREFIX + "copyJtxtUMLProblems";

	public static val CLASS_HIERARCHY_CYCLE = ISSUE_CODE_PREFIX + "classHierarchyCycle";

	public static val ASSOCIATION_END_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.AssociationEndCountMismatch";
	public static val ASSOCIATION_CONTAINS_CONTAINER_END = "hu.elte.txtuml.xtxtuml.issues.AssociationContainsContainerEnd";
	public static val CONTAINER_END_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.CompositionMissesContainerEnd";

	public static val CONNECTOR_ASSEMBLY_CONTAINS_CONTAINER_ROLE = "hu.elte.txtuml.xtxtuml.issues.ConnectorAssemblyContainsContainerRole";
	public static val CONNECTOR_CONTAINER_ROLE_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorContainerRoleCountMismatch";
	public static val CONNECTOR_INCOMPATIBLE_PORTS = "hu.elte.txtuml.xtxtuml.ConnectorIncompatiblePorts";
	public static val CONNECTOR_ROLE_COMPOSITION_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorRoleCompositionMismatch";

	public static val CONNECTOR_END_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorEndCountMismatch";
	public static val CONNECTOR_END_PORT_OWNER_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorEndPortOwnerMismatch";

	public static val PORT_INTERFACE_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.PortInterfaceCountMismatch";

	public static val TRIGGER_PORT_IS_NOT_BEHAVIOR = "hu.elte.txtuml.xtxtuml.issues.TriggerPortIsNotBehavior";
	public static val TRIGGER_PORT_OWNER_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.TriggerPortOwnerMismatch";

}
