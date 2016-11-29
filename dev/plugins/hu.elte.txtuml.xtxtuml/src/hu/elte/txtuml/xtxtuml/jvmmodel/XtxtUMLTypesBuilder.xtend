package hu.elte.txtuml.xtxtuml.jvmmodel;

import hu.elte.txtuml.xtxtuml.compiler.XtxtUMLGenerator
import hu.elte.txtuml.xtxtuml.compiler.XtxtUMLModelPackageInfoAdapter
import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.naming.QualifiedName
import org.eclipse.xtext.xbase.jvmmodel.JvmTypesBuilder

class XtxtUMLTypesBuilder extends JvmTypesBuilder {

	/**
	 * Builds a special class, which represents a package-info.
	 * The code generator will handle these classes differently.
	 * @see XtxtUMLGenerator
	 */
	def JvmGenericType toPackageInfo(EObject sourceElement, QualifiedName packageName, String modelName) {
		val result = createJvmGenericType(sourceElement, packageName + ".package-info")
		if (result == null) {
			return null
		}
		result.eAdapters.add(new XtxtUMLModelPackageInfoAdapter(modelName))
		copyDocumentationTo(sourceElement, result)
		associate(sourceElement, result)
	}

}
