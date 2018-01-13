package hu.elte.txtuml.api.model.execution.impl.base;

import static com.google.common.util.concurrent.Uninterruptibles.joinUninterruptibly;
import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.Function;

import hu.elte.txtuml.api.model.execution.CastedModelExecutor;
import hu.elte.txtuml.api.model.execution.CheckLevel;
import hu.elte.txtuml.api.model.execution.ErrorListener;
import hu.elte.txtuml.api.model.execution.Execution;
import hu.elte.txtuml.api.model.execution.LockedModelExecutorException;
import hu.elte.txtuml.api.model.execution.LogLevel;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.api.model.execution.TraceListener;
import hu.elte.txtuml.api.model.execution.WarningListener;
import hu.elte.txtuml.utils.NotifierOfTermination.TerminationManager;

/**
 * Abstract base class for {@link ModelExecutor} implementations.
 */
public abstract class AbstractModelExecutor<S extends CastedModelExecutor<S>> implements CastedModelExecutor<S> {

	/*
	 * Implementation note: the fields of this executor which contain
	 * information about settings (trace/error/warningListeners, dynamicChecks,
	 * executionTimeMultiplier, etc.) are not protected by any synchronization
	 * as they can only be accessed from the thread which created this executor.
	 * The settings which are required at runtime are copied into the
	 * ModelRuntime instance upon its creation.
	 */

	private final String name;
	private final TerminationManager terminationManager = new TerminationManager();

	private final List<TraceListener> traceListeners = new ArrayList<>();
	private final List<ErrorListener> errorListeners = new ArrayList<>();
	private final List<WarningListener> warningListeners = new ArrayList<>();
	private final SwitchOnLogging switchOnLogging;

	/**
	 * May only be accessed while holding the monitor of this collection
	 * instance.
	 */
	private final Set<Object> terminationBlockers = new HashSet<>();
	private final Object defaultBlocker = createAndAddDefaultTerminationBlocker();

	private final CountDownLatch isInitialized = new CountDownLatch(1);

	private final ConcurrentHashMap<Object, Object> featureMap = new ConcurrentHashMap<>();

	/**
	 * May only be accessed while holding the monitor of this collection
	 * instance.
	 */
	private final List<OwnedThread<?>> threads = new ArrayList<>(1);

	private volatile boolean shouldShutDownImmediately = false;

	private volatile Status status = Status.CREATED;

	private volatile ModelSchedulerImpl scheduler;

	private Execution.Settings settings = new Execution.Settings();

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
	public ModelSchedulerImpl getScheduler() {
		/*
		 * The field 'scheduler' is volatile. We must ensure that the same
		 * reference is used below in the more then one occasions it is
		 * accessed.
		 */
		ModelSchedulerImpl scheduler = this.scheduler;
		if (scheduler == null) {
			throw new IllegalStateException();
		}
		return scheduler;
	}

	@Override
	public S startNoWait() throws LockedModelExecutorException {
		checkIfLocked();
		switchOnLogging.switchOnFor(this);

		status = Status.ACTIVE;
		scheduler = new ModelSchedulerImpl(this, settings.timeMultiplier);
		traceListeners.forEach(x -> x.executionStarted());

		createRuntime(() -> {
			if (initialization != null) {
				initialization.run();
				initialization = null; // Release it. Not needed anymore.
			}
			isInitialized.countDown();
		}).start();

		return self();
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
		shouldShutDownImmediately = true;
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
		boolean isEmpty = false;
		synchronized (terminationBlockers) {
			terminationBlockers.remove(blocker);
			isEmpty = terminationBlockers.isEmpty();
		}
		if (isEmpty) {
			wakeAllThreads();
		}
		return self();
	}

	@Override
	public S awaitInitializationNoCatch() throws InterruptedException {
		isInitialized.await();
		return self();
	}

	@Override
	public void awaitTerminationNoCatch() throws InterruptedException {
		while (true) {
			OwnedThread<?>[] copy;
			synchronized (threads) {
				if (threads.isEmpty()) {
					return;
				}
				copy = threads.toArray(new OwnedThread<?>[threads.size()]);
			}
			for (OwnedThread<?> e : copy) {
				joinUninterruptibly(e);
			}
		}
	}

	@Override
	public Execution.Settings getSettings() {
		return settings.clone();
	}

	@Override
	public ModelExecutor set(Consumer<Execution.Settings> consumer) throws LockedModelExecutorException {
		checkIfLocked();
		consumer.accept(settings);
		settings = settings.clone();
		return self();
	}

