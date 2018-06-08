package hu.elte.txtuml.xtxtuml.common;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.scoping.XtxtUMLXImportSectionNamespaceScopeProvider
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.scoping.IGlobalScopeProvider
import org.eclipse.xtext.scoping.impl.SelectableBasedScope

class XtxtUMLReferenceProposalScopeProvider extends XtxtUMLXImportSectionNamespaceScopeProvider {

	@Inject IGlobalScopeProvider jGlobalScopeProvider;
	@Inject XtxtUMLReferenceProposalGlobalScopeProvider xGlobalScopeProvider;
	@Inject extension XtxtUMLExternalityHelper;

	/**
	 * Extends the super implementation to use either the default JDT-based or the
	 * {@link XtxtUMLReferenceProposalGlobalScopeProvider custom} txtUML-based global
	 * scope provider. The decision depends on the externality of the context. Should
	 * be used only during reference proposal construction.
	 */
	override getScope(EObject context, EReference reference) {
		if (context == null)
			throw new NullPointerException("context");
		if (context.eResource == null)
			throw new IllegalArgumentException("context must be contained in a resource");

		// start of modified code

		var globalScopeProvider = if(context.external) jGlobalScopeProvider else xGlobalScopeProvider;
		var globalScope = getGlobalScope(context.eResource, reference, globalScopeProvider);

		// end of modified code

		return internalGetScope(globalScope, globalScope, context, reference);
	}

	/**
	 * Extends the {@link #getGlobalScope(Resource, EReference)} method to take the global scope provider
	 * to be queried as a parameter.
	 */
	def private getGlobalScope(Resource context, EReference reference, IGlobalScopeProvider globalScopeProvider) {
		val globalScope = wrap(globalScopeProvider.getScope(context, reference, null));
		return SelectableBasedScope.createScope(globalScope, getAllDescriptions(context), reference.getEReferenceType(),
			isIgnoreCase(reference));
	}

}
