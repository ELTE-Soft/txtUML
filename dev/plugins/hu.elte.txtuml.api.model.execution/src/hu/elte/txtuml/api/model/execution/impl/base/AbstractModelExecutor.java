package hu.elte.txtuml.api.model.execution.impl.base;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import hu.elte.txtuml.api.model.execution.CastedModelExecutor;
import hu.elte.txtuml.api.model.execution.ErrorListener;
import hu.elte.txtuml.api.model.execution.LockedModelExecutorException;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.api.model.execution.TraceListener;
import hu.elte.txtuml.api.model.execution.WarningListener;
import hu.elte.txtuml.utils.NotifierOfTermination.TerminationManager;

/**
 * Abstract base class for {@link ModelExecutor} implementations.
 */
public abstract class AbstractModelExecutor<S extends AbstractModelExecutor<S>> implements CastedModelExecutor<S> {

	/*
	 * Implementation note: the fields of this executor which contain
	 * information about settings (trace/error/warningListeners, dynamicChecks,
	 * executionTimeMultiplier, etc.) are not protected by any synchronization
	 * as they can only be accessed from the thread which created this executor.
	 * The settings which are required at runtime are copied into the Runtime
	 * instance upon its creation.
	 */

	private final String name;
	private final TerminationManager terminationManager = new TerminationManager();

	private final List<TraceListener> traceListeners = new ArrayList<>();
	private final List<ErrorListener> errorListeners = new ArrayList<>();
	private final List<WarningListener> warningListeners = new ArrayList<>();
	private final SwitchOnLogging switchOnLogging;

	/**
	 * May only be accessed while holding the monitor of this set instance.
	 */
	private final Set<Object> terminationBlockers = new HashSet<>();
	private final Object defaultBlocker = createAndAddDefaultTerminationBlocker();

	private final CountDownLatch isInitialized = new CountDownLatch(1);

	private volatile Status status = Status.CREATED;
	private volatile boolean shutdownImmediately = false;

	private boolean dynamicChecks = false;
	private double executionTimeMultiplier = 1;
	private boolean traceLogging = false;

	private Runnable initialization = null;

	public AbstractModelExecutor(String name, SwitchOnLogging switchOnLogging) {
		this.name = name;

		if (switchOnLogging == null) {
			this.switchOnLogging = SwitchOnLogging.NONE;
		} else {
			this.switchOnLogging = switchOnLogging;
		}
	}

