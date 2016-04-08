package hu.elte.txtuml.xtxtuml.validation

import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelDeclaration
import java.util.List
import java.util.Map
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.validation.IssueCodes
import org.eclipse.xtext.xtype.XImportDeclaration

class XtxtUMLFileValidator extends XtxtUMLExpressionValidator {

	@Check
	def checkModelDeclarationIsInModelInfoFile(TUModelDeclaration modelDeclaration) {
		var name = modelDeclaration.eResource?.URI?.lastSegment ?: ""
		if (!"model-info.xtxtuml".equals(name)) {
			error('Model declaration must be specified in "model-info.xtxtuml".', modelDeclaration, null)
		}
	}

	override protected addImportUnusedIssues(Map<String, List<XImportDeclaration>> imports) {
		for (List<XImportDeclaration> importDeclarations : imports.values()) {
			for (XImportDeclaration importDeclaration : importDeclarations) {
				addIssue("The import '" + importDeclaration.importedType.getQualifiedName(".") + "' is never used.",
					importDeclaration, IssueCodes.IMPORT_UNUSED);
			}
		}
	}

}
