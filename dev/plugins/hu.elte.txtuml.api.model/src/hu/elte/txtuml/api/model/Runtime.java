package hu.elte.txtuml.api.model;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import hu.elte.txtuml.api.model.ConnectorBase.ConnectorEnd;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.error.MissingRuntimeContextError;
import hu.elte.txtuml.api.model.runtime.BaseModelExecutor;
import hu.elte.txtuml.api.model.runtime.ModelClassWrapper;
import hu.elte.txtuml.api.model.runtime.PortWrapper;
import hu.elte.txtuml.api.model.runtime.RuntimeContext;
import hu.elte.txtuml.api.model.runtime.RuntimeInfo;

/**
 * txtUML models has to be run in a txtUML runtime context, that is, an instance
 * of this class must manage them.
 * 
 * <p>
 * <b>Represents:</b> no model element
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Cannot be used in a model, it has to be subclassed by model executors.
 * 
 * <p>
 * <b>Note:</b> all model executor threads (Java threads on which a model runs)
 * must implement the {@link RuntimeContext} interface to provide access to a
 * runtime instance. Model executors has to suffice this requirement.
 * 
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@External
public abstract class Runtime {

	/**
	 * Gets the current model runtime instance which is associated with the
	 * current thread.
	 * 
	 * @return the model runtime which is associated with the current thread
	 * @throws MissingRuntimeContextError
	 *             if the caller thread does not implement
	 *             {@link RuntimeContext}
	 */
	public static Runtime currentRuntime() throws MissingRuntimeContextError {
		try {
			return ((RuntimeContext) Thread.currentThread()).getRuntime();
		} catch (ClassCastException e) {
			throw new MissingRuntimeContextError();
		}
	}

	/**
	 * The model executor associated with this runtime instance.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 */
	public abstract BaseModelExecutor getExecutor();

	/**
	 * Returns whether optional dynamic checks are on. These checks include
	 * checking lower bounds of multiplicities, checking whether the guards of
	 * two transitions from the same vertex are overlapping, etc.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 */
	public abstract boolean dynamicChecks();

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
	public abstract double getExecutionTimeMultiplier();

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
	public final long inExecutionTime(long time) {
		return Math.round(time * getExecutionTimeMultiplier());
	}

	/**
	 * Schedules a timed event for this runtime instance. Delay is interpreted
	 * in execution time.
	 */
	public abstract <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit);

	/**
	 * Called by {@link Action#connect(Class, Port, Class, Port)}.
	 */
	public abstract <C1 extends ConnectorEnd<?, P1>, P1 extends Port<I1, I2>, C2 extends ConnectorEnd<?, P2>, P2 extends Port<I2, I1>, I1 extends Interface, I2 extends Interface> void connect(
			Class<C1> leftEnd, P1 leftPort, Class<C2> rightEnd, P2 rightPort);

	/**
	 * Called by {@link Action#connect(Port, Class, Port)}.
	 */
	public abstract <P1 extends Port<I1, I2>, C extends ConnectorEnd<?, P2>, P2 extends Port<I1, I2>, I1 extends Interface, I2 extends Interface> void connect(
			P1 parentPort, Class<C> childEnd, P2 childPort);

	/**
	 * Called by {@link Action#link(Class, ModelClass, Class, ModelClass)}.
	 */
	public abstract <L extends ModelClass, R extends ModelClass, CL extends GeneralCollection<L>, CR extends GeneralCollection<R>> void link(
			Class<? extends AssociationEnd<CL>> leftEnd, L leftObj, Class<? extends AssociationEnd<CR>> rightEnd,
			R rightObj);

	/**
	 * Called by {@link Action#unlink(Class, ModelClass, Class, ModelClass)}.
	 */
	public abstract <L extends ModelClass, R extends ModelClass, CL extends GeneralCollection<L>, CR extends GeneralCollection<R>> void unlink(
			Class<? extends AssociationEnd<CL>> leftEnd, L leftObj, Class<? extends AssociationEnd<CR>> rightEnd,
			R rightObj);

	/**
	 * Creates a model class wrapper for the given wrapped object.
	 */
	protected abstract ModelClassWrapper createModelClassWrapper(ModelClass wrapped);

	/**
	 * Creates a model class wrapper for the given wrapped port instance.
	 */
	protected abstract PortWrapper createPortWrapper(Port<?, ?> wrapped, ModelClass owner);

	/**
	 * Base class for model elements that have a runtime information provider.
	 * 
	 * <i>Note:</i> some methods of this class are marked external because they
	 * are visible in subclasses but should not be used in the model.
	 * 
	 * @param <I>
	 *            the type of the runtime information provider of this model
	 *            element
	 */
	protected static abstract class Described<I extends RuntimeInfo> {

		/**
		 * The runtime information provider of this model element.
		 */
		private final I runtimeInfo = createRuntimeInfo();

		/**
		 * Creates the runtime information provider for this model element.
		 * Implementation must not depend on fields of any subclass of
		 * {@link Described} as this method is called in the constructor.
		 */
		@External
		abstract I createRuntimeInfo();

		/**
		 * Returns the runtime information provider of this model element.
		 */
		@External
		public final I runtimeInfo() {
			return runtimeInfo;
		}

		/**
		 * A shorthand operation for {@link #runtimeInfo()}.
		 * {@link RuntimeInfo#getRuntime() getRuntime()}.
		 */
		@External
		final Runtime getRuntime() {
			return runtimeInfo().getRuntime();
		}

		/**
		 * Returns a short string representation of this model element.
		 */
		@Override
		public String toString() {
			return runtimeInfo().getStringRepresentation();
		}

	}

}
