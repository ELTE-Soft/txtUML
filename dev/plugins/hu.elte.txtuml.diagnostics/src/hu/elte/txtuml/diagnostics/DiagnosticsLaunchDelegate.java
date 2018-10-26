package hu.elte.txtuml.diagnostics;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.JavaLaunchDelegate;
import org.eclipse.jdt.launching.SocketUtil;

import hu.elte.txtuml.api.model.execution.diagnostics.protocol.GlobalSettings;
import hu.elte.txtuml.diagnostics.session.DiagnosticsPlugin;
import hu.elte.txtuml.diagnostics.session.IDisposable;
import hu.elte.txtuml.diagnostics.session.RuntimeSessionTracker;
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.Pair;

/**
 * Launches txtUML apps with all debugging aids. Makes sure client service knows
 * about the plugin diagnostics port. It should cease to exist after the process
 * was launched.
 */
public class DiagnosticsLaunchDelegate extends JavaLaunchDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		Pair<Integer, ILaunchConfiguration> httpPortToModifiedConfiguration = getAndSetPort(configuration,
				GlobalSettings.TXTUML_DIAGNOSTICS_HTTP_PORT_KEY);
		int httpPort = httpPortToModifiedConfiguration.getFirst();
		ILaunchConfiguration modifiedConfiguration = httpPortToModifiedConfiguration.getSecond();
		
		Pair<Integer, ILaunchConfiguration> socketPortToModifiedConfiguration = getAndSetPort(modifiedConfiguration,
				GlobalSettings.TXTUML_DIAGNOSTICS_SOCKET_PORT_KEY);
		int socketPort = socketPortToModifiedConfiguration.getFirst();
		modifiedConfiguration = socketPortToModifiedConfiguration.getSecond();

		IDisposable diagnosticsPlugin;
		try {
			diagnosticsPlugin = new DiagnosticsPlugin(socketPort, httpPort,
					modifiedConfiguration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""),
					modifiedConfiguration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, "."));
		} catch (IOException ex) {
			throw new RuntimeException("Launching txtUML DiagnosticsPlugin failed miserably");
		}

		new RuntimeSessionTracker(launch, diagnosticsPlugin);
		super.launch(modifiedConfiguration, mode, launch, monitor);
	}

	private Pair<Integer, ILaunchConfiguration> getAndSetPort(ILaunchConfiguration configuration, String portKey)
			throws CoreException {
		int port = 0;
		String portToken = "-D" + portKey + "=";
		ILaunchConfiguration resultConfiguration = configuration;

		try {
			String vmargs = resultConfiguration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "");
			int portIdx = vmargs.lastIndexOf(portToken);
			if (portIdx != -1) {
				portIdx += portToken.length();
				int endIdx = vmargs.indexOf(" ", portIdx);
				if (endIdx == -1) {
					endIdx = vmargs.length();
				}
				String strPort = vmargs.substring(portIdx, endIdx);
				port = Integer.decode(strPort).intValue();
			}
		} catch (CoreException ex) {
			Logger.sys.error("Failed to acquire VM arguments for " + portKey);
		} catch (NumberFormatException ex) {
			Logger.sys.error("VM argument problem, use " + portToken + "<portNumber> as VM argument");
		}

		if (port == 0) {
			port = SocketUtil.findFreePort();
			try {
				ILaunchConfigurationWorkingCopy workingCopy = resultConfiguration.getWorkingCopy();
				workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
						resultConfiguration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "") + " "
								+ portToken + port);
				resultConfiguration = workingCopy;
			} catch (CoreException | IllegalArgumentException | SecurityException ex) {
				Logger.sys.error("Cannot set VM arguments: " + ex);
				throw ex;
			}
		}

		return new Pair<>(port, resultConfiguration);
	}

}
