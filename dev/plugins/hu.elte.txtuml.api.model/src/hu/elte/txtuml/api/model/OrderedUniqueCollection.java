package hu.elte.txtuml.api.model;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;

import hu.elte.txtuml.api.model.GeneralCollection.Ordered;
import hu.elte.txtuml.api.model.GeneralCollection.Unique;

/**
 * A base class for ordered, unique JtxtUML API collections in the model. Should
 * not be used in the model (only its subtypes) as this type does not specify
 * the multiplicity of the actual collection instance.
 * 
 * <p>
 * <b>Represents:</b> ordered, unique collection
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * This class is only used to create new collections which are ordered and
 * unique. Create a subtype with no fields, methods, constructors or nested
 * classes. Apply the {@link Min} and {@link Max} annotations to it to set the
 * lower and upper bound of the collection. If {@code Min} is omitted, the lower
 * bound will be 0, if {@code Max} is, the upper bound will be positive
 * infinity.
 * <p>
 * When defining the subtype, there are two main options: The new collection may
 * either contain any type, further specified upon usage, or only a specific
 * type. See the examples below on how to define these two kinds of collections.
 * <p>
 * The second type parameter of this type must be explicitly set to the newly
 * created subtype in order to let the inherited methods work properly.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li>must be a top level class (not a nested or local class)</li>
 * </ul>
 * <p>
 * <b>Subtype restrictions:</b>
 * <ul>
 * <li><i>Be abstract:</i> disallowed</li>
 * <li><i>Generic parameters:</i> disallowed</li>
 * <li><i>Constructors:</i> disallowed</li>
 * <li><i>Initialization blocks:</i> disallowed</li>
 * <li><i>Fields:</i> disallowed</li>
 * <li><i>Methods:</i> disallowed</li>
 * <li><i>Nested interfaces:</i> disallowed</li>
 * <li><i>Nested classes:</i> disallowed</li>
 * <li><i>Nested enums:</i> disallowed</li>
 * </ul>
 * </li>
 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>
 * 	{@literal @Min(2) @Max(4)}
 * 	{@literal class OrderedUniqueTwoToFour<T> extends OrderedUniqueCollection<T, OrderedUniqueTwoToFour<T>>} {
 * 	}
 * 
 * 	{@literal @Min(3) @Max(3)}
 * 	{@literal class OrderedUniqueThreeCars extends OrderedUniqueCollection<Car, OrderedUniqueThreeCars>} {
 * 	}
 * </code>
 * </pre>
 * 
 * In action code:
 * 
 * <pre>
 * <code>
 * 	{@literal OrderedUniqueTwoToFour<String> strings = Action.collectIn(OrderedUniqueTwoToFour.class, "A", "B");}
 * 	{@literal OrderedUniqueThreeCars cars = Action.collectIn(OrderedUniqueThreeCars.class, car1, car2, car3);}
 * </code>
 * </pre>
 * 
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 * 
 * @param <E>
 *            the elements contained in this collection
 * @param <C>
 *            the implementing subtype
 */
public abstract class OrderedUniqueCollection<E, C extends OrderedUniqueCollection<E, C>>
		extends AbstractOrderedCollection<E, C> implements @External Ordered<E>, @External Unique<E> {

	@ExternalBody
	protected OrderedUniqueCollection() {
	}

	/**
	 * Must be used with extreme care as this constructor sets the backend of
	 * this collection without any multiplicity checks.
	 */
	OrderedUniqueCollection(ImmutableList<E> backend) {
		super(backend);
	}

	@ExternalBody
	@Override
	@SuppressWarnings("unchecked")
	public final <C2 extends GeneralCollection<? super E>, C3 extends GeneralCollection<?>> C2 as(
			Class<C3> collectionType) {
		return (C2) asUnsafe(collectionType);
	}

	@ExternalBody
	@Override
	public final OrderedUniqueAny<E> unbound() {
		return new OrderedUniqueAny<>(getBackend());
	}

	@External
	@Override
	public final boolean isOrdered() { // to become final
		return Ordered.super.isOrdered();
	}

	@External
	@Override
	public final boolean isUnique() { // to become final
		return Unique.super.isUnique();
	}

	@Override
	ImmutableList<E> createBackend(Consumer<Builder<E>> backendBuilder) {
		ImmutableList.Builder<E> builder = ImmutableList.builder();
		Set<E> tmp = new HashSet<>();
		backendBuilder.accept(e -> {
			if (!tmp.contains(e)) {
				tmp.add(e);
				builder.add(e);
			}
		});
		return builder.build();
	}

	@ExternalBody
	@Override
	public final String toString() {
		return "<" + super.toString() + ">";
	}

}
