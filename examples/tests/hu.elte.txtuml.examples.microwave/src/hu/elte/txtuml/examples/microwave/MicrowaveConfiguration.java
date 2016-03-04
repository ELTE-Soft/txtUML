package hu.elte.txtuml.examples.microwave;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;

import hu.elte.txtuml.examples.microwave.model.*;

//Not valid model -> Completion failed
@Group(contains = { Human.class })
@Group(contains = { Microwave.class })
public class MicrowaveConfiguration extends Configuration {

}
