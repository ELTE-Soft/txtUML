package car.j.model.signals;

import car.j.model.datatypes.GearType;
import hu.elte.txtuml.api.model.Signal;

public class ChangeGear extends Signal {
	public GearType gearType;

	public ChangeGear(final GearType gearType) {
		this.gearType = gearType;
	}
}
