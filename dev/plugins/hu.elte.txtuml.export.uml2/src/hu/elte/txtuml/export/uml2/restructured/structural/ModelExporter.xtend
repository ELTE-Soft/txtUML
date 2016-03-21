package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.utils.eclipse.PackageUtils
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.uml2.uml.Model
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.UMLPackage

class ModelExporter extends AbstractPackageExporter<Model> {

	static final val STDLIB_URI = "pathmap://TXTUML_STDLIB/stdlib.uml";

	def export(IPackageFragment pf) {
		cache.export(this, pf, pf)
	}
	
	override create(IPackageFragment pf) { factory.createModel }
	
	override getImportedElement(String name) {
		result.getImportedMember(name)
	}

	override exportContents(IPackageFragment packageFragment) {
		val unit = packageFragment.getCompilationUnit(PackageUtils.PACKAGE_INFO).parseCompUnit
		result.name = unit.findModelName
		importStandardLib
		exportPackageContents(packageFragment)
	}
	
	def findModelName(CompilationUnit unit) {
		for (annot : unit.package.resolveBinding.annotations) {
			if (annot.annotationType.qualifiedName.equals(hu.elte.txtuml.api.model.Model.canonicalName)) {
				for (valName : annot.allMemberValuePairs) {
					if (valName.key == null || valName.key.equals("value")) {
						return valName.value as String
					}
				}
			}
		}
	}
	
	def importStandardLib() {
		// Load standard library
		val resourceSet = result.eResource.resourceSet
		val resource = resourceSet.getResource(URI.createURI(STDLIB_URI), true)
		val stdLib = EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE) as Package

		// Import standard library into the generated model
		val packageImport = factory.createPackageImport()
		packageImport.importedPackage = stdLib
		result.packageImports.add(packageImport)
	}
	
}
