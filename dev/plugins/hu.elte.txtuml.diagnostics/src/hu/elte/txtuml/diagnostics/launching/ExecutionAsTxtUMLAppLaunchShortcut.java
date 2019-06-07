package hu.elte.txtuml.diagnostics.launching;

import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaApplicationLaunchShortcut;

public class ExecutionAsTxtUMLAppLaunchShortcut extends JavaApplicationLaunchShortcut {
	@Override
	protected ILaunchConfigurationType getConfigurationType() {
		return DebugPlugin.getDefault().getLaunchManager()
				.getLaunchConfigurationType(ILaunchConstants.ID_TXTUML_APPLICATION);
	}
}
