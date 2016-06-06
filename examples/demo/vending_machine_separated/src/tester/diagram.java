package tester;


import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Row;
import vending_machine.CashRegister;
import vending_machine.Drink;
import vending_machine.VendingMachine;

class diagram extends ClassDiagram {
	@Row({Drink.class, VendingMachine.class, CashRegister.class})
	
	class myLayout extends Layout {}
}