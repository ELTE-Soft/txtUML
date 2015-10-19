package hu.elte.txtuml.diagnostics.session;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;

/**
 * Tracks the launched VM run and shuts down DiagnosticsPlugin in sync with it
 * @author gerazo
 */
public class RuntimeSessionTracker implements IDebugEventSetListener {

	private ILaunch trackedLaunch;
	private int liveProcessCount = 0;
	private IDisposable managedPlugin;

	public RuntimeSessionTracker(ILaunch trackedLaunch, IDisposable managedPlugin) {
		this.trackedLaunch = trackedLaunch;
		this.managedPlugin = managedPlugin;
		if (trackedLaunch != null) { // use without a real launch for testing purposes
			DebugPlugin.getDefault().addDebugEventListener(this);
		}
	}
	
	private void dispose() {
		if (trackedLaunch != null) {
			DebugPlugin.getDefault().removeDebugEventListener(this);
		}
		managedPlugin.dispose();
		managedPlugin = null;
		trackedLaunch = null;
	}
	
	@Override
	public void handleDebugEvents(DebugEvent[] events) {
		for (DebugEvent debugEvent : events) {
			if (debugEvent.getSource() instanceof IProcess &&
					((IProcess)debugEvent.getSource()).getLaunch() == trackedLaunch) {
				if (debugEvent.getKind() == DebugEvent.CREATE) {
					liveProcessCount++;
					assert liveProcessCount > 0;
				}
				else if (debugEvent.getKind() == DebugEvent.TERMINATE) {
					liveProcessCount--;
					assert liveProcessCount >= 0;
					if (liveProcessCount == 0) {
						dispose();
					}
				}
			}
		}
	}

}
