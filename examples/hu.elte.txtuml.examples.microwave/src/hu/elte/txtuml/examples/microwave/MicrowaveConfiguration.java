package hu.elte.txtuml.examples.microwave;

import hu.elte.txtuml.api.platform.Configuration;
import hu.elte.txtuml.api.platform.Group;
import hu.elte.txtuml.api.platform.Multithreading;

import hu.elte.txtuml.examples.microwave.model.*;

//Not valid model -> Completion failed
@Group(contains = {Human.class})
@Group(contains = {Microwave.class})
@Multithreading(false)
public class MicrowaveConfiguration extends Configuration{

}
