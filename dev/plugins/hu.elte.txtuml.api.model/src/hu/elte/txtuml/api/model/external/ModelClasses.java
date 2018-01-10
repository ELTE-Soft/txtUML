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
	public static String getIdentifierOf(ModelClass obj) {
		return RuntimeProvider.INSTANCE.getRuntimeOf(obj).getIdentifier();
	}

	/**
	 * Returns the modifiable name of the given model class instance.
	 * <p>
	 * Thread-safe.
	 */
	public static String getNameOf(ModelClass obj) {
		return RuntimeProvider.INSTANCE.getRuntimeOf(obj).getName();
	}

	/**
	 * Sets the modifiable name of the given model class instance.
	 * <p>
	 * Thread-safe.
	 */
	public static void setNameOf(ModelClass obj, String newName) {
		RuntimeProvider.INSTANCE.getRuntimeOf(obj).setName(newName);
	}

	/**
	 * Returns the current status of a model class instance.
	 * <p>
	 * Thread-safe.
	 */
	public static ModelClass.Status getStatusOf(ModelClass obj) {
		return RuntimeProvider.INSTANCE.getRuntimeOf(obj).getStatus();
	}

}
