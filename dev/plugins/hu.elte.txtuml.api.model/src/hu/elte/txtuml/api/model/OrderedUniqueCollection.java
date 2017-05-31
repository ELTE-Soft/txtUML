package hu.elte.txtuml.api.model;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;

import hu.elte.txtuml.api.model.GeneralCollection.Ordered;
import hu.elte.txtuml.api.model.GeneralCollection.Unique;

//TODO document
public abstract class OrderedUniqueCollection<E, C extends OrderedUniqueCollection<E, C>> extends AbstractOrderedCollection<E, C>
		implements Ordered<E>, Unique<E> {

	protected OrderedUniqueCollection() {
	}

	@Override
	public <C2 extends GeneralCollection<E>> C2 as(Class<C2> collectionType) {
		return asUnsafe(collectionType);
	}

	@Override
	public final GeneralCollection<E> unbound() {
		return asOrderedUniqueAnyUnsafe();
	}

	@Override
	java.util.List<E> createBackend(Consumer<Builder<E>> backendBuilder) {
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

}
