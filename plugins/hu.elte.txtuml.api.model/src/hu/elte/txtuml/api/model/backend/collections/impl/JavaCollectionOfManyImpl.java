package hu.elte.txtuml.api.model.backend.collections.impl;

import hu.elte.txtuml.api.model.backend.collections.JavaCollectionOfMany;

import java.util.ArrayList;

/**
 * Default implementation for {@link JavaCollectionOfMany}.
 * <p>
 * Despite being a subclass of the {@link java.io.Serializable} interface
 * through {@link ArrayList}, this class does not provide a
 * <code>serialVersionUID</code> because serialization is never used on it.
 * 
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
@SuppressWarnings("serial")
public class JavaCollectionOfManyImpl<T> extends
		ArrayList<T> implements JavaCollectionOfMany<T> {
}
