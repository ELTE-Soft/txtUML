package hu.elte.txtuml.xtxtuml.validation

/**
 * Issue codes to be used in validators and quick fix providers.
 */
class XtxtUMLIssueCodes {

	protected static val ISSUE_CODE_PREFIX = "hu.elte.txtuml.xtxtuml.validation.IssueCodes.";

	/**
	 * Used by XtxtUMLDerivedResourceMarkerCopier to propagate JtxtUML validation errors to XtxtUML source.
	 */
	public static val COPY_JTXTUML_PROBLEMS = ISSUE_CODE_PREFIX + "copyJtxtUMLProblems";

	public static val CLASS_HIERARCHY_CYCLE = ISSUE_CODE_PREFIX + "classHierarchyCycle";
	
	public static val ASSOCIATION_END_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.AssociationEndCountMismatch";
	public static val ASSOCIATION_END_NAME_IS_NOT_UNIQUE = "hu.elte.txtuml.xtxtuml.issues.AssociationEndNameIsNotUnique";
	public static val ASSOCIATION_CONTAINS_CONTAINER_END = "hu.elte.txtuml.xtxtuml.issues.AssociationContainsContainerEnd";
	public static val CONTAINER_END_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.CompositionMissesContainerEnd";
	
	public static val CONNECTOR_ASSEMBLY_CONTAINS_CONTAINER_ROLE = "hu.elte.txtuml.xtxtuml.issues.ConnectorAssemblyContainsContainerRole";
	public static val CONNECTOR_CONTAINER_ROLE_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorContainerRoleCountMismatch";
	public static val CONNECTOR_INCOMPATIBLE_PORTS = "hu.elte.txtuml.xtxtuml.ConnectorIncompatiblePorts";
	public static val CONNECTOR_ROLE_COMPOSITION_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorRoleCompositionMismatch";

	public static val CONNECTOR_END_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorEndCountMismatch";
	public static val CONNECTOR_END_DUPLICATE = "hu.elte.txtuml.xtxtuml.issues.ConnectorEndDuplicate";
	public static val CONNECTOR_END_PORT_OWNER_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.ConnectorEndPortOwnerMismatch";

	public static val PORT_INTERFACE_COUNT_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.PortInterfaceCountMismatch";
	public static val PORT_NAME_IS_NOT_UNIQUE = "hu.elte.txtuml.xtxtuml.issues.PortNameIsNotUnique";

	public static val TRIGGER_PORT_IS_NOT_BEHAVIOR = "hu.elte.txtuml.xtxtuml.issues.TriggerPortIsNotBehavior";
	public static val TRIGGER_PORT_OWNER_MISMATCH = "hu.elte.txtuml.xtxtuml.issues.TriggerPortOwnerMismatch";

}
