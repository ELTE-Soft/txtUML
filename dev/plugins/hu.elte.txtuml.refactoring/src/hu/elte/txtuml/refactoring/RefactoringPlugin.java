package hu.elte.txtuml.refactoring;

import org.osgi.framework.BundleContext;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.ui.plugin.AbstractUIPlugin;

public class RefactoringPlugin extends AbstractUIPlugin {

	private static RefactoringPlugin plugin;

	public RefactoringPlugin() {
		plugin = this;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static RefactoringPlugin getDefault() {
		return plugin;
	}

	public static void log(Throwable throwable) {
		getDefault().getLog().log(new Status(IStatus.ERROR, "org.eclipse.refactoring", 0, throwable.getMessage(), throwable));
	}
}