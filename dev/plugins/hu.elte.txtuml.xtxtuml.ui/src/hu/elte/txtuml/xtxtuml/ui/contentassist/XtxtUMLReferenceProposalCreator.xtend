package hu.elte.txtuml.xtxtuml.ui.contentassist

import com.google.common.base.Function
import com.google.common.base.Predicate
import com.google.inject.Inject
import hu.elte.txtuml.api.model.DataType
import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.api.model.Signal
import hu.elte.txtuml.api.model.external.ExternalType
import hu.elte.txtuml.xtxtuml.common.XtxtUMLReferenceProposalScopeProvider
import hu.elte.txtuml.xtxtuml.common.XtxtUMLReferenceProposalTypeScope
import hu.elte.txtuml.xtxtuml.common.XtxtUMLUtils
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUComposition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.jface.text.contentassist.ICompletionProposal
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.TypesPackage
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.scoping.IScope
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal
import org.eclipse.xtext.xbase.XBlockExpression
import org.eclipse.xtext.xbase.XVariableDeclaration
import org.eclipse.xtext.xbase.XbasePackage
import org.eclipse.xtext.xbase.scoping.batch.InstanceFeatureDescription
import org.eclipse.xtext.xbase.typesystem.IBatchTypeResolver
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReferenceFactory
import org.eclipse.xtext.xbase.typesystem.references.StandardTypeReferenceOwner
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices
import org.eclipse.xtext.xbase.ui.contentassist.MultiNameDescription
import org.eclipse.xtext.xbase.ui.contentassist.XbaseReferenceProposalCreator

class XtxtUMLReferenceProposalCreator extends XbaseReferenceProposalCreator {

