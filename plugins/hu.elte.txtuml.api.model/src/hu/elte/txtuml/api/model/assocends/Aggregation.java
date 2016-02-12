package hu.elte.txtuml.api.model.assocends;

/**
 * A base interface to represent the aggregation property of association ends.
 * Association ends might have an aggregation type of {@link None} or
 * {@link Composite}.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public interface Aggregation<T extends Aggregation<T>> {

	/**
	 * Implementing classes represent association ends with no aggregation.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	interface None extends Aggregation<None> {
	}

	/**
	 * Implementing classes represent association ends with composite
	 * aggregation.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	interface Composite extends Aggregation<Composite> {
	}

}
