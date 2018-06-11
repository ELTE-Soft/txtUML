package hu.elte.txtuml.xtxtuml.compiler

import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectiveEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import java.util.Map
import org.eclipse.emf.common.notify.impl.AdapterImpl
import org.eclipse.emf.ecore.EReference

class XtxtUMLBindExpressionAdapter extends AdapterImpl {
	val Map<EReference, TUConnectiveEnd> featureToEnd = newHashMap

	def put(EReference feature, TUConnectiveEnd end) {
		featureToEnd.put(feature, end);
	}

	def leftEnd() {
		featureToEnd.get(XtxtUMLPackage.Literals.TU_BIND_EXPRESSION__LEFT_END)
	}

	def rightEnd() {
		featureToEnd.get(XtxtUMLPackage.Literals.TU_BIND_EXPRESSION__RIGHT_END)
	}
}
