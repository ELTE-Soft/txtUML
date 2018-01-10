package hu.elte.txtuml.api.model.execution.impl.base;

import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import hu.elte.txtuml.api.model.execution.ModelScheduler;

/**
 * Base implementation for {@link ModelScheduler}.
 */
public class ModelSchedulerImpl implements ModelScheduler {

	private final AbstractModelExecutor<?> executor;
	private final double executionTimeMultiplier;

	/**
	 * May only be accessed while holding the monitor of {@code this}.
	 */
	private ScheduledExecutorService backend = null;

	public ModelSchedulerImpl(AbstractModelExecutor<?> executor, double executionTimeMultiplier) {
		this.executor = executor;
		this.executionTimeMultiplier = executionTimeMultiplier;
	}

	@Override
	public AbstractModelExecutor<?> getExecutor() {
		return executor;
	}

	@Override
	public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
		ScheduledExecutorService currentScheduler;
		synchronized (this) {
			if (backend == null) {
				backend = createBackend();
				getExecutor().addTerminationListener(backend::shutdownNow);
			}
			currentScheduler = backend;
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

	@Override
	public double getExecutionTimeMultiplier() {
		return executionTimeMultiplier;
	}

	/**
	 * Creates a new {@link ScheduledExecutorService} to be used by this runtime
	 * instance to schedule timed events.
	 * <p>
	 * The default implementation creates a single thread executor service.
	 */
	protected ScheduledExecutorService createBackend() {
		return Executors.newSingleThreadScheduledExecutor();
	}

}
