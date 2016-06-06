package tester_GUI;

import java.util.concurrent.CountDownLatch;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
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

public class Glue implements ExternalClass, IControl, IControlled {
	VendingMachine machine;
	CashRegister register;
	Drink cola;
	Drink aqua;
	Drink fantom;
	Drink stripe;
	
	CountDownLatch initReady = new CountDownLatch(1);

	IControlled controlled;

	public void setControlled(IControlled ctd) {
		controlled = ctd;
	}

	// Singleton pattern
	static Glue instance = null;

	private Glue() {
		ModelExecutor.create().setTraceLogging(true).start(() -> {
			machine = Action.create(VendingMachine.class);
			register = Action.create(CashRegister.class);
			cola = Action.create(Drink.class, 260, 5, "Highway Cola");
			aqua = Action.create(Drink.class, 180, 1, "Fresh Aqua");
			fantom = Action.create(Drink.class, 260, 2, "Fantom Orange");
			stripe = Action.create(Drink.class, 230, 4, "Stripe");

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
			
			initReady.countDown();
		});
	}

	public static synchronized Glue getInstance() {
		if (instance == null) {
			instance = new Glue();
		}
		return instance;
	}

	@Override
	public void showDrink(Drink drink) {
		awaitInit();
		controlled.showDrink(drink);
	}

	@Override
	public void showMoney() {
		awaitInit();
		controlled.showMoney();
	}

	@Override
	public void pressCola() {
		awaitInit();
		Action.send(new drinkChosen(cola.getName()), machine);
	}

	@Override
	public void pressFantom() {
		awaitInit();
		Action.send(new drinkChosen(fantom.getName()), machine);
	}

	@Override
	public void pressStripe() {
		awaitInit();
		Action.send(new drinkChosen(stripe.getName()), machine);
	}

	@Override
	public void pressAqua() {
		awaitInit();
		Action.send(new drinkChosen(aqua.getName()), machine);
	}

	@Override
	public void cashReturn() {
		awaitInit();
		Action.send(new giveBackCash(register.howMuchIsInside()), register);
	}

	@Override
	public void insertTen() {
		awaitInit();
		Action.send(new insertCash(10), register);
	}

	@Override
	public void insertTwenty() {
		awaitInit();
		Action.send(new insertCash(20), register);
	}

	@Override
	public void insertFifty() {
		awaitInit();
		Action.send(new insertCash(50), register);
	}

	@Override
	public void insertHundred() {
		awaitInit();
		Action.send(new insertCash(100), register);
	}

	@Override
	public void insertTwoHundred() {
		awaitInit();
		Action.send(new insertCash(200), register);
	}

	@Override
	public java.lang.String showMessage() {
		awaitInit();
		return machine.getMessage();
	}

	@Override
	public void refillStripe(int q) {
		awaitInit();
		Action.send(new refill(q), stripe);
	}

	@Override
	public void refillCola(int q) {
		awaitInit();
		Action.send(new refill(q), cola);
	}

	@Override
	public void refillFantom(int q) {
		awaitInit();
		Action.send(new refill(q), fantom);
	}

	@Override
	public void refillAqua(int q) {
		awaitInit();
		Action.send(new refill(q), aqua);
	}

	private void awaitInit() {
		try {
			initReady.await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}