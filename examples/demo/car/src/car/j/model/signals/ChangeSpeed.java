package car.j.model.signals;

import car.j.model.datatypes.SpeedType;
import hu.elte.txtuml.api.model.Signal;

public class ChangeSpeed extends Signal {
	public SpeedType speedType;

	public ChangeSpeed(final SpeedType speedType) {
		this.speedType = speedType;
	}
}