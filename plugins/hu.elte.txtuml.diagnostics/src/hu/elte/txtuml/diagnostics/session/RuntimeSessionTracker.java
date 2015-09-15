package hu.elte.txtuml.diagnostics.session;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;

/**
 * Tracks the launched VM under running and shuts down diagnostics back-end in sync with it
 * @author gerazo
 */
public class RuntimeSessionTracker implements IDebugEventSetListener {

	private ILaunch trackedLaunch;
	private int liveProcessCount = 0;
	private IBackend managedBackend;

	public RuntimeSessionTracker(ILaunch trackedLaunch, IBackend managedBackend) {
		this.trackedLaunch = trackedLaunch;
		this.managedBackend = managedBackend;
		DebugPlugin.getDefault().addDebugEventListener(this);
	}
	
	void dispose() {
		DebugPlugin.getDefault().removeDebugEventListener(this);
		managedBackend.shutdown();
		managedBackend = null;
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
