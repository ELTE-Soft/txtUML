package hu.elte.txtuml.xtxtuml.common;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.scoping.XtxtUMLXImportSectionNamespaceScopeProvider
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.scoping.impl.SelectableBasedScope

class XtxtUMLReferenceProposalScopeProvider extends XtxtUMLXImportSectionNamespaceScopeProvider {

	@Inject XtxtUMLReferenceProposalGlobalScopeProvider globalScopeProvider;

	/**
	 * Uses a specialized global scope provider, see {@link XtxtUMLReferenceProposalGlobalScopeProvider}.
	 * Should be used only during reference proposal construction.
	 */
	override protected getGlobalScope(Resource context, EReference reference) {
		val globalScope = wrap(globalScopeProvider.getScope(context, reference, null));
		return SelectableBasedScope.createScope(globalScope, getAllDescriptions(context), reference.getEReferenceType(),
			isIgnoreCase(reference));
	}

}
