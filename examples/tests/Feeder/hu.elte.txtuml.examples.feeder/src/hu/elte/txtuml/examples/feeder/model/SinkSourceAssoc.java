package hu.elte.txtuml.examples.feeder.model;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.ModelClass;

public class SinkSourceAssoc extends Association {

	class sink extends One<ModelClass> {
	}

	class source extends One<ModelClass> {
	}

}
