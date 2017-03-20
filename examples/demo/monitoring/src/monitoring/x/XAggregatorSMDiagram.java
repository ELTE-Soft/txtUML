package monitoring.x;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import monitoring.x.model.Aggregator;
import monitoring.x.model.Aggregator.Aggregate;
import monitoring.x.model.Aggregator.Init;

public class XAggregatorSMDiagram extends StateMachineDiagram<Aggregator> {
	@Column({ Init.class, Aggregate.class })
	class L extends Layout {
	}
}
