package car.x.diagrams;

import car.x.model.Car;
import car.x.model.Car.Backwards;
import car.x.model.Car.Forwards;
import car.x.model.Car.Init;
import car.x.model.Car.Stopped;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;

public class CarSMDiagram extends StateMachineDiagram<Car> {

	@Row({ Init.class })
	@Row({ Forwards.class, Stopped.class, Backwards.class })
	class CarLay extends Layout {
	}
}
