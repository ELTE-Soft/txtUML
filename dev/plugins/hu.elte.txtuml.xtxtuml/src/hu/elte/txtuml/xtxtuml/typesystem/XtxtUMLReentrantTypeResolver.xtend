package hu.elte.txtuml.xtxtuml.typesystem

import com.google.common.collect.ImmutableMap
import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.xbase.scoping.batch.IFeatureNames
import org.eclipse.xtext.xbase.scoping.batch.IFeatureScopeSession
import org.eclipse.xtext.xbase.typesystem.internal.LogicalContainerAwareReentrantTypeResolver
import org.eclipse.xtext.xbase.typesystem.references.ITypeReferenceOwner

class XtxtUMLReentrantTypeResolver extends LogicalContainerAwareReentrantTypeResolver {

	override protected IFeatureScopeSession addThisAndSuper(IFeatureScopeSession session, ITypeReferenceOwner owner,
		JvmDeclaredType thisType, /* @Nullable */ JvmTypeReference superType, boolean addNestedTypes) {
		var childSession = session;

		// customized `this` and `super` lookup
		var thisMaybeModelClassType = thisType;
		var foundModelClassParentOrSelf = thisMaybeModelClassType.sourceElement instanceof TUClass;
		while (!foundModelClassParentOrSelf && thisMaybeModelClassType.eContainer instanceof JvmDeclaredType) {
			thisMaybeModelClassType = thisMaybeModelClassType.eContainer as JvmDeclaredType;
			foundModelClassParentOrSelf = thisMaybeModelClassType.sourceElement instanceof TUClass;
		}

		var JvmTypeReference superMaybeModelClassType;
		if (foundModelClassParentOrSelf) {
			superMaybeModelClassType = if (thisMaybeModelClassType.extendedClass?.type?.identifier != ModelClass.name) {
				thisMaybeModelClassType.extendedClass
			} else {
				null
			}
		} else {
			thisMaybeModelClassType = thisType;
			superMaybeModelClassType = null;
		}

		// end of customization
		if (thisMaybeModelClassType.eContainer != null) {
			if (thisMaybeModelClassType.isStatic) {
				childSession = childSession.dropLocalElements;
			} else {
				childSession = childSession.captureLocalElements;
			}
		}

		if (superMaybeModelClassType != null && superMaybeModelClassType.type != null) {
			val builder = ImmutableMap.builder;
			builder.put(IFeatureNames.THIS, thisMaybeModelClassType);
			builder.put(IFeatureNames.SUPER, superMaybeModelClassType.type);
			childSession = childSession.addLocalElements(builder.build, owner);
		} else {
			childSession = childSession.addLocalElement(IFeatureNames.THIS, thisMaybeModelClassType, owner);
		}

		childSession = addThisTypeToStaticScope(childSession, thisMaybeModelClassType);
		if (addNestedTypes)
			childSession = childSession.addNestedTypesToScope(thisMaybeModelClassType);

		return childSession;
	}

}
