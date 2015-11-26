package hu.elte.txtuml.xtxtuml.compiler

import hu.elte.txtuml.api.model.Model
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.xbase.compiler.GeneratorConfig
import org.eclipse.xtext.xbase.compiler.ImportManager
import org.eclipse.xtext.xbase.compiler.JvmModelGenerator

class XtxtUMLGenerator extends JvmModelGenerator {

	/**
	 * Extends default behavior to implement generation of package-info files.
	 * @see XtxtUMLModelPackageInfoAdapter
	 */
	override CharSequence generateType(JvmDeclaredType type, GeneratorConfig config) {
		val modelAdapter = type.eAdapters.filter(XtxtUMLModelPackageInfoAdapter).head
		if (null == modelAdapter) {
			return super.generateType(type, config)
		}

		val appendable = createAppendable(type, new ImportManager(), config)
		type.generateJavaDoc(appendable, config)
		appendable.append("@Model")
		if (null != modelAdapter.modelName) {
			appendable.append('("').append(modelAdapter.modelName).append('")')
		}
		appendable.newLine
		appendable.append("package ").append(type.packageName).append(";");
		appendable.newLine.newLine
		appendable.append("import ").append(Model.name).append(";").newLine
		return appendable
	}
}
