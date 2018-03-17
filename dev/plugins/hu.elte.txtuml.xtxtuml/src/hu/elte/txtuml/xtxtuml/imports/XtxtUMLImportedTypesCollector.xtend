package hu.elte.txtuml.xtxtuml.imports;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TULinkExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPortMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUReception
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionPort
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import java.util.ArrayList
import org.eclipse.emf.common.util.TreeIterator
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.resource.ILocationInFileProvider
import org.eclipse.xtext.util.ITextRegion
import org.eclipse.xtext.util.TextRegion
import org.eclipse.xtext.xbase.imports.ImportedTypesCollector
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations

class XtxtUMLImportedTypesCollector extends ImportedTypesCollector {

	@Inject extension IJvmModelAssociations;
	@Inject extension ILocationInFileProvider;

	/**
	 * Extends the default behavior to collect XtxtUML references as well.
	 */
	override protected collectAllReferences(EObject rootElement) {
		super.collectAllReferences(rootElement);

		// explicit type declaration is required to avoid incorrect type inference
		val TreeIterator<EObject> contents = EcoreUtil.getAllContents(rootElement, true);

		while (contents.hasNext()) {
			val references = new ArrayList<Pair<JvmType, ITextRegion>>();

			// determine the grammar-level cross-referenced types inside XtxtUML expressions
			switch (next : contents.next()) {
				TUSignal:
					references.add(next.superSignal?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUSignal_SuperSignal, 0))
				TUClass:
					references.add(next.superClass?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUClass_SuperClass, 0))
				TUTransitionTrigger:
					references.add(next.trigger?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUTransitionTrigger_Trigger, 0))
				TUTransitionVertex:
					references.add(next.vertex?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUTransitionVertex_Vertex, 0))
				TUAssociationEnd:
					references.add(next.endClass?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUAssociationEnd_EndClass, 0))
				TUClassPropertyAccessExpression:
					references.add(adjustedNestedClassReference(next.right?.getPrimaryJvmElement as JvmType,
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUClassPropertyAccessExpression_Right, 0)))
				TULinkExpression: {
					references.add(adjustedNestedClassReference(next.association?.getPrimaryJvmElement as JvmType,
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TULinkExpression_Association, 0)))
					#[next.leftEnd -> XtxtUMLPackage::eINSTANCE.TULinkExpression_LeftEnd,
							next.rightEnd -> XtxtUMLPackage::eINSTANCE.TULinkExpression_RightEnd].forEach[ endToFeature |
						references.add(adjustedNestedClassReference(endToFeature.key?.getPrimaryJvmElement as JvmType,
							next.getFullTextRegion(endToFeature.value, 0)))
					]
				}
				TUReception:
					references.add(next.signal?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUReception_Signal, 0))
				TUConnectorEnd: {
					references.add(adjustedNestedClassReference(next.role?.getPrimaryJvmElement as JvmType,
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUConnectorEnd_Role, 0)))
					references.add(adjustedNestedClassReference(next.port?.getPrimaryJvmElement as JvmType,
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUConnectorEnd_Port, 0)))
				}
				TUPortMember:
					references.add(next.interface?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUPortMember_Interface, 0))
				TUTransitionPort:
					references.add(next.port?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUTransitionPort_Port, 0))
			}

			for (ref : references) {
				if (ref.key != null && ref.value != null) {
					acceptType(ref.key, ref.value);
				}
			}
		}
	}

	/**
	 * Overrides the super implementation to accept types even in case of fully qualified name references.
	 * This aspect is used to make fully qualified name references simplifiable during import organization.
	 */
	override protected acceptType(JvmType type, JvmType usedType, ITextRegion refRegion) {
		val currentContext = getCurrentContext();
		if (currentContext == null) {
			return;
		}

		if (type == null || type.eIsProxy()) {
			throw new IllegalArgumentException();
		}

		if (type instanceof JvmDeclaredType) {
			getTypeUsages().addTypeUsage(type as JvmDeclaredType, usedType as JvmDeclaredType, refRegion,
				currentContext);
		}
	}

	/**
	 * Adjusts the given (type, reference) pair if the type is a nested class which is referenced
	 * through its enclosing class, such that the actually used type becomes the enclosing class.
	 * Used to preserve this indirection during import organization.
	 */
	def private adjustedNestedClassReference(JvmType nestedClass, ITextRegion refRegion) {
		if (refRegion == null || nestedClass == null || !(nestedClass.eContainer instanceof JvmType)) {
			return null -> null;
		}

		if (refRegion.length > nestedClass.simpleName.length) {
			val enclosingClassRefLength = refRegion.length - ("." + nestedClass.simpleName).length;
			return nestedClass.eContainer as JvmType ->
				new TextRegion(refRegion.offset, enclosingClassRefLength) as ITextRegion;
		}

		return nestedClass -> refRegion;
	}

}
