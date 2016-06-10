package hu.elte.txtuml.api.model.assocends;

import hu.elte.txtuml.api.model.Max;
import hu.elte.txtuml.api.model.Min;

/**
 * A base interface to represent the multiplicity property of association ends.
 * Association ends might have a multiplicity of {@link One}, {@link ZeroToOne},
 * {@link ZeroToUnlimited}, {@link OneToUnlimited} or {@link MinToMax} to
 * represent custom multiplicity.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public interface Multiplicity<T extends Multiplicity<T>> extends Bounds {

	/**
	 * Implementing classes represent association ends with a multiplicity of
	 * 1..1.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	public interface One extends Multiplicity<One> {
		@Override
		default int lowerBound() {
			return 1;
		}

		@Override
		default int upperBound() {
			return 1;
		}
	}

	/**
	 * Implementing classes represent association ends with a multiplicity of
	 * 0..1.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	public interface ZeroToOne extends Multiplicity<ZeroToOne> {
		@Override
		default int lowerBound() {
			return 0;
		}

		@Override
		default int upperBound() {
			return 1;
		}

		@Override
		default boolean checkLowerBound(int actualSize) {
			return true;
		}
	}

	/**
	 * Implementing classes represent association ends with a multiplicity of
	 * 0..*.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	public interface ZeroToUnlimited extends Multiplicity<ZeroToUnlimited> {
		@Override
		default int lowerBound() {
			return 0;
		}

		@Override
		default int upperBound() {
			return -1;
		}

		@Override
		default boolean checkLowerBound(int actualSize) {
			return true;
		}

		@Override
		default boolean checkUpperBound(int actualSize) {
			return true;
		}
	}

	/**
	 * Implementing classes represent association ends with a multiplicity of
	 * 1..*.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	public interface OneToUnlimited extends Multiplicity<OneToUnlimited> {
		@Override
		default int lowerBound() {
			return 1;
		}

		@Override
		default int upperBound() {
			return -1;
		}

		@Override
		default boolean checkUpperBound(int actualSize) {
			return true;
		}
	}

	/**
	 * Implementing classes represent association ends which have a user-defined
	 * multiplicity with two specified bounds.
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
	 * overview on modeling in JtxtUML.
	 */
	public interface MinToMax extends Multiplicity<MinToMax> {
		@Override
		default int lowerBound() {
			Min min = getClass().getAnnotation(Min.class);
			return min == null ? 0 : min.value();
		}

		@Override
		default int upperBound() {
			Max max = getClass().getAnnotation(Max.class);
			return max == null ? -1 : max.value();
		}
	}

}
