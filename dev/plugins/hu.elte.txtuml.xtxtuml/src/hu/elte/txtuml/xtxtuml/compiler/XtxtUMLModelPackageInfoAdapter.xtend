package hu.elte.txtuml.xtxtuml.compiler;

import org.eclipse.emf.common.notify.impl.AdapterImpl

/**
 * When this adapter is present on a class, it represents a package-info.
 * The model name will be used by the code generator to emit the corresponding
 * {@link hu.elte.txtuml.api.model.Model Model} annotation on the package.
 */
class XtxtUMLModelPackageInfoAdapter extends AdapterImpl {

	val String modelName;

	def getModelName() {
		modelName
	}

	new(String modelName) {
		this.modelName = modelName
	}

	override isAdapterForType(Object type) {
		XtxtUMLModelPackageInfoAdapter == type
	}

}
