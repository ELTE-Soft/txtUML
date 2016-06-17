package airport;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import airport.xmodel.*;

@Group(contains = { Plane.class})
@Group(contains = { Tower.class})
@Group(contains = { Weather.class})
@Group(contains = { Runway.class})
public class DefaultConfiguration extends Configuration {
}
