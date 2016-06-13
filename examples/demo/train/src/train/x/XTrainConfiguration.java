package train.x;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import train.x.model.Engine;
import train.x.model.Gearbox;

@Group(contains = { Engine.class, Gearbox.class }, max = 5, constant = 2)
public class XTrainConfiguration extends Configuration {

}
