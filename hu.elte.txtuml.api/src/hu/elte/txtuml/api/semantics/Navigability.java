package hu.elte.txtuml.api.semantics;

/**
 * A base interface to represent the navigability property of association ends.
 * Association ends might be {@link Navigable} or {@link NonNavigable}.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public interface Navigability {

	/**
	 * Implementing classes of this interface represent navigable association
	 * ends in the model.
	 * <p>
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public interface Navigable extends Navigability {
	}

	/**
	 * Implementing classes of this interface represent non-navigable
	 * association ends in the model.
	 * <p>
	 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
	 * overview on modeling in txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public interface NonNavigable extends Navigability {
	}

}
