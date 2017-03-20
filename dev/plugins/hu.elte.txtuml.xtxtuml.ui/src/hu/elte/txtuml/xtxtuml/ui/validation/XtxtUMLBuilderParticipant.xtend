package hu.elte.txtuml.xtxtuml.ui.validation;

import com.google.inject.Inject
import hu.elte.txtuml.utils.Logger
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import org.eclipse.core.internal.resources.ResourceException
import org.eclipse.core.resources.IMarker
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.IResourceStatus
import org.eclipse.core.runtime.CoreException
import org.eclipse.emf.common.util.URI
import org.eclipse.xtext.builder.BuilderParticipant
import org.eclipse.xtext.resource.ILocationInFileProvider
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.ui.MarkerTypes
import org.eclipse.xtext.ui.resource.IResourceSetProvider

class XtxtUMLBuilderParticipant extends BuilderParticipant {

	@Inject IResourceSetProvider resourceSetProvider;
	@Inject extension ILocationInFileProvider;

	/**
	 * Overrides the default implementation to provide meaningful error messages in case
	 * of a {@link ResourceException} occurs due to case insensitive class file duplication.
	 */
	override protected addMarkerAndLogError(URI uri, Throwable e) {
		for (storage : storage2UriMapper.getStorages(uri)) {
			var IResource resource = null;
			if (storage.first instanceof IResource) {
				resource = storage.first as IResource;
			} else {
				resource = storage.second;
			}
			if (resource != null) {
				try {
					val marker = resource.createMarker(MarkerTypes.NORMAL_VALIDATION);

					// start of modified code

					var startChar = 0;
					var endChar = 1;
					var message = e.message + " - See error log for details";

					if (e instanceof ResourceException &&
						(e as ResourceException).status.code == IResourceStatus.CASE_VARIANT_EXISTS) {
						val xResource = resourceSetProvider.get(resource.project).getResource(uri, true);
						if (xResource instanceof XtextResource) {
							val startPos = e.message.lastIndexOf("/") + 1;
							val endPos = e.message.indexOf(".java");
							val conflictingName = e.message.substring(startPos, endPos);
								// cross-file conflicts occur only on class level, there's no need to handle '$'-s

							val rootElement = xResource.contents.findFirst[it instanceof TUFile] as TUFile;
							val duplicateElement = rootElement?.elements?.findFirst [
								eGet(XtxtUMLPackage.Literals.TU_MODEL_ELEMENT__NAME, true)?.toString?.toLowerCase ==
									conflictingName.toLowerCase;
							]

							if (duplicateElement != null) {
								val nameRegion = duplicateElement.significantTextRegion;
								startChar = nameRegion.offset;
								endChar = startChar + nameRegion.length;

								val name = duplicateElement.eGet(XtxtUMLPackage.Literals.TU_MODEL_ELEMENT__NAME, true)?.toString;
								message = '''Model element «name» is a duplicate of model element «conflictingName» – uniqueness validation in this case is case insensitive''';
							}
						}
					}

					marker.setAttribute(IMarker.CHAR_START, startChar);
					marker.setAttribute(IMarker.CHAR_END, endChar);
					marker.setAttribute(IMarker.MESSAGE, message);
					marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);

					// end of modified code
				} catch (CoreException ce) {
					Logger.sys.error("Could not create marker", ce);
				}
			}
		}
		var Throwable cause = e;
		if (cause instanceof CoreException) {
			cause = cause.cause;
		}
		if (uri == null) {
			Logger.sys.error("Error during compilation.", e);
		} else {
			Logger.sys.error("Error during compilation of '" + uri + "'.", e);
		}
	}

}
