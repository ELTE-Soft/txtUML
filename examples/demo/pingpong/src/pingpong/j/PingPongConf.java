package pingpong.j;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import pingpong.j.model.Game;
import pingpong.j.model.Player;
import pingpong.j.model.Racket;

@Group(contains = {Player.class, Game.class, Racket.class})
@Runtime(RuntimeType.SINGLE)
public class PingPongConf extends Configuration{

}
