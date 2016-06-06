package tester;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import vending_machine.CashRegister;
import vending_machine.Drink;
import vending_machine.VendingMachine;

@Group(contains = { CashRegister.class})
@Group(contains = { Drink.class})
@Group(contains = { VendingMachine.class})
public class DefaultConfiguration extends Configuration {
}
