package pingpong.j.model.associations;

import hu.elte.txtuml.api.model.Composition;
import pingpong.j.model.Game;
import pingpong.j.model.Player;

public class RightPlayer extends Composition {
	public class inGame extends HiddenContainer<Game> {
	}

	public class player extends One<Player> {
	}
}
