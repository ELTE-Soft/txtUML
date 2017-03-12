package hu.elte.txtuml.xtxtuml.common;

import com.google.common.base.Predicate
import com.google.inject.Inject
import org.eclipse.xtext.common.types.access.IJvmTypeProvider
import org.eclipse.xtext.common.types.access.jdt.IJdtTypeProvider
import org.eclipse.xtext.common.types.xtext.ui.JdtBasedSimpleTypeScopeProvider
import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.resource.IEObjectDescription

class XtxtUMLReferenceProposalTypeScopeProvider extends JdtBasedSimpleTypeScopeProvider {

	@Inject IQualifiedNameConverter qualifiedNameConverter;

	/**
	 * Returns a specialized type scope, see {@link XtxtUMLReferenceProposalTypeScope}.
	 * Should be used only during reference proposal construction.
	 */
	override createTypeScope(IJvmTypeProvider typeProvider, Predicate<IEObjectDescription> filter) {
		return new XtxtUMLReferenceProposalTypeScope(typeProvider as IJdtTypeProvider, qualifiedNameConverter, filter);
	}

}
