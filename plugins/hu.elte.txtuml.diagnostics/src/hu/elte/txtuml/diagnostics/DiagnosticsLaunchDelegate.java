package hu.elte.txtuml.diagnostics;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.eclipse.jdt.launching.SocketUtil;

import hu.elte.txtuml.api.diagnostics.protocol.GlobalSettings;
import hu.elte.txtuml.diagnostics.session.DiagnosticsPlugin;
import hu.elte.txtuml.diagnostics.session.IDisposable;
import hu.elte.txtuml.diagnostics.session.RuntimeSessionTracker;
import hu.elte.txtuml.utils.platform.PluginLogWrapper;

/**
 * Launches txtUML apps with all debugging aids.
 * Makes sure client service knows about the plugin diagnostics port.
 * It should cease to exist after the process was launched.
 * @author gerazo
 */
public class DiagnosticsLaunchDelegate extends JavaLaunchDelegate {
	
	private static final String TXTUML_DIAGNOSTICS_PORT_TOKEN = "-D" + GlobalSettings.TXTUML_DIAGNOSTICS_PORT_KEY + "=";

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		int diagnosticsPort = 0;
		try {
			String vmargs = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "");
			int portIdx = vmargs.lastIndexOf(TXTUML_DIAGNOSTICS_PORT_TOKEN);
			if (portIdx != -1) {
				portIdx += TXTUML_DIAGNOSTICS_PORT_TOKEN.length();
				int endIdx = vmargs.indexOf(" ", portIdx);
				if (endIdx == -1) {
					endIdx = vmargs.length();
				}
				String strPort = vmargs.substring(portIdx, endIdx);
				diagnosticsPort = Integer.decode(strPort).intValue();
			}
		} catch (CoreException ex) {
			PluginLogWrapper.getInstance().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to acquire VM arguments for " + GlobalSettings.TXTUML_DIAGNOSTICS_PORT_KEY));
		} catch (NumberFormatException ex) {
			PluginLogWrapper.getInstance().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "VM argument problem, use " + TXTUML_DIAGNOSTICS_PORT_TOKEN + "<portNumber> as VM argument"));
		}
		
		if (diagnosticsPort == 0) {
			diagnosticsPort = SocketUtil.findFreePort();
			try {
				ILaunchConfigurationWorkingCopy workingCopy = configuration.getWorkingCopy();
				workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
						configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "")
						+ " " + TXTUML_DIAGNOSTICS_PORT_TOKEN + diagnosticsPort);
				configuration = workingCopy;
			} catch (CoreException | IllegalArgumentException | SecurityException ex) {
				PluginLogWrapper.getInstance().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Cannot set VM arguments: " + ex));
				throw ex;
			}
		}
		
		IDisposable diagnosticsPlugin;
		try {
			diagnosticsPlugin = new DiagnosticsPlugin(diagnosticsPort,
					configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""),
					configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, "."));
		} catch (IOException ex) {
			throw new CoreException(new Status(IStatus.ERROR, Activator.PLUGIN_ID, -1, "Launching txtUML DiagnosticsPlugin failed miserably", ex));
		}
		new RuntimeSessionTracker(launch, diagnosticsPlugin);
		super.launch(configuration, mode, launch, monitor);
	}
}
