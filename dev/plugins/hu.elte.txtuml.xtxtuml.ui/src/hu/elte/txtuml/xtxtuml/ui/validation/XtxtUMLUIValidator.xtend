package hu.elte.txtuml.xtxtuml.ui.validation

import com.google.inject.Inject
import hu.elte.txtuml.utils.Logger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import java.util.List
import org.eclipse.core.resources.IFile
import org.eclipse.emf.ecore.EPackage
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.JavaModelException
import org.eclipse.xtext.ui.resource.IStorage2UriMapper
import org.eclipse.xtext.validation.Check
import org.eclipse.xtext.xbase.ui.validation.XbaseUIValidator

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

class XtxtUMLUIValidator extends XbaseUIValidator {

	@Inject extension IStorage2UriMapper;

	/**
	 * Overrides the default implementation to let this class validate XtxtUML elements too.
	 */
	override protected List<EPackage> getEPackages() {
		(super.EPackages + #[XtxtUMLPackage.eINSTANCE]).toList
	}

	@Check
	def checkExpectedPackageName(TUFile file) {
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

	def private getExpectedPackageName(TUFile file) {
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

	def private packageNameAsString(String packageName) {
		if (packageName == null || packageName.empty) {
			"(default package)"
		} else {
			packageName
		}
	}

}
