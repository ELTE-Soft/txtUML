package hu.elte.txtuml.stdlib;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.ExternalClass;
import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.ModelExecutor;
import hu.elte.txtuml.api.ModelInt;
import hu.elte.txtuml.api.Signal;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * An external class which enhances the model with the ability of using timed
 * events.
 * <p>
 * By calling the {@link Timer#start(ModelClass, Signal, ModelInt) start}
 * method, a new delayed send operation can be started, which means that a
 * signal will be asynchronously sent to the a target model object after a
 * specified timeout.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public class Timer extends ExternalClass {

	/**
	 * Sole constructor of <code>Timer</code>.
	 */
	private Timer() {
	}

	/**
	 * Starts a new delayed send operation.
	 * <p>
	 * Sends asynchronously a signal to the a target model object after a
	 * specified timeout.
	 * 
	 * @param targetObj
	 *            the target model object of the delayed send
	 * @param signal
	 *            the signal which is to be sent after the delay
	 * @param millisecs
	 *            the time in millisecs to wait before sending the signal
	 * @return a handle object to manage this delayed send before it happens
	 */
	public static Handle start(ModelClass targetObj, Signal signal,
			ModelInt millisecs) {
		ModelExecutor.Settings.getExecutionTimeMultiplier();
		return new Handle(targetObj, signal, millisecs);
	}

	/**
	 * The handle of a certain timed event created by the {@link Timer} class.
	 * Until the timed event happens, this handle can be used to manage the
	 * event.
	 * <p>
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public static class Handle extends ExternalClass {

		/**
		 * The scheduler used by this class to schedule timed events.
		 */
		private static final ScheduledExecutorService scheduler = Executors
				.newSingleThreadScheduledExecutor();

		/**
		 * The handle of the event scheduled with the {@link Handle#scheduler scheduler}.
		 */
		private ScheduledFuture<?> handle;
		
		/**
		 * The signal to send after the timeout.
		 */
		private final Signal signal;

		/**
		 * The target of the delayed send.
		 */
		private final ModelClass targetObj;

		/**
		 * An action of the send operation to be performed after the timeout.
		 */
		private final Runnable action;

		/**
		 * Sole constructor of <code>Handle</code>.
		 * 
		 * @param obj
		 *            the target of the delayed send
		 * @param s
		 *            the signal to send after the timeout
		 * @param millisecs
		 *            millisecs to wait before the timeout
		 */
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

		/**
		 * @return the remaining delay in millisecs; zero or negative values indicate that the delay
		 *         has already elapsed
		 */
		private long queryLong() {
			return handle.getDelay(TimeUnit.MILLISECONDS)
					* ModelExecutor.Settings.getExecutionTimeMultiplier();
		}

		/**
		 * @return a new <code>ModelInt</code> representing the remaining delay
		 *         in millisecs; zero or negative values indicate that the delay
		 *         has already elapsed
		 */
		public ModelInt query() {
			return new ModelInt(queryLong());
		}

		/**
		 * The timed event this handle manages is rescheduled to happen after
		 * the specified time from now. If it has already happened, it will be
		 * schdeduled for a second time.
		 * 
		 * @param millisecs
		 *            new delay in millisecs
		 * @throws NullPointerException
		 *             if <code>millisecs</code> is <code>null</code>
		 */
		public synchronized void reset(ModelInt millisecs) {
			handle.cancel(false);
			schedule(millisecs);
		}

		/**
		 * The timed event this handle manages is rescheduled to have a delay
		 * increased by the specified amount of time. If it has already
		 * happened, it will be schdeduled for a second time.
		 * 
		 * @param millisecs
		 *            the amount of time to add in millisecs
		 * @throws NullPointerException
		 *             if <code>millisecs</code> is <code>null</code>
		 */
		public synchronized void add(ModelInt millisecs) {
			long delay = queryLong();
			if (delay < 0) {
				delay = 0;
			}
			reset(new ModelInt(delay).add(millisecs));
		}

		/**
		 * Cancel the timed event managed by this handle object.
		 * 
		 * @return a new <code>ModelBool</code> representing <code>true</code>
		 *         if the cancel was successful so the timed event managed by
		 *         this handle was <i>not</i> yet cancelled; a new
		 *         <code>ModelBool</code> representing <code>false</code>
		 *         otherwise
		 */
		public ModelBool cancel() {
			boolean wasCancelled = handle.isCancelled();
			handle.cancel(false);
			return new ModelBool(!wasCancelled);
		}

		/**
		 * Schedule the timed event this handle manages with the specified
		 * <code>millisecs</code> delay.
		 * 
		 * @param millisecs
		 *            the delay in millisecs
		 */
		private void schedule(ModelInt millisecs) {
			handle = scheduler.schedule(action, ((long) convert(millisecs))
					/ ModelExecutor.Settings.getExecutionTimeMultiplier(),
					TimeUnit.MILLISECONDS);
		}
	}
}
