package pingpong.j;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import pingpong.j.model.Racket;
import pingpong.j.model.Racket.Check;
import pingpong.j.model.Racket.Init;
import pingpong.j.model.Racket.Waiting;

public class RacketSMDiagram extends StateMachineDiagram<Racket> {

	@Row({ Init.class, Waiting.class, Check.class })
	class PlayerLayout extends Layout {}
}
