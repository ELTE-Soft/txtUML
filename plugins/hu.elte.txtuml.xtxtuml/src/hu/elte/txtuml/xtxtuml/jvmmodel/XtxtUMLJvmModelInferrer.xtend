package hu.elte.txtuml.xtxtuml.jvmmodel;

// Guice
import com.google.inject.Inject;

// EMF
import org.eclipse.emf.ecore.EObject;

// Xtext / Xbase
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.TypesFactory;
import org.eclipse.xtext.common.types.JvmVisibility;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.common.types.JvmFormalParameter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder;
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations;
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer;
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor;

// XtxtUML
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModel
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex

class XtxtUMLJvmModelInferrer extends AbstractModelInferrer {

	// Helper extensions
	
	@Inject extension JvmTypesBuilder;
	@Inject extension IJvmModelAssociations;
	@Inject extension IQualifiedNameProvider;

	// Main inferrer methods, called automatically by the framework
	
	def dispatch void infer(TUModel model, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(model.toClass(model.fullyQualifiedName)) [
			visibility = JvmVisibility.PUBLIC;
			superTypes += hu.elte.txtuml.api.model.Model.typeRef;

			for (element : model.elements) {
				if (element instanceof TUAssociation) {
					members += element.toJvmMember;
				} else {
					members += element.getPrimaryJvmElement as JvmMember;
				}
			}
		]
		
		for (element : model.elements) {
			if (!(element instanceof TUAssociation)) {
				internalInfer(element, acceptor, isPreIndexingPhase);
			}
		}
	}
	
	def dispatch void infer(TUExecution exec, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(exec.toClass(exec.fullyQualifiedName)) [
			visibility = JvmVisibility.PUBLIC;
			
			members += exec.toMethod("main", Void.TYPE.typeRef) [
				parameters += exec.toParameter("args", String.typeRef.addArrayTypeDimension);
				varArgs = true;
				static = true;
				body = exec.body;
			]
		]
	}
	
	// Internal inferrer methods
	