	static val allowedJavaClassTypes = #["java.lang.Boolean", "java.lang.Double", "java.lang.Integer",
		"java.lang.String"];

	@Inject extension IBatchTypeResolver;
	@Inject extension IQualifiedNameProvider;
	@Inject extension XtxtUMLUtils;

	@Inject XtxtUMLReferenceProposalScopeProvider scopeProvider;
	@Inject CommonTypeComputationServices services;

	/**
	 * Provides a scope provider with a customized JDT based superscope.
	 * @see XtxtUMLReferenceProposalTypeScope
	 */
	override getScopeProvider() {
		return scopeProvider;
	}

	/**
	 * Replaces the dollar mark with a dot in content assist proposals.
	 */
	override protected getWrappedFactory(EObject model, EReference reference,
		Function<IEObjectDescription, ICompletionProposal> proposalFactory) {
		[
			val proposal = super.getWrappedFactory(model, reference, proposalFactory).apply(it);
			if (proposal instanceof ConfigurableCompletionProposal) {
				proposal.replacementString = proposal.replacementString.replace("$", ".");
			}

			return proposal;
		]
	}

	/**
	 * Extends the default implementation with proposals for XtxtUML cross references.
	 */
	override queryScope(IScope scope, EObject model, EReference ref, Predicate<IEObjectDescription> filter) {
		switch (ref) {
			case XtxtUMLPackage::eINSTANCE.TUConnectorEnd_Role:
				scope.selectCompositionEnds(model)
			case XtxtUMLPackage::eINSTANCE.TUConnectorEnd_Port:
				scope.selectOwnedPorts(model)
			case XtxtUMLPackage::eINSTANCE.TUTransitionPort_Port:
				scope.selectOwnedBehaviorPorts(model)
			case XtxtUMLPackage::eINSTANCE.TUTransitionTrigger_Trigger:
				scope.selectApplicableTriggers(model)
			case XtxtUMLPackage::eINSTANCE.TUTransitionVertex_Vertex:
				scope.selectOwnedStates(model)
			case XtxtUMLPackage::eINSTANCE.TUClassPropertyAccessExpression_Right:
				scope.selectNavigableClassProperties(model)
			case XtxtUMLPackage::eINSTANCE.TUSignal_SuperSignal:
				scope.selectExtendableSignals(model)
			case XtxtUMLPackage::eINSTANCE.TUClass_SuperClass:
				scope.selectExtendableClasses(model)
			case XbasePackage::eINSTANCE.XAbstractFeatureCall_Feature:
				scope.selectAllowedFeatures(model, ref, filter)
			case TypesPackage::eINSTANCE.jvmParameterizedTypeReference_Type:
				scope.selectAllowedTypes(model)
		} ?: super.queryScope(scope, model, ref, filter)
	}

	def private selectCompositionEnds(IScope scope, EObject model) {
		if (model instanceof TUConnector || model instanceof TUConnectorEnd) {
			scope.allElements.filter [
				EContainerDescription.EObjectOrProxy instanceof TUComposition
			]
		}
	}

	def private selectOwnedPorts(IScope scope, EObject model) {
		if (model instanceof TUConnectorEnd) {
			val roleClassName = model.role?.endClass?.fullyQualifiedName;
			return scope.allElements.filter [
				EContainerDescription.qualifiedName == roleClassName
			];
		}
	}

	def private selectOwnedBehaviorPorts(IScope scope, EObject model) {
		if (model instanceof TUTransitionPort) {
			val containerClassName = EcoreUtil2.getContainerOfType(model, TUClass).fullyQualifiedName;
			return scope.allElements.filter [
				val port = EObjectOrProxy as TUPort;
				return port != null && port.behavior && EContainerDescription.qualifiedName == containerClassName;
			]
		}
	}

	def private selectApplicableTriggers(IScope scope, EObject model) {
		if (model instanceof TUTransitionTrigger && model.eContainer instanceof TUTransition) {
			val transPort = (model.eContainer as TUTransition).members?.findFirst [
				it instanceof TUTransitionPort
			] as TUTransitionPort;

			if (transPort != null) { // check if port is specified
				val providedIFace = transPort.port?.members?.findFirst[!required];
				val providedSignals = providedIFace?.interface?.receptions?.map[signal];

				return scope.allElements.filter [ descr |
					providedSignals?.findFirst[fullyQualifiedName == descr.qualifiedName] != null
				// `findFirst` is used instead of `exists` to eliminate the warning about null-safe'd primitives
				]
			}
		}
	}

	def private selectOwnedStates(IScope scope, EObject model) {
		if (model instanceof TUTransitionVertex) {
			val transContainerName = model.eContainer?.eContainer?.fullyQualifiedName;
			return scope.allElements.filter [
				EContainerDescription.qualifiedName == transContainerName &&
					(model.from || (EObjectOrProxy as TUState).type != TUStateType.INITIAL)
			];
		}
	}

	def private selectNavigableClassProperties(IScope scope, EObject model) {
		if (model instanceof TUClassPropertyAccessExpression) {
			val containerClassName = model.left.resolveTypes.getActualType(model.left).type.fullyQualifiedName;
			return scope.allElements.filter [ descr |
				switch (proposedObj : descr.EObjectOrProxy) {
					TUPort:
						descr.EContainerDescription.qualifiedName == containerClassName
					TUAssociationEnd:
						!proposedObj.notNavigable && descr.endsOfEnclosingAssociation.exists [
							endClass?.fullyQualifiedName == containerClassName &&
								fullyQualifiedName != descr.qualifiedName
						]
					default:
						false // to make Xtend happy
				}
			]
		}
	}

	def private endsOfEnclosingAssociation(IEObjectDescription assocEndDescription) {
		val container = assocEndDescription.EObjectOrProxy.eContainer ?:
			assocEndDescription.EContainerDescription.EObjectOrProxy;

		return if (container instanceof TUAssociation) {
			container.ends
		} else {
			newArrayList
		}
	}

	def private selectExtendableSignals(IScope scope, EObject model) {
		if (model instanceof TUSignal) {
			val selfName = model.fullyQualifiedName;
			return scope.allElements.filter [
				qualifiedName != selfName
			]
		}
	}

	def private selectExtendableClasses(IScope scope, EObject model) {
		if (model instanceof TUClass) {
			val selfName = model.fullyQualifiedName;
			return scope.allElements.filter [
				qualifiedName != selfName
			]
		}
	}

	def private selectAllowedFeatures(IScope scope, EObject model, EReference ref,
		Predicate<IEObjectDescription> filter) {
		super.queryScope(scope, model, ref, filter).filter [
			val fqn = qualifiedName?.toString;
			val isObjectOrXbaseLibCall = fqn != null &&
				(fqn.startsWith("java.lang.Object") || fqn.startsWith("org.eclipse.xtext.xbase.lib"));

			if (isObjectOrXbaseLibCall) {
				return false;
			}

			val isExtensionCall = it instanceof MultiNameDescription && {
				val delegate = (it as MultiNameDescription).delegate;
				if (delegate instanceof InstanceFeatureDescription) {
					delegate.isExtension
				} else {
					false
				}
			}

			return !isExtensionCall;
		]
	}

	def private selectAllowedTypes(IScope scope, EObject model) {
		val isClassAllowed = switch (model) {
			TUSignal,
			TUSignalAttribute: false
			TUClass,
			TUAttributeOrOperationDeclarationPrefix,
			TUOperation,
			XBlockExpression,
			XVariableDeclaration: true
			default: null
		}

		return if (isClassAllowed != null) {
			val isSignalAllowed = EcoreUtil2.getContainerOfType(model, XBlockExpression) != null;
			scope.allElements.filter[isAllowedType(isClassAllowed, isSignalAllowed)]
		}
	}

	def private isAllowedType(IEObjectDescription desc, boolean isClassAllowed, boolean isSignalAllowed) {
		// primitives are handled as keywords on this level, nothing to do with them
		allowedJavaClassTypes.contains(desc.qualifiedName.toString) || {
			val proposedObj = desc.EObjectOrProxy;

			// investigating superTypes only is sufficient and convenient here
			// sufficient:
			// a valid txtUML class always has a JtxtUML API supertype
			// convenient:
			// supertypes are already in type reference format, which state would
			// be difficult to achieve starting from a plain JvmType
			proposedObj instanceof JvmGenericType && (proposedObj as JvmGenericType).superTypes.exists [
				val typeRef = toLightweightTypeReference;
				typeRef.isSubtypeOf(DataType) || typeRef.isInterface && typeRef.isSubtypeOf(ExternalType) ||
					isClassAllowed && typeRef.isSubtypeOf(ModelClass) || isSignalAllowed && typeRef.isSubtypeOf(Signal)
			]
		}
	}

	def private isInterface(LightweightTypeReference typeRef) {
		typeRef.type instanceof JvmGenericType && (typeRef.type as JvmGenericType).isInterface();
	}

	def private toLightweightTypeReference(JvmTypeReference typeRef) {
		val owner = new StandardTypeReferenceOwner(services, typeRef);
		val factory = new LightweightTypeReferenceFactory(owner, false);
		return factory.toLightweightReference(typeRef);
	}

}
