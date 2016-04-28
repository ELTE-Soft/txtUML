package hu.elte.txtuml.xtxtuml.validation

import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import org.eclipse.emf.ecore.EClass
import org.eclipse.xtext.common.types.TypesPackage
import org.eclipse.xtext.diagnostics.Diagnostic
import org.eclipse.xtext.diagnostics.DiagnosticMessage
import org.eclipse.xtext.diagnostics.Severity
import org.eclipse.xtext.linking.impl.IllegalNodeException
import org.eclipse.xtext.linking.impl.LinkingDiagnosticMessageProvider

class XtxtUMLLinkingDiagnosticMessageProvider extends LinkingDiagnosticMessageProvider {

	override getUnresolvedProxyMessage(ILinkingDiagnosticContext context) {
		val referenceType = context.reference.EReferenceType;
		var linkText = "";

		try {
			linkText = context.linkText;
		} catch (IllegalNodeException e) {
			linkText = e.node.text;
		}

		val msg = "Couldn't resolve reference to " + referenceType.humanReadableName + " '" + linkText + "'.";
		return new DiagnosticMessage(msg, Severity.ERROR, Diagnostic.LINKING_DIAGNOSTIC);
	}

	def private humanReadableName(EClass type) {
		if (type.EPackage.nsURI == XtxtUMLPackage.eNS_URI) {
			type.name.split("(?=[A-Z])").drop(2).map[toLowerCase].join(" ") // e.g. "TUConnectorEnd" ~> "connector end"
		} else {
			switch (type) {
				case TypesPackage.eINSTANCE.jvmType,
				case TypesPackage.eINSTANCE.jvmDeclaredType: "type"
				case TypesPackage.eINSTANCE.jvmConstructor: "constructor"
				default: "element"
			}
		}
	}

}
