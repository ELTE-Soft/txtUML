package pingpong.j.model.connectors;

import hu.elte.txtuml.api.model.Delegation;
import pingpong.j.model.Player;
import pingpong.j.model.Racket;
import pingpong.j.model.associations.PlayerOwnsRacket;

public class PlayerHitsOrMisses extends Delegation {
	public class player extends One<PlayerOwnsRacket.player, Player.ShoutPort> {
	}

	public class withRacket extends One<PlayerOwnsRacket.racket, Racket.HitOrMissPort> {
	}
}
