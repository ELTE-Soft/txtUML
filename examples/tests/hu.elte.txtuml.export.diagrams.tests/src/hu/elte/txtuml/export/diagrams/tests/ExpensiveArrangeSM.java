package hu.elte.txtuml.export.diagrams.tests;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Inside;
import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.api.layout.TopMost;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.Init;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S1;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S2;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S3;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S4;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S4.InitS4;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S4.S41;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S4.S42;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S4.S43;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S4.S44;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S4.S44.InitS44;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S4.S44.S441;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S4.S44.S442;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S4.S44.S443;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S4.S44.S444;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S4.S45;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S5;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S6;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S6.InitS6;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S6.S61;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S6.S62;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S6.S63;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S6.S64;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S6.S65;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S6.S66;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S6.S66.InitS66;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S6.S66.S661;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S6.S66.S662;
import hu.elte.txtuml.export.diagrams.tests.model.ExpensiveArrange.S7;

public class ExpensiveArrangeSM extends StateMachineDiagram<ExpensiveArrange> {

	@Row({ Init.class, S1.class, S2.class, S3.class, S4.class })
	@North(from = S5.class, val = S1.class)
	@Row({ S5.class, S6.class, S7.class })
	class L extends Layout {
	}

	@Inside(S4.class)
	class CompositeStateS4 extends Box {

		@Right(from = InitS4.class, val = S41.class)
		@Above(from = S41.class, val = S42.class)
		@Right(from = S42.class, val = S43.class)
		@Above(from = S43.class, val = S44.class)
		@Right(from = S44.class, val = S45.class)
		class L extends Layout {
		}

		@Inside(S44.class)
		class CompositeStateS44 extends Box {

			@TopMost(InitS44.class)
			@Above(from = S441.class, val = InitS44.class)
			// @Diamond(top=S441.class, left=S442.class, right=S443.class,
			// bottom=S444.class)
			@Row({ S441.class, S442.class, S443.class, S444.class })
			class L extends Layout {
			}
		}
	}

	@Inside(S6.class)
	class CompositeStateS6 extends Box {

		@Left(from = S61.class, val = InitS6.class)
		@Row({ S61.class, S62.class })
		@Row({ S63.class, S64.class })
		@Row({ S65.class, S66.class })
		@Column({ S61.class, S63.class, S65.class })
		@Column({ S62.class, S64.class, S66.class })
		class L extends Layout {
		}

		@Inside(S66.class)
		class CompositeStateS66 extends Box {

			@Row({ InitS66.class, S661.class, S662.class })
			class L extends Layout {
			}
		}
	}

}
