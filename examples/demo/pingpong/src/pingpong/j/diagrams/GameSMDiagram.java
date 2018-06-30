package pingpong.j.diagrams;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import pingpong.j.model.Game;
import pingpong.j.model.Game.Init;
import pingpong.j.model.Game.Waiting;

public class GameSMDiagram extends StateMachineDiagram<Game> {
	
	@Row({Init.class, Waiting.class})
	class GameLayout extends Layout{}
}
