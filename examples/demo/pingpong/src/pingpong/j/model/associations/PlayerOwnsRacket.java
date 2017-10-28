package pingpong.j.model.associations;

import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.api.model.One;
import pingpong.j.model.Player;
import pingpong.j.model.Racket;

public class PlayerOwnsRacket extends Composition {
	public class player extends HiddenContainerEnd<Player> {
	}

	public class racket extends End<One<Racket>> {
	}

}
