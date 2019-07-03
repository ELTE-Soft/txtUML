package car.x.diagrams;

import car.x.model.Car;
import car.x.model.Gearbox;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Column;

public class CarClassDiagram extends ClassDiagram {

	@Column({ Car.class, Gearbox.class })
	class CarLayout extends Layout {
	}
}
