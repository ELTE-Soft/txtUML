package hu.elte.txtuml.xtxtuml.jvmmodel;

import com.google.inject.Inject
import hu.elte.txtuml.api.model.Association
import hu.elte.txtuml.api.model.BehaviorPort
import hu.elte.txtuml.api.model.Composition
import hu.elte.txtuml.api.model.Composition.Container
import hu.elte.txtuml.api.model.Composition.HiddenContainer
import hu.elte.txtuml.api.model.Connector
import hu.elte.txtuml.api.model.ConnectorBase.One
import hu.elte.txtuml.api.model.Delegation
import hu.elte.txtuml.api.model.External
import hu.elte.txtuml.api.model.ExternalBody
import hu.elte.txtuml.api.model.From
import hu.elte.txtuml.api.model.Interface
import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.api.model.ModelClass.Port
import hu.elte.txtuml.api.model.ModelEnum
import hu.elte.txtuml.api.model.Signal
import hu.elte.txtuml.api.model.StateMachine
import hu.elte.txtuml.api.model.To
import hu.elte.txtuml.api.model.Trigger
import hu.elte.txtuml.xtxtuml.common.XtxtUMLUtils
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUComposition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEnumeration
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEnumerationLiteral
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution
import hu.elte.txtuml.xtxtuml.xtxtUML.TUInterface
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelDeclaration
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPortMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUReception
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility
import java.util.Deque
import java.util.Map
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmMember
import org.eclipse.xtext.common.types.JvmVisibility
import org.eclipse.xtext.common.types.TypesFactory
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations

/**
 * Infers a JVM model equivalent from an XtxtUML resource. If not stated otherwise,
 * the infer methods map XtxtUML entities to corresponding JtxtUML ones.
 */
class XtxtUMLJvmModelInferrer extends AbstractModelInferrer {

	Map<EObject, JvmDeclaredType> registeredTypes = newHashMap;

	@Inject extension IJvmModelAssociations;
	@Inject extension IQualifiedNameProvider;
	@Inject extension XtxtUMLTypesBuilder;
	@Inject extension XtxtUMLUtils;

	/**
	 * Infers the given model declaration as a specialized {@link JvmGenericType},
	 * which will be translated to a <i>package-info.java</i> file later.
	 */
	def dispatch void infer(TUModelDeclaration decl, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(decl.toPackageInfo(decl.fullyQualifiedName, decl.modelName))
	}

	/**
	 * Infers the given execution as a class, containing a conventional main method.
	 */
	def dispatch void infer(TUExecution exec, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(exec.toClass(exec.fullyQualifiedName)) [
			documentation = exec.documentation
			visibility = JvmVisibility.PUBLIC

			members += exec.toMethod("main", Void.TYPE.typeRef) [
				documentation = exec.documentation
				parameters += exec.toParameter("args", String.typeRef.addArrayTypeDimension)
				varArgs = true

				static = true
				body = exec.body
			]
		]
	}

	def dispatch void infer(TUAssociation assoc, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(assoc.toClass(assoc.fullyQualifiedName)) [
			documentation = assoc.documentation
			superTypes += switch assoc {
				TUComposition: Composition
				default: Association
			}.typeRef

			for (end : assoc.ends) {
				members += end.inferredType as JvmMember
			}
		]

		for (end : assoc.ends) {
			register(end, acceptor, isPreIndexingPhase)
		}
	}

	def dispatch void infer(TUSignal signal, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(signal.toClass(signal.fullyQualifiedName)) [
			documentation = signal.documentation
			if (signal.superSignal != null) {
				superTypes += signal.superSignal.inferredTypeRef
			} else {
				superTypes += Signal.typeRef
			}

			for (attr : signal.attributes) {
				members += attr.toJvmMember
			}

			if (signal.attributes.isEmpty && signal.superSignal == null) {
				return
			}

			members += signal.toConstructor [
				val Deque<TUSignal> supers = newLinkedList
				if (signal.superSignal.travelSignalHierarchy [
					supers.add(it)
					false
				] == null) {
					return // cycle in hierarchy
				}

				val Deque<TUSignalAttribute> superAttributes = newLinkedList
				while (!supers.empty) {
					superAttributes.addAll(supers.last.attributes)
					supers.removeLast
				}

				val (TUSignalAttribute)=>void addAsParam = [ attr |
					if (parameters.findFirst[name == attr.name] == null) {
						// to eliminate 'duplicate local variable' errors
						parameters += attr.toParameter(attr.name, attr.type)
					}
				]

				superAttributes.forEach(addAsParam)
				signal.attributes.forEach(addAsParam) 

				val lastSuperAttribute = if (superAttributes.empty) {
						null
					} else {
						superAttributes.removeLast
					}

				body = '''
					«IF lastSuperAttribute != null»					
						super(«FOR attr : superAttributes»«attr.name», «ENDFOR»«lastSuperAttribute.name»);
					«ENDIF»
					«FOR attr : signal.attributes»
						this.«attr.name» = «attr.name»;
					«ENDFOR»
				'''
			]
		]
	}

