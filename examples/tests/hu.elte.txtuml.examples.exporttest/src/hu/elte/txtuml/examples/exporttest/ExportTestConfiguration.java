package hu.elte.txtuml.examples.exporttest;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.examples.exporttest.model.OtherClass;
import hu.elte.txtuml.examples.exporttest.model.OtherClassWithCtor;
import hu.elte.txtuml.examples.exporttest.model.SomeClass;

@Group(contains = { SomeClass.class, OtherClass.class, OtherClassWithCtor.class }, max = 3, gradient = 1)
public class ExportTestConfiguration extends Configuration {

}
