/*
 * generated by Xtext 2.10.0
 */
package hu.elte.txtuml.xd.jvmmodel

import com.google.inject.Inject
import hu.elte.txtuml.api.layout.Above
import hu.elte.txtuml.api.layout.Alignment
import hu.elte.txtuml.api.layout.AlignmentType
import hu.elte.txtuml.api.layout.Below
import hu.elte.txtuml.api.layout.BottomMost
import hu.elte.txtuml.api.layout.ClassDiagram
import hu.elte.txtuml.api.layout.Column
import hu.elte.txtuml.api.layout.Contains
import hu.elte.txtuml.api.layout.Diagram
import hu.elte.txtuml.api.layout.Diagram.Layout
import hu.elte.txtuml.api.layout.Diagram.LinkGroup
import hu.elte.txtuml.api.layout.Diagram.NodeGroup
import hu.elte.txtuml.api.layout.Diamond
import hu.elte.txtuml.api.layout.East
import hu.elte.txtuml.api.layout.Left
import hu.elte.txtuml.api.layout.LeftMost
import hu.elte.txtuml.api.layout.LinkEnd
import hu.elte.txtuml.api.layout.North
import hu.elte.txtuml.api.layout.Priority
import hu.elte.txtuml.api.layout.Right
import hu.elte.txtuml.api.layout.RightMost
import hu.elte.txtuml.api.layout.Row
import hu.elte.txtuml.api.layout.Show
import hu.elte.txtuml.api.layout.South
import hu.elte.txtuml.api.layout.Spacing
import hu.elte.txtuml.api.layout.StateMachineDiagram
import hu.elte.txtuml.api.layout.TopMost
import hu.elte.txtuml.api.layout.West
import hu.elte.txtuml.api.model.Association
import hu.elte.txtuml.api.model.Composition
import hu.elte.txtuml.api.model.ModelClass
import hu.elte.txtuml.api.model.StateMachine.Transition
import hu.elte.txtuml.api.model.StateMachine.Vertex
import hu.elte.txtuml.xd.xDiagramDefinition.XDArgumentExpression
import hu.elte.txtuml.xd.xDiagramDefinition.XDBinaryIdentifierInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDBinaryListInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDDiagram
import hu.elte.txtuml.xd.xDiagramDefinition.XDDiamondInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDGroupInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDModel
import hu.elte.txtuml.xd.xDiagramDefinition.XDNumericExpression
import hu.elte.txtuml.xd.xDiagramDefinition.XDPackageDeclaration
import hu.elte.txtuml.xd.xDiagramDefinition.XDPhantomInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDPriorityInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDTypeExpression
import hu.elte.txtuml.xd.xDiagramDefinition.XDTypeExpressionList
import hu.elte.txtuml.xd.xDiagramDefinition.XDUnaryListInstruction
import hu.elte.txtuml.xd.xDiagramDefinition.XDUnaryNumberInstruction
import java.util.ArrayList
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.common.types.JvmTypeReference
import org.eclipse.xtext.common.types.TypesFactory
import org.eclipse.xtext.common.types.impl.JvmEnumerationTypeImplCustom
import org.eclipse.xtext.xbase.jvmmodel.AbstractModelInferrer
import org.eclipse.xtext.xbase.jvmmodel.IJvmDeclaredTypeAcceptor
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import org.eclipse.xtext.xbase.typesystem.IBatchTypeResolver

//import hu.elte.txtuml.api.layout.Diagram;

/**
 * <p>Infers a JVM model from the source model.</p> 
 *
 * <p>The JVM model should contain all elements that would appear in the Java code 
 * which is generated from the source model. Other models link against the JVM model rather than the source model.</p>     
 */
class XDiagramDefinitionJvmModelInferrer extends AbstractModelInferrer {

	/**
	 * convenience API to build and initialize JVM types and their members.
	 */
	@Inject extension JvmTypesBuilder
	@Inject private IBatchTypeResolver typeResolver;
	
//	@Inject extension IQualifiedNameProvider
	@Extension protected XDiagramDefinitionTypeHelper typeHelper = new XDiagramDefinitionTypeHelper();
	
//	@Inject TypeReferences typeReferences 

