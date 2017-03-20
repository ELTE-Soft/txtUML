package pingpong.j;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.Spacing;
import pingpong.j.model.Game;
import pingpong.j.model.Player;
import pingpong.j.model.Racket;

public class PingPongClassDiagram extends ClassDiagram {

	public class Space extends Phantom{}
	
	@Above(val = Game.class, from = Space.class)
	@Row({Player.class, Space.class, Racket.class})
	@Spacing(0.5)
	public class PingPongLayout extends Layout{}
}
