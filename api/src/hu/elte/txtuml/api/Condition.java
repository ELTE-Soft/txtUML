package hu.elte.txtuml.api;

@FunctionalInterface
public interface Condition extends ModelElement {
	ModelBool check();
}
