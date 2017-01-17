package hu.elte.txtuml.xtxtuml.ui.launching;

import com.google.inject.Inject
import com.google.inject.Provider
import org.eclipse.core.resources.IResource
import org.eclipse.ui.IEditorPart
import org.eclipse.ui.IFileEditorInput
import org.eclipse.xtext.xbase.ui.launching.JavaElementDelegate
import org.eclipse.xtext.xbase.ui.launching.JavaElementDelegateAdapterFactory

class XtxtUMLJavaElementDelegateAdapterFactory extends JavaElementDelegateAdapterFactory {

	@Inject Provider<XtxtUMLJavaElementDelegateMainLaunch> mainDelegateProvider;

	/**
	 * Provides only main launchers.
	 */
	override getAdapter(Object adaptableObject, Class adapterType /* warning is unavoidable */ ) {
		if (adaptableObject instanceof JavaElementDelegate) {
			return adaptableObject.getAdapter(adapterType);
		}

		var JavaElementDelegate result = null;
		if (adapterType == XtxtUMLJavaElementDelegateMainLaunch) {
			result = mainDelegateProvider.get;
		}

		switch (adaptableObject) {
			IFileEditorInput: result?.initializeWith(adaptableObject)
			IResource: result?.initializeWith(adaptableObject)
			IEditorPart: result?.initializeWith(adaptableObject)
		}

		return result;
	}

}
