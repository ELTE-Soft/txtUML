package pingpong.j.model.associations;

import hu.elte.txtuml.api.model.Composition;
import pingpong.j.model.Player;
import pingpong.j.model.Racket;

public class PlayerOwnsRacket extends Composition {
	public class player extends HiddenContainer<Player> {
	}

	public class racket extends One<Racket> {
	}

}
