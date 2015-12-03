package hu.elte.txtuml.xtxtuml.jvmmodel;

import com.google.inject.Inject
import hu.elte.txtuml.api.model.Association
import hu.elte.txtuml.api.model.Composition
import hu.elte.txtuml.api.model.Composition.Container
import hu.elte.txtuml.api.model.Composition.HiddenContainer
import hu.elte.txtuml.api.model.From
import hu.elte.txtuml.api.model.Max
import hu.elte.txtuml.api.model.Min
import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.api.model.Signal
import hu.elte.txtuml.api.model.StateMachine
import hu.elte.txtuml.api.model.To
import hu.elte.txtuml.api.model.Trigger
import hu.elte.txtuml.xtxtuml.naming.IPackageNameCalculator
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUComposition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelDeclaration
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.common.types.JvmMember
import org.eclipse.xtext.common.types.JvmVisibility
import org.eclipse.xtext.common.types.TypesFactory
import org.eclipse.xtext.naming.IQualifiedNameProvider
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations

class XtxtUMLJvmModelInferrer extends AbstractModelInferrer {

	// Helper extensions
	@Inject extension XtxtUMLTypesBuilder
	@Inject extension IJvmModelAssociations
	@Inject extension IQualifiedNameProvider
	@Inject extension IPackageNameCalculator

	def dispatch void infer(TUModelDeclaration decl, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		val packageName = decl.expectedPackageName
		if (null != packageName) {
			acceptor.accept(decl.toPackageInfo(packageName, decl.name))
		}
	}

	def dispatch void infer(TUExecution exec, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(exec.toClass(exec.fullyQualifiedName)) [
			visibility = JvmVisibility.PUBLIC;

			members += exec.toMethod("main", Void.TYPE.typeRef) [
				documentation = exec.documentation
				parameters += exec.toParameter("args", String.typeRef.addArrayTypeDimension)
				varArgs = true;

				static = true;
				body = exec.body;
			]
		]
	}

	def dispatch void infer(TUAssociation assoc, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(assoc.toClass(assoc.fullyQualifiedName) [
			documentation = assoc.documentation
			superTypes += switch assoc {
				TUComposition: Composition
				default: Association
			}.typeRef

			for (end : assoc.ends) {
				members += end.toJvmMember;
			}
		])
	}

	def dispatch void infer(TUSignal signal, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(signal.toClass(signal.fullyQualifiedName)) [
			documentation = signal.documentation
			superTypes += Signal.typeRef;

			for (attr : signal.attributes) {
				members += attr.toJvmMember;
			}

			if (!signal.attributes.isEmpty) {
				members += signal.toConstructor [
					for (attr : signal.attributes) {
						parameters += attr.toParameter(attr.name, attr.type);
					}

					body = '''
						«FOR attr : signal.attributes»
							this.«attr.name» = «attr.name»;
						«ENDFOR»
					'''
				]
			}
		]
	}

	def dispatch void infer(TUClass tUClass, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(tUClass.toClass(tUClass.fullyQualifiedName)) [
			documentation = tUClass.documentation
			if (tUClass.superClass != null) {
				superTypes += (tUClass.superClass.getPrimaryJvmElement as JvmDeclaredType).typeRef;
			} else {
				superTypes += ModelClass.typeRef;
			}

			for (member : tUClass.members) {
				if (member instanceof TUState) {
					// just use the element which was inferred earlier
					members += member.getPrimaryJvmElement as JvmMember;
				} else {
					members += member.toJvmMember;
				}
			}
		]

		tUClass.members.filter[s|s instanceof TUState].forEach [ s |
			// enforce early inference of sub-states
			earlyInfer(s as TUState, acceptor, isPreIndexingPhase)
		]
	}

	def void earlyInfer(TUState state, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(state.toClass(state.fullyQualifiedName)) [
			documentation = state.documentation
			superTypes += switch (state.type) {
				case PLAIN: StateMachine.State.typeRef
				case INITIAL: StateMachine.Initial.typeRef
				case CHOICE: StateMachine.Choice.typeRef
				case COMPOSITE: StateMachine.CompositeState.typeRef
			}

			for (member : state.members) {
				if (member instanceof TUState) {
					// just use the element which was inferred earlier
					members += member.getPrimaryJvmElement as JvmMember;
				} else {
					members += member.toJvmMember;
				}
			}
		]

		state.members.filter[s|s instanceof TUState].forEach [ s |
			// enforce early inference of sub-states
			earlyInfer(s as TUState, acceptor, isPreIndexingPhase)
		]
	}

	// Internal helper methods
	def dispatch private toJvmMember(TUConstructor ctor) {
		ctor.toConstructor [
			documentation = ctor.documentation
			visibility = ctor.visibility.toJvmVisibility;

			for (param : ctor.parameters) {
				parameters += param.toParameter(param.name, param.parameterType) => [
					documentation = ctor.documentation
				];
			}

			body = ctor.body;
		]
	}

	def dispatch private toJvmMember(TUAttribute attr) {
		attr.toField(attr.name, attr.prefix.type) [
			documentation = attr.documentation
			visibility = attr.prefix.visibility.toJvmVisibility;
		]
	}

	def dispatch private toJvmMember(TUSignalAttribute attr) {
		attr.toField(attr.name, attr.type) [
			documentation = attr.documentation
			visibility = attr.visibility.toJvmVisibility;
		]
	}

