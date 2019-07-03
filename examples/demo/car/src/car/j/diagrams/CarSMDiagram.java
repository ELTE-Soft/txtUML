package car.j.diagrams;

import car.j.model.Car;
import car.j.model.Car.Backwards;
import car.j.model.Car.Forwards;
import car.j.model.Car.Init;
import car.j.model.Car.Stopped;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;

public class CarSMDiagram extends StateMachineDiagram<Car> {

	@Row({ Init.class })
	@Row({ Forwards.class, Stopped.class, Backwards.class })
	class CarLay extends Layout {
	}
}
