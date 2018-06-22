package hu.elte.txtuml.api.model.execution;

import hu.elte.txtuml.api.model.Model;

/**
 * This interface helps writing specialized {@link ModelExecutor}s which can
 * still be subtyped.
 * <p>
 * This overrides all the {@code ModelExecutor} interface's chainable methods to
 * have a return type of {@link S} (a parameter of this interface). Therefore a
 * specialized model executor interface does not have to override these methods
 * again to ensure that the user may chain over the specialized type instead of
 * the base ({@code ModelExecutor}).
 * <p>
 * This interface also helps in case of abstract implementor classes. If the
 * abstract class has a type parameter {@code S} and extends
 * {@code CastedModelExecutor<S>}, then in the implementation of these chainable
 * methods, it may return the value of {@link #self()}. This way only the
 * {@code self()} method has to be implemented in a concrete subclass to support
 * method chaining.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 * 
 * @param <S>
 *            a subtype of this interface which will be returned by the
 *            chainable methods
 */
public interface CastedModelExecutor<S extends CastedModelExecutor<S>> extends ModelExecutor {

	@Override
	default S start() throws LockedModelExecutorException {
		return startNoWait().awaitInitialization();
	}

	@Override
	default S start(Runnable initialization) throws LockedModelExecutorException {
		return setInitialization(initialization).start();
	}

	@Override
	S setInitialization(Runnable initialization) throws LockedModelExecutorException;

	@Override
	S shutdown();

	@Override
	S shutdownNow();

	@Override
	S addTerminationListener(Runnable listener);

	@Override
	S removeTerminationListener(Runnable listener);

	@Override
	S addTerminationBlocker(Object blocker);

	@Override
	S removeTerminationBlocker(Object blocker);

	@Override
	default S awaitInitialization() {
		while (true) {
			try {
				return awaitInitializationNoCatch();
			} catch (InterruptedException e) {
			}
		}
	}

	@Override
	S awaitInitializationNoCatch() throws InterruptedException;

	@Override
	S startNoWait() throws LockedModelExecutorException;

	@Override
	default S startNoWait(Runnable initialization) throws LockedModelExecutorException {
		return setInitialization(initialization).startNoWait();
	}

	@Override
	S addTraceListener(TraceListener listener) throws LockedModelExecutorException;

	@Override
	S addErrorListener(ErrorListener listener) throws LockedModelExecutorException;

	@Override
	S addWarningListener(WarningListener listener) throws LockedModelExecutorException;

	@Override
	S removeTraceListener(TraceListener listener) throws LockedModelExecutorException;

	@Override
	S removeErrorListener(ErrorListener listener) throws LockedModelExecutorException;

	@Override
	S removeWarningListener(WarningListener listener) throws LockedModelExecutorException;

	@Override
	S setLogLevel(LogLevel logLevel) throws LockedModelExecutorException;

	@Override
	S setCheckLevel(CheckLevel checkLevel) throws LockedModelExecutorException;

	@Override
	S setExecutionTimeMultiplier(double newMultiplier) throws LockedModelExecutorException;

	/**
	 * @return this
	 */
	S self();

}
