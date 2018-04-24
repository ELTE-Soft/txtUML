package pingpong.x.cpp;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import pingpong.x.model.Game;
import pingpong.x.model.Player;
import pingpong.x.model.Racket;

@Group(contains = { Player.class, Game.class, Racket.class })
@Runtime(RuntimeType.SINGLE)
public class XPingPongConfiguration extends Configuration {

}
