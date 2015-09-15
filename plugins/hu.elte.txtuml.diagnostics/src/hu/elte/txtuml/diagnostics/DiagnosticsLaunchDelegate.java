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
import hu.elte.txtuml.diagnostics.session.DiagnosticsBackend;
import hu.elte.txtuml.diagnostics.session.IBackend;
import hu.elte.txtuml.diagnostics.session.RuntimeSessionTracker;

/**
 * Launches txtUML apps with all debugging aids.
 * Makes sure client knows about the backend diagnostics port.
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
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Failed to acquire VM arguments for " + GlobalSettings.TXTUML_DIAGNOSTICS_PORT_KEY));
		} catch (NumberFormatException ex) {
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "VM argument problem, use " + TXTUML_DIAGNOSTICS_PORT_TOKEN + "<portNumber> as VM argument"));
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
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Cannot set VM arguments: " + ex));
				throw ex;
			}
		}
		
		IBackend backend;
		try {
			backend = new DiagnosticsBackend(diagnosticsPort,
					configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, ""),
					configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, "."));
		} catch (IOException ex) {
			throw new CoreException(new BackendErrorStatus(ex));
		}
		new RuntimeSessionTracker(launch, backend);
		super.launch(configuration, mode, launch, monitor);
	}

	private class BackendErrorStatus implements IStatus {		
		
		private final Throwable cause;
		
		public BackendErrorStatus(Throwable cause) {
			this.cause = cause;
		}
		
		@Override
		public IStatus[] getChildren() {
			return new IStatus[0];
		}
		
		@Override
		public int getCode() {
			return -1;
		}
		
		@Override
		public Throwable getException() {
			return cause;
		}
		
		@Override
		public String getMessage() {
			return "Launching txtUML Diagnostics back-end failed miserably";
		}
		
		@Override
		public String getPlugin() {
			return Activator.PLUGIN_ID;
		}

		@Override
		public int getSeverity() {
			return IStatus.ERROR;
		}

		@Override
		public boolean isMultiStatus() {
			return false;
		}

		@Override
		public boolean isOK() {
			return false;
		}

		@Override
		public boolean matches(int severityMask) {
			return (IStatus.ERROR & severityMask) != 0;
		}
	}

}
