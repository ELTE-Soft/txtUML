package hu.elte.txtuml.api.stdlib.world;

import java.util.concurrent.ConcurrentHashMap;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.runtime.BaseModelExecutor;

/**
 * This is a helper class, only required for the implementation of the
 * {@link World} class.
 */
@External
class WorldHelper {

	private static final Object WORLD_FEATURE_KEY = new Object();

	private final BaseModelExecutor executor;
	private final ConcurrentHashMap<String, WorldObjectHelper> objects = new ConcurrentHashMap<>();

	/**
	 * May only be accessed when holding a monitor of this {@link WorldHelper}
	 * instance.
	 */
	private World world = null;

	public WorldHelper(BaseModelExecutor executor) {
		this.executor = executor;
	}

	static WorldHelper getOrCreateHelperInstance(BaseModelExecutor forExecutor) {
		return (WorldHelper) forExecutor.getOrCreateFeature(WORLD_FEATURE_KEY, k -> new WorldHelper(forExecutor));
	}

	/**
	 * May only be called from a model executor thread.
	 */
	static World getOrCreateWorldInstance() {
		BaseModelExecutor current = BaseModelExecutor.currentExecutor();

		return getOrCreateHelperInstance(current).getOrCreateWorld();
	}

	BaseModelExecutor getExecutor() {
		return executor;
	}

	/**
	 * May return {@code null}.
	 */
	World getWorld() {
		synchronized (this) {
			return world;
		}
	}

	/**
	 * May only be called from a model executor thread.
	 */
	World getOrCreateWorld() {
		synchronized (this) {
			if (world == null) {
				world = Action.create(World.class, this);
			}
			return world;
		}
	}

	WorldObjectHelper getOrCreateObjectHelper(String id) {
		return objects.computeIfAbsent(id, s -> new WorldObjectHelper(id, this));
	}

	/**
	 * May only be called from a model executor thread.
	 */
	WorldObject getOrCreateObject(String id) {
		return getOrCreateObjectHelper(id).getOrCreateObject();
	}

}
