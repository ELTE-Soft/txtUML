package hu.elte.txtuml.api.model;

/**
 * Functional interface that might be used to pass method references of Java
 * methods representing receptions.
 * 
 * <p>
 * <b>Represents:</b> reception
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <p>
 * Should only be instantiated through method references of certain interface
 * methods representing receptions.
 * <p>
 * See {@link Action#send(Signal, Reception)}.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
@FunctionalInterface
public interface Reception<S extends Signal> {
	void accept(S signal);
}
