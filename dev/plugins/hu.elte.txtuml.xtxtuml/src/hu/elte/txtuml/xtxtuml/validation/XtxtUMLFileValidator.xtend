package hu.elte.txtuml.xtxtuml.validation;

import com.google.inject.Inject
import hu.elte.txtuml.utils.Logger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelDeclaration
import java.util.List
import java.util.Map
import org.eclipse.core.resources.IFile
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.JavaModelException
import org.eclipse.xtext.ui.resource.IStorage2UriMapper
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.validation.IssueCodes
import org.eclipse.xtext.xtype.XImportDeclaration

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

class XtxtUMLFileValidator extends XtxtUMLExpressionValidator {

	@Inject extension IStorage2UriMapper;

	@Check
	def checkModelDeclarationIsInModelInfoFile(TUModelDeclaration modelDeclaration) {
		var name = modelDeclaration.eResource?.URI?.lastSegment ?: "";
		if (name != "model-info.xtxtuml") {
			error('Model declaration must be specified in "model-info.xtxtuml".', modelDeclaration,
				TU_MODEL_DECLARATION__MODEL, MISPLACED_MODEL_DECLARATION);
		}
	}

	@Check
	def checkExpectedPackageName(TUFile file) {
		if (file instanceof TUModelDeclaration) {
			return;
		}

		val declaredPackage = file.name;
		val expectedPackage = file.expectedPackageName;

		if (expectedPackage != null &&
			!(expectedPackage.isEmpty && declaredPackage == null || expectedPackage == declaredPackage)) {
			error(
				"The declared package '" + packageNameAsString(declaredPackage) +
					"' does not match the expected package '" + packageNameAsString(expectedPackage) + "'", file,
				TU_FILE__NAME, WRONG_PACKAGE);
		}
	}

	def protected getExpectedPackageName(TUFile file) {
		val fileURI = file.eResource.URI;

		for (storage : fileURI.storages) {
			if (storage.first instanceof IFile) {
				val javaProject = JavaCore.create(storage.second);
				if (javaProject != null && javaProject.exists && javaProject.open) {
					try {

						for (root : javaProject.packageFragmentRoots) {
							if (!root.archive && !root.external) {
								val resource = root.resource;
								if (resource != null) {
									val fileWorkspacePath = storage.first.fullPath;
									val sourceFolderPath = resource.fullPath;

									if (sourceFolderPath.isPrefixOf(fileWorkspacePath)) {
										val claspathRelativePath = fileWorkspacePath.makeRelativeTo(sourceFolderPath);
										return claspathRelativePath.removeLastSegments(1).toString.replace("/", ".");
									}
								}
							}
						}

					} catch (JavaModelException e) {
						Logger.sys.error("Error while resolving expected path for TUFile", e);
					}
				}
			}
		}

		return null;
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
