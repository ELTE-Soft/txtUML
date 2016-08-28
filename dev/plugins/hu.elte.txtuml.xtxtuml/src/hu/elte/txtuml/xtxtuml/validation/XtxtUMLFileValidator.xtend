package hu.elte.txtuml.xtxtuml.validation;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
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
		if (!isModelInfo(modelDeclaration)) {
			error("Model declarations must be specified either in 'model-info.xtxtuml' or 'model-info.txtuml'",
				modelDeclaration, TU_MODEL_DECLARATION__MODEL, MISPLACED_MODEL_DECLARATION);
		}
	}

	@Check
	def checkModelInfoContainsModelDeclaration(TUFile file) {
		if (isModelInfo(file) && !(file instanceof TUModelDeclaration)) {
			error("A 'model-info' file must contain exactly one model declaration", file, null,
				WRONG_MODEL_INFO);
		}
	}

	def isModelInfo(TUFile file) {
		var fileName = file.eResource?.URI?.trimFileExtension?.lastSegment ?: "";
		return fileName == "model-info";
	}

	@Check
	def checkDefaultPackageIsNotUsed(TUFile file) {
		if (file.name == null) {
			error("The default package cannot be used in txtUML", file, null, WRONG_PACKAGE);
		}
	}

	override protected addImportUnusedIssues(Map<String, List<XImportDeclaration>> imports) {
		// unused import warnings would be misleading in the current implementation
	}

}
