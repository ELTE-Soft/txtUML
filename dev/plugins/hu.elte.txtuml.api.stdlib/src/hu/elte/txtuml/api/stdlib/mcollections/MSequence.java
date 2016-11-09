package hu.elte.txtuml.api.stdlib.mcollections;

import java.util.ArrayList;

/**
 * A <b>mutable</b> sequence (non-unique ordered collection) which may contain
 * any number of elements (including zero).
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 *
 * @param <T>
 *            the type of the contained elements
 */
public interface MSequence<T> extends MCollection<T> {

	/**
	 * Returns the element of this mutable sequence at a certain index.
	 * 
	 * @param index
	 *            the index of the required element
	 * @return the element at the given index
	 */
	T at(int index);

	/**
	 * Adds a new element to this mutable sequence at the given index.
	 * 
	 * @param index
	 *            the index where the new element is to be placed; the elements
	 *            at this index and subsequent elements are shifted
	 * @param element
	 *            the element to add
	 */
	void add(int index, T element);

	/**
	 * Replaces the element at the specified position in this mutable sequence
	 * with the specified element.
	 * 
	 * @param index
	 *            the index of the element to be replaced
	 * @param element
	 *            the new element
	 * @return the element previously at the specified position
	 */
	T set(int index, T element);

	/**
	 * Creates an empty {@code MSequence}.
	 */
	static <T> MSequence<T> empty() {
		return new MSequenceImpl<>();
	}

	/**
	 * Creates a {@code MSequence} which will contain the given elements.
	 */
	@SafeVarargs
	static <T> MSequence<T> of(T... elements) {
		MSequence<T> seq = empty();
		for (T e : elements) {
			seq.add(e);
		}
		return seq;
	}

	/**
	 * Creates a {@code MSequence} which will contain the elements of the given
	 * iterable.
	 */
	static <T> MSequence<T> copyOf(Iterable<T> elements) {
		MSequence<T> seq = empty();
		for (T e : elements) {
			seq.add(e);
		}
		return seq;
	}

}

class MSequenceImpl<T> extends AbstractMCollection<T, ArrayList<T>> implements MSequence<T> {

	MSequenceImpl() {
		super(new ArrayList<T>());
	}

	@Override
	public T at(int index) {
		return backend.get(index);
	}

	@Override
	public void add(int index, T element) {
		backend.add(index, element);
	}

	@Override
	public T set(int index, T element) {
		return backend.set(index, element);
	}

}
