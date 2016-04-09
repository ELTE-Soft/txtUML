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
