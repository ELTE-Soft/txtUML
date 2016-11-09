package hu.elte.txtuml.api.stdlib.mcollections;

import java.util.HashSet;

/**
 * A <b>mutable</b> set (unique unordered collection) which may contain any
 * number of elements (including zero).
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 *
 * @param <T>
 *            the type of the contained elements
 */
public interface MSet<T> extends MCollection<T> {

	/**
	 * Creates an empty {@code MSet}.
	 */
	static <T> MSet<T> empty() {
		return new MSetImpl<>();
	}

	/**
	 * Creates a {@code MSet} which will contain the given elements.
	 */
	@SafeVarargs
	static <T> MSet<T> of(T... elements) {
		MSet<T> set = empty();
		for (T e : elements) {
			set.add(e);
		}
		return set;
	}

	/**
	 * Creates a {@code MSet} which will contain the elements of the given
	 * iterable.
	 */
	static <T> MSet<T> copyOf(Iterable<T> elements) {
		MSet<T> set = empty();
		elements.forEach(set::add);
		return set;
	}

}

class MSetImpl<T> extends AbstractMCollection<T, HashSet<T>> implements MSet<T> {

	MSetImpl() {
		super(new HashSet<T>());
	}

}
