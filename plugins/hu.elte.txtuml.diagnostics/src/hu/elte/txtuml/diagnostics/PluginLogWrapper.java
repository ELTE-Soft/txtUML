package hu.elte.txtuml.diagnostics;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Access plugin logging through this class!
 * It wraps the real plugin logs normally
 * but mocks one if Eclipse is not present (for headless testing)
 */
public class PluginLogWrapper {
	
	private static PluginLogWrapper instance = null;
	
	private ILog log;
	private Set<ILogListener> logListeners;
	
	static void pluginStarted(Activator activator) {
		assert instance == null;
		instance = new PluginLogWrapper(activator);
	}

	static void pluginStopped() {
		assert instance != null;
		instance = null;
	}
	
	static boolean isPluginRunning() {
		return instance != null;
	}

	public static PluginLogWrapper getInstance() {
		if (instance == null) {
			instance = new PluginLogWrapper();
		}
		return instance;
	}
	
	public static void logError(String message, Throwable exception) {
		// TODO: exceptions somehow got lost. inspect
		System.out.println(message);
		exception.printStackTrace();
		getInstance().log(new Status(Status.ERROR, "hu.elte.txtuml.diagnostics", Status.OK, message, exception));
	}
	
	private PluginLogWrapper(Activator activator) {
		log = activator.getLog();
	}
	
	private PluginLogWrapper() {
		log = null;
		logListeners = new HashSet<ILogListener>();
	}

	public void log(IStatus status) {
		if (log != null) {
			log.log(status);
		}
		else {
			for (ILogListener listener : logListeners) {
				listener.logging(status, null);
			}
		}
	}
	
	public void addLogListener(ILogListener listener) {
		if (log != null) {
			log.addLogListener(listener);
		}
		else {
			logListeners.add(listener);
		}
	}

	public void removeLogListener(ILogListener listener) {
		if (log != null) {
			log.removeLogListener(listener);
		}
		else {
			logListeners.remove(listener);
		}
	}
}
