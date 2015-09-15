package hu.elte.txtuml.api.model.assocends;

/**
 * A base interface to represent the multiplicity property of association ends.
 * Association ends might have a multiplicity of {@link One}, {@link ZeroToOne},
 * {@link ZeroToUnlimited} or {@link OneToUnlimited}.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an overview on
 * modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public interface Multiplicity {

	/**
	 * Implementing classes of this interface represent association ends in the
	 * model which have a multiplicity of 1.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an overview
	 * on modeling in txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public interface One extends Multiplicity {
	}

	/**
	 * Implementing classes of this interface represent association ends in the
	 * model which have a multiplicity of 0..1.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an overview
	 * on modeling in txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public interface ZeroToOne extends Multiplicity {
	}

	/**
	 * Implementing classes of this interface represent association ends in the
	 * model which have a multiplicity of 0..*.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an overview
	 * on modeling in txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public interface ZeroToUnlimited extends Multiplicity {
	}

	/**
	 * Implementing classes of this interface represent association ends in the
	 * model which have a multiplicity of 1..*.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an overview
	 * on modeling in txtUML.
	 *
	 * @author Gabor Ferenc Kovacs
	 *
	 */
	public interface OneToUnlimited extends Multiplicity {
	}

}
