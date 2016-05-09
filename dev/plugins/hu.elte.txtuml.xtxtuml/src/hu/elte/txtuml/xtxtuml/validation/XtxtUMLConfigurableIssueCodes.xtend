package hu.elte.txtuml.xtxtuml.validation

import org.eclipse.xtext.preferences.PreferenceKey
import org.eclipse.xtext.util.IAcceptor
import org.eclipse.xtext.validation.SeverityConverter
import org.eclipse.xtext.xbase.validation.XbaseConfigurableIssueCodes

class XtxtUMLConfigurableIssueCodes extends XbaseConfigurableIssueCodes {

	override protected initialize(IAcceptor<PreferenceKey> iAcceptor) {
		super.initialize(iAcceptor)
		iAcceptor.accept(create(XtxtUMLIssueCodes.COPY_JTXTUML_PROBLEMS, SeverityConverter.SEVERITY_ERROR))
	}

}
