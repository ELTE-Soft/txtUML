package hu.elte.txtuml.api.model.execution.impl.base;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.impl.ExecutorThread;
import hu.elte.txtuml.utils.Logger;

/**
 * A model executor thread is used by a model executor to run a model or parts
 * of a model (the executor may use multiple threads). Every
 * {@link hu.elte.txtuml.api.model.ModelClass} and
 * {@link hu.elte.txtuml.api.model.ModelClass.Port} instance is owned by one
 * model executor thread which runs and manages that instance.
 * <p>
 * Whenever an asynchronous event is raised (for example, a signal is sent to
 * such an instance), it is reported to the owner thread, which puts that event
 * into its mailbox and later processes it. The processing of one such event is
 * called a model execution step, which is also an iteration of the main loop of
 * a model executor thread. This thread stops if:
 * <ol>
 * <li>Either
 * <ul>
 * <li>the model executor to which this thread belongs has been forced to shut
 * down immediately,</li>
 * <li>or the model executor has no termination blockers (which implies that its
 * graceful shutdown procedure has been started) <b>and</b> this thread's
 * mailbox is empty.</li>
 * </ul>
 * </li>
 * <li>The
 * {@link AbstractModelExecutor#unregisterThread(AbstractModelExecutor.OwnedThread)}
 * method returns true.</li>
 * </ol>
 * Otherwise, the loop tries to continue working, even if it has been
 * interrupted while waiting for new events.
 */
public abstract class AbstractExecutorThread extends AbstractModelExecutor.OwnedThread<AbstractModelExecutor<?>>
		implements ExecutorThread, Runnable {

	private static final AtomicLong count = new AtomicLong();

	private final AbstractModelRuntime<?, ?> runtime;
	private final Runnable initialization;
	private final long identifier;
	private final ConcurrentLinkedQueue<Runnable> delayedActions = new ConcurrentLinkedQueue<>();

	private Signal triggeringSignal;

	public AbstractExecutorThread(AbstractModelExecutor<?> modelExecutor, AbstractModelRuntime<?, ?> runtime,
			Runnable initialization) {
		this(modelExecutor, runtime, initialization, count.getAndIncrement());
	}

	private AbstractExecutorThread(AbstractModelExecutor<?> modelExecutor, AbstractModelRuntime<?, ?> runtime,
			Runnable initialization, long identifier) {
		super("Model_thread-" + identifier, modelExecutor);
		this.runtime = runtime;
		this.initialization = initialization;
		this.identifier = identifier;
	}

	@Override
	public AbstractModelRuntime<?, ?> getModelRuntime() {
		return runtime;
	}

	/**
	 * Returns a unique identifier of this model executor thread.
	 * <p>
	 * Note: This is <i>not</i> the same identifier which is returned by the
	 * {@link Thread#getId} method. The main difference is that this identifier
	 * is not reusable after the deletion of this thread instance.
	 */
	public long getIdentifier() {
		return identifier;
	}

	/**
	 * Gets the current triggering signal.
	 * <p>
	 * As this method is <i>not</i> thread-safe, it may only be called from this
	 * thread.
	 */
	public Signal getCurrentTriggeringSignal() {
		return triggeringSignal;
	}

	/**
	 * Sets the current triggering signal.
	 * <p>
	 * As this method is <i>not</i> thread-safe, it may only be called from this
	 * thread.
	 */
	public void setCurrentTriggeringSignal(Signal triggeringSignal) {
		this.triggeringSignal = triggeringSignal;
	}

	@Override
	public void doRun() {
		initialization.run();
		runLoop();
	}

	/**
	 * Runs the main loop of this thread until {@link #shouldContinue()} returns
	 * false. Called from {@link #doRun()} after running the initialization of
	 * this thread and before unregistering it.
	 * <p>
	 * As this method is <i>not</i> thread-safe, it may only be called from this
	 * thread.
	 */
	public void runLoop() {
		try {
			while (shouldContinue()) {
				try {
					processNext();
					for (Runnable action; (action = delayedActions.poll()) != null;) {
						action.run();
					}
				} catch (Exception e) {
					Logger.sys.error("Exception thrown on a model executor thread (id: " + getIdentifier()
							+ "). Trying to continue by processing the next signal.", e);
				}
			}
		} catch (Error e) {
			Logger.sys.fatal(
					"Error thrown on a model executor thread (id: " + getIdentifier() + "). The thread now stops.", e);
		}
	}

	/**
	 * This method is responsible for deciding whether the main loop of this
	 * thread should continue or not.
	 * <p>
	 * The default implementation depends only on the lifetime of the owner
	 * model executor.
	 * <p>
	 * As this method is <i>not</i> thread-safe, it may only be called from this
	 * thread.
	 * 
	 * @see AbstractModelExecutor#shouldShutDownImmediately()
	 * @see AbstractModelExecutor#shouldShutDownWhenNothingToDo()
	 */
	public boolean shouldContinue() {
		if (shouldShutDownImmediately() || (shouldShutDownWhenNothingToDo() && isEmpty())) {
			return false;
		}
		return true;
	}

	/**
	 * Adds a new delayed action which will be performed (on this thread) when
	 * the current execution step (the processing of a signal) is finished. If
	 * during the execution of the registered delayed actions new ones are
	 * registered, they are also performed.
	 * <p>
	 * Thread-safe.
	 */
	public void addDelayedAction(Runnable action) {
		Objects.requireNonNull(action);
		delayedActions.add(action);
	}

	/**
	 * Takes and processes the next entry in the mailbox, waiting if necessary
	 * until a new entry arrives.
	 * <p>
	 * As this method is <i>not</i> thread-safe, it may only be called from this
	 * thread.
	 */
	protected abstract void processNext() throws InterruptedException;

	/**
	 * Checks whether the mailbox of this thread is empty.
	 * <p>
	 * Thread-safe.
	 */
	public abstract boolean isEmpty();

	/**
	 * Adds a new entry to this thread's mailbox to send the given signal to the
	 * given target object.
	 * <p>
	 * Thread-safe.
	 */
	public abstract void receiveLater(SignalWrapper signal, AbstractPortRuntime target);

	/**
	 * Adds a new entry to this thread's mailbox to send the given signal to the
	 * given target object.
	 * <p>
	 * Thread-safe.
	 */
	public abstract void receiveLater(SignalWrapper signal, AbstractModelClassRuntime target);

	/**
	 * Adds a new entry to this thread's mailbox to send the given signal to the
	 * given target object; the signal is known to have been sent via the API
	 * class.
	 * <p>
	 * Thread-safe.
	 */
	public void receiveLaterViaAPI(SignalWrapper signal, AbstractModelClassRuntime target) {
		receiveLater(signal, target);
	}

}
