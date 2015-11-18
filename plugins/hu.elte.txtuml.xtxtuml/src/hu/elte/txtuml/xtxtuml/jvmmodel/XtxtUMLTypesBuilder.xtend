package hu.elte.txtuml.xtxtuml.jvmmodel

import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder
import hu.elte.txtuml.xtxtuml.compiler.XtxtUMLModelPackageInfoAdapter

class XtxtUMLTypesBuilder extends JvmTypesBuilder {

	def JvmGenericType toPackageInfo(EObject sourceElement, QualifiedName packageName, String modelName) {
		val result = createJvmGenericType(sourceElement, packageName + ".package-info");
		if (result == null) {
			return null;
		}
		result.eAdapters.add(new XtxtUMLModelPackageInfoAdapter(modelName))
		super.copyDocumentationTo(sourceElement, result)
		associate(sourceElement, result)
	}
}
