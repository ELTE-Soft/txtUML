package pingpong.j.model.connectors;

import hu.elte.txtuml.api.model.Connector;
import pingpong.j.model.Player;
import pingpong.j.model.associations.LeftPlayer;
import pingpong.j.model.associations.RightPlayer;

public class Table extends Connector {
	public class left extends One<LeftPlayer.player, Player.BallAtPlayerPort> {
	}

	public class right extends One<RightPlayer.player, Player.BallAtPlayerPort> {
	}
}
