package txtuml.api;

@FunctionalInterface
public interface ParameterizedBlockBody<T> extends ModelElement {
	void run(T param);
}
