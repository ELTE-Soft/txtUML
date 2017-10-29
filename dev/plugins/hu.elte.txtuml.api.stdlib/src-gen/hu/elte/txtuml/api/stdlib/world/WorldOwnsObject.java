package hu.elte.txtuml.api.stdlib.world;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.api.stdlib.world.World;
import hu.elte.txtuml.api.stdlib.world.WorldObject;

/**
 * Represent the composite relation between the {@link World} instance in the current
 * model execution and the {@link WorldObject}s. When a new world object is created, it
 * is automatically linked to the world.
 * <p>
 * The {@link WorldOwnsObject.objects} end of this composition is hidden,
 * use the {@link World#getObject} method instead.
 * <p>
 * Note that despite this composition is automatically linked between the world
 * and newly created world objects, it is never used by the <i>txtUML World</i> model. This means
 * that linking and unlinking this composition has no effect on the services of the
 * <i>txtUML World</i> model.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api.stdlib.world}
 * package or further details and examples about the services provided by
 * the <i>txtUML World</i> model.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("all")
public class WorldOwnsObject extends Composition {
  public class world extends Composition.ContainerEnd<World> {
  }
  
  public class objects extends Association.HiddenEnd<Any<WorldObject>> {
  }
}
