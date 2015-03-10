package hu.elte.txtuml.api;

@FunctionalInterface
public interface ParameterizedCondition<T> extends ModelElement {
	ModelBool check(T selected);
}