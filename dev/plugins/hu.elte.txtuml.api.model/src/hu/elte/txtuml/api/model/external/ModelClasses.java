package hu.elte.txtuml.api.model.external;

import hu.elte.txtuml.api.model.ModelClass;

/**
 * A helper class that provides runtime utilities and information about model
 * classes.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public final class ModelClasses {

	private ModelClasses() {
	}

	/**
	 * Returns the unique identifier of the given model class instance. This
	 * identifier is generated upon the creation of the object and is guaranteed
	 * to be unique in a single model execution.
	 * <p>
	 * Thread-safe.
	 */
	public static String getIdentifierOf(ModelClass cls) {
		return RuntimeProvider.INSTANCE.getRuntimeOf(cls).getIdentifier();
	}

	/**
	 * Returns the name of the given model class instance. This name can be set
	 * upon the creation of an object with the appropriate
	 * {@link hu.elte.txtuml.api.model.Action Action} methods.
	 * <p>
	 * Thread-safe.
	 */
	public static String getNameOf(ModelClass cls) {
		return RuntimeProvider.INSTANCE.getRuntimeOf(cls).getName();
	}

	/**
	 * Returns the current status of a model class instance.
	 * <p>
	 * Thread-safe.
	 */
	public static ModelClass.Status getStatusOf(ModelClass cls) {
		return RuntimeProvider.INSTANCE.getRuntimeOf(cls).getStatus();
	}

}
