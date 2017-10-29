package hu.elte.txtuml.api.stdlib.world;

import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.stdlib.world.WorldHelper;
import hu.elte.txtuml.api.stdlib.world.WorldObject;

/**
 * This class represents the outside world in a txtUML model. It can be viewed as a singleton as
 * there is at most one instance of this class for every running model execution.
 * <p>
 * A {@link World} consists of {@link WorldObject}s, which represent active entities
 * outside the model that can accept certain signals.
 * Each world object is identified by a {@code String} identifier
 * which is unique in a model execution.
 * <p>
 * To get a world object by its identifier, use the {@link #get} method of this class.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api.stdlib.world}
 * package or further details and examples about the services provided by
 * the <i>txtUML World</i> model.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("all")
public class World extends ModelClass {
  /**
   * Required for implementation purposes.
   */
  @External
  WorldHelper helper;
  
  /**
   * This class cannot be instantiated directly.
   */
  @ExternalBody
  private World() {
  }
  
  @External
  World(final WorldHelper helper) {
    this.helper = helper;
  }
  
  /**
   * Returns the single {@link WorldObject} instance identified by the specified name
   * in this model execution. If such a {@link WorldObject} does not exist, it is created
   * upon the call of this method.
   * <p>
   * <i>Note:</i> a shorthand operation for {@link getInstance()}.{@link getObject}(name).
   */
  public static WorldObject get(final String name) {
    World _instance = World.getInstance();
    return _instance.getObject(name);
  }
  
  /**
   * Returns the single {@link World} instance in this model execution.
   * If it does not exist, it is created upon the call of this method.
   */
  @ExternalBody
  public static World getInstance() {
    return WorldHelper.getOrCreateWorldInstance();
  }
  
  /**
   * Returns the single {@link WorldObject} instance identified by the specified name
   * in this model execution. If such a {@link WorldObject} does not exist, it is created
   * upon the call of this method.
   */
  @ExternalBody
  public WorldObject getObject(final String name) {
    return this.helper.getOrCreateObject(name);
  }
}
