package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.utils.eclipse.PackageUtils
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.CompilationUnit
import org.eclipse.uml2.uml.Model

class ModelExporter extends AbstractPackageExporter<Model> {
	
	override create(IPackageFragment pf) { factory.createModel }

	override exportContents(IPackageFragment packageFragment) {
		val unit = packageFragment.getCompilationUnit(PackageUtils.PACKAGE_INFO).parseCompUnit
		result.name = unit.findModelName
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
	
}
