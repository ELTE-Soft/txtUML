package hu.elte.txtuml.api;

@FunctionalInterface
public interface BlockBody extends ModelElement {
	void run();
}
