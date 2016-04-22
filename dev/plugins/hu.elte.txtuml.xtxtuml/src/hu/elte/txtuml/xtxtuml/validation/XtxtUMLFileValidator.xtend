package hu.elte.txtuml.xtxtuml.validation;

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.naming.IPackageNameCalculator
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelDeclaration
import java.util.List
import java.util.Map
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.validation.IssueCodes
import org.eclipse.xtext.xtype.XImportDeclaration

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

class XtxtUMLFileValidator extends XtxtUMLExpressionValidator {

	@Inject extension IPackageNameCalculator;

	@Check
	def checkModelDeclarationIsInModelInfoFile(TUModelDeclaration modelDeclaration) {
		var name = modelDeclaration.eResource?.URI?.lastSegment ?: "";
		if (name != "model-info.xtxtuml") {
			error('Model declaration must be specified in "model-info.xtxtuml"', modelDeclaration,
				TU_MODEL_DECLARATION__MODEL, MISPLACED_MODEL_DECLARATION);
		}
	}

	@Check
	def checkExpectedPackageName(TUFile file) {
		if (file instanceof TUModelDeclaration) {
			return;
		}

		val declaredPackage = file.name;
		val expectedPackage = file.expectedPackageName?.toString;

		if (expectedPackage != null &&
			!(expectedPackage.isEmpty && declaredPackage == null || expectedPackage == declaredPackage)) {
			error(
				"The declared package '" + packageNameAsString(declaredPackage) +
					"' does not match the expected package '" + packageNameAsString(expectedPackage) + "'", file,
				TU_FILE__NAME, WRONG_PACKAGE);
		}
	}

	def protected packageNameAsString(String packageName) {
		if (packageName == null || packageName.empty) {
			"(default package)"
		} else {
			packageName
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
