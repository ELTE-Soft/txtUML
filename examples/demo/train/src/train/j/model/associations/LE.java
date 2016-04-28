package train.j.model.associations;

import hu.elte.txtuml.api.model.Association;
import train.j.model.Engine;
import train.j.model.Lamp;

public class LE extends Association {
	public class l extends One<Lamp> {
	}

	public class e extends One<Engine> {
	}
}
