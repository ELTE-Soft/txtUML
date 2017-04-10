package hu.elte.txtuml.api.model.execution.impl.base;

import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.runtime.RuntimeContext;
import hu.elte.txtuml.utils.Logger;

/**
 * A model executor thread is used by a model executor to run a model or parts
 * of a model (the executor may use multiple threads). Every
 * {@link hu.elte.txtuml.api.model.ModelClass} and
 * {@link hu.elte.txtuml.api.model.ModelClass.Port} instance is owned by one
 * model executor thread which runs and manages that instance. Whenever an
 * asynchronous event is raised (for example, a signal is sent to such an
 * instance), it is reported to the owner thread, which puts that event into its
 * mailbox and later processes it. The processing of one such event is called a
 * model execution step, which is also an iteration of the main loop of a model
 * executor thread. This thread stops if:
 * <ol>
 * <li>Either
 * <ul>
 * <li>the model executor to which this thread belongs has been forced to shut
 * down immediately,</li>
 * <li>or the model executor has no termination blockers (which implies that its
 * graceful shutdown procedure has been started) <b>and</b> this thread's
 * mailbox is empty,</li>
 * <li>or {@link #earlyStop} returns true (is false by default).</li>
 * </ul>
 * </li>
 * <li>The {@link AbstractModelExecutor#unregisterThread(ModelExecutorThread)}
 * method returns true.</li>
 * </ol>
 * Otherwise, the loop tries to continue working, even if it has been
 * interrupted while waiting for new events.
 */
public abstract class ModelExecutorThread extends Thread implements RuntimeContext {

	private static final AtomicLong count = new AtomicLong();

	private final AbstractModelExecutor<?> executor;
	private final AbstractRuntime<?, ?> runtime;
	private final Runnable initialization;
	private final long identifier;
	private final ConcurrentLinkedQueue<Runnable> delayedActions = new ConcurrentLinkedQueue<>();

	private Signal triggeringSignal;

	public ModelExecutorThread(AbstractModelExecutor<?> executor, AbstractRuntime<?, ?> runtime,
			Runnable initialization) {
		this(executor, runtime, initialization, count.getAndIncrement());
	}

	private ModelExecutorThread(AbstractModelExecutor<?> executor, AbstractRuntime<?, ?> runtime,
			Runnable initialization, long identifier) {
		super("Model_thread-" + identifier);
		this.executor = executor;
		this.runtime = runtime;
		this.initialization = initialization;
		this.identifier = identifier;
	}

	@Override
	public AbstractRuntime<?, ?> getRuntime() {
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
	 * Gets the current triggering signal.
	 * <p>
	 * As this method is <i>not</i> thread-safe, it may only be called from this
	 * thread.
	 */
	public void setCurrentTriggeringSignal(Signal triggeringSignal) {
		this.triggeringSignal = triggeringSignal;
	}

	@Override
	public synchronized void start() {
		executor.registerThread(this);
		super.start();
	}

	@Override
	public void run() {
		initialization.run();

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
	}

	private boolean shouldContinue() {
		if (executor.shouldShutDownImmediately() || (isEmpty() && executor.shouldShutDownWhenNothingToDo())
				|| earlyStop()) {
			if (executor.unregisterThread(this)) {
				return false;
			}
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
	 * Overridable method to indicate that this model executor may stop even if
	 * no shutdown process is initiated (for example, because all of the objects
	 * that were run on this thread are deleted). Even if it does return true,
	 * the thread only stops when the
	 * {@link AbstractModelExecutor#unregisterThread} returns true on the owning
	 * model executor.
	 * <p>
	 * As this method is <i>not</i> thread-safe, it may only be called from this
	 * thread.
	 * <p>
	 * The default implementation always returns false.
	 */
	protected boolean earlyStop() {
		return false;
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
	 * Checks whether this mailbox is empty.
	 * <p>
	 * Thread-safe.
	 */
	public abstract boolean isEmpty();

	/**
	 * If this thread is blocking because it called {@link #processNext} but it
	 * is empty, a call of this method wakes the thread.
	 * <p>
	 * Thread-safe.
	 */
	public abstract void wake();

	/**
	 * Adds a new entry to this thread's mailbox to send the given signal to the
	 * given target port.
	 * <p>
	 * Thread-safe.
	 */
	public abstract void send(Signal signal, AbstractPortWrapper target);

	/**
	 * Adds a new entry to this thread's mailbox to send the given signal to the
	 * given target object.
	 * <p>
	 * Thread-safe.
	 */
	public abstract void send(Signal signal, AbstractModelClassWrapper target);

	/**
	 * Adds a new entry to this thread's mailbox to send the given signal to the
	 * given target port with the given sender as the last port which this
	 * message came through.
	 * <p>
	 * Thread-safe.
	 */
	public abstract void send(Signal signal, AbstractPortWrapper sender, AbstractPortWrapper target);

	/**
	 * Adds a new entry to this thread's mailbox to send the given signal to the
	 * given target object with the given sender as the last port which this
	 * message came through.
	 * <p>
	 * Thread-safe.
	 */
	public abstract void send(Signal signal, AbstractPortWrapper sender, AbstractModelClassWrapper target);

	public abstract void sent(Signal signal, AbstractModelClassWrapper sender);

}