	def dispatch private void internalInfer(TUSignal signal, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(signal.toClass(signal.fullyQualifiedName)) [
			superTypes += hu.elte.txtuml.api.model.Signal.typeRef;
			static = true;
			
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
	
	def dispatch private void internalInfer(TUClass tUClass, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(tUClass.toClass(tUClass.fullyQualifiedName)) [
			if (tUClass.superClass != null) {
				superTypes += (tUClass.superClass.getPrimaryJvmElement as JvmDeclaredType).typeRef;
			} else {
				superTypes += hu.elte.txtuml.api.model.ModelClass.typeRef;
			}
	
			for (member : tUClass.members) {
				if (member instanceof TUState) {
					members += member.getPrimaryJvmElement as JvmMember;
				} else {
					members += member.toJvmMember;
				}				
			}
		]
		
		for (member : tUClass.members) {
			if (member instanceof TUState) {
				internalInfer(member, acceptor, isPreIndexingPhase);
			}
		}
	}
	
	def dispatch private void internalInfer(TUState state, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		acceptor.accept(state.toClass(state.fullyQualifiedName)) [
			superTypes += switch (state.type) {
				case PLAIN     : hu.elte.txtuml.api.model.StateMachine.State.typeRef
				case INITIAL   : hu.elte.txtuml.api.model.StateMachine.Initial.typeRef
				case CHOICE    : hu.elte.txtuml.api.model.StateMachine.Choice.typeRef
				case COMPOSITE : hu.elte.txtuml.api.model.StateMachine.CompositeState.typeRef
			}
			
			for (member : state.members) {
				if (member instanceof TUState) {
					members += member.getPrimaryJvmElement as JvmMember;
				} else {
					members += member.toJvmMember;
				}
			}
		]
		
		for (member : state.members) {
			if (member instanceof TUState) {
				internalInfer(member, acceptor, isPreIndexingPhase);
			}
		}
	}

	// Internal helper methods
	
	/**
	 * Explicit return type required due to recursion
	 */
	def dispatch private JvmMember toJvmMember(TUAssociation assoc) {
		assoc.toClass(assoc.fullyQualifiedName) [
			visibility = JvmVisibility.PUBLIC;
			superTypes += hu.elte.txtuml.api.model.Association.typeRef;
			
			for (end : assoc.ends) {
				members += end.toJvmMember;
			}
		]
	}
	
	def dispatch private toJvmMember(TUConstructor ctor) {
		ctor.toConstructor[
			visibility = ctor.visibility.toJvmVisibility;
			
			for (param : ctor.parameters) {
				parameters += param.toParameter(param.name, param.parameterType);
			}
			
			body = ctor.body;
		]
	}
	
	def dispatch private toJvmMember(TUAttribute attr) {
		attr.toField(attr.name, attr.prefix.type) [
			visibility = attr.prefix.visibility.toJvmVisibility;
		]
	}
	
	def dispatch private toJvmMember(TUSignalAttribute attr) {
		attr.toField(attr.name, attr.type) [
			visibility = attr.visibility.toJvmVisibility;
		]
	}
	
	def dispatch private toJvmMember(TUOperation op) {
		op.toMethod(op.name, op.prefix.type) [
			visibility = op.prefix.visibility.toJvmVisibility;

			for (JvmFormalParameter param : op.parameters) {
				parameters += param.toParameter(param.name, param.parameterType);
			}

			body = op.body;
		]
	}

	def dispatch private toJvmMember(TUEntryOrExitActivity act) {	
		val name = if (act.entry) "entry" else "exit";
		
		return act.toMethod(name, Void.TYPE.typeRef) [
			visibility = JvmVisibility.PUBLIC;
			annotations += annotationRef(java.lang.Override);
			body = act.body;
		]
	}
	
	/**
	 * Explicit return type required due to recursion
	 */ 
	def dispatch private JvmMember toJvmMember(TUTransition trans) {
		trans.toClass(trans.name) [
			superTypes += hu.elte.txtuml.api.model.StateMachine.Transition.typeRef;
		
			for (member : trans.members) {
				switch (member) {
					TUTransitionTrigger, TUTransitionVertex : {
						annotations += member.toAnnotationRef;
					}
					
					default : {
						members += member.toJvmMember;
					}
				}
			}
		]
	}
	
	def dispatch private toJvmMember(TUTransitionEffect effect) {
		effect.toMethod("effect", Void.TYPE.typeRef) [
			visibility = JvmVisibility.PUBLIC;
			annotations += annotationRef(java.lang.Override);
			body = effect.body;
		]
	}
	
	def dispatch private toJvmMember(TUTransitionGuard guard) {
		guard.toMethod("guard", Boolean.TYPE.typeRef) [
			visibility = JvmVisibility.PUBLIC;
			annotations += annotationRef(java.lang.Override);
			body = guard.expression;
		]
	}
	
	def dispatch private toJvmMember(TUAssociationEnd end) {
		end.toClass(end.fullyQualifiedName) [
			visibility = JvmVisibility.PUBLIC;
			superTypes += end.calculateApiSuperType;
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
		trigger.toAnnotationRef(hu.elte.txtuml.api.model.Trigger)
	}
	
	def dispatch private toAnnotationRef(TUTransitionVertex it) {
		vertex.toAnnotationRef(
			if (from) {
				hu.elte.txtuml.api.model.From
			} else {
				hu.elte.txtuml.api.model.To
			}
		)
	}
	
	def dispatch private toAnnotationRef(TUState it) {
		toAnnotationRef(hu.elte.txtuml.api.model.From)
	}

	def private toAnnotationRef(EObject obj, java.lang.Class<?> annotationType) {
		annotationRef(annotationType) => [
			explicitValues += TypesFactory::eINSTANCE.createJvmTypeAnnotationValue => [
				values += (obj.getPrimaryJvmElement as JvmDeclaredType).typeRef;
			]
		]
	}

	def private calculateApiSuperType(TUAssociationEnd it) {
		val optionalHidden = if (notNavigable) "Hidden" else "";
		val apiBoundTypeName
		 = 	if (multiplicity.any) // *
		 		"Many"
		 	else if (!multiplicity.upperSet) { // <lower> (exact)
		 		if (multiplicity.lower == 1)
		 			"One"
		 		else
		 			"Many"
		 	} else { // <lower> .. <upper>
		 		if (multiplicity.lower == 0 && multiplicity.upper == 1)
					"MaybeOne"
				else if (multiplicity.lower == 1 && multiplicity.upper == 1)
					"One"
				else if (multiplicity.lower == 0 && multiplicity.upperInf)
					"Many"
				else if (multiplicity.lower == 1 && multiplicity.upperInf)
					"Some"
				else
					"Many"
		 	}
		
		return ("hu.elte.txtuml.api.model.Association$" + optionalHidden + apiBoundTypeName)
			.typeRef((endClass.getPrimaryJvmElement as JvmDeclaredType).typeRef);
	}

}

