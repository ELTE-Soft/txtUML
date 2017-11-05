package hu.elte.txtuml.api.stdlib.world;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import hu.elte.txtuml.api.model.External;

/**
 * A {@link WorldObjectListener} that uses a {@link LinkedBlockingQueue} to
 * store received signals.
 * <p>
 * This listener does <i>not</i> call any signal handlers, only puts them in its
 * {@link #queue} for later use.
 * <p>
 * From any thread, call the {@link handleNext} method to wait for and handle
 * the next arriving signal. Use the {@link #run()} or {@link #run(Consumer)}
 * method instead to wait for signals in an infinite loop. (The loop can be
 * stopped by calling {@link shutdown}.)
 * <p>
 * Alternatively, access the {@link #queue} and use its methods to take and
 * handle the signals. Call the {@link #invokeHandler} method to search for
 * signal handlers.
 * <p>
 * The queue of this class has the capacity of {@link Integer#MAX_VALUE}.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api.stdlib.world} package
 * or further details and examples about the services provided by the <i>txtUML
 * World</i> model.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@External
public class QueueWorldObjectListener extends WorldObjectListener implements Runnable {

	/**
	 * Stores the received signals.
	 */
	protected final LinkedBlockingQueue<SignalToWorld> queue = new LinkedBlockingQueue<>();

	@Override
	void acceptAny(SignalToWorld signal) {
		queue.add(signal);
	}

	/**
	 * Waits for the next signal to arrive in the queue and then calls the
	 * appropriate signal handler for each. Can be stopped by calling
	 * {@link #shutdown}. Also stops when the current thread is interrupted.
	 * 
	 * @return {@code true} if found a signal and properly handled it;
	 *         {@code false} if {@link #shutdown} has been called and all the
	 *         signals that were in the queue at the time of that call have
	 *         already been handled <b><i>or</i></b> if this thread has been
	 *         interrupted while waiting
	 * 
	 * @see #run
	 */
	public boolean handleNext() {
		return handleNext(this::invokeHandler);
	}

	/**
	 * Waits for the next signal to arrive in the queue and then calls the
	 * specified consumer for it. Can be stopped by calling {@link #shutdown}.
	 * Also stops when the current thread is interrupted.
	 * 
	 * @return {@code true} if found a signal and properly handled it;
	 *         {@code false} if {@link #shutdown} has been called and all the
	 *         signals that were in the queue at the time of that call have
	 *         already been handled <b><i>or</i></b> if this thread has been
	 *         interrupted while waiting

	 * @see #run(Consumer)
	 */
	public boolean handleNext(Consumer<SignalToWorld> signalHandler) {
		try {
			SignalToWorld signal = queue.take();
			if (signal instanceof StopWaitingSignal) {
				return false;
			}
			signalHandler.accept(signal);
			return true;
		} catch (InterruptedException e) {
			return false;
		}
	}

	/**
	 * Starts an infinite loop that waits for signals indefinitely to arrive in
	 * the queue and then calls the appropriate signal handler for each. Can be
	 * stopped by calling {@link shutdown}. Also stops when the current thread
	 * is interrupted.
	 */
	@Override
	public void run() {
		while (handleNext()) {
			// nothing to do
		}
	}

	/**
	 * Starts an infinite loop that waits for signals indefinitely to arrive in
	 * the queue and then calls the specified consumer for each. Can be stopped
	 * by calling {@link shutdown}. Also stops when the current thread is
	 * interrupted.
	 */
	public void run(Consumer<SignalToWorld> signalHandler) {
		while (handleNext(signalHandler)) {
			// nothing to do
		}
	}

	/**
	 * Makes the {@link run()} or {@link #run(Consumer)} method of this object
	 * listener terminate after it has processed all the already received
	 * signals.
	 */
	public void shutdown() {
		queue.add(new StopWaitingSignal());
	}

	/**
	 * See {@link #shutdown} and {@link #run}.
	 */
	private class StopWaitingSignal extends SignalToWorld {
	}

}
