package hu.elte.txtuml.xtxtuml.common;

import com.google.common.base.Predicate
import com.google.inject.Inject
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.common.types.TypesPackage
import org.eclipse.xtext.common.types.xtext.TypesAwareDefaultGlobalScopeProvider
import org.eclipse.xtext.resource.IEObjectDescription

class XtxtUMLReferenceProposalGlobalScopeProvider extends TypesAwareDefaultGlobalScopeProvider {

	@Inject XtxtUMLReferenceProposalTypeScopeProvider typeScopeProvider;

	/**
	 * Copy of the super implementation. Uses a specialized type scope provider, see
	 * {@link XtxtUMLReferenceProposalTypeScopeProvider}. Should be used only during
	 * reference proposal construction.
	 */
	override getScope(Resource resource, EReference reference, Predicate<IEObjectDescription> filter) {
		val referenceType = getEReferenceType(resource, reference);

		if (EcoreUtil2.isAssignableFrom(TypesPackage.Literals.JVM_TYPE, referenceType)) {
			return typeScopeProvider.getScope(resource, reference, filter);
		}

		if (EcoreUtil2.isAssignableFrom(TypesPackage.Literals.JVM_CONSTRUCTOR, referenceType)) {
			return typeScopeProvider.getScope(resource, reference, filter);
		}

		return super.getScope(resource, reference, filter);
	}

}
