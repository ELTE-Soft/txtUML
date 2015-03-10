package txtuml.external;

import java.util.concurrent.*;

import txtuml.api.*;

public class Timer extends ExternalClass {
	protected Timer() {
	}

	public static Handle start(ModelClass targetObj, Signal signal,
			ModelInt millisecs) {

		return new Handle(targetObj, signal, millisecs, null);
	}
	
	public static Handle startOn(ModelClass targetObj, Signal signal,
			ModelInt millisecs, ModelExecutor<?> executor) {
		
		return new Handle(targetObj, signal, millisecs, executor);
	}	

	public static class Handle extends ExternalClass {
		private final Signal signal;
		private final ModelClass targetObj;
		private final Runnable action;
		private final ModelExecutor<?> executor;
		private ScheduledFuture<?> handle;
		private final static ScheduledExecutorService scheduler = Executors
				.newSingleThreadScheduledExecutor();

		Handle(ModelClass obj, Signal s, ModelInt millisecs, ModelExecutor<?> executor) {
			this.signal = s;
			this.targetObj = obj;
			this.action = new Runnable() {
				public void run() {
					Action.send(targetObj, signal);
				}
			};
			
			if (executor == null) {
				this.executor = ModelExecutor.getExecutorStatic();
			} else {
				this.executor = executor;	
			}
			this.executor.lockExecutionTimeMultiplier();
			
			schedule(millisecs);
		}

		private long queryLong() {
			return handle.getDelay(TimeUnit.MILLISECONDS)
					* executor.getExecutionTimeMultiplier();
		}

		public ModelInt query() {
			return new ModelInt(queryLong());
		}

		public synchronized void reset(ModelInt millisecs) {
			handle.cancel(false);
			schedule(millisecs);
		}

		public synchronized void add(ModelInt millisecs) {
			long delay = queryLong();
			if (delay < 0) {
				delay = 0;
			}
			reset(new ModelInt(delay).add(millisecs));
		}

		public boolean cancel() {
			boolean wasCancelled = handle.isCancelled();
			handle.cancel(false);
			return !wasCancelled;
		}

		private void schedule(ModelInt millisecs) {
			handle = scheduler.schedule(action, ((long) convert(millisecs))
					/ executor.getExecutionTimeMultiplier(),
					TimeUnit.MILLISECONDS);
		}
	}
}