package train.j.model.associations;

import hu.elte.txtuml.api.model.Association;
import train.j.model.Gearbox;
import train.j.model.Lamp;

public class GL extends Association {
	public class g extends One<Gearbox> {
	}

	public class l extends One<Lamp> {
	}
}
