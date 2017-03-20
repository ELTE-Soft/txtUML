package pingpong.j;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import pingpong.j.model.Player;
import pingpong.j.model.Player.Init;
import pingpong.j.model.Player.Waiting;

public class PlayerSMDiagram extends StateMachineDiagram<Player> {

	@Row({ Init.class, Waiting.class })
	class PlayerLayout extends Layout {}
}
