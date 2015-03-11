package hu.elte.txtuml.api.backend.collections.impl;

import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.backend.collections.JavaCollectionOfMany;

import java.util.LinkedList;

@SuppressWarnings("serial")
public class JavaCollectionOfManyImpl<T extends ModelClass> extends
		LinkedList<T> implements JavaCollectionOfMany<T> {
}
