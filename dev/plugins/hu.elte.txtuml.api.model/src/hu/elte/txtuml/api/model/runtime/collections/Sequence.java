package hu.elte.txtuml.api.model.runtime.collections;

import java.util.ArrayList;

import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;

/**
 * An immutable ordered collection which may contain any number of elements
 * (including zero).
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 *
 * @param <T>
 *            the type of the contained elements
 */
public class Sequence<T> extends @External AbstractCollection<T, ArrayList<T>> implements Collection<T> {

	@External
	static <T> Builder<T, Sequence<T>> builder() {
		return Builder.create(ArrayList::new, Sequence<T>::new);
	}

	/**
	 * Creates an empty {@code Sequence}.
	 */
	@ExternalBody
	public static <T> Sequence<T> empty() {
		return new Sequence<>();
	}

	/**
	 * Creates a {@code Sequence} which will contain the elements of the given
	 * collection.
	 */
	@ExternalBody
	public static <T> Sequence<T> of(Collection<T> elements) {
		return Sequence.<T> builder().addAll(elements).build();
	}

	@ExternalBody
	private Sequence() {
		super(new ArrayList<>());
	}

	@ExternalBody
	private Sequence(ArrayList<T> backend) {
		super(backend);
	}

	@Override
	@ExternalBody
	protected Builder<T, Sequence<T>> createBuilder() {
		return builder();
	}

}
