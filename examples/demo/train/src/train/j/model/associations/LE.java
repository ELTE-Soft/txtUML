package train.j.model.associations;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import train.j.model.Engine;
import train.j.model.Lamp;

public class LE extends Association {
	public class l extends End<One<Lamp>> {
	}

	public class e extends End<One<Engine>> {
	}
}
