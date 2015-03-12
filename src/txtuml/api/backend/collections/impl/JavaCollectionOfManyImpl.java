package txtuml.api.backend.collections.impl;

import java.util.LinkedList;

import txtuml.api.ModelClass;
import txtuml.api.backend.collections.JavaCollectionOfMany;

@SuppressWarnings("serial")
public class JavaCollectionOfManyImpl<T extends ModelClass> extends
		LinkedList<T> implements JavaCollectionOfMany<T> {
}
