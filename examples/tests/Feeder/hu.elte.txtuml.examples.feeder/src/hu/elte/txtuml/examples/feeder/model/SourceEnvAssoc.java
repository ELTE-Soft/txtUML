package hu.elte.txtuml.examples.feeder.model;

import hu.elte.txtuml.api.deployment.fmi.FMUEnvironment;
import hu.elte.txtuml.api.model.Association;

public class SourceEnvAssoc extends Association {

	class sink extends One<FMUEnvironment> {
	}

	public class source extends One<Source> {
	}

}
