package txtuml.external;

import java.util.concurrent.*;

import txtuml.api.*;
import txtuml.api.Runtime;

public class Timer extends ExternalClass {
	protected Timer() {}

	public static Handle start(ModelClass targetObj, Signal signal, long millisecs) {
		Runtime.Settings.lockSimulationTimeMultiplier();
		return new Handle(targetObj, signal, millisecs);		
	}
	
	public static class Handle extends ExternalClass {
		private final Signal signal;
		private final ModelClass targetObj;
		private final Runnable action;
		private ScheduledFuture<?> handle;
		private final static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

		Handle(ModelClass obj, Signal s, long millisecs) {
			this.signal = s;
			this.targetObj = obj;
			this.action = new Runnable() {
				public void run() {
					Action.send(targetObj, signal);
				}
			};
	        schedule(millisecs);
		}
				
		public long query() {
			return handle.getDelay(TimeUnit.MILLISECONDS) * Runtime.Settings.getSimulationTimeMultiplier();
		}
		
		public synchronized void reset(long millisecs) {
			handle.cancel(false);
			schedule(millisecs);
		}
		
		public synchronized void add(long millisecs) {
			long delay = query();
			if (delay < 0) {
				delay = 0;
			}
			reset(delay + millisecs);
		}
		
		public boolean cancel() {
			boolean wasCancelled = handle.isCancelled();
			handle.cancel(false);
			return !wasCancelled;
		}
		
		private void schedule(long millisecs) {
			handle = scheduler.schedule(action, millisecs / Runtime.Settings.getSimulationTimeMultiplier(), TimeUnit.MILLISECONDS);
		}
	}
}