	/**
	 * The dispatch method {@code infer} is called for each instance of the
	 * given element's type that is contained in a resource.
	 * 
	 * @param element
	 *            the model to create one or more
	 *            {@link JvmDeclaredType declared
	 *            types} from.
	 * @param acceptor
	 *            each created
	 *            {@link JvmDeclaredType type}
	 *            without a container should be passed to the acceptor in order
	 *            get attached to the current resource. The acceptor's
	 *            {@link IJvmDeclaredTypeAcceptor#accept(org.eclipse.xtext.common.types.JvmDeclaredType)
	 *            accept(..)} method takes the constructed empty type for the
	 *            pre-indexing phase. This one is further initialized in the
	 *            indexing phase using the lambda you pass as the last argument.
	 * @param isPreIndexingPhase
	 *            whether the method is called in a pre-indexing phase, i.e.
	 *            when the global index is not yet fully updated. You must not
	 *            rely on linking using the index if isPreIndexingPhase is
	 *            <code>true</code>.
	 */
//	def dispatch void infer(Model element, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
//		// Here you explain how your model is mapped to Java elements, by writing the actual translation code.
//		
//		// An implementation for the initial hello world example could look like this:
//// 		acceptor.accept(element.toClass("my.company.greeting.MyGreetings")) [
//// 			for (greeting : element.greetings) {
//// 				members += greeting.toMethod("hello" + greeting.name, typeRef(String)) [
//// 					body = '''
////						return "Hello �greeting.name�";
////					'''
////				]
////			}
////		]
//	}

	var currentDiagramClass = null as JvmGenericType;
	var currentLayoutClass = null as JvmGenericType;
	
	var anonPhantoms = newArrayList();

	def dispatch void infer(XDInstruction element, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		infer(element.wrapped, acceptor, isPreIndexingPhase);
	}
	
	def dispatch void infer(XDPackageDeclaration element, IJvmDeclaredTypeAcceptor acceptor, boolean isPreindexintPhase) {
		currentPackageDecl = element;
		currentTypeResolver = typeResolver;		
	}
	
