package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.utils.ResourceSetFactory
import hu.elte.txtuml.utils.eclipse.PackageUtils
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.uml2.uml.Model
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.resource.UMLResource
import java.util.Map

class ModelExporter extends AbstractPackageExporter<Model> {

	static final val STDLIB_URI = "pathmap://TXTUML_STDLIB/stdlib.uml";

	static final val Map<String, String> NAME_MAP = #{String.canonicalName -> "String",
		Integer.canonicalName -> "Integer", int.canonicalName -> "Integer", Boolean.canonicalName -> "Boolean",
		boolean.canonicalName -> "Boolean"}

	def export(IPackageFragment pf) {
		cache.export(this, pf, pf)
	}

	override create(IPackageFragment pf) { factory.createModel }

	override getImportedElement(String name) {
		result.getImportedMember(NAME_MAP.get(name) ?: name)

//		val member = result.getImportedMember(name)
//		member ?: result.importedPackages.map[it.getMember(name)].filterNull.head
	}

	override exportContents(IPackageFragment packageFragment) {
		val unit = packageFragment.getCompilationUnit(PackageUtils.PACKAGE_INFO).parseCompUnit
		setupResourceSet(packageFragment)
		result.name = unit.findModelName
		importStandardLib
		super.exportContents(packageFragment)
	}

	def setupResourceSet(IPackageFragment packageFragment) {
		val uri = URI.createFileURI(packageFragment.getJavaProject().getProject().getLocation().toOSString()).
			appendSegment("gen").appendSegment(packageFragment.elementName).appendFileExtension(
				UMLResource.FILE_EXTENSION);
		val resourceSet = new ResourceSetFactory().createAndInitResourceSet();
		val modelResource = resourceSet.createResource(uri);
		modelResource.contents.add(result)
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
