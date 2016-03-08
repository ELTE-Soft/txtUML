package hu.elte.txtuml.xtxtuml.imports

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassPropertyAccessExpression
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnectorEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUPortMember
import hu.elte.txtuml.xtxtuml.xtxtUML.TUReception
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

	override collectAllReferences(EObject rootElement) {
		super.collectAllReferences(rootElement);

		// explicit type declaration is required to avoid incorrect type inference
		val TreeIterator<EObject> contents = EcoreUtil.getAllContents(rootElement, true);

		while (contents.hasNext()) {
			val references = new ArrayList<Pair<JvmType, ITextRegion>>();

			// determine the grammar-level cross-referenced types inside XtxtUML expressions
			switch (next : contents.next()) {
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
					references.add(next.right?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUClassPropertyAccessExpression_Right, 0))
				TUReception:
					references.add(next.signal?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUReception_Signal, 0))
				TUConnectorEnd: {
					references.add(next.role?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUConnectorEnd_Role, 0))
					references.add(next.port?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUConnectorEnd_Port, 0))
				}
				TUPortMember:
					references.add(next.interface?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUPortMember_Interface, 0))
				TUTransitionPort:
					references.add(next.port?.getPrimaryJvmElement as JvmType ->
						next.getFullTextRegion(XtxtUMLPackage::eINSTANCE.TUTransitionPort_Port, 0))
			}

			for (ref : references) {
				var jvmType = ref.key;
				var refRegion = ref.value;

				/* Apart from the original type references, enclosing types shall be considered too.
				 * For example, if A is imported, B is a nested class of A and A.B is referenced, then
				 * A is used as well, and so on.
				 */
				while (jvmType != null && refRegion != null) {
					acceptType(jvmType, refRegion);

					val enclosingClassRefLength = refRegion.length - jvmType.simpleName.length - 1;
					refRegion = if (enclosingClassRefLength > 0) {
						new TextRegion(refRegion.offset, enclosingClassRefLength)
					} else {
						null
					}

					jvmType = jvmType.eContainer as JvmType;
				}
			}
		}
	}

	/*
	 * Copy of the super implementation, as the used isIgnored method needs customization,
	 * but it is private. This comment not being documentation is intentional.
	 */
	override acceptType(JvmType type, JvmType usedType, ITextRegion refRegion) {
		val currentContext = getCurrentContext();
		if (currentContext == null) {
			return;
		}

		if (type == null || type.eIsProxy()) {
			throw new IllegalArgumentException();
		}

		if (type instanceof JvmDeclaredType && !isIgnored(type, refRegion)) {
			getTypeUsages().addTypeUsage(type as JvmDeclaredType, usedType as JvmDeclaredType, refRegion,
				currentContext);
		}
	}

	/*
	 * Copy of the super implementation, apart from the commented line.
	 * This comment not being documentation is intentional.
	 */
	def isIgnored(JvmType type, ITextRegion refRegion) {
		val parseResult = getResource().getParseResult();
		if (parseResult == null) {
			return false;
		}

		val completeText = parseResult.getRootNode().getText();
		val refText = completeText.subSequence(refRegion.getOffset(), refRegion.getOffset() + refRegion.getLength());

		/*
		 * By default, generated Java equivalents of nested XtxtUML types are distinguished by using
		 * '$' as separator instead of '.' between enclosing and nested classes in qualified names.
		 * This leads to false unused import warnings in XtxtUML, therefore '.' should be used
		 * uniformly.
		 */
		return type.getQualifiedName(".").equals(refText);
	}

}
