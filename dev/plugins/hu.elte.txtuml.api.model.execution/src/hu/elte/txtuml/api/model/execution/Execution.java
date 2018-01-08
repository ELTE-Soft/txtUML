package hu.elte.txtuml.api.model.execution;

/**
 * TODO document
 */
public interface Execution {

	/**
	 * The fields of this class describe the settings of a model execution.
	 */
	public class Settings implements Cloneable {

		/**
		 * Determines which level of model execution logs should be shown.
		 * <p>
		 * Log level is {@link LogLevel#WARNING} by default.
		 */
		public LogLevel logLevel = LogLevel.WARNING;

		/**
		 * Determines which level of dynamic checks should be performed during
		 * the model execution.
		 * <p>
		 * Check level is {@link CheckLevel#OPTIONAL} by default.
		 */
		public CheckLevel checkLevel = CheckLevel.OPTIONAL;

		/**
		 * The model execution time helps testing txtUML models in the following
		 * way: when any time-related event inside the model is set to take
		 * <i>ms</i> milliseconds, that event will take <i>ms</i> <code>*</code>
		 * <i>mul</i> milliseconds during model execution, where <i>mul</i> is
		 * the current execution time multiplier. This way, txtUML models might
		 * be tested at the desired speed.
		 * <p>
		 * Execution time multiplier is 1 by default.
		 */
		public double timeMultiplier = 1.0;

		@Override
		public Settings clone() {
			try {
				return (Settings) super.clone();
			} catch (CloneNotSupportedException e) {
				// Cannot happen.
				return null;
			}
		}
	}

}