	def dispatch void infer(TUClass tUClass, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(tUClass.toClass(tUClass.fullyQualifiedName)) [
			documentation = tUClass.documentation
			if (tUClass.superClass != null) {
				superTypes += tUClass.superClass.inferredTypeRef
			} else {
				superTypes += ModelClass.typeRef
			}

			for (member : tUClass.members) {
				if (member instanceof TUState || member instanceof TUPort) {
					members += member.inferredType as JvmMember
				} else if (!(member instanceof TUAttributeOrOperationDeclarationPrefix)) {
					members += member.toJvmMember
				}
			}
		]

		for (member : tUClass.members) {
			if (member instanceof TUState || member instanceof TUPort) {
				register(member, acceptor, isPreIndexingPhase)
			}
		}
	}

	def dispatch void infer(TUEnumeration enumeration, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(enumeration.toEnumerationType(enumeration.fullyQualifiedName.toString)) [
			documentation = enumeration.documentation
			superTypes += ModelEnum.typeRef
			enumeration.literals.forEach [ literal |
				members += literal.toJvmMember
			]
		]
	}

	def dispatch void infer(TUConnector connector, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(connector.toClass(connector.fullyQualifiedName)) [
			documentation = connector.documentation
			superTypes += if (connector.delegation) {
				Delegation.typeRef
			} else {
				Connector.typeRef
			}

			for (end : connector.ends) {
				members += end.inferredType as JvmMember
			}
		]

		for (end : connector.ends) {
			register(end, acceptor, isPreIndexingPhase)
		}
	}

	def dispatch void infer(TUInterface iFace, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(iFace.toClass(iFace.fullyQualifiedName)) [
			documentation = iFace.documentation

			superTypes += Interface.typeRef
			interface = true

			for (reception : iFace.receptions) {
				members += reception.toJvmMember
			}
		]
	}

	/**
	 * @see register(IJvmDeclaredTypeAcceptor,EObject,JvmGenericType,(JvmGenericType)=>void)
	 */
	def private dispatch void register(TUAssociationEnd assocEnd, IJvmDeclaredTypeAcceptor acceptor,
		boolean isPreIndexingPhase) {
		acceptor.register(assocEnd, assocEnd.toClass(assocEnd.fullyQualifiedName)) [
			documentation = assocEnd.documentation
			visibility = JvmVisibility.PUBLIC

			superTypes += assocEnd.calculateApiSuperType
		]
	}

	/**
	 * @see register(IJvmDeclaredTypeAcceptor,EObject,JvmGenericType,(JvmGenericType)=>void)
	 */
	def private dispatch void register(TUPort port, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.register(port, port.toClass(port.fullyQualifiedName)) [
			documentation = port.documentation
			visibility = JvmVisibility.PUBLIC

			val requiredIFace = port.members.findFirst[required]
			val providedIFace = port.members.findFirst[!required]

			superTypes += typeRef(Port, providedIFace.toInterfaceTypeRef, requiredIFace.toInterfaceTypeRef)

			if (port.behavior) {
				annotations += BehaviorPort.annotationRef
			}
		]
	}

	/**
	 * @see register(IJvmDeclaredTypeAcceptor,EObject,JvmGenericType,(JvmGenericType)=>void)
	 */
	def private dispatch void register(TUState state, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.register(state, state.toClass(state.fullyQualifiedName)) [
			documentation = state.documentation
			superTypes += switch (state.type) {
				case PLAIN: StateMachine.State.typeRef
				case INITIAL: StateMachine.Initial.typeRef
				case CHOICE: StateMachine.Choice.typeRef
				case COMPOSITE: StateMachine.CompositeState.typeRef
			}

			for (member : state.members) {
				if (member instanceof TUState) {
					members += member.inferredType as JvmMember
				} else {
					members += member.toJvmMember
				}
			}
		]

		for (member : state.members) {
			if (member instanceof TUState) {
				register(member, acceptor, isPreIndexingPhase)
			}
		}
	}

