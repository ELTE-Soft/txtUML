package pingpong.x;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import pingpong.x.model.Player;
import pingpong.x.model.Player.Init;
import pingpong.x.model.Player.Waiting;

public class XPlayerSMDiagram extends StateMachineDiagram<Player> {

	@Row({ Init.class, Waiting.class })
	class PlayerLayout extends Layout {}
}
