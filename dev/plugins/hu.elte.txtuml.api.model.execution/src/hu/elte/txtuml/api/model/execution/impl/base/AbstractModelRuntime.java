package hu.elte.txtuml.api.model.execution.impl.base;

import java.util.ArrayList;
import java.util.List;
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
	private final ModelSchedulerImpl scheduler;

	private final List<TraceListener> traceListeners;
	private final List<ErrorListener> errorListeners;
	private final List<WarningListener> warningListeners;

	private final CheckLevel checkLevel;

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
		this.scheduler = executor.getScheduler();

		this.traceListeners = new ArrayList<>(traceListeners);
		this.errorListeners = new ArrayList<>(errorListeners);
		this.warningListeners = new ArrayList<>(warningListeners);

		this.checkLevel = settings.checkLevel;
	}

	@Override
	public AbstractModelExecutor<?> getExecutor() {
		return executor;
	}

	@Override
	public ModelSchedulerImpl getScheduler() {
		return scheduler;
	}

	public CheckLevel getCheckLevel() {
		return checkLevel;
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
