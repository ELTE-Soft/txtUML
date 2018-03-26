package hu.elte.txtuml.api.model.external;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import hu.elte.txtuml.api.model.error.NotModelExecutorThreadError;
import hu.elte.txtuml.api.model.impl.ExecutorThread;

/**
 * Through this interface some management features of the currently operating
 * model scheduler can be accessed in a thread-safe way. A model scheduler is
 * responsible for handling timed events in the model.
 * <p>
 * The features provided by this interface are those which has to be provided by
 * <i>any</i> txtUML model scheduler and therefore <i>these and only these</i>
 * can be used to implement external libraries for txtUML models.
 * <p>
 * As a general rule, all methods of this interface will throw
 * {@link NullPointerException} if a null reference is given to them as a
 * parameter, unless the opposite is explicitly stated in the documentation of
 * the methods.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public interface BaseModelScheduler {

	/**
	 * Gets the model scheduler which is associated with the current model
	 * executor thread.
	 * 
	 * @return the model executor which is associated with the current thread
	 * @throws NotModelExecutorThreadError
	 *             if the caller thread is not a model executor thread
	 */
	static BaseModelScheduler current() throws NotModelExecutorThreadError {
		return ExecutorThread.current().getModelRuntime().getScheduler();
	}

	/**
	 * The model executor this scheduler belongs to.
	 * <p>
	 * Thread-safe.
	 */
	BaseModelExecutor getExecutor();

	/**
	 * Schedules a timed event for this runtime instance. Delay is not
	 * necessarily interpreted as it is, it may be altered based on the settings
	 * of the model executor this scheduler belongs to.
	 * <p>
	 * The given callable is <b>not</b> executed on a model executor thread and
	 * has no synchronization to anything when called. If it wishes to
	 * communicate with the model, it must use the methods provided by the
	 * {@link hu.elte.txtuml.api.model.API API} class.
	 */
	<V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);

}
