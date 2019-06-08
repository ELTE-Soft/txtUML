package car.j.diagrams;

import car.x.model.Car;
import car.x.model.Gearbox;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Column;

public class CarClassDiagram extends ClassDiagram {

	// Car and Gearbox will be shown beside eachother in a column
	@Column({ Car.class, Gearbox.class })
	class CarLayout extends Layout {
	}
}
