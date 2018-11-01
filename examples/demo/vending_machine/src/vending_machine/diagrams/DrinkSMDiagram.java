package vending_machine.diagrams;

import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import vending_machine.model.Drink;
import vending_machine.model.Drink.Available;
import vending_machine.model.Drink.Giving;
import vending_machine.model.Drink.Init;
import vending_machine.model.Drink.IsThereMore;
import vending_machine.model.Drink.Loading;
import vending_machine.model.Drink.NotGiving;
import vending_machine.model.Drink.OutOfStock;
import vending_machine.model.Drink.Refilling;

public class DrinkSMDiagram extends StateMachineDiagram<Drink> {

	@Row({ Available.class, Giving.class, IsThereMore.class, Init.class })
	@North(val=Available.class, from=Loading.class)
	@Row({ Loading.class, NotGiving.class, OutOfStock.class, Refilling.class })
	class MyLayout extends Layout {
	}

}
