package tester_GUI;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Row;
import vending_machine.CashRegister;
import vending_machine.Drink;
import vending_machine.VendingMachine;

public class VendingMachineDiagram extends ClassDiagram {
	@Row({ Drink.class, VendingMachine.class, CashRegister.class })

	class myLayout extends Layout {
	}
}