package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.ConnectorBase.ConnectorEnd;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.error.MissingRuntimeContextError;
import hu.elte.txtuml.api.model.runtime.BaseModelExecutor;
import hu.elte.txtuml.api.model.runtime.ModelClassWrapper;
import hu.elte.txtuml.api.model.runtime.PortWrapper;
import hu.elte.txtuml.api.model.runtime.RuntimeContext;
import hu.elte.txtuml.api.model.runtime.RuntimeInfo;

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
	 * Returns whether optional dynamic checks are on.
	 * <p>
	 * Must be <b>thread-safe</b>.
	 */
	public abstract boolean dynamicChecks();

	/**
	 * The model execution time helps testing txtUML models in the following
	 * way: when any time-related event inside the model is set to take
	 * <i>ms</i> milliseconds, that event will take <i>ms</i> <code>*</code>
	 * <i>mul</i> millseconds during model execution, where <i>mul</i> is the
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
	 * Called by {@link Action#connect(Class, Port, Class, Port)} .
	 */
	public abstract <C1 extends ConnectorEnd<?, P1>, P1 extends Port<I1, I2>, C2 extends ConnectorEnd<?, P2>, P2 extends Port<I2, I1>, I1 extends Interface, I2 extends Interface> void connect(
			Class<C1> leftEnd, P1 leftPort, Class<C2> rightEnd, P2 rightPort);

	/**
	 * Called by {@link Action#connect(Port, Class, Port)}.
	 */
	public abstract <P1 extends Port<I1, I2>, C extends ConnectorEnd<?, P2>, P2 extends Port<I1, I2>, I1 extends Interface, I2 extends Interface> void connect(
			P1 parentPort, Class<C> childEnd, P2 childPort);

	/**
	 * Called by {@link Action#link(Class, ModelClass, Class, ModelClass)} .
	 */
	public abstract <L extends ModelClass, R extends ModelClass> void link(
			Class<? extends AssociationEnd<L, ?>> leftEnd, L leftObj, Class<? extends AssociationEnd<R, ?>> rightEnd,
			R rightObj);

	/**
	 * Called by {@link Action#unlink(Class, ModelClass, Class, ModelClass)} .
	 */
	public abstract <L extends ModelClass, R extends ModelClass> void unlink(
			Class<? extends AssociationEnd<L, ?>> leftEnd, L leftObj, Class<? extends AssociationEnd<R, ?>> rightEnd,
			R rightObj);

	protected abstract ModelClassWrapper createRuntimeInfoFor(ModelClass object);

	protected abstract PortWrapper createRuntimeInfoFor(Port<?, ?> portInstance, ModelClass owner);

	protected static abstract class Described<I extends RuntimeInfo> {

		private final I runtimeInfo = createRuntimeInfo();

		abstract I createRuntimeInfo();

		/**
		 * Returns the runtime information provider associated with this object.
		 */
		public final I runtimeInfo() {
			return runtimeInfo;
		}

		Runtime getRuntime() {
			return runtimeInfo().getRuntime();
		}

		@Override
		public String toString() {
			return runtimeInfo().getStringRepresentation();
		}

	}

}
