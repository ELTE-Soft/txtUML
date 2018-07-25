package pingpong.x.diagrams;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import pingpong.x.model.Racket;
import pingpong.x.model.Racket.Check;
import pingpong.x.model.Racket.Init;
import pingpong.x.model.Racket.Waiting;

public class XRacketSMDiagram extends StateMachineDiagram<Racket> {

	@Row({ Init.class, Waiting.class, Check.class })
	class PlayerLayout extends Layout {}
}
