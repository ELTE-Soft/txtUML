package hu.elte.txtuml.xtxtuml.naming

import com.google.inject.Inject
import org.eclipse.core.resources.IFile
import org.eclipse.emf.ecore.EObject
import org.eclipse.jdt.core.JavaCore
import org.eclipse.jdt.core.JavaModelException
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.ui.resource.IStorage2UriMapper

class XtxtUMLPackageNameCalculator implements IPackageNameCalculator {

	@Inject
	private IStorage2UriMapper storage2UriMapper;

	override getExpectedPackageName(EObject astNode) {
		val expected = astNode.expectedPackageNameString
		if (null != expected) {
			return QualifiedName.create(expected.split("\\."))
		}
		return null
	}

	/**
	 * Copied from org.eclipse.xtend.ide.validator.XtendUIValidator
	 */
	private def String getExpectedPackageNameString(EObject astNode) {
		val fileURI = astNode.eResource().getURI();
		for (storage : storage2UriMapper.getStorages(fileURI)) {
			if (storage.getFirst() instanceof IFile) {
				val fileWorkspacePath = storage.getFirst().getFullPath();
				val javaProject = JavaCore.create(storage.getSecond());
				if (javaProject != null && javaProject.exists() && javaProject.isOpen()) {
					try {
						for (root : javaProject.getPackageFragmentRoots()) {
							if (!root.isArchive() && !root.isExternal()) {
								val resource = root.getResource();
								if (resource != null) {
									val sourceFolderPath = resource.getFullPath();
									if (sourceFolderPath.isPrefixOf(fileWorkspacePath)) {
										val claspathRelativePath = fileWorkspacePath.makeRelativeTo(sourceFolderPath);
										return claspathRelativePath.removeLastSegments(1).toString().replace("/", ".");
									}
								}
							}
						}
					} catch (JavaModelException e) {
						return null;
					}
				}
			}
		}
		return null;
	}
}
