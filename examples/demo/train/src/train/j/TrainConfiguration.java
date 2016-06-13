package train.j;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import train.j.model.Engine;
import train.j.model.Gearbox;

@Group(contains = { Engine.class, Gearbox.class }, max = 5, constant = 2)
public class TrainConfiguration extends Configuration {

}
