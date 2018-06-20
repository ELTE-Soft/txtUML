package hu.elte.txtuml.export.plantuml.seqdiag;

import org.eclipse.jdt.core.dom.MethodInvocation;

/**
 * Utility class for sequence diagram exporter.
 */
public class ExporterUtils {

	/**
	 * Returns the fully qualified name of the given method invocation.
	 * 
	 * @param invocation
	 *            The {@link MethodInvocation} we need the fully qualified name
	 *            of.
	 */
	public static String getFullyQualifiedName(MethodInvocation invocation) {
		return invocation.resolveMethodBinding().getDeclaringClass().getQualifiedName().toString() + "."
				+ invocation.getName().toString();
	}

}
