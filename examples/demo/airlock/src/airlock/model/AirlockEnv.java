package airlock.model;

import hu.elte.txtuml.api.deployment.fmi.FMUEnvironment;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;

public class AirlockEnv extends Association {

	public class env extends End<One<FMUEnvironment>> {
	}

	public class airlock extends End<One<Airlock>> {
	}
}