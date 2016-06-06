package tester;

import java.io.IOException;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import vending_machine.CashRegister;
import vending_machine.Drink;
import vending_machine.Serve;
import vending_machine.VendingMachine;
import vending_machine.WorkTogether;
import vending_machine.drinkChosen;
import vending_machine.giveBackCash;
import vending_machine.insertCash;

public class tester {

	void test() {
		ModelExecutor.create().setTraceLogging(true).run(() -> {
			Drink cola = Action.create(Drink.class, 260, 5, "Highway Cola");
			Drink aqua = Action.create(Drink.class, 180, 1, "Fresh Aqua");
			Drink fantom = Action.create(Drink.class, 260, 2, "Fantom Orange");
			Drink stripe = Action.create(Drink.class, 230, 4, "Stripe");
			Drink proba = Action.create(Drink.class, 222, 1, "asd");
			CashRegister cr = Action.create(CashRegister.class);
			VendingMachine vm = Action.create(VendingMachine.class);

			Action.link(Serve.drinks.class, cola, Serve.theMachine.class, vm);
			Action.link(Serve.drinks.class, aqua, Serve.theMachine.class, vm);
			Action.link(Serve.drinks.class, fantom, Serve.theMachine.class, vm);
			Action.link(Serve.drinks.class, stripe, Serve.theMachine.class, vm);
			Action.link(Serve.drinks.class, proba, Serve.theMachine.class, vm);
			Action.link(WorkTogether.theCashRegister.class, cr, WorkTogether.theMachine.class, vm);

			Action.start(cola);
			Action.start(aqua);
			Action.start(fantom);
			Action.start(stripe);
			Action.start(proba);
			Action.start(cr);
			Action.start(vm);

			try {
				char c = (char) System.in.read();
				while (c != 'q') {
					switch (c) {
					case 'c':
						Action.send(new drinkChosen(cola.getName()), vm);
						break;
					case 'a':
						Action.send(new drinkChosen(aqua.getName()), vm);
						break;
					case 'f':
						Action.send(new drinkChosen(fantom.getName()), vm);
						break;
					case 's':
						Action.send(new drinkChosen(stripe.getName()), vm);
						break;
					case 'd':
						Action.send(new drinkChosen(proba.getName()), vm);
						break;
					case 'r':
						Action.send(new giveBackCash(cr.howMuchIsInside()), cr);
						break;
					case '1':
						Action.send(new insertCash(100), cr);
						break;
					case '2':
						Action.send(new insertCash(20), cr);
						break;
					}

					c = (char) System.in.read();
				}
			}

			catch (IOException e) {
				System.out.print("Hiba t�rt�nt a beolvas�s k�zben, a program kil�p");
			}

			Action.unlink(Serve.drinks.class, cola, Serve.theMachine.class, vm);
			Action.unlink(Serve.drinks.class, aqua, Serve.theMachine.class, vm);
			Action.unlink(Serve.drinks.class, fantom, Serve.theMachine.class, vm);
			Action.unlink(Serve.drinks.class, stripe, Serve.theMachine.class, vm);
			Action.unlink(WorkTogether.theCashRegister.class, cr, WorkTogether.theMachine.class, vm);

			Action.delete(cr);
			Action.delete(cola);
			Action.delete(fantom);
			Action.delete(aqua);
			Action.delete(stripe);
		});
	}

	public static void main(String[] args) {
		new tester().test();
	}
}