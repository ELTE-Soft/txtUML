package hu.elte.txtuml.examples.feeder.model;

import hu.elte.txtuml.api.deployment.fmi.FMUEnvironment;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;

public class SourceEnvAssoc extends Association {

	class sink extends End<One<FMUEnvironment>> {
	}

	public class source extends End<One<Source>> {
	}

}
