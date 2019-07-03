package car.j.diagrams;

import car.j.model.Car;
import car.j.model.Gearbox;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Column;

public class CarClassDiagram extends ClassDiagram {

	@Column({ Car.class, Gearbox.class })
	class CarLayout extends Layout {
	}
}
