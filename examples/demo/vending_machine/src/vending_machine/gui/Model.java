package vending_machine.gui;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;
import vending_machine.model.CashRegister;
import vending_machine.model.Drink;
import vending_machine.model.DrinkChosen;
import vending_machine.model.DrinkCollection;
import vending_machine.model.GiveBackCash;
import vending_machine.model.InsertCoin;
import vending_machine.model.Refill;
import vending_machine.model.Serve;
import vending_machine.model.VendingMachine;
import vending_machine.model.WorkTogether;

public class Model implements Execution {
	public static final String COLA_NAME = "Highway Cola";
	public static final String AQUA_NAME = "Fresh Aqua";
	public static final String PHANTOM_NAME = "Phantom Orange";
	public static final String STRIPE_NAME = "Stripe";

	private VendingMachine machine;
	private CashRegister register;
	private Drink cola;
	private Drink aqua;
	private Drink phantom;
	private Drink stripe;

	@Override
	public void initialization() {
		machine = Action.create(VendingMachine.class);
		register = Action.create(CashRegister.class);
		cola = Action.create(Drink.class, 260, 5, COLA_NAME);
		aqua = Action.create(Drink.class, 180, 1, AQUA_NAME);
		phantom = Action.create(Drink.class, 260, 2, PHANTOM_NAME);
		stripe = Action.create(Drink.class, 230, 4, STRIPE_NAME);

		DrinkCollection<Drink> drinks = Action.collectIn(DrinkCollection.class, cola, aqua, phantom, stripe);

		for (Drink drink : drinks) {
			Action.link(Serve.drinks.class, drink, Serve.theMachine.class, machine);
		}
		Action.link(WorkTogether.theCashRegister.class, register, WorkTogether.theMachine.class, machine);

		for (Drink drink : drinks) {
			Action.start(drink);
		}
		Action.start(register);
		Action.start(machine);
	}

	public void chooseDrink(String drinkName) {
		API.send(new DrinkChosen(drinkName), machine);
	}

	public void cashReturn() {
		API.send(new GiveBackCash(0), register);
	}

	public void insertCoin(int coin) {
		API.send(new InsertCoin(coin), register);
	}

	public void refillDrink(String drinkName, int quantity) {
		Drink drink;
		switch (drinkName) {
		case AQUA_NAME:
			drink = aqua;
			break;
		case COLA_NAME:
			drink = cola;
			break;
		case PHANTOM_NAME:
			drink = phantom;
			break;
		case STRIPE_NAME:
			drink = stripe;
			break;
		default:
			return;
		}
		API.send(new Refill(quantity), drink);
	}

}