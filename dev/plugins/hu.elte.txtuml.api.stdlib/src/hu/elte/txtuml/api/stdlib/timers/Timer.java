package hu.elte.txtuml.api.stdlib.timers;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Runtime;
import hu.elte.txtuml.api.model.Signal;

/**
 * An external class which enhances the txtUML models with the ability of using
 * timed events.
 * <p>
 * By calling the {@link #start(ModelClass, Signal, int)} method, a new delayed
 * send operation can be started, which means that a signal will be
 * asynchronously sent to the a target model object after a specified timeout.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public class Timer extends ModelClass {

	@External
	private final Callable<Void> action;

	@External
	private ScheduledFuture<Void> future;

	/**
	 * Starts a new delayed send operation. Sends asynchronously a signal to the
	 * target model object after a specified timeout.
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
	@ExternalBody
	public static Timer start(ModelClass targetObj, Signal signal, int millisecs) {
		return new Timer(targetObj, signal, millisecs);
	}

	/**
	 * @param obj
	 *            the target of the delayed send operation
	 * @param s
	 *            the signal to send after the timeout
	 * @param millisecs
	 *            millisecs to wait before the timeout
	 */
	@External
	private Timer(ModelClass targetObj, Signal signal, int millisecs) {
		this.action = () -> {
			Action.send(signal, targetObj);
			return null;
		};
		this.future = schedule(millisecs);
	}

	/**
	 * @return the remaining delay in millisecs; zero or negative values
	 *         indicate that the delay has already elapsed
	 */
	@ExternalBody
	public int query() {
		return (int) future.getDelay(TimeUnit.MILLISECONDS);
	}

	/**
	 * Reschedules the timed event this handle manages to happen after the
	 * specified time from now. If it has already happened, it will be scheduled
	 * for a second time.
	 * 
	 * @param millisecs
	 *            new delay in millisecs
	 * @throws NullPointerException
	 *             if <code>millisecs</code> is <code>null</code>
	 */
	@ExternalBody
	public void reset(int millisecs) {
		ScheduledFuture<Void> newFuture = schedule(millisecs);
		cancel();
		this.future = newFuture;
	}

	/**
	 * Reschedules the timed event this handle manages to have a delay increased
	 * by the specified amount of time. If it has already happened, it will be
	 * scheduled for a second time.
	 * 
	 * @param millisecs
	 *            the amount of time to add in millisecs
	 * @throws NullPointerException
	 *             if <code>millisecs</code> is <code>null</code>
	 */
	@ExternalBody
	public void add(int millisecs) {
		reset(query() + millisecs);
	}

	/**
	 * Cancels the timed event managed by this handle object.
	 * 
	 * @return <code>true</code> if the cancel was successful, so the timed
	 *         event managed by this handle was <i>not</i> yet cancelled or
	 *         performed; <code>false</code> otherwise
	 */
	@ExternalBody
	public boolean cancel() {
		boolean isDone = future.isDone();
		future.cancel(false);
		return isDone;
	}

	@External
	private ScheduledFuture<Void> schedule(int millisecs) {
		return Runtime.currentRuntime().schedule(action, millisecs, TimeUnit.MILLISECONDS);
	}
}
