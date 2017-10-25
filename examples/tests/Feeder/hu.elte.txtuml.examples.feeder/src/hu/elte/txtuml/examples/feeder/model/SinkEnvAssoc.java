package hu.elte.txtuml.examples.feeder.model;

import hu.elte.txtuml.api.deployment.fmi.FMUEnvironment;
import hu.elte.txtuml.api.model.Association;

public class SinkEnvAssoc extends Association {

	public class sink extends One<Sink> {
	}

	class source extends One<FMUEnvironment> {
	}

}
