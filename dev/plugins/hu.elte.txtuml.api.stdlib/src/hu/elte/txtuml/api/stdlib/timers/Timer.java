package hu.elte.txtuml.api.stdlib.timers;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.external.ExternalClass;
import hu.elte.txtuml.api.stdlib.impl.timers.TimerImpl;

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
public interface Timer extends ExternalClass {

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
	 * @return a timer instance to manage this delayed send operation before it
	 *         happens
	 */
	static Timer start(ModelClass targetObj, Signal signal, int millisecs) {
		return new TimerImpl(targetObj, signal, millisecs);
	}

	/**
	 * @return the remaining delay in millisecs; zero or negative values
	 *         indicate that the delay has already elapsed
	 */
	int query();

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
	void reset(int millisecs);

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
