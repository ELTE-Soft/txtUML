package hu.elte.txtuml.xtxtuml.ui.validation;

import com.google.common.collect.ImmutableSet
import com.google.inject.Inject
import hu.elte.txtuml.utils.Logger
import hu.elte.txtuml.validation.JtxtUMLCompilationParticipant
import hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes
import java.util.Set
import org.eclipse.core.resources.IFile
import org.eclipse.core.resources.IMarker
import org.eclipse.core.resources.IResource
import org.eclipse.core.resources.IWorkspace
import org.eclipse.core.runtime.CoreException
import org.eclipse.emf.common.util.URI
import org.eclipse.emf.ecore.resource.impl.ResourceImpl
import org.eclipse.ui.texteditor.MarkerUtilities
import org.eclipse.xtext.builder.smap.DerivedResourceMarkerCopier
import org.eclipse.xtext.generator.trace.ILocationInResource
import org.eclipse.xtext.generator.trace.ITrace
import org.eclipse.xtext.resource.IResourceServiceProvider
import org.eclipse.xtext.ui.MarkerTypes
import org.eclipse.xtext.ui.validation.MarkerTypeProvider
import org.eclipse.xtext.util.TextRegion
import org.eclipse.xtext.validation.CheckType
import org.eclipse.xtext.validation.Issue
import org.eclipse.xtext.validation.IssueSeveritiesProvider

class XtxtUMLDerivedResourceMarkerCopier extends DerivedResourceMarkerCopier {

	public static val PROPAGATED_FROM_FILE = "fromFile"
	val fileExtensions = ImmutableSet.of("txtuml", "xtxtuml")

	@Inject
	private var IResourceServiceProvider.Registry serviceProviderRegistry;

	override void reflectErrorMarkerInSource(IFile javaFile, ITrace traceToSource) throws CoreException {
		val srcFile = findSourceFile(traceToSource, javaFile.getWorkspace());
		if (srcFile == null || !srcFile.exists()) {
			return;
		}

		if (!fileExtensions.contains(srcFile.location.fileExtension)) {
			super.reflectErrorMarkerInSource(javaFile, traceToSource)
			return
		}

		val maxSeverity = getMaxSeverity(srcFile)

		// clean up marker in source file derived from this java file
		cleanUpCreatedMarkers(javaFile, srcFile)

		val problemsInJtxtUML = findJtxtUMLProblemMarker(javaFile, maxSeverity)
		// Any problems found in target, nothing to copy -> return
		if (problemsInJtxtUML.empty) {
			return;
		}

		copyProblemMarkers(javaFile, traceToSource, problemsInJtxtUML, srcFile)
	}

	private def getMaxSeverity(IFile srcFile) {
		val resourceURI = URI.createPlatformResourceURI(srcFile.getFullPath().toString(), true);
		val serviceProvider = serviceProviderRegistry.getResourceServiceProvider(resourceURI);
		if (serviceProvider == null) {
			return Integer.MAX_VALUE;
		}
		val severitiesProvider = serviceProvider.get(IssueSeveritiesProvider);
		val severity = severitiesProvider.getIssueSeverities(new ResourceImpl(resourceURI)).getSeverity(
			XtxtUMLIssueCodes.COPY_JTXTUML_PROBLEMS);
		switch severity {
			case WARNING:
				return IMarker.SEVERITY_WARNING
			case ERROR:
				return IMarker.SEVERITY_ERROR
			case INFO,
			case IGNORE:
				return Integer.MAX_VALUE
		}
		return Integer.MAX_VALUE;
	}

	private def copyProblemMarkers(IFile javaFile, ITrace traceToSource, Set<IMarker> problemsInJava,
		IFile srcFile) throws CoreException {
		var String sourceMarkerType = null;
		for (marker : problemsInJava) {
			val message = marker.getAttribute(IMarker.MESSAGE) as String;
			if (message != null) {
				val charStart = marker.getAttribute(IMarker.CHAR_START, 0);
				val charEnd = marker.getAttribute(IMarker.CHAR_END, 0);
				val severity = MarkerUtilities.getSeverity(marker);

				val associatedLocation = traceToSource.getBestAssociatedLocation(new TextRegion(charStart,
					charEnd - charStart));
				if (associatedLocation != null) {
					if (sourceMarkerType == null) {
						sourceMarkerType = determinateMarkerTypeByURI(associatedLocation.getSrcRelativeResourceURI());
					}
					if (!srcFile.equals(findIFile(associatedLocation, srcFile.getWorkspace()))) {
						Logger.sys.error("File in associated location is not the same as main source file.");
					}
					val xtendMarker = srcFile.createMarker(sourceMarkerType);
					xtendMarker.setAttribute(IMarker.MESSAGE, message);
					xtendMarker.setAttribute(IMarker.SEVERITY, severity);
					val region = associatedLocation.getTextRegion();
					xtendMarker.setAttribute(IMarker.LINE_NUMBER, region.getLineNumber());
					xtendMarker.setAttribute(IMarker.CHAR_START, region.getOffset());
					xtendMarker.setAttribute(IMarker.CHAR_END, region.getOffset() + region.getLength());
					xtendMarker.setAttribute(PROPAGATED_FROM_FILE, javaFile.getFullPath().toString());
				}

				// remove original marker
				marker.delete();
			}
		}
	}

	private def determinateMarkerTypeByURI(URI resourceUri) {
		val serviceProvider = serviceProviderRegistry.getResourceServiceProvider(resourceUri);
		if (serviceProvider == null) {
			return null;
		}
		val typeProvider = serviceProvider.get(MarkerTypeProvider);
		val issue = new Issue.IssueImpl();
		issue.setType(CheckType.NORMAL);
		return typeProvider.getMarkerType(issue);
	}

	private def findJtxtUMLProblemMarker(IFile javaFile, int maxSeverity) throws CoreException {
		var problems = newHashSet()
		for (marker : javaFile.findMarkers(JtxtUMLCompilationParticipant.JTXTUML_MARKER_TYPE, true,
			IResource.DEPTH_ZERO)) {
			if (MarkerUtilities.getSeverity(marker) >= maxSeverity) {
				problems.add(marker)
			}
		}
		return problems
	}

	private def cleanUpCreatedMarkers(IFile javaFile, IResource srcFile) throws CoreException {
		for (iMarker : srcFile.findMarkers(MarkerTypes.ANY_VALIDATION, true, IResource.DEPTH_ZERO)) {
			if (javaFile.getFullPath().toString().equals(iMarker.getAttribute(PROPAGATED_FROM_FILE, ""))) {
				iMarker.delete()
			}
		}
	}

	private def IFile findSourceFile(ITrace traceToSource, IWorkspace workspace) {
		val iterator = traceToSource.getAllAssociatedLocations().iterator()
		if (iterator.hasNext()) {
			val srcLocation = iterator.next()
			return findIFile(srcLocation, workspace)
		}
		return null
	}

	private def findIFile(ILocationInResource locationInResource, IWorkspace workspace) {
		val storage = locationInResource.getStorage()
		if (storage == null) {
			Logger.sys.warn("Failed to find Storage. Please make sure there are no outdated generated files. URI: " +
				locationInResource.getAbsoluteResourceURI())
			return null;
		}
		return workspace.getRoot().getFile(storage.getFullPath())
	}

}
