package hu.elte.txtuml.export.uml2.restructured

import hu.elte.txtuml.utils.eclipse.PackageUtils
import org.eclipse.emf.ecore.EObject
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Model
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.Element

class ModelExporter extends AbstractPackageExporter<Model> {
	
	new(ExporterRegistry registry) {
		super(registry, Model)
	}
	
	override create() { factory.createModel }

	override exportContents(Model model, IPackageFragment packageFragment) {
		val unit = packageFragment.getCompilationUnit(PackageUtils.PACKAGE_INFO).parseCompUnit
		model.name = unit.findModelName
		packageFragment.children.forEach[model.exportChild(it)]
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
	
	override tryStore(Model model, Element contained) { {
		switch contained {
			Package: model.nestedPackages.add(contained)
			Class: model.ownedTypes.add(contained)
			default: return false
		}
		return true
	}
}
