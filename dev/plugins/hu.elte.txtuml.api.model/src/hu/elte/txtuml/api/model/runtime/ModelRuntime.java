package hu.elte.txtuml.api.model.runtime;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.ConnectorBase.ConnectorEnd;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ImplRelated;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.error.NotModelExecutorThreadError;

/**
 * A txtUML model execution is associated with an implementor instance of this
 * interface.
 * <p>
 * As a member of the {@linkplain hu.elte.txtuml.api.model.runtime} package, this
 * type should <b>only be used to implement model executors</b>, not in the
 * model or in external libraries.
 */
public interface ModelRuntime extends ImplRelated {

	/**
	 * Gets the model runtime instance which is associated with the current
	 * model execution that owns the current model executor thread.
	 * <p>
	 * <i>Note:</i> calls
	 * {@link ExecutorThread#current()}.{@link ExecutorThread#getModelRuntime()
	 * getModelRuntime()}.
	 * 
	 * @return the model runtime which is associated with the current model
	 *         execution
	 * @throws NotModelExecutorThreadError
	 *             if the caller thread is not a model executor thread
	 */
	static ModelRuntime current() throws NotModelExecutorThreadError {
		return ExecutorThread.current().getModelRuntime();
	}

	/**
	 * The model executor associated with this runtime instance.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 */
	BaseModelExecutor getExecutor();

	/**
	 * The model execution time helps testing txtUML models in the following
	 * way: when any time-related event inside the model is set to take
	 * <i>ms</i> milliseconds, that event will take <i>ms</i> <code>*</code>
	 * <i>mul</i> milliseconds during model execution, where <i>mul</i> is the
	 * current execution time multiplier. This way, txtUML models might be
	 * tested at the desired speed.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 * 
	 * @return the current model execution time multiplier
	 */
	double getExecutionTimeMultiplier();

	/**
	 * Provides a conversion from <i>'real time'</i> to model execution time by
	 * multiplying its parameter with the execution time multiplier.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 * 
	 * @param time
	 *            the amount of time to be given in model execution time
	 * @return the specified amount of time in model execution time
	 * 
	 * @see #getExecutionTimeMultiplier()
	 */
	default long inExecutionTime(long time) {
		return Math.round(time * getExecutionTimeMultiplier());
	}

	/**
	 * Schedules a timed event for this runtime instance. Delay is interpreted
	 * in execution time.
	 */
	<V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);

	/**
	 * Called by {@link Action#connect(Class, Port, Class, Port)}.
	 */
	<C1 extends ConnectorEnd<?, P1>, P1 extends Port<I1, I2>, C2 extends ConnectorEnd<?, P2>, P2 extends Port<I2, I1>, I1 extends Interface, I2 extends Interface> void connect(
			Class<C1> leftEnd, P1 leftPort, Class<C2> rightEnd, P2 rightPort);

	/**
	 * Called by {@link Action#connect(Port, Class, Port)}.
	 */
	<P1 extends Port<I1, I2>, C extends ConnectorEnd<?, P2>, P2 extends Port<I1, I2>, I1 extends Interface, I2 extends Interface> void connect(
			P1 parentPort, Class<C> childEnd, P2 childPort);

	/**
	 * Called by {@link Action#link(Class, ModelClass, Class, ModelClass)}.
	 */
	<L extends ModelClass, R extends ModelClass, CL extends GeneralCollection<L>, CR extends GeneralCollection<R>> void link(
			Class<? extends AssociationEnd<CL>> leftEnd, L leftObj, Class<? extends AssociationEnd<CR>> rightEnd,
			R rightObj);

	/**
	 * Called by {@link Action#unlink(Class, ModelClass, Class, ModelClass)}.
	 */
	<L extends ModelClass, R extends ModelClass, CL extends GeneralCollection<L>, CR extends GeneralCollection<R>> void unlink(
			Class<? extends AssociationEnd<CL>> leftEnd, L leftObj, Class<? extends AssociationEnd<CR>> rightEnd,
			R rightObj);

	/**
	 * Creates a model class wrapper for the given wrapped object.
	 */
	ModelClassRuntime createModelClassRuntime(ModelClass wrapped);

	/**
	 * Creates a port wrapper for the given wrapped port instance.
	 */
	PortRuntime createPortRuntime(Port<?, ?> wrapped, ModelClass owner);

}
