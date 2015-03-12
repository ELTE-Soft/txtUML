package txtuml.api.backend.collections;

import txtuml.api.ModelClass;
import txtuml.api.backend.collections.impl.JavaCollectionOfManyImpl;

public interface JavaCollectionOfMany<T extends ModelClass> extends
		java.util.Collection<T> {

	static <T extends ModelClass> JavaCollectionOfMany<T> create() {
		return new JavaCollectionOfManyImpl<>();
	}

}
