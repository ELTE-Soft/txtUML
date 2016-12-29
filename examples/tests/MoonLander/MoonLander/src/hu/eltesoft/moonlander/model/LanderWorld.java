package hu.eltesoft.moonlander.model;

import hu.elte.txtuml.api.deployment.fmi.FMUEnvironment;
import hu.elte.txtuml.api.model.Association;

public class LanderWorld extends Association {
	public class world extends One<FMUEnvironment> {
	}

	public class lander extends One<MoonLander> {
	}
}