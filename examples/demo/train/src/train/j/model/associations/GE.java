package train.j.model.associations;

import hu.elte.txtuml.api.model.Association;
import train.j.model.Engine;
import train.j.model.Gearbox;

public class GE extends Association {
	public class g extends One<Gearbox> {
	}

	public class e extends One<Engine> {
	}
}
