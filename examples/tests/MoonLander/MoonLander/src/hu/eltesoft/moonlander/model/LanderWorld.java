package hu.eltesoft.moonlander.model;

import hu.elte.txtuml.api.model.Association;

public class LanderWorld extends Association {
	public class world extends One<World> {
	}

	public class lander extends One<MoonLander> {
	}
}