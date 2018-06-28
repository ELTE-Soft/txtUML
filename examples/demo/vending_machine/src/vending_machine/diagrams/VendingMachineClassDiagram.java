package vending_machine.diagrams;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Show;
import vending_machine.model.CashRegister;
import vending_machine.model.Drink;
import vending_machine.model.VendingMachine;

public class VendingMachineClassDiagram extends ClassDiagram {

	@Show({ Drink.class, VendingMachine.class, CashRegister.class })
	class MyLayout extends Layout {
	}

}
