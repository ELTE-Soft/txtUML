package hu.elte.txtuml.api.model.backend.collections;

import hu.elte.txtuml.api.model.backend.collections.impl.JavaCollectionOfManyImpl;

/**
 * A Java collection for implementations of txtUML collections having a
 * multiplicity of <i>'many'</i> (0..*).
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
public interface JavaCollectionOfMany<T> extends java.util.Collection<T> {

	/**
	 * Creates a new <code>JavaCollectionOfMany</code> instance.
	 * 
	 * @param <T>
	 *            the type of model objects to be contained in this collection
	 * @return the new instance
	 */
	static <T> JavaCollectionOfMany<T> create() {
		return new JavaCollectionOfManyImpl<>();
	}

}
