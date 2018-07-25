package pingpong.x.diagrams;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import pingpong.x.model.Game;
import pingpong.x.model.Game.Init;
import pingpong.x.model.Game.Waiting;

public class XGameSMDiagram extends StateMachineDiagram<Game> {
	
	@Row({Init.class, Waiting.class})
	class GameLayout extends Layout{}
}
