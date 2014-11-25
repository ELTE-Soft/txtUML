package txtuml.api;

@FunctionalInterface
public interface Condition extends ModelElement {
	ModelBool check();
}