	@Override
	public S setCheckLevel(CheckLevel checkLevel) throws LockedModelExecutorException {
		checkIfLocked();
		settings.checkLevel = checkLevel;
		return self();
	}

	@Override
	public S setLogLevel(LogLevel logLevel) throws LockedModelExecutorException {
		checkIfLocked();
		settings.logLevel = logLevel;
		return self();
	}

	@Override
	public S setExecutionTimeMultiplier(double newMultiplier) throws LockedModelExecutorException {
		checkIfLocked();
		settings.timeMultiplier = newMultiplier;
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
	public void setFeature(Object key, Object feature) {
		featureMap.put(key, feature);
	}

	@Override
	public Object getFeature(Object key) {
		return featureMap.get(key);
	}

	@Override
	public Object getOrCreateFeature(Object key, Function<Object, Object> supplier) {
		return featureMap.computeIfAbsent(key, supplier);
	}

	/**
	 * Runs all the registered termination listeners, and sets this model
	 * executor terminated.
	 * <p>
	 * Thread-safe.
	 */

	protected void performTermination() {
		terminationManager.notifyAllOfTermination();
		status = Status.TERMINATED;
		traceListeners.forEach(x -> x.executionTerminated());
	}

	/**
	 * Checks if {@link #status} is equal to {@link Status#CREATED} and throws
	 * an exception otherwise.
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
		return shouldShutDownImmediately;
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
	protected void wakeAllThreads() {
		OwnedThread<?>[] copy;
		synchronized (threads) {
			copy = threads.toArray(new OwnedThread<?>[threads.size()]);
		}
		for (OwnedThread<?> e : copy) {
			e.wake();
		}
	}

	/**
	 * Tries to register a model executor thread which is part of this
	 * executor's runtime; the thread can only be started if this method returns
	 * true.
	 * <p>
	 * That naturally means that this method has to be called from the thread
	 * that starts the given thread. This is because otherwise (if this method
	 * would be called from the new thread) this model executor could terminate
	 * before the new thread starts.
	 */
	public boolean registerThread(OwnedThread<?> t) {
		if (shouldShutDownImmediately) {
			return false;
		}
		synchronized (threads) {
			return threads.add(t);
		}
	}

	/**
	 * Unregisters a model executor thread in this executor which is part of
	 * this executor's runtime.
	 * <p>
	 * Called from the given thread.
	 */
	public void unregisterThread(OwnedThread<?> t) {
		boolean isEmpty;
		synchronized (threads) {
			threads.remove(t);
			isEmpty = threads.isEmpty();
		}
		if (isEmpty) {
			performTermination();
		}
	}

	public List<TraceListener> getTraceListeners() {
		return Collections.unmodifiableList(traceListeners);
	}

	public List<WarningListener> getWarningListeners() {
		return Collections.unmodifiableList(warningListeners);
	}

	public List<ErrorListener> getErrorListeners() {
		return Collections.unmodifiableList(errorListeners);
	}

	/**
	 * Creates a new runtime instance for this model executor.
	 */
	protected abstract AbstractModelRuntime<?, ?> createRuntime(Runnable initialization);

	@Override
	public abstract S self();

	public static abstract class OwnedThread<E extends AbstractModelExecutor<?>> extends Thread {

		private final E executor;

		public OwnedThread(String name, E owner) {
			super(name);
			executor = owner;
		}

		public E getExecutor() {
			return executor;
		}

		@Override
		public synchronized void start() {
			if (executor.registerThread(this)) {
				super.start();
			}
		}

		/**
		 * Use {@link #doRun()} instead.
		 */
		@Override
		public final void run() {
			doRun();
			executor.unregisterThread(this);
		}

		/**
		 * True iff {@link #shutdownNow} has already been called.
		 * <p>
		 * Thread-safe.
		 */
		protected boolean shouldShutDownImmediately() {
			return executor.shouldShutDownImmediately();
		}

		/**
		 * True iff no termination blocker is actually registered.
		 * <p>
		 * Thread-safe.
		 */
		protected boolean shouldShutDownWhenNothingToDo() {
			return executor.shouldShutDownWhenNothingToDo();
		}

		/**
		 * Used instead of {@link #run()}.
		 */
		public abstract void doRun();

		/**
		 * If this thread is blocking, a call of this method wakes the thread.
		 * <p>
		 * Thread-safe.
		 */
		public abstract void wake();
	}

}
