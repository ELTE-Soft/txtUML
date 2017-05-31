package hu.elte.txtuml.examples.printer;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.examples.printer.model.Human;

@Group(contains = { Human.class }) // the human will be a separate thread and
				// everything else will be an other
public class PrinterConfiguration extends Configuration {

}
