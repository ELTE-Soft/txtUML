package hu.eltesoft.moonlander.model;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;

public class LanderWorld extends Association {
	public class world extends End<One<World>> {
	}

	public class lander extends End<One<MoonLander>> {
	}
}