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
import hu.elte.txtuml.api.model.execution.CheckLevel;
import hu.elte.txtuml.api.model.execution.ErrorListener;
import hu.elte.txtuml.api.model.execution.Execution;
import hu.elte.txtuml.api.model.execution.TraceListener;
import hu.elte.txtuml.api.model.execution.WarningListener;
import hu.elte.txtuml.api.model.impl.ModelClassRuntime;
import hu.elte.txtuml.api.model.impl.ModelRuntime;
import hu.elte.txtuml.api.model.impl.PortRuntime;

/**
 * Abstract base class for {@link ModelRuntime} implementations.
 */
public abstract class AbstractModelRuntime<C extends ModelClassRuntime, P extends PortRuntime> implements ModelRuntime {

	private final AbstractModelExecutor<?> executor;

	private final List<TraceListener> traceListeners;
	private final List<ErrorListener> errorListeners;
	private final List<WarningListener> warningListeners;

	private final CheckLevel checkLevel;
	private final double timeMultiplier;

	private final Object LOCK_ON_SCHEDULER = new Object();

	/**
	 * May only be accessed while holding the monitor of
	 * {@link #LOCK_ON_SCHEDULER}.
	 */
	private ScheduledExecutorService scheduler = null;

	/**
	 * Must be called on the thread which manages the given
	 * {@link AbstractModelExecutor} instance.
	 */
	protected AbstractModelRuntime(AbstractModelExecutor<?> executor) {
		this(executor, executor.getTraceListeners(), executor.getErrorListeners(), executor.getWarningListeners(),
				executor.getSettings());
	}

	protected AbstractModelRuntime(AbstractModelExecutor<?> executor, List<TraceListener> traceListeners,
			List<ErrorListener> errorListeners, List<WarningListener> warningListeners, Execution.Settings settings) {
		this.executor = executor;

		this.traceListeners = new ArrayList<>(traceListeners);
		this.errorListeners = new ArrayList<>(errorListeners);
		this.warningListeners = new ArrayList<>(warningListeners);

		this.checkLevel = settings.checkLevel;
		this.timeMultiplier = settings.timeMultiplier;
	}

	@Override
	public AbstractModelExecutor<?> getExecutor() {
		return executor;
	}

	public CheckLevel getCheckLevel() {
		return checkLevel;
	}

	@Override
	public double getExecutionTimeMultiplier() {
		return timeMultiplier;
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
		}

		final Object blocker = new Object();
		getExecutor().addTerminationBlocker(blocker);

		final ScheduledFuture<V> future = currentScheduler.schedule(() -> {
			V result = callable.call();
			getExecutor().removeTerminationBlocker(blocker);
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
					getExecutor().removeTerminationBlocker(blocker);
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
	protected C getRuntimeOf(ModelClass cls) {
		return (C) ModelRuntime.super.getRuntimeOf(cls);
	}

	@SuppressWarnings("unchecked")
	protected P getRuntimeOf(Port<?, ?> port) {
		return (P) ModelRuntime.super.getRuntimeOf(port);
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
	protected boolean isLinkingDeleted(AbstractModelClassRuntime wrapper) {
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
	protected boolean isUnlinkingDeleted(AbstractModelClassRuntime wrapper) {
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
	public abstract void connect(Port<?, ?> portInstance, AbstractModelClassRuntime object);

	@Override
	public abstract C createModelClassRuntime(ModelClass object);

	@Override
	public abstract P createPortRuntime(Port<?, ?> portInstance, ModelClass owner);

}
