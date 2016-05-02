package hu.elte.txtuml.xtxtuml.validation;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelDeclaration
import java.util.List
import java.util.Map
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xtype.XImportDeclaration

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

class XtxtUMLFileValidator extends XtxtUMLExpressionValidator {

	@Check
	def checkModelDeclarationIsInModelInfoFile(TUModelDeclaration modelDeclaration) {
		var name = modelDeclaration.eResource?.URI?.lastSegment ?: "";
		if (name != "model-info.xtxtuml") {
			error('Model declaration must be specified in "model-info.xtxtuml"', modelDeclaration,
				TU_MODEL_DECLARATION__MODEL, MISPLACED_MODEL_DECLARATION);
		}
	}

	override protected addImportUnusedIssues(Map<String, List<XImportDeclaration>> imports) {
		// unused import warnings would be misleading in the current implementation
	}

}