	/**
	 * @see register(IJvmDeclaredTypeAcceptor,EObject,JvmGenericType,(JvmGenericType)=>void)
	 */
	def private dispatch void register(TUConnectorEnd connEnd, IJvmDeclaredTypeAcceptor acceptor,
		boolean isPreIndexingPhase) {
		acceptor.register(connEnd, connEnd.toClass(connEnd.fullyQualifiedName)) [
			documentation = connEnd.documentation
			visibility = JvmVisibility.PUBLIC
			superTypes += typeRef(One, connEnd.role.inferredTypeRef, connEnd.port.inferredTypeRef)
		]
	}

	def dispatch private toJvmMember(TUConstructor ctor) {
		ctor.toConstructor [
			documentation = ctor.documentation
			val modifiers = ctor.modifiers
			visibility = modifiers.visibility.toJvmVisibility
			switch (modifiers.externality) {
				case EXTERNAL: annotations += External.annotationRef
				case EXTERNAL_BODY: annotations += ExternalBody.annotationRef
				default: {}
			}

			for (param : ctor.parameters) {
				parameters += param.toParameter(param.name, param.parameterType) => [
					documentation = ctor.documentation
				]
			}

			body = ctor.body
		]
	}

	def dispatch private toJvmMember(TUAttribute attr) {
		attr.toField(attr.name, attr.prefix.type) [
			documentation = attr.documentation
			
			val modifiers = attr.prefix.modifiers
			static = modifiers.static
			visibility = modifiers.visibility.toJvmVisibility

			switch (modifiers.externality) {
				case EXTERNAL: annotations += External.annotationRef
				default: {}
			}

			initializer = attr.initExpression
		]
	}

	def dispatch private toJvmMember(TUEnumerationLiteral literal) {
		literal.toEnumerationLiteral(literal.name) [
			documentation = literal.documentation
		]
	}

	def dispatch private toJvmMember(TUSignalAttribute attr) {
		attr.toField(attr.name, attr.type) [
			documentation = attr.documentation
			visibility = attr.visibility.toJvmVisibility
		]
	}

	def dispatch private toJvmMember(TUOperation op) {
		op.toMethod(op.name, op.prefix.type) [
			documentation = op.documentation
			val modifiers = op.prefix.modifiers
			static = modifiers.static
			visibility = modifiers.visibility.toJvmVisibility

			switch (modifiers.externality) {
				case EXTERNAL: annotations += External.annotationRef
				case EXTERNAL_BODY: annotations += ExternalBody.annotationRef
				default: {}
			}

			for (JvmFormalParameter param : op.parameters) {
				parameters += param.toParameter(param.name, param.parameterType) => [
					documentation = param.documentation
				]
			}

			body = op.body
		]
	}

	def dispatch private toJvmMember(TUEntryOrExitActivity act) {
		val name = if(act.entry) "entry" else "exit"

		return act.toMethod(name, Void.TYPE.typeRef) [
			documentation = act.documentation
			visibility = JvmVisibility.PUBLIC
			annotations += annotationRef(Override)
			body = act.body
		]
	}

	def dispatch private JvmMember toJvmMember(TUTransition trans) {
		trans.toClass(trans.fullyQualifiedName) [
			documentation = trans.documentation
			superTypes += StateMachine.Transition.typeRef

			for (member : trans.members) {
				switch (member) {
					TUTransitionTrigger,
					TUTransitionVertex: {
						annotations += member.toAnnotationRef
					}
					TUTransitionPort: {
					} // do nothing, handled together with triggers
					default: {
						members += member.toJvmMember
					}
				}
			}
		]
	}

	def dispatch private toJvmMember(TUTransitionEffect effect) {
		effect.toMethod("effect", Void.TYPE.typeRef) [
			documentation = effect.documentation
			visibility = JvmVisibility.PUBLIC
			annotations += annotationRef(Override)
			body = effect.body
		]
	}

	def dispatch private toJvmMember(TUTransitionGuard guard) {
		guard.toMethod("guard", Boolean.TYPE.typeRef) [
			documentation = guard.documentation
			visibility = JvmVisibility.PUBLIC

			annotations += annotationRef(Override)
			if (guard.^else) {
				body = '''return Else();'''
			} else {
				body = guard.expression
			}
		]
	}

