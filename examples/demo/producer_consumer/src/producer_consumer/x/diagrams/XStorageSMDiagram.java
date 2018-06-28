package producer_consumer.x.diagrams;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import producer_consumer.x.model.Storage.Init;
import producer_consumer.x.model.Storage.Working;
import producer_consumer.x.model.Storage;

public class XStorageSMDiagram extends StateMachineDiagram<Storage>{
	@Column({Init.class, Working.class})
	class L extends Layout{}
}
