package hu.elte.txtuml.api.model.execution.impl.base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.Runtime;
import hu.elte.txtuml.api.model.execution.ErrorListener;
import hu.elte.txtuml.api.model.execution.TraceListener;
import hu.elte.txtuml.api.model.execution.WarningListener;
import hu.elte.txtuml.api.model.runtime.ModelClassWrapper;
import hu.elte.txtuml.api.model.runtime.PortWrapper;

/**
 * Abstract base class for {@link Runtime} implementations.
 */
public abstract class AbstractRuntime<C extends ModelClassWrapper, P extends PortWrapper> extends Runtime {

	private final AbstractModelExecutor<?> executor;

	private final List<TraceListener> traceListeners;
	private final List<ErrorListener> errorListeners;
	private final List<WarningListener> warningListeners;

	private final boolean dynamicChecks;
	private final double executionTimeMultiplier;

	/**
	 * Is also used as a termination blocker in the executor while
	 * scheduledCount > 0.
	 */
	private final Object LOCK_ON_SCHEDULER = new Object();

	/**
	 * May only be accessed while holding the monitor of
	 * {@link #LOCK_ON_SCHEDULER}.
	 */
	private ScheduledExecutorService scheduler = null;

	/**
	 * May only be accessed while holding the monitor of
	 * {@link #LOCK_ON_SCHEDULER}.
	 */
	private int scheduledCount = 0;

	/**
	 * Must be called on the thread which manages the given
	 * {@link AbstractModelExecutor} instance.
	 */
	protected AbstractRuntime(AbstractModelExecutor<?> executor) {
		this(executor, executor.getTraceListeners(), executor.getErrorListeners(), executor.getWarningListeners(),
				executor.dynamicChecks(), executor.getExecutionTimeMultiplier());
	}

	protected AbstractRuntime(AbstractModelExecutor<?> executor, List<TraceListener> traceListeners,
			List<ErrorListener> errorListeners, List<WarningListener> warningListeners, boolean dynamicChecks,
			double executionTimeMultiplier) {
		this.executor = executor;

		this.traceListeners = new ArrayList<>(traceListeners);
		this.errorListeners = new ArrayList<>(errorListeners);
		this.warningListeners = new ArrayList<>(warningListeners);

		this.dynamicChecks = dynamicChecks;
		this.executionTimeMultiplier = executionTimeMultiplier;
	}

	@Override
	public AbstractModelExecutor<?> getExecutor() {
		return executor;
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
	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
		ScheduledExecutorService currentScheduler;
		synchronized (LOCK_ON_SCHEDULER) {
			if (scheduler == null) {
				scheduler = createScheduler();
				getExecutor().addTerminationListener(scheduler::shutdownNow);
			}
			currentScheduler = scheduler;
			if (scheduledCount == 0) {
				getExecutor().addTerminationBlocker(LOCK_ON_SCHEDULER);
			}
			++scheduledCount;
		}

		final ScheduledFuture<V> future = currentScheduler.schedule(() -> {
			V result = callable.call();
			decreaseScheduledCount();
			return result;
		}, inExecutionTime(delay), unit);

		return new ScheduledFuture<V>() {

			/*
			 * A mock of 'future'. Only differs in the 'cancel' method.
			 */

			@Override
			public long getDelay(TimeUnit unit) {
				return future.getDelay(unit);
			}

			@Override
			public int compareTo(Delayed o) {
				return future.compareTo(o);
			}

			@Override
			public boolean cancel(boolean mayInterruptIfRunning) {
				boolean hasBeenCancelledNow = future.cancel(mayInterruptIfRunning);
				if (hasBeenCancelledNow) {
					decreaseScheduledCount();
				}
				return hasBeenCancelledNow;
			}

			@Override
			public V get() throws InterruptedException, ExecutionException {
				return future.get();
			}

			@Override
			public V get(long timeout, TimeUnit unit)
					throws InterruptedException, ExecutionException, TimeoutException {
				return future.get(timeout, unit);
			}

			@Override
			public boolean isCancelled() {
				return future.isCancelled();
			}

			@Override
			public boolean isDone() {
				return future.isDone();
			}

		};
	}
	
	private void decreaseScheduledCount() {
		synchronized (LOCK_ON_SCHEDULER) {
			--scheduledCount;
			if (scheduledCount == 0) {
				getExecutor().removeTerminationBlocker(LOCK_ON_SCHEDULER);
			}
		}
	}

	public void trace(Consumer<TraceListener> eventReporter) {
		traceListeners.forEach(eventReporter);
	}

	public void error(Consumer<ErrorListener> errorReporter) {
		errorListeners.forEach(errorReporter);
	}

	public void warning(Consumer<WarningListener> warningReporter) {
		warningListeners.forEach(warningReporter);
	}

	@SuppressWarnings("unchecked")
	protected C getInfo(ModelClass about) {
		return (C) about.runtimeInfo();
	}

	@SuppressWarnings("unchecked")
	protected P getInfo(Port<?, ?> about) {
		return (P) about.runtimeInfo();
	}

	/**
	 * Checks whether the specified model object is deleted; if it is, this
	 * method shows an error about a failed linking operation because of the
	 * deleted model object given as parameter to the {@code link} action.
	 * 
	 * @param wrapper
	 *            wrapper of the model object whose deleted status is to be
	 *            checked
	 * @return {@code true} if the object is deleted, {@code false} otherwise
	 * @throws NullPointerException
	 *             if {@code object} is {@code null}
	 */
	protected boolean isLinkingDeleted(AbstractModelClassWrapper wrapper) {
		if (wrapper.isDeleted()) {
			error(x -> x.linkingDeletedObject(wrapper.getWrapped()));
			return true;
		}
		return false;
	}

	/**
	 * Checks whether the specified model object is deleted; if it is, this
	 * method shows an error about a failed unlinking operation because of the
	 * deleted model object given as parameter to the {@code unlink} action.
	 * 
	 * @param wrapper
	 *            wrapper of the model object whose deleted status is to be
	 *            checked
	 * @return {@code true} if the object is deleted, {@code false} otherwise
	 * @throws NullPointerException
	 *             if {@code object} is {@code null}
	 */
	protected boolean isUnlinkingDeleted(AbstractModelClassWrapper wrapper) {
		if (wrapper.isDeleted()) {
			error(x -> x.unlinkingDeletedObject(wrapper.getWrapped()));
			return true;
		}
		return false;
	}

	/**
	 * Creates a new {@link ScheduledExecutorService} to be used by this runtime
	 * instance to schedule timed events.
	 * <p>
	 * The default implementation creates a single thread executor service.
	 */
	protected ScheduledExecutorService createScheduler() {
		return Executors.newSingleThreadScheduledExecutor();
	}

	/**
	 * Starts the initial thread of this runtime and the model execution with
	 * it.
	 */
	public abstract void start();

	/**
	 * Connects a behavior port to the state machine of the given model class
	 * instance.
	 */
	public abstract void connect(Port<?, ?> portInstance, AbstractModelClassWrapper object);

	@Override
	protected abstract C createModelClassWrapper(ModelClass object);

	@Override
	protected abstract P createPortWrapper(Port<?, ?> portInstance, ModelClass owner);

}
