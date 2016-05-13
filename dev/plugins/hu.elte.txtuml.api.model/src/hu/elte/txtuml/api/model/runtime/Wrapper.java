package hu.elte.txtuml.api.model.runtime;

/**
 * An immutable wrapper of an object which can be used as a base interface for
 * other wrappers which may extend the wrapped object with additional
 * capabilities.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public interface Wrapper<W> {

	/**
	 * Gets the wrapped object.
	 */
	W getWrapped();

	/**
	 * A shorthand operation for {@link #getWrapped()}. {@link Object#getClass()
	 * getClass()}.
	 */
	default Class<?> getTypeOfWrapped() {
		return getWrapped().getClass();
	}

	/**
	 * A wrapper should have a custom string representation based on the wrapped
	 * element.
	 */
	@Override
	String toString();

}
