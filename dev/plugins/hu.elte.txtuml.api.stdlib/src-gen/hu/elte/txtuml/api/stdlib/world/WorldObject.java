package hu.elte.txtuml.api.stdlib.world;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.stdlib.world.SignalToWorld;
import hu.elte.txtuml.api.stdlib.world.World;
import hu.elte.txtuml.api.stdlib.world.WorldObjectHelper;
import hu.elte.txtuml.api.stdlib.world.WorldOwnsObject;

/**
 * Represents an active entity outside the model that can accept certain signals.
 * Belongs to a {@link World}, more precisely, to the only existing {@link World} instance in
 * the
 * Each world object is identified by a {@code String} identifier which is unique
 * in a model execution.
 * <p>
 * A world object is automatically started upon creation. Do <b>not</b> delete them. They are
 * not deleted by the <i>txtUML World</i> model either, they exist until the end of
 * the current model execution even if the represented outside entity does not exist anymore.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api.stdlib.world}
 * package or further details and examples about the services provided by
 * the <i>txtUML World</i> model.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("all")
public class WorldObject extends ModelClass {
  @External
  WorldObjectHelper helper;
  
  /**
   * This class cannot be instantiated directly.
   */
  @ExternalBody
  private WorldObject() {
  }
  
  @External
  WorldObject(final World world, final WorldObjectHelper helper) {
    this.helper = helper;
    Action.<World, WorldObject>link(hu.elte.txtuml.api.stdlib.world.WorldOwnsObject.world.class, world, WorldOwnsObject.objects.class, this);
    Action.start(this);
  }
  
  public class init extends StateMachine.Initial {
  }
  
  @From(WorldObject.init.class)
  @To(WorldObject.waiting.class)
  public class initialize extends StateMachine.Transition {
  }
  
  public class waiting extends StateMachine.State {
  }
  
  @From(WorldObject.waiting.class)
  @To(WorldObject.waiting.class)
  @Trigger(SignalToWorld.class)
  public class acceptSignal extends StateMachine.Transition {
    @Override
    public void effect() {
      WorldObject.this.accept(getTrigger(SignalToWorld.class));
    }
  }
  
  /**
   * Returns the identifier of this world object which is unique in a model execution.
   */
  @ExternalBody
  public String getIdentifier() {
    return this.helper.getIdentifier();
  }
  
  /**
   * Handles the specified signal.
   */
  @ExternalBody
  private void accept(final SignalToWorld sig) {
    this.helper.accept(sig);
  }
}