	def dispatch private toJvmMember(TUReception reception) {
		reception.toMethod("reception", Void.TYPE.typeRef) [
			visibility = JvmVisibility.DEFAULT
			documentation = reception.documentation
			parameters += reception.toParameter("signal", reception.signal.inferredTypeRef)
		]
	}

	def private toJvmVisibility(TUVisibility it) {
		if (it == TUVisibility.PACKAGE)
			JvmVisibility.DEFAULT
		else
			JvmVisibility.getByName(getName())
	}

	def dispatch private toAnnotationRef(TUTransitionTrigger it) {
		val port = (eContainer as TUTransition).members.findFirst[it instanceof TUTransitionPort] as TUTransitionPort

		createAnnotationRef(Trigger, if (port != null) {
			#["port" -> port.port, "value" -> trigger]
		} else {
			#["value" -> trigger]
		})
	}

	def dispatch private toAnnotationRef(TUTransitionVertex it) {
		createAnnotationRef(
			if (from) {
				From
			} else {
				To
			},
			"value" -> vertex
		)
	}

	/**
	 * Creates a type reference for the given annotation type with the given parameters.
	 */
	def private createAnnotationRef(Class<?> annotationType, Pair<String, EObject>... params) {
		annotationRef(annotationType) => [ annotationRef |
			for (param : params) {
				annotationRef.explicitValues += TypesFactory::eINSTANCE.createJvmTypeAnnotationValue => [
					values += param.value.inferredTypeRef
					if (params.size != 1 || param.key != "value") {
						operation = annotationRef.annotation.declaredOperations.findFirst[it.simpleName == param.key]
					}
				]
			}

		]
	}

	def private calculateApiSuperType(TUAssociationEnd it) {
		val endClassTypeParam = endClass.inferredTypeRef
		if (isContainer) {
			// Do not try to simplify the code here, as it breaks standalone builds.
			// The inferred type will be Class<? extend MaybeOneBase>, which is invalid,
			// as MaybeOneBase is a package private class in its own package.
			if (notNavigable) {
				return HiddenContainer.typeRef(endClassTypeParam)
			} else {
				return Container.typeRef(endClassTypeParam)
			}
		}

		val optionalHidden = if(notNavigable) "Hidden" else ""
		val apiBoundTypeName = if (multiplicity == null) // omitted
				"One"
			else if (multiplicity.any) // *
				"Any"
			else if (!multiplicity.upperSet) { // <lower> (exact)
				if (multiplicity.lower == 1)
					"One"
			} else { // <lower> .. <upper>
				if (multiplicity.lower == 0 && multiplicity.upper == 1)
					"ZeroToOne"
				else if (multiplicity.lower == 1 && multiplicity.upper == 1)
					"One"
				else if (multiplicity.lower == 0 && multiplicity.upperInf)
					"Any"
				else if (multiplicity.lower == 1 && multiplicity.upperInf)
					"ZeroToAny"
			}

		val endClassImpl = "hu.elte.txtuml.api.model.Association$" + optionalHidden + "End"
		val endCollectionImpl = "hu.elte.txtuml.api.model." + apiBoundTypeName
		return endClassImpl.typeRef(endCollectionImpl.typeRef(endClassTypeParam))
	}

	def private inferredTypeRef(EObject sourceElement) {
		val type = sourceElement.inferredType
		if (type instanceof JvmDeclaredType) {
			return type.typeRef
		}
	}

	def private toInterfaceTypeRef(TUPortMember portMember) {
		if (portMember?.interface != null) {
			portMember.interface.inferredTypeRef
		} else {
			Interface.Empty.typeRef
		}
	}

	/**
	 * Associates the given <code>type</code> with the given <code>sourceElement</code>, and also
	 * registers the given <code>initializer</code> function, which will be used after the JVM inference
	 * process is complete. This method does not add the given type to the contents of the resource.
	 * @see <a href="https://github.com/ELTE-Soft/txtUML/issues/173#issuecomment-206116066">#173</a>
	 */
	def private void register(IJvmDeclaredTypeAcceptor acceptor, EObject sourceElement, JvmGenericType type,
		(JvmGenericType)=>void initializer) {
		registeredTypes.put(sourceElement, type)
		acceptor.accept(type, initializer)
		if (type?.eResource != null) { // to eliminate warning about null-safe'd primitives
			type.eResource.contents.remove(type)
		}
	}

	def private inferredType(EObject sourceElement) {
		registeredTypes.get(sourceElement) ?: sourceElement.getPrimaryJvmElement
	}

}