	def dispatch private toJvmMember(TUOperation op) {
		op.toMethod(op.name, op.prefix.type) [
			documentation = op.documentation
			visibility = op.prefix.visibility.toJvmVisibility;

			for (JvmFormalParameter param : op.parameters) {
				parameters += param.toParameter(param.name, param.parameterType) => [
					documentation = param.documentation
				];
			}

			body = op.body;
		]
	}

	def dispatch private toJvmMember(TUEntryOrExitActivity act) {
		val name = if(act.entry) "entry" else "exit";

		return act.toMethod(name, Void.TYPE.typeRef) [
			documentation = act.documentation
			visibility = JvmVisibility.PUBLIC;
			annotations += annotationRef(Override);
			body = act.body;
		]
	}

	def dispatch private JvmMember toJvmMember(TUTransition trans) {
		trans.toClass(trans.name) [
			documentation = trans.documentation
			superTypes += StateMachine.Transition.typeRef;

			for (member : trans.members) {
				switch (member) {
					TUTransitionTrigger,
					TUTransitionVertex: {
						annotations += member.toAnnotationRef;
					}
					default: {
						members += member.toJvmMember;
					}
				}
			}
		]
	}

	def dispatch private toJvmMember(TUTransitionEffect effect) {
		effect.toMethod("effect", Void.TYPE.typeRef) [
			documentation = effect.documentation
			visibility = JvmVisibility.PUBLIC;
			annotations += annotationRef(Override);
			body = effect.body;
		]
	}

	def dispatch private toJvmMember(TUTransitionGuard guard) {
		guard.toMethod("guard", Boolean.TYPE.typeRef) [
			documentation = guard.documentation
			visibility = JvmVisibility.PUBLIC;

			annotations += annotationRef(Override);
			if (guard.^else) {
				body = '''return Else();''';
			} else {
				body = guard.expression;
			}
		]
	}

	def dispatch private toJvmMember(TUAssociationEnd end) {
		end.toClass(end.fullyQualifiedName) [
			documentation = end.documentation
			visibility = JvmVisibility.PUBLIC;

			val calcApiSuperTypeResult = end.calculateApiSuperType
			superTypes += calcApiSuperTypeResult.key;

			if (calcApiSuperTypeResult.value != null) {
				annotations += calcApiSuperTypeResult.value.key.toAnnotationRef(Min);
				if (!end.multiplicity.isUpperInf) {
					annotations += calcApiSuperTypeResult.value.value.toAnnotationRef(Max);
				}
			}
		]
	}

	// Commons
	def private toJvmVisibility(TUVisibility it) {
		if (it == TUVisibility.PACKAGE)
			JvmVisibility.DEFAULT
		else
			JvmVisibility.getByName(getName())
	}

	def dispatch private toAnnotationRef(TUTransitionTrigger it) {
		trigger.toAnnotationRef(Trigger)
	}

	def dispatch private toAnnotationRef(TUTransitionVertex it) {
		vertex.toAnnotationRef(
			if (from) {
				From
			} else {
				To
			}
		)
	}

	def dispatch private toAnnotationRef(TUState it) {
		toAnnotationRef(From)
	}

	def private toAnnotationRef(EObject obj, Class<?> annotationType) {
		annotationRef(annotationType) => [
			explicitValues += TypesFactory::eINSTANCE.createJvmTypeAnnotationValue => [
				values += (obj.getPrimaryJvmElement as JvmDeclaredType).typeRef;
			]
		]
	}

	def private toAnnotationRef(int i, Class<?> annotationType) {
		annotationRef(annotationType) => [
			explicitValues += TypesFactory::eINSTANCE.createJvmIntAnnotationValue => [
				values += i;
			]
		]
	}

	def private calculateApiSuperType(TUAssociationEnd it) {
		val endClassTypeParam = (endClass.getPrimaryJvmElement as JvmDeclaredType).typeRef
		if (isContainer) {
			// Do not try to simplify the code here, as it breaks standalone builds.
			// The inferred type will be Class<? extend MaybeOneBase>, which is invalid,
			// as MaybeOneBase is a package private class in its own package.
			if (notNavigable) {
				return HiddenContainer.typeRef(endClassTypeParam) -> null
			} else {
				return Container.typeRef(endClassTypeParam) -> null
			}
		}

		val optionalHidden = if(notNavigable) "Hidden" else "";
		var Pair<Integer, Integer> explicitMultiplicities = null;
		val apiBoundTypeName = if (multiplicity.any) // *
				"Many"
			else if (!multiplicity.upperSet) { // <lower> (exact)
				if (multiplicity.lower == 1)
					"One"
				else {
					explicitMultiplicities = multiplicity.lower -> multiplicity.lower;
					"Multiple"
				}
			} else { // <lower> .. <upper>
				if (multiplicity.lower == 0 && multiplicity.upper == 1)
					"MaybeOne"
				else if (multiplicity.lower == 1 && multiplicity.upper == 1)
					"One"
				else if (multiplicity.lower == 0 && multiplicity.upperInf)
					"Many"
				else if (multiplicity.lower == 1 && multiplicity.upperInf)
					"Some"
				else {
					explicitMultiplicities = multiplicity.lower -> multiplicity.upper;
					"Multiple"
				}
			}

		val endClassImpl = "hu.elte.txtuml.api.model.Association$" + optionalHidden + apiBoundTypeName
		return endClassImpl.typeRef(endClassTypeParam) -> explicitMultiplicities;
	}
}
