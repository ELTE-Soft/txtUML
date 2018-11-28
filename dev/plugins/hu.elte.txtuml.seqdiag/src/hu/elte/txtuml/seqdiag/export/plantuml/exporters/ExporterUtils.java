package hu.elte.txtuml.seqdiag.export.plantuml.exporters;

import org.eclipse.jdt.core.dom.ASTNode;
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

	
	public static boolean isCommunication(ASTNode curElement) {
 		String fullName = getFullyQualifiedName((MethodInvocation) curElement);
 		return fullName.equals("hu.elte.txtuml.api.model.seqdiag.Sequence.send")
 				|| fullName.equals("hu.elte.txtuml.api.model.seqdiag.Sequence.fromActor");
 	}

 	public static boolean isParFragment(ASTNode curElement) {
 		String fullName = getFullyQualifiedName((MethodInvocation) curElement);
 		return fullName.equals("hu.elte.txtuml.api.model.seqdiag.Sequence.par");
 	}
	
	public static boolean isActionMethodInvocation(MethodInvocation invocation) {
 		return getDeclaringClass(invocation).equals("hu.elte.txtuml.api.model.Action");
 	}
	
	private static String getDeclaringClass(MethodInvocation invocation) {
 		return invocation.resolveMethodBinding().getDeclaringClass().getQualifiedName().toString();
 	}
}
