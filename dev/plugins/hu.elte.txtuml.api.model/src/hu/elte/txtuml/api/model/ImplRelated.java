package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.impl.ModelElementRuntime;

/**
 * This interface and its subtypes should <b>only be used to implement model
 * executors</b>, not in the model or in external libraries.
 * <p>
 * Marker interface for implementation related types.
 * <p>
 * Enables subtypes to get the runtime of a {@link RequiresRuntime} instance
 * which is otherwise inaccessible from outside this package.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
@External
public interface ImplRelated {

	/**
	 * Returns the runtime wrapper of the given object which provides
	 * information and management capabilities.
	 * 
	 * @param element
	 *            the model element whose runtime wrapper is queried
	 * @return runtime wrapper of the given object
	 */
	default <R extends ModelElementRuntime<?>> R getRuntimeOf(RequiresRuntime<R> element) {
		return element.runtime();
	}

	/**
	 * Base class for JtxtUML classes which require an accessible runtime
	 * wrapper.
	 * <p>
	 * As an external class, this type should not be used inside the model. It
	 * helps implementing executors.
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 */
	@External
	static abstract class RequiresRuntime<R extends ModelElementRuntime<?>> {

		private final R runtime = createRuntime();

		RequiresRuntime() {
		}

		/**
		 * Creates the runtime wrapper for this model element. Implementation
		 * must <i>not</i> depend on fields of any subclass of
		 * {@link RequiresRuntime} as this method is called upon initialization.
		 */
		abstract R createRuntime();

		/**
		 * Returns the runtime wrapper of this object which provides information
		 * and management capabilities.
		 * 
		 * @return runtime wrapper of this object
		 */
		final R runtime() {
			return runtime;
		}

		@ExternalBody
		@Override
		public String toString() {
			return runtime.getStringRepresentation();
		}

	}
}
