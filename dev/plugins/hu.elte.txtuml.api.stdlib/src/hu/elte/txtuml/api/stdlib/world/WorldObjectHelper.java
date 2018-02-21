package hu.elte.txtuml.api.stdlib.world;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.utils.Logger;

/**
 * This is a helper class, only required for the implementation of the
 * {@link WorldObject} class.
 */
@External
class WorldObjectHelper {

	private final String id;
	private final WorldHelper worldHelper;

	/**
	 * May only be accessed when holding a monitor of this
	 * {@link WorldHelperObject} instance.
	 */
	private WorldObject object = null;

	private volatile WorldObjectListener listener = null;

	public WorldObjectHelper(String name, WorldHelper worldHelper) {
		this.id = name;
		this.worldHelper = worldHelper;
	}

	String getIdentifier() {
		return id;
	}

	/**
	 * May return {@code null}.
	 */
	WorldObject getObject() {
		synchronized (this) {
			return object;
		}
	}

	/**
	 * May only be called from a model executor thread.
	 */
	WorldObject getOrCreateObject() {
		synchronized (this) {
			if (object == null) {
				object = Action.create(WorldObject.class, worldHelper.getWorld(), this);
			}
			return object;
		}
	}

	void accept(SignalToWorld signal) {
		WorldObjectListener listener = this.listener;
		// To ensure that 'listener' is not updated between the following
		// accesses.
		if (listener == null) {
			Logger.executor.warn("A signal arrived to world object " + this.id + " in the model executor "
					+ worldHelper.getExecutor() + " but no listener is registered for this object. The signal was: "
					+ signal);
		} else {
			listener.acceptAny(signal);
		}
	}

	void setListener(WorldObjectListener listener) {
		this.listener = listener;
	}

}
