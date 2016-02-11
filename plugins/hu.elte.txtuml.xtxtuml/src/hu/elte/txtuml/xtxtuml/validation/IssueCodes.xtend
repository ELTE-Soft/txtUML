package hu.elte.txtuml.xtxtuml.validation

/**
 * Issue codes to be used in validators and quick fix providers.
 */
class IssueCodes {

	protected static val ISSUE_CODE_PREFIX = "hu.elte.txtuml.xtxtuml.validation.IssueCodes.";

	/**
	 * Used by XtxtUMLDerivedResourceMarkerCopier to propagate JtxtUML validation errors to XtxtUML source.
	 */
	public static val COPY_JTXTUML_PROBLEMS = ISSUE_CODE_PREFIX + "copyJtxtUMLProblems";

	public static val CLASS_HIERARCHY_CYCLE = ISSUE_CODE_PREFIX + "classHierarchyCycle";
}
