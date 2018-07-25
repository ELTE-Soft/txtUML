package pingpong.x.diagrams;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.Spacing;
import pingpong.x.model.Game;
import pingpong.x.model.Player;
import pingpong.x.model.Racket;

public class XPingPongClassDiagram extends ClassDiagram {

	public class Space extends Phantom{}
	
	@Above(val = Game.class, from = Space.class)
	@Row({Player.class, Space.class, Racket.class})
	@Spacing(0.5)
	public class PingPongLayout extends Layout{}
}
