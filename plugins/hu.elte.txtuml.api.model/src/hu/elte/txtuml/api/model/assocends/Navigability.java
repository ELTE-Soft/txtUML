package hu.elte.txtuml.api.model.assocends;

/**
 * A base interface to represent the navigability property of association ends.
 * Association ends might be {@link Navigable} or {@link NonNavigable}.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public interface Navigability {

	/**
	 * Implementing classes of this interface represent navigable association
	 * ends.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public interface Navigable extends Navigability {
	}

	/**
	 * Implementing classes of this interface represent non-navigable
	 * association ends.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public interface NonNavigable extends Navigability {
	}

}
