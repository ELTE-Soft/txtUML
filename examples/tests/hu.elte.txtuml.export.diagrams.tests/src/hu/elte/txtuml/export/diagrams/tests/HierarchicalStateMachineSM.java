package hu.elte.txtuml.export.diagrams.tests;

import hu.elte.txtuml.api.layout.Inside;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.Spacing;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.export.diagrams.tests.model.HierarchicalStateMachine;
import hu.elte.txtuml.export.diagrams.tests.model.HierarchicalStateMachine.CStateB;
import hu.elte.txtuml.export.diagrams.tests.model.HierarchicalStateMachine.CStateB.CStateBC;
import hu.elte.txtuml.export.diagrams.tests.model.HierarchicalStateMachine.CStateB.CStateBC.InitBC;
import hu.elte.txtuml.export.diagrams.tests.model.HierarchicalStateMachine.CStateB.CStateBC.StateBCA;
import hu.elte.txtuml.export.diagrams.tests.model.HierarchicalStateMachine.CStateB.CStateBC.StateBCB;
import hu.elte.txtuml.export.diagrams.tests.model.HierarchicalStateMachine.CStateB.CStateBC.StateBCC;
import hu.elte.txtuml.export.diagrams.tests.model.HierarchicalStateMachine.CStateB.InitB;
import hu.elte.txtuml.export.diagrams.tests.model.HierarchicalStateMachine.CStateB.StateBA;
import hu.elte.txtuml.export.diagrams.tests.model.HierarchicalStateMachine.CStateB.StateBB;
import hu.elte.txtuml.export.diagrams.tests.model.HierarchicalStateMachine.Init;
import hu.elte.txtuml.export.diagrams.tests.model.HierarchicalStateMachine.StateA;
import hu.elte.txtuml.export.diagrams.tests.model.HierarchicalStateMachine.StateC;

public class HierarchicalStateMachineSM extends StateMachineDiagram<HierarchicalStateMachine>{
	
	@Row({Init.class, StateA.class })
	@North(from=CStateB.class, val=StateA.class)
	@Row({StateC.class, CStateB.class })
	@Spacing(0.1)
	class L extends Layout{}
	
	
	@Inside(CStateB.class)
	class CompositeStateB extends Box{
		
		@Row({InitB.class, StateBA.class })
		@North(from=StateBB.class, val=StateBA.class)
		@Row({CStateBC.class, StateBB.class })
		@Spacing(0.3)
		class L extends Layout{}
		
		
		@Inside(CStateBC.class)
		class CompositeStateBC extends Box{
			
			@Row({InitBC.class, StateBCA.class })
			@North(from=StateBCB.class, val=StateBCA.class)
			@Row({StateBCC.class, StateBCB.class })
			class L extends Layout{}
		}
	}
}
