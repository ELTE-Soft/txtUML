package hu.elte.txtuml.export.uml2.restructured

import hu.elte.txtuml.utils.eclipse.PackageUtils
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Model
import org.eclipse.uml2.uml.Package

class ModelExporter extends AbstractPackageExporter<Model> {
	
	new(Exporter<?,?> parent) {
		super(parent)
	}
	
	override create() { factory.createModel }

	override exportContents(Model model, IPackageFragment packageFragment) {
		val unit = packageFragment.getCompilationUnit(PackageUtils.PACKAGE_INFO).parseCompUnit
		model.name = unit.findModelName
		packageFragment.children.forEach[exportChild]
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
	
	override tryStore(Element contained) {
		switch contained {
			Package: result.nestedPackages.add(contained)
			Class: result.ownedTypes.add(contained)
			default: return false
		}
		return true
	}
}
