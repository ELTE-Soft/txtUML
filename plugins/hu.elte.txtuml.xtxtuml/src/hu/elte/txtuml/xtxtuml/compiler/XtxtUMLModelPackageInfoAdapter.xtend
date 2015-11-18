package hu.elte.txtuml.xtxtuml.compiler

import org.eclipse.emf.common.notify.impl.AdapterImpl

class XtxtUMLModelPackageInfoAdapter extends AdapterImpl {

	val String modelName

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