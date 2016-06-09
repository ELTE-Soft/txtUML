package airport;

import airport.xmodel.Plane;
import airport.xmodel.Runway;
import airport.xmodel.Tower;
import airport.xmodel.Weather;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.Show;

public class AirportClassDiagram extends ClassDiagram {

	@Row({ Plane.class, Tower.class, Runway.class })
	// @Below(val = Weather.class, from = Tower.class)

	@Show({ Plane.class, Tower.class, Runway.class, Weather.class })
	class PlaneLayout extends Layout {
	}
}
