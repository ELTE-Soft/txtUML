package pingpong.j.model.associations;

import hu.elte.txtuml.api.model.Composition;
import pingpong.j.model.Player;
import pingpong.j.model.Game;

public class LeftPlayer extends Composition {
	public class inGame extends HiddenContainer<Game> {
	}

	public class player extends One<Player> {
	}
}
