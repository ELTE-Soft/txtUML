package hu.elte.txtuml.examples.feeder.model;

import hu.elte.txtuml.api.deployment.fmi.FMUEnvironment;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;

public class SinkEnvAssoc extends Association {

	public class sink extends End<One<Sink>> {
	}

	class source extends End<One<FMUEnvironment>> {
	}

}
