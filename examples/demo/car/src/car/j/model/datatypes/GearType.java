package car.j.model.datatypes;

import hu.elte.txtuml.api.model.DataType;

public class GearType extends DataType {
	public final int gear;

	public GearType(final int x) {
		this.gear = x;
	}
}
