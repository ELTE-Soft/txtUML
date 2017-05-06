// SOURCE: ? http://www.lorenzobettini.it/2013/01/the_ximportsection_in_xbase_2_4/
// SOURCE: ? https://www.eclipse.org/forums/index.php/t/980986/
package hu.elte.txtuml.xd.imports

import com.google.inject.Inject
import hu.elte.txtuml.xd.xDiagramDefinition.XDDiagram
import hu.elte.txtuml.xd.xDiagramDefinition.XDTypeExpression
import hu.elte.txtuml.xd.xDiagramDefinition.XDiagramDefinitionPackage
import java.util.ArrayList
import org.eclipse.emf.common.util.TreeIterator
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.xtext.common.types.JvmType
import org.eclipse.xtext.resource.ILocationInFileProvider
import org.eclipse.xtext.util.ITextRegion
import org.eclipse.xtext.xbase.imports.ImportedTypesCollector
import org.eclipse.xtext.xbase.jvmmodel.IJvmModelAssociations

class XDiagramDefinitionImportedTypesCollector extends ImportedTypesCollector {
	@Inject extension IJvmModelAssociations;
	@Inject extension ILocationInFileProvider;

	override protected collectAllReferences(EObject rootElement) {
		println("collectAllReferences called for " + rootElement)
		
		super.collectAllReferences(rootElement)
		
		// explicit type declaration is required to avoid incorrect type inference
		val TreeIterator<EObject> contents = EcoreUtil.getAllContents(rootElement, true);

		while (contents.hasNext()) {
			val references = new ArrayList<Pair<JvmType, ITextRegion>>();

			// determine the grammar-level cross-referenced types inside XtxtUML expressions
			switch (next : contents.next()) {
				XDTypeExpression:
					references.add(next.name -> next.getFullTextRegion(XDiagramDefinitionPackage::eINSTANCE.XDTypeExpression_Name, 0))
				XDDiagram:
					references.add(next.genArg -> next.getFullTextRegion(XDiagramDefinitionPackage::eINSTANCE.XDDiagram_GenArg, 0))	
			}
			
			for (ref : references) {
				if (ref.key != null && ref.value != null) {
					acceptType(ref.key, ref.value);
				}
			}
		}
	}
}
