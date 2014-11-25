package txtuml.api;

@FunctionalInterface
public interface BlockBody extends ModelElement {
	void run();
}
