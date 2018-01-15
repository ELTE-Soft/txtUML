package train.j.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import train.j.model.Gearbox;
import train.j.model.Lamp;

public class GL extends Association {
	public class g extends End<One<Gearbox>> {
	}

	public class l extends End<One<Lamp>> {
	}
}
