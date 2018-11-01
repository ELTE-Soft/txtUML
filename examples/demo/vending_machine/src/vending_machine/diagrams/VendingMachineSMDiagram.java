package vending_machine.diagrams;

import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import vending_machine.model.VendingMachine;
import vending_machine.model.VendingMachine.Idle;
import vending_machine.model.VendingMachine.Init;
import vending_machine.model.VendingMachine.IsMoneyInside;
import vending_machine.model.VendingMachine.ShowingPrice;
import vending_machine.model.VendingMachine.Vending;

public class VendingMachineSMDiagram extends StateMachineDiagram<VendingMachine> {
	
	@North(from=Idle.class, val=Init.class)
	@Row({Idle.class, ShowingPrice.class })
	@North(from=Vending.class, val=Idle.class)
	@Row({ Vending.class, IsMoneyInside.class })
	class MyLayout extends Layout {}
}
