package pingpong.x;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import pingpong.x.model.Racket;
import pingpong.x.model.Racket.Init;
import pingpong.x.model.Racket.Waiting;
import pingpong.x.model.Racket.Check;

public class XRacketSMDiagram extends StateMachineDiagram<Racket> {

	@Row({ Init.class, Waiting.class, Check.class })
	class PlayerLayout extends Layout {}
}
