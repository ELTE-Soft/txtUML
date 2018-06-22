package hu.elte.txtuml.api.model.impl;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.ConnectorBase.ConnectorEnd;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.ImplRelated;
import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.error.NotModelExecutorThreadError;
import hu.elte.txtuml.api.model.external.BaseModelExecutor;
import hu.elte.txtuml.api.model.external.BaseModelScheduler;

/**
 * A txtUML model execution is associated with an implementor instance of this
 * interface.
 * <p>
 * As a member of the {@linkplain hu.elte.txtuml.api.model.impl} package, this
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
	 * Thread-safe.
	 */
	BaseModelExecutor getExecutor();

	/**
	 * The model scheduler associated with this runtime instance.
	 * <p>
	 * Thread-safe.
	 */
	BaseModelScheduler getScheduler();

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
	<L extends ModelClass, R extends ModelClass, CL extends GeneralCollection<? super L>, CR extends GeneralCollection<? super R>> void link(
			Class<? extends AssociationEnd<CL>> leftEnd, L leftObj, Class<? extends AssociationEnd<CR>> rightEnd,
			R rightObj);

	/**
	 * Called by {@link Action#unlink(Class, ModelClass, Class, ModelClass)}.
	 */
	<L extends ModelClass, R extends ModelClass, CL extends GeneralCollection<? super L>, CR extends GeneralCollection<? super R>> void unlink(
			Class<? extends AssociationEnd<CL>> leftEnd, L leftObj, Class<? extends AssociationEnd<CR>> rightEnd,
			R rightObj);

	/**
	 * Creates a model class wrapper for the given wrapped object.
	 * 
	 * @param wrapped
	 *            the model class instance instance to create a runtime for
	 * @return the new runtime
	 */
	ModelClassRuntime createModelClassRuntime(ModelClass wrapped);

	/**
	 * Creates a port wrapper for the given wrapped port instance.
	 * 
	 * @param wrapped
	 *            the port instance to create a runtime for
	 * @param owner
	 *            the owner of the port
	 * @return the new runtime
	 */
	PortRuntime createPortRuntime(Port<?, ?> wrapped, ModelClass owner);

}
