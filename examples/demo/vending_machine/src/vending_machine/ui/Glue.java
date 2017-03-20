package vending_machine.ui;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.external.ExternalClass;
import vending_machine.CashRegister;
import vending_machine.Drink;
import vending_machine.Serve;
import vending_machine.VendingMachine;
import vending_machine.WorkTogether;
import vending_machine.drinkChosen;
import vending_machine.giveBackCash;
import vending_machine.insertCash;
import vending_machine.refill;
import vending_machine.model.*;
import vending_machine.ui.IControl;

public class Glue implements ExternalClass, IControl {
	VendingMachine machine = new VendingMachine();
	CashRegister register = new CashRegister();
	Drink cola = new Drink(260, 5, "Highway Cola");
	Drink aqua = new Drink(180, 1, "Fresh Aqua");
	Drink fantom = new Drink(260, 2, "Fantom Orange");
	Drink stripe = new Drink(230, 4, "Stripe");

	// Singleton pattern
	static Glue instance = null;

	private Glue() {
		Action.link(Serve.drinks.class, cola, Serve.theMachine.class, machine);
		Action.link(Serve.drinks.class, aqua, Serve.theMachine.class, machine);
		Action.link(Serve.drinks.class, fantom, Serve.theMachine.class, machine);
		Action.link(Serve.drinks.class, stripe, Serve.theMachine.class, machine);
		Action.link(WorkTogether.theCashRegister.class, register, WorkTogether.theMachine.class, machine);

		Action.start(cola);
		Action.start(aqua);
		Action.start(fantom);
		Action.start(stripe);
		Action.start(register);
		Action.start(machine);
	}

	public static synchronized Glue getInstance() {
		if (instance == null) {
			instance = new Glue();
		}
		return instance;
	}

	@Override
	public void pressCola() {
		Action.send(new drinkChosen(cola.getName()), machine);
	}

	@Override
	public void pressFantom() {
		Action.send(new drinkChosen(fantom.getName()), machine);
	}

	@Override
	public void pressStripe() {
		Action.send(new drinkChosen(stripe.getName()), machine);
	}

	@Override
	public void pressAqua() {
		Action.send(new drinkChosen(aqua.getName()), machine);
	}

	@Override
	public void cashReturn() {
		Action.send(new giveBackCash(register.howMuchIsInside()), register);
	}

	@Override
	public void insertTen() {
		Action.send(new insertCash(10), register);
	}

	@Override
	public void insertTwenty() {
		Action.send(new insertCash(20), register);
	}

	@Override
	public void insertFifty() {
		Action.send(new insertCash(50), register);
	}

	@Override
	public void insertHundred() {
		Action.send(new insertCash(100), register);
	}

	@Override
	public void insertTwoHundred() {
		Action.send(new insertCash(200), register);
	}

	@Override
	public java.lang.String showMessage() {
		return machine.getMessage();
	}

	@Override
	public void refillStripe(int q) {
		Action.send(new refill(q), stripe);
	}

	@Override
	public void refillCola(int q) {
		Action.send(new refill(q), cola);
	}

	@Override
	public void refillFantom(int q) {
		Action.send(new refill(q), fantom);
	}

	@Override
	public void refillAqua(int q) {
		Action.send(new refill(q), aqua);
	}

}