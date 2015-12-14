package hu.elte.txtuml.utils.platform;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Access plugin logging through this class!
 * It wraps the real plugin logs normally
 * but mocks one if Eclipse is not present (for headless testing)
 * @author gerazo
 */
public class PluginLogWrapper {
	
	private static PluginLogWrapper instance = null;
	
	private ILog log;
	private Set<ILogListener> logListeners;
	
	public static void pluginStarted(AbstractUIPlugin activator) {
		assert instance == null;
		instance = new PluginLogWrapper(activator);
	}

	public static void pluginStopped() {
		assert instance != null;
		instance = null;
	}
	
	public static boolean isPluginRunning() {
		return instance != null;
	}

	public static PluginLogWrapper getInstance() {
		if (instance == null) {
			instance = new PluginLogWrapper();
		}
		return instance;
	}
	
	public static void logError(String message, Throwable exception) {
		getInstance().log(new Status(Status.ERROR, "hu.elte.txtuml.diagnostics", Status.OK, message, exception));
	}
	
	private PluginLogWrapper(AbstractUIPlugin activator) {
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
