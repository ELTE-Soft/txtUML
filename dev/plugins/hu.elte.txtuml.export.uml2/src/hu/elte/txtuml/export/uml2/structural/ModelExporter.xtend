package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.ExportMode
import hu.elte.txtuml.export.uml2.utils.ResourceSetFactory
import hu.elte.txtuml.utils.eclipse.PackageUtils
import java.io.IOException
import java.util.List
import java.util.Map
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.util.EcoreUtil
import org.eclipse.jdt.core.IJavaProject
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.uml2.uml.Model
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.Profile
import org.eclipse.uml2.uml.UMLPackage
import org.eclipse.uml2.uml.resource.UMLResource

class ModelExporter extends AbstractPackageExporter<List<IPackageFragment>, Model> {

	static final val STDLIB_URI = "pathmap://TXTUML_STDLIB/stdlib.uml";
	static final val STDPROF_URI = "pathmap://UML_PROFILES/Standard.profile.uml";
	static final val STD_PROF_NAME = "StandardProfile";

	static final val Map<String, String> NAME_MAP = #{String.canonicalName -> "String",
		Boolean.canonicalName -> "Boolean", boolean.canonicalName -> "Boolean", int.canonicalName -> "Integer",
		Integer.canonicalName -> "Integer", short.canonicalName -> "Integer", Short.canonicalName -> "Integer",
		byte.canonicalName -> "Integer", Byte.canonicalName -> "Integer", long.canonicalName -> "Integer",
		Long.canonicalName -> "Integer", double.canonicalName -> "Real", Double.canonicalName -> "Real",
		float.canonicalName -> "Real", Float.canonicalName -> "Real"}

	new(ExportMode mode) {
		super(mode)
	}

	def export(List<IPackageFragment> pf) {
		cache.export(this, pf, pf, [])
	}

	override create(List<IPackageFragment> fragments) {
		if(getModelName(fragments) != null) factory.createModel
	}

	def getModelName(List<IPackageFragment> rootFragments) {
		for (IPackageFragment pf : rootFragments) {
			try {
				val unit = pf.getCompilationUnit(PackageUtils.PACKAGE_INFO).parseCompUnit
				val modelName = unit.findModelName
				if (modelName != null)
					return modelName
			} catch (IOException e) {
			}
		}
	}

	override getImportedElement(String name) {
		result.getImportedMember(NAME_MAP.get(name) ?: name) ?: result.importedPackages.findFirst[it.name == name]
	}

	override exportContents(List<IPackageFragment> rootFragments) {
		val firstFragment = rootFragments.get(0)
		setupResourceSet(firstFragment.javaProject, firstFragment.elementName)
		result.name = getModelName(rootFragments)
		addPackageImport(STDLIB_URI)
		addPackageImport(STDPROF_URI)
		addProfileApplication(getImportedElement(STD_PROF_NAME) as Profile)
		for (IPackageFragment packageFragment : rootFragments) {
			super.exportPackageFragment(packageFragment)
		}
	}

	def setupResourceSet(IJavaProject project, String packageName) {
		val uri = URI.createFileURI(project.getProject().getLocation().toOSString()).appendSegment("gen").
			appendSegment(packageName).appendFileExtension(UMLResource.FILE_EXTENSION);
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

	def addPackageImport(String uri) {
		// Load standard library
		val resourceSet = result.eResource.resourceSet
		val resource = resourceSet.getResource(URI.createURI(uri), true)
		val lib = EcoreUtil.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE) as Package

		// Import standard library into the generated model
		val packageImport = factory.createPackageImport
		packageImport.importedPackage = lib
		result.packageImports += packageImport
	}

	def addProfileApplication(Profile profile) {
		result.applyProfile(profile)
	}

}
