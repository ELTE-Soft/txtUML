package hu.elte.txtuml.api.model.assocends;

/**
 * A base interface to represent the multiplicity property of association ends.
 * Association ends might have a multiplicity of {@link One}, {@link ZeroToOne},
 * {@link ZeroToUnlimited}, {@link OneToUnlimited} or {@link MinToMax} to
 * represent custom multiplicity.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public interface Multiplicity<T extends Multiplicity<T>> {

	/**
	 * Implementing classes represent association ends with a multiplicity of
	 * 1..1.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	public interface One extends Multiplicity<One> {
	}

	/**
	 * Implementing classes represent association ends with a multiplicity of
	 * 0..1.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	public interface ZeroToOne extends Multiplicity<ZeroToOne> {
	}

	/**
	 * Implementing classes represent association ends with a multiplicity of
	 * 0..*.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	public interface ZeroToUnlimited extends Multiplicity<ZeroToUnlimited> {
	}

	/**
	 * Implementing classes represent association ends with a multiplicity of
	 * 1..*.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	public interface OneToUnlimited extends Multiplicity<OneToUnlimited> {
	}

	/**
	 * Implementing classes represent association ends which have a user-defined
	 * multiplicity with two specified bounds.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	public interface MinToMax extends Multiplicity<MinToMax> {
	}

}
