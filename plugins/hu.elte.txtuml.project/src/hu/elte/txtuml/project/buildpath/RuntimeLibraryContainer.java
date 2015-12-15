package hu.elte.txtuml.project.buildpath;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.osgi.framework.Bundle;

import hu.elte.txtuml.project.Messages;
import hu.elte.txtuml.utils.Logger;

/**
 * Contains the model execution runtime library and its dependencies.
 */
public class RuntimeLibraryContainer implements IClasspathContainer {

	private static final String BIN_FOLDER_IN_PLUGIN = "bin"; //$NON-NLS-1$
	private static final String SRC_FOLDER_IN_PLUGIN = "src"; //$NON-NLS-1$

	public static final String[] BUNDLE_IDS_TO_INCLUDE = { "com.google.guava", //$NON-NLS-1$
			"org.eclipse.uml2.uml", "org.eclipse.uml2.uml.resources", //$NON-NLS-1$ //$NON-NLS-2$
			"org.eclipse.xtext.xbase.lib", "hu.elte.txtuml.api.model", //$NON-NLS-1$ //$NON-NLS-2$
			"hu.elte.txtuml.api.layout", "hu.elte.txtuml.api.stdlib", //$NON-NLS-1$ //$NON-NLS-2$
			"hu.elte.txtuml.api.diagnostics", "hu.elte.txtuml.export.papyrus", //$NON-NLS-1$ //$NON-NLS-2$
			"hu.elte.txtuml.export.uml2", "hu.elte.txtuml.layout.export", //$NON-NLS-1$ //$NON-NLS-2$
			"hu.elte.txtuml.layout.visualizer", "hu.elte.txtuml.utils", //$NON-NLS-1$ //$NON-NLS-2$
			"hu.elte.txtuml.xtxtuml.lib", "hu.elte.txtuml.xtxtuml", //$NON-NLS-1$ //$NON-NLS-2$
			"hu.elte.txtuml.xtxtuml.ui" }; //$NON-NLS-1$

	private final IPath containerPath;
	private IClasspathEntry[] classPathEntries;

	public RuntimeLibraryContainer(IPath containerPath) {
		this.containerPath = containerPath;
	}

	@Override
	public IClasspathEntry[] getClasspathEntries() {
		if (classPathEntries == null) {
			List<IClasspathEntry> cpEntries = new ArrayList<IClasspathEntry>();
			for (String bundleId : BUNDLE_IDS_TO_INCLUDE) {
				addEntry(cpEntries, bundleId);
			}
			classPathEntries = cpEntries.toArray(new IClasspathEntry[] {});
		}
		return classPathEntries;
	}

	private void addEntry(final List<IClasspathEntry> cpEntries,
			final String bundleId) {
		Bundle bundle = Platform.getBundle(bundleId);
		if (bundle != null) {
			cpEntries.add(JavaCore
					.newLibraryEntry(bundlePath(bundle), sourcePath(bundle),
							null, new IAccessRule[] {}, null, false));
		}
	}

	private IPath bundlePath(Bundle bundle) {
		IPath path = binFolderPath(bundle);
		if (path == null) {
			// common jar file case, no bin folder
			try {
				path = new Path(FileLocator.getBundleFile(bundle)
						.getAbsolutePath());
			} catch (IOException e) {
				String message = "Can't resolve path '"
						+ bundle.getSymbolicName() + "'"; //$NON-NLS-1$ //$NON-NLS-2$
				Logger.logError(message, e);
			}
		}
		return path;
	}

	private IPath binFolderPath(Bundle bundle) {
		URL binFolderURL = FileLocator.find(bundle, new Path(
				BIN_FOLDER_IN_PLUGIN), null);
		if (binFolderURL != null) {
			try {
				URL binFolderFileURL = FileLocator.toFileURL(binFolderURL);
				return new Path(binFolderFileURL.getPath()).makeAbsolute();
			} catch (IOException e) {
				String message = "Can't resolve path '"
						+ bundle.getSymbolicName() + "'"; //$NON-NLS-1$ //$NON-NLS-2$
				Logger.logError(message, e);
			}
		}
		return null;
	}

	private IPath sourcePath(Bundle bundle) {
		IPath path = srcFolderPath(bundle);
		if (path == null) {
			// common jar file case, no bin folder
			try {
				path = new Path(FileLocator.getBundleFile(bundle)
						.getAbsolutePath());
			} catch (IOException e) {
				String message = "Can't resolve path '"
						+ bundle.getSymbolicName() + "'"; //$NON-NLS-1$ //$NON-NLS-2$
				Logger.logError(message, e);
			}
		}
		return path;
	}

	private IPath srcFolderPath(Bundle bundle) {
		URL binFolderURL = FileLocator.find(bundle, new Path(
				SRC_FOLDER_IN_PLUGIN), null);
		if (binFolderURL != null) {
			try {
				URL binFolderFileURL = FileLocator.toFileURL(binFolderURL);
				return new Path(binFolderFileURL.getPath()).makeAbsolute();
			} catch (IOException e) {
				String message = "Can't resolve path '"
						+ bundle.getSymbolicName() + "'"; //$NON-NLS-1$ //$NON-NLS-2$
				Logger.logError(message, e);
			}
		}
		return null;
	}

	@Override
	public String getDescription() {
		return Messages.RuntimeLibraryContainer_libraryDescription;
	}

	@Override
	public int getKind() {
		return IClasspathContainer.K_APPLICATION;
	}

	@Override
	public IPath getPath() {
		return containerPath;
	}
}
