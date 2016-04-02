package hu.elte.txtuml.xtxtuml.common

import com.google.inject.Inject
import org.eclipse.xtext.resource.IEObjectDescription
import org.eclipse.xtext.resource.IResourceDescriptions

public class XtxtUMLUtils {

	@Inject IResourceDescriptions index;

	def getEContainerDescription(IEObjectDescription objectDescription) {
		val objectUriFragment = objectDescription.EObjectURI.fragment;
		val containerUriFragment = objectUriFragment.substring(0, objectUriFragment.lastIndexOf('/'));
		val resourceDescription = index.getResourceDescription(objectDescription.EObjectURI.trimFragment);

		return resourceDescription.exportedObjects.findFirst [
			EObjectURI.fragment == containerUriFragment
		];
	}

}
