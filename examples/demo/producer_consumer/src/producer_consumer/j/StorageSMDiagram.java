package producer_consumer.j;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import producer_consumer.j.model.Storage.Init;
import producer_consumer.j.model.Storage.Working;
import producer_consumer.j.model.Storage;

public class StorageSMDiagram extends StateMachineDiagram<Storage>{
	@Column({Init.class, Working.class})
	class L extends Layout{}
}
