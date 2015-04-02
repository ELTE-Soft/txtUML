package hu.elte.txtuml.stdlib;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.ExternalClass;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelExecutor;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.Signal;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Timer extends ExternalClass {
	protected Timer() {
	}

	public static Handle start(ModelClass targetObj, Signal signal,
			ModelInt millisecs) {
		ModelExecutor.Settings.getExecutionTimeMultiplier();
		return new Handle(targetObj, signal, millisecs);
	}

	public static class Handle extends ExternalClass {
		private final Signal signal;
		private final ModelClass targetObj;
		private final Runnable action;
		private ScheduledFuture<?> handle;
		private final static ScheduledExecutorService scheduler = Executors
				.newSingleThreadScheduledExecutor();

		Handle(ModelClass obj, Signal s, ModelInt millisecs) {
			this.signal = s;
			this.targetObj = obj;
			this.action = new Runnable() {
				@Override
				public void run() {
					Action.send(targetObj, signal);
				}
			};
			schedule(millisecs);
		}

		private long queryLong() {
			return handle.getDelay(TimeUnit.MILLISECONDS)
					* ModelExecutor.Settings.getExecutionTimeMultiplier();
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
					/ ModelExecutor.Settings.getExecutionTimeMultiplier(),
					TimeUnit.MILLISECONDS);
		}
	}
}