	def dispatch void infer(XDDiagram element, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {		
		currentSignature = element;

		val diagType = switch (element.diagramType){
			case "state-machine-diagram": StateMachineDiagram
			case "class-diagram" : ClassDiagram
			default: null
		}
		
		val generics = new ArrayList<JvmTypeReference>()
		if ("state-machine-diagram".equals(element.diagramType)){
			generics.add(element.genArg.typeRef());
		}
		
		var result = currentDiagramClass = element.toClass(element.findPackageName + element.name)[
			superTypes += typeRef(diagType, generics);
			documentation = element.documentation;
		];
		
		element.associate(result);
		acceptor.accept(result);
		
		currentLayoutClass = element.toClass("__Layout")[
			superTypes += Layout.typeRef();
			declaringType = currentDiagramClass;
		];
		
		element.instructions.forEach[infer(acceptor, isPreIndexingPhase)];
	}
	
	def private boolean isNodeType(XDTypeExpression exp){
		if (exp.phantom != null) return true;
		if (exp.name.checkSuperTypes(NodeGroup, ModelClass, Vertex)) return true;
		return false;
	}
	
	def private boolean isLinkType(XDTypeExpression exp){
		if (exp.phantom != null) return false;
		if (exp.name.checkSuperTypes(LinkGroup, Association, Composition, Transition)) return true;
		return false;
	}
	
	def dispatch void infer(XDGroupInstruction element, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		val nodeArgsCount = element.^val.wrapped.expressions.filter[it.isNodeType()].size();
		val linkArgsCount = element.^val.wrapped.expressions.filter[it.isLinkType()].size();
		
		val isLinkGroup = nodeArgsCount == 0 && linkArgsCount > 0;
		
		var groupType = element.toClass(element.name) [
			documentation = element.documentation;
			declaringType = currentDiagramClass;
			superTypes += if (isLinkGroup) LinkGroup.typeRef else NodeGroup.typeRef();
			annotations += Contains.createAnnotationTEList("value" -> element.^val.wrapped);
		]
		
		acceptor.accept(groupType);

		val alignment = if (element.align == null || isLinkGroup) {
			null
		} else {
			String.valueOf(switch(element.align){
				case "top-to-bottom" : AlignmentType.TopToBottom
				case "bottom-to-top" : AlignmentType.BottomToTop
				case "left-to-right" : AlignmentType.LeftToRight
				case "right-to-left" : AlignmentType.RightToLeft
				default : null		
			})
		};

		if (alignment != null){
			var newAnnotation = annotationRef(Alignment) => [ annotationRef |
				annotationRef.explicitValues += TypesFactory::eINSTANCE.createJvmEnumAnnotationValue => [
					values += (AlignmentType.typeRef().type as JvmEnumerationTypeImplCustom).literals.findFirst[simpleName == alignment];
					operation = annotationRef.annotation.declaredOperations.findFirst[simpleName == "value"];
				]
			]
			groupType.annotations += newAnnotation;
		}
	}
	
	def private parseDoubleValue(XDNumericExpression expression){
		if (expression.perc == "%"){
			return Double.valueOf(expression.value) / 100.0;
		}
		return Double.valueOf(expression.value);
	}
	
	def dispatch void infer(XDUnaryNumberInstruction element, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		var annType = null as Class<?>;
		annType = switch(element.op){
			case "spacing": Spacing
		};
		
		var newAnnotation = annotationRef(annType) => [ annotationRef |
			annotationRef.explicitValues += TypesFactory::eINSTANCE.createJvmDoubleAnnotationValue => [
				values += element.^val.wrapped.parseDoubleValue();
			]
		]
		
		this.currentLayoutClass.annotations += newAnnotation;
	}
	
	def dispatch void infer(XDPhantomInstruction element, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase){
		acceptor.accept(element.toClass(element.name) [
			documentation = element.documentation;
			declaringType = currentDiagramClass;
			superTypes += Diagram.Phantom.typeRef();
		]);
	}
	

	def dispatch void infer(XDDiamondInstruction element, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		var top = null as XDArgumentExpression;
		var right = null as XDArgumentExpression;
		var bottom = null as XDArgumentExpression;
		var left = null as XDArgumentExpression;
		
		if (element.args.wrapped.expressions.exists[x|x.argName != null]){
			top = element.args.wrapped.expressions.findFirst[x | "top".equals(x.argName)];			
			right = element.args.wrapped.expressions.findFirst[x | "right".equals(x.argName)];			
			bottom = element.args.wrapped.expressions.findFirst[x | "bottom".equals(x.argName)];			
			left = element.args.wrapped.expressions.findFirst[x | "left".equals(x.argName)];			
		} else {
			top = element.args.wrapped.expressions.get(0);			
			right = element.args.wrapped.expressions.get(1);			
			bottom = element.args.wrapped.expressions.get(2);			
			left = element.args.wrapped.expressions.get(3);			
		}
		
		var newAnnotation = Diamond.createAnnotationJVMGenericType(			
			"top" -> buildTypeOrPhantom(element, top),
			"right" -> buildTypeOrPhantom(element, right),
			"bottom" -> buildTypeOrPhantom(element, bottom),
			"left" -> buildTypeOrPhantom(element, left)
		);
		
		this.currentLayoutClass.annotations += newAnnotation;
	}
	
	def JvmGenericType buildTypeOrPhantom(XDDiamondInstruction element, XDArgumentExpression expr){
		if (expr == null) {
			return element.buildPhantom();
		} else {
			return expr.expression.handlePhantom();
		}
	}
	
	def dispatch void infer(XDBinaryIdentifierInstruction element, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
        var annType = null as Class<?>; 
		annType = switch(element.op){
			case "left-of": Left
			case "right-of": Right
			case "above": Above
			case "below": Below			
		};

		var newAnnotation = annType.createAnnotationTE(
			"val" -> element.^val.wrapped,
			"from" -> element.of.wrapped
		);
				
		this.currentLayoutClass.annotations += newAnnotation;
	}
	
	def dispatch void infer(XDPriorityInstruction element,  IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
		var newAnnotation = annotationRef(Priority) => [ annotationRef |
			annotationRef.explicitValues += TypesFactory::eINSTANCE.createJvmIntAnnotationValue => [
				values += element.prior.wrapped;
				operation = annotationRef.annotation.declaredOperations.findFirst[it.simpleName == "prior"];
			]
			annotationRef.explicitValues += TypesFactory::eINSTANCE.createJvmTypeAnnotationValue => [
				values += element.^val.wrapped.expressions.map[it.name.typeRef()];
				operation = annotationRef.annotation.declaredOperations.findFirst[it.simpleName == "val"];
			]
		]
		
		this.currentLayoutClass.annotations += newAnnotation;
			
	}
		
	def dispatch void infer(XDUnaryListInstruction element,  IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
        var annType = null as Class<?>; 
		annType = switch(element.op){
			case "row" : Row
			case "column" : Column
			case "topmost" : TopMost
			case "bottommost" : BottomMost
			case "leftmost" : LeftMost
			case "rightmost" : RightMost
			case "show" : Show
		};
		
		var newAnnotation = annType.createAnnotationTEList(
			"value" -> element.^val.wrapped
		);
		
		this.currentLayoutClass.annotations += newAnnotation;
	}
	
	
	
	def dispatch void infer(XDBinaryListInstruction element, IJvmDeclaredTypeAcceptor acceptor, boolean isPreIndexingPhase) {
        var annType = null as Class<?>; 
		annType = switch(element.op){
			case "east-of": East
			case "west-of": West
			case "north-of": North
			case "south-of": South
		};
		
		val linkEnd = if (element.linkEnd == null) { 
			LinkEnd.Default
		} else { 
			switch(element.linkEnd){
				case "start" : LinkEnd.Start
				case "end" : LinkEnd.End
				default : LinkEnd.Default
			}
		};
		
		// SOURCE: https://www.eclipse.org/forums/index.php/t/300722/
		
		var newAnnotation = annotationRef(annType) => [ annotationRef |
			if (linkEnd != LinkEnd.Default){
				annotationRef.explicitValues += TypesFactory::eINSTANCE.createJvmEnumAnnotationValue => [
					val name = String.valueOf(linkEnd);
					values += (LinkEnd.typeRef().type as JvmEnumerationTypeImplCustom).literals.findFirst[simpleName == name];
					operation = annotationRef.annotation.declaredOperations.findFirst[simpleName == "end"];
				]
			}
			annotationRef.explicitValues += TypesFactory::eINSTANCE.createJvmTypeAnnotationValue => [
				values += element.^val.wrapped.expressions.map[it.handlePhantom().typeRef()];
				operation = annotationRef.annotation.declaredOperations.findFirst[simpleName == "val"];
			]
			annotationRef.explicitValues += TypesFactory::eINSTANCE.createJvmTypeAnnotationValue => [
				values += element.of.wrapped.expressions.map[it.handlePhantom().typeRef()];
				operation = annotationRef.annotation.declaredOperations.findFirst[simpleName == "from"];
			]
		]
		
		// ???
		element.of.wrapped.expressions.map[name.typeRef()]
						
		this.currentLayoutClass.annotations += newAnnotation;
	}
		
	def private createAnnotationJVMGenericType(Class<?> annotationType, Pair<String, JvmGenericType>... params){
		annotationRef(annotationType) => [ annotationRef |
			for (param : params) {
				annotationRef.explicitValues += TypesFactory::eINSTANCE.createJvmTypeAnnotationValue => [
					values += param.value.typeRef();
					if (params.size != 1 || param.key != "value") {
						operation = annotationRef.annotation.declaredOperations.findFirst[it.simpleName == param.key]
					}
				]
			}
		]
	}
		
	def private createAnnotationTE(Class<?> annotationType, Pair<String, XDTypeExpression>... params) {
		annotationRef(annotationType) => [ annotationRef |
			for (param : params) {
				annotationRef.explicitValues += TypesFactory::eINSTANCE.createJvmTypeAnnotationValue => [
					values += param.value.handlePhantom().typeRef();
					if (params.size != 1 || param.key != "value") {
						operation = annotationRef.annotation.declaredOperations.findFirst[it.simpleName == param.key]
					}
				]
			}
		]
	}
	
	def private createAnnotationTEList(Class<?> annotationType, Pair<String, XDTypeExpressionList>... params) {
		annotationRef(annotationType) => [ annotationRef |
			for (param : params) {
				annotationRef.explicitValues += TypesFactory::eINSTANCE.createJvmTypeAnnotationValue => [
					values += param.value.expressions.map[x | x.handlePhantom.typeRef()];
					if (params.size != 1 || param.key != "value") {
						operation = annotationRef.annotation.declaredOperations.findFirst[it.simpleName == param.key]
					}
				]
			}
		]
	}
	
	def private JvmGenericType handlePhantom(XDTypeExpression expression){
		if (expression.phantom != null){
			return expression.buildPhantom();
		}
		return expression.name;
	}
	
	def JvmGenericType buildPhantom(EObject element){			
		var phantom = element.toClass("__Phantom" + anonPhantoms.size()) [
				declaringType = currentDiagramClass;
				superTypes += Diagram.Phantom.typeRef();
			];
			anonPhantoms.add(phantom)
			return phantom;
	}
		
	var model = null as XDModel;

	def XDModel findModel(EObject element){
		if (model != null) return model;
		
		var result = element;

		while(result != null) {		
			if (result instanceof XDModel){ 
				return model = result as XDModel;
			}
			result = result.eContainer;	
		} 
		
		return result as XDModel;
	}
	
	def String findPackageName(EObject element){
		return (element.findModel.package.name + ".") ?: "";
	}
	
	def String findDiagramName(EObject element){
		return element.findModel.diagram?.name ?: "";
	}
	
	def JvmType findGenericArg(EObject element){
		return element.findModel.diagram?.genArg ?: null;
	}
}