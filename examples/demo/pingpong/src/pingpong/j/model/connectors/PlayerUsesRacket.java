package pingpong.j.model.connectors;

import hu.elte.txtuml.api.model.Delegation;
import pingpong.j.model.Player;
import pingpong.j.model.Racket;
import pingpong.j.model.associations.PlayerOwnsRacket;

public class PlayerUsesRacket extends Delegation {
	public class player extends One<PlayerOwnsRacket.player, Player.BallAtPlayerPort> {
	}

	public class racket extends One<PlayerOwnsRacket.racket, Racket.BallAtRacketPort> {
	}
}
