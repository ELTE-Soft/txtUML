package vending_machine;

import hu.elte.txtuml.api.layout.Show;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import vending_machine.model.CashRegister;
import vending_machine.model.CashRegister.Init;
import vending_machine.model.CashRegister.Returning;
import vending_machine.model.CashRegister.Taking;

public class CashRegisterSMDiagram extends StateMachineDiagram<CashRegister> {

	@Show({ Init.class, Taking.class, Returning.class })
	class MyLayout extends Layout {
	}

}
