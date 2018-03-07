package train.j.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import train.j.model.Engine;
import train.j.model.Gearbox;

public class GE extends Association {
	public class g extends End<One<Gearbox>> {
	}

	public class e extends End<One<Engine>> {
	}
}
