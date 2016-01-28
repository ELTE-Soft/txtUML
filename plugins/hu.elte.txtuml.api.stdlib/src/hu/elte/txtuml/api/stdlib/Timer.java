package hu.elte.txtuml.api.stdlib;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelExecutor;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.external.ExternalClass;
import hu.elte.txtuml.api.model.external.ExternalType;

/**
 * An external class which enhances the txtUML models with the ability of using
 * timed events.
 * <p>
 * By calling the {@link Timer#start(ModelClass, Signal, int) start} method, a
 * new delayed send operation can be started, which means that a signal will be
 * asynchronously sent to the a target model object after a specified timeout.
 * <p>
 * When using timers, calling {@link Timer#shutdown} to shut down the model
 * executor is recommended instead of {@link ModelExecutor#shutdown} or
 * {@link ModelExecutor#shutdownNow} as those two methods do not take timed
 * events into consideration.
 * <p>
 * <b>Warning:</b> starting timers after the model executor is shut down will
 * result in errors.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.Model} for an overview on
 * modeling in JtxtUML.
 */
public class Timer implements ExternalClass {

	/**
	 * Indicates whether the shutdown process was initiated.
	 */
	private static volatile boolean shutdownInitiated = false;

	/**
	 * The count of currently scheduled events.
	 */
	private static final AtomicInteger scheduledEvents = new AtomicInteger();

	/**
	 * The scheduler used by this class to schedule timed events.
	 */
	private static final ScheduledExecutorService scheduler = Executors
			.newSingleThreadScheduledExecutor();

	/**
	 * Makes sure that the scheduler used to schedule timed events is shut down
	 * along with the model executor.
	 */
	static {
		ModelExecutor.addToShutdownQueue(() -> scheduler.shutdownNow());
	}

	/**
	 * Sole constructor of <code>Timer</code>.
	 */
	private Timer() {
	}

	/**
	 * Starts a new delayed send operation. Sends asynchronously a signal to the
	 * a target model object after a specified timeout.
	 * <p>
	 * Calls the
	 * {@link hu.elte.txtuml.api.ModelExecutor.Settings#lockExecutionTimeMultiplier()
	 * ModelExecutor.Settings.lockExecutionTimeMultiplier} method in the txtUML
	 * API to prevent any errors caused by modifying the execution time
	 * multiplier after the first time-related event happened in the model.
	 * <p>
	 * <b>Warning:</b> starting timers after the model executor is shut down
	 * will result in errors.
	 * 
	 * @param targetObj
	 *            the target model object of the delayed send operation
	 * @param signal
	 *            the signal which is to be sent after the delay
	 * @param millisecs
	 *            the time in millisecs to wait before sending the signal
	 * @return a handle object to manage this delayed send operation before it
	 *         happens
	 */
	public static Handle start(ModelClass targetObj, Signal signal,
			int millisecs) {
		ModelExecutor.Settings.lockExecutionTimeMultiplier();
		return new HandleImpl(targetObj, signal, millisecs);
	}

	/**
	 * Initiates a shutdown process of the model executor which calls
	 * {@link ModelExecutor#shutdown()} the moment when no actions are currently
	 * scheduled. This means that all currently scheduled actions will be
	 * performed, and also every other one that is scheduled while waiting for
	 * the delay of other (previously scheduled) actions to end.
	 * <p>
	 * Calling this method is recommended when using timers instead of methods
	 * {@link ModelExecutor#shutdown} or {@link ModelExecutor#shutdownNow}.
	 */
	public static void shutdown() {
		shutdownInitiated = true;
		if (scheduledEvents.get() == 0) {
			ModelExecutor.shutdown();
		}
	}
	
	public interface Handle extends ExternalType {

		/**
		 * @return the remaining delay in millisecs; zero or negative values
		 *         indicate that the delay has already elapsed
		 */
		long query();

		/**
		 * Reschedules the timed event this handle manages to happen after the
		 * specified time from now. If it has already happened, it will be
		 * scheduled for a second time.
		 * 
		 * @param millisecs
		 *            new delay in millisecs
		 * @throws NullPointerException
		 *             if <code>millisecs</code> is <code>null</code>
		 */
		void reset(long millisecs);

		/**
		 * Reschedules the timed event this handle manages to have a delay
		 * increased by the specified amount of time. If it has already
		 * happened, it will be scheduled for a second time.
		 * 
		 * @param millisecs
		 *            the amount of time to add in millisecs
		 * @throws NullPointerException
		 *             if <code>millisecs</code> is <code>null</code>
		 */
		void add(int millisecs);

		/**
		 * Cancels the timed event managed by this handle object.
		 * 
		 * @return <code>true</code> if the cancel was successful, so the timed
		 *         event managed by this handle was <i>not</i> yet cancelled or
		 *         performed; <code>false</code> otherwise
		 */
		boolean cancel();

	}


	private static class HandleImpl implements Handle, ExternalClass {

		/**
		 * The handle of the event scheduled with {@link Timer#scheduler
		 * scheduler}.
		 */
		private ScheduledFuture<?> handle;

		/**
		 * An action of the send operation to be performed after the timeout.
		 */
		private final Runnable action;

		/**
		 * Sole constructor of <code>Handle</code>.
		 * 
		 * @param obj
		 *            the target of the delayed send operation
		 * @param s
		 *            the signal to send after the timeout
		 * @param millisecs
		 *            millisecs to wait before the timeout
		 */
		HandleImpl(ModelClass targetObj, Signal signal, int millisecs) {
			this.action = () -> {
				Action.send(targetObj, signal);
				int currentCount = scheduledEvents.decrementAndGet();
				if (currentCount == 0 && shutdownInitiated) {
					ModelExecutor.shutdown();
				}
			};
			scheduledEvents.incrementAndGet();
			schedule(millisecs);
		}

		public long query() {
			return ModelExecutor.Settings.inExecutionTime(handle
					.getDelay(TimeUnit.MILLISECONDS));
		}

		public void reset(long millisecs) {
			boolean wasCancelled = handle.cancel(false);
			if (wasCancelled) {
				scheduledEvents.incrementAndGet();
			}
			schedule(millisecs);
		}

		public void add(int millisecs) {
			long delay = query();
			if (delay < 0) {
				delay = 0;
			}
			reset(delay + millisecs);
		}

		public boolean cancel() {
			boolean cancelledNow = handle.cancel(false);
			scheduledEvents.decrementAndGet();
			return cancelledNow;
		}

		private void schedule(long millisecs) {
			if (scheduler.isShutdown()) {
				return;
			}
			handle = scheduler.schedule(action,
					ModelExecutor.Settings.inExecutionTime(millisecs),
					TimeUnit.MILLISECONDS);
		}
	}
}