	private Object createAndAddDefaultTerminationBlocker() {
		Object obj = new Object();
		addTerminationBlocker(obj);
		return obj;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public S start() throws LockedModelExecutorException {
		checkIfLocked();
		switchOnLogging.switchOnFor(this);

		status = Status.ACTIVE;
		traceListeners.forEach(x -> x.executionStarted());

		createRuntime(() -> {
			if (initialization != null) {
				initialization.run();
			}
			isInitialized.countDown();
		}).start();

		return self();
	}

	@Override
	public S start(Runnable initialization) throws LockedModelExecutorException {
		return setInitialization(initialization).start();
	}

	@Override
	public S setInitialization(Runnable initialization) throws LockedModelExecutorException {
		requireNonNull(initialization);
		checkIfLocked();
		this.initialization = initialization;
		return self();
	}

	@Override
	public S shutdown() {
		removeTerminationBlocker(defaultBlocker);
		return self();
	}

	@Override
	public S shutdownNow() {
		shutdownImmediately = true;
		wakeAllThreads();
		return self();
	}

	@Override
	public S addTerminationListener(Runnable listener) {
		requireNonNull(listener);
		if (!terminationManager.addTerminationListener(listener)) {
			listener.run();
		}
		return self();
	}

	@Override
	public S removeTerminationListener(Runnable listener) {
		requireNonNull(listener);
		terminationManager.removeTerminationListener(listener);
		return self();
	}

	@Override
	public S addTerminationBlocker(Object blocker) {
		requireNonNull(blocker);
		synchronized (terminationBlockers) {
			terminationBlockers.add(blocker);
		}
		return self();
	}

	@Override
	public synchronized S removeTerminationBlocker(Object blocker) {
		requireNonNull(blocker);
		synchronized (terminationBlockers) {
			terminationBlockers.remove(blocker);
			if (terminationBlockers.isEmpty()) {
				wakeAllThreads();
			}
		}
		return self();
	}

	@Override
	public S awaitInitialization() {
		while (true) {
			try {
				return awaitInitializationNoCatch();
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public S awaitInitializationNoCatch() throws InterruptedException {
		isInitialized.await();
		return self();
	}

	@Override
	public void awaitTermination() {
		while (true) {
			try {
				awaitTerminationNoCatch();
				return;
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	public S launch() throws LockedModelExecutorException {
		return start().awaitInitialization();
	}

	@Override
	public S launch(Runnable initialization) throws LockedModelExecutorException {
		return setInitialization(initialization).start().awaitInitialization();
	}

	@Override
	public void run() throws LockedModelExecutorException {
		start().shutdown().awaitTermination();
	}

	@Override
	public void run(Runnable initialization) throws LockedModelExecutorException {
		setInitialization(initialization).start().shutdown().awaitTermination();
	}

	@Override
	public S setDynamicChecks(boolean newValue) throws LockedModelExecutorException {
		checkIfLocked();
		dynamicChecks = newValue;
		return self();
	}

	@Override
	public S setExecutionTimeMultiplier(double newMultiplier) throws LockedModelExecutorException {
		checkIfLocked();
		executionTimeMultiplier = newMultiplier;
		return self();
	}

	@Override
	public S setTraceLogging(boolean newValue) throws LockedModelExecutorException {
		checkIfLocked();
		traceLogging = newValue;
		return self();
	}

	@Override
	public S addTraceListener(TraceListener listener) throws LockedModelExecutorException {
		requireNonNull(listener);
		checkIfLocked();
		traceListeners.add(listener);
		return self();
	}

	@Override
	public S addErrorListener(ErrorListener listener) throws LockedModelExecutorException {
		requireNonNull(listener);
		checkIfLocked();
		errorListeners.add(listener);
		return self();
	}

	@Override
	public S addWarningListener(WarningListener listener) throws LockedModelExecutorException {
		requireNonNull(listener);
		checkIfLocked();
		warningListeners.add(listener);
		return self();
	}

	@Override
	public S removeTraceListener(TraceListener listener) throws LockedModelExecutorException {
		requireNonNull(listener);
		checkIfLocked();
		traceListeners.remove(listener);
		return self();
	}

	@Override
	public S removeErrorListener(ErrorListener listener) throws LockedModelExecutorException {
		requireNonNull(listener);
		checkIfLocked();
		errorListeners.remove(listener);
		return self();
	}

	@Override
	public S removeWarningListener(WarningListener listener) throws LockedModelExecutorException {
		requireNonNull(listener);
		checkIfLocked();
		warningListeners.remove(listener);
		return self();
	}

	@Override
	public List<TraceListener> getTraceListeners() {
		return Collections.unmodifiableList(traceListeners);
	}

	@Override
	public List<ErrorListener> getErrorListeners() {
		return Collections.unmodifiableList(errorListeners);
	}

	@Override
	public List<WarningListener> getWarningListeners() {
		return Collections.unmodifiableList(warningListeners);
	}

	@Override
	public boolean dynamicChecks() {
		return dynamicChecks;
	}

	@Override
	public double getExecutionTimeMultiplier() {
		return executionTimeMultiplier;
	}

	@Override
	public boolean traceLogging() {
		return traceLogging;
	}

	/**
	 * Runs all the registered termination listeners, and marks that this model
	 * executor is terminated.
	 * <p>
	 * Thread-safe.
	 */
	protected void performTermination() {
		terminationManager.notifyAllOfTermination();
		status = Status.TERMINATED;
		traceListeners.forEach(x -> x.executionTerminated());
	}

	/**
	 * Checks if {@link #status} is equal to {@link Status#CREATED}. Throws an
	 * exception otherwise.
	 * 
	 * @throws LockedModelExecutorException
	 *             if current status is not equal to Status.CREATED
	 */
	protected void checkIfLocked() throws LockedModelExecutorException {
		if (status != Status.CREATED) {
			throw new LockedModelExecutorException();
		}
	}

	/**
	 * True iff {@link #shutdownNow} has already been called.
	 * <p>
	 * Thread-safe.
	 */
	protected boolean shouldShutDownImmediately() {
		return shutdownImmediately;
	}

	/**
	 * True iff no termination blocker is actually registered.
	 * <p>
	 * Thread-safe.
	 */
	protected boolean shouldShutDownWhenNothingToDo() {
		synchronized (terminationBlockers) {
			return terminationBlockers.isEmpty();
		}
	}

	/**
	 * Called if previously either {@link #shouldShutDownImmediately} or
	 * {@link #shouldShutDownWhenNothingToDo} returned false and now it returns
	 * true. All threads run by this model executor must be waken to check
	 * whether they should shut down.
	 * <p>
	 * <b>Note:</b> It is not guaranteed that when this method is called, either
	 * of the two above mentioned methods will return true. This is because the
	 * call to this method is not protected by any synchronization and therefore
	 * a concurrent action may freely change the state of this model executor.
	 */
	protected abstract void wakeAllThreads();

	/**
	 * Creates a new runtime instance for this model executor.
	 */
	protected abstract AbstractRuntime<?, ?> createRuntime(Runnable initialization);

	/**
	 * Registers a newly created model executor thread which is part of this
	 * executor's runtime.
	 * <p>
	 * Called from the creator thread of the given thread.
	 */
	protected abstract void registerThread(ModelExecutorThread thread);

	/**
	 * Tries to unregister a model executor thread in this executor which is
	 * part of this executor's runtime. The thread may only terminate after this
	 * method returned {@code true}. In case of {@code false}, it must continue
	 * its loop and try unregistering later.
	 * <p>
	 * Called from the given thread.
	 * 
	 * @return whether the thread may terminate
	 */
	protected abstract boolean unregisterThread(ModelExecutorThread thread);

	@Override
	public abstract void awaitTerminationNoCatch() throws InterruptedException;

	@Override
	public abstract S self();

}
