package hu.elte.txtuml.api.model.execution;

import hu.elte.txtuml.api.model.external.BaseModelScheduler;

/**
 * A model scheduler is responsible for handling timed events in the model.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public interface ModelScheduler extends BaseModelScheduler {

	@Override
	ModelExecutor getExecutor();

	/**
	 * The model execution time helps testing txtUML models in the following
	 * way: when any time-related event inside the model is set to take
	 * <i>ms</i> milliseconds, that event will take <i>ms</i> <code>*</code>
	 * <i>mul</i> milliseconds during model execution, where <i>mul</i> is the
	 * current execution time multiplier. This way, txtUML models might be
	 * tested at the desired speed.
	 * <p>
	 * Thread-safe.
	 * 
	 * @return the current model execution time multiplier
	 */
	double getExecutionTimeMultiplier();

	/**
	 * Returns the given amount of time multiplied by the execution time
	 * multiplier.
	 * 
	 * @param time
	 *            the amount of time to multiply
	 * @return the given amount of time multiplied by the execution time
	 *         multiplier
	 */
	default long inExecutionTime(long time) {
		return Math.round(time * getExecutionTimeMultiplier());
	}

}
