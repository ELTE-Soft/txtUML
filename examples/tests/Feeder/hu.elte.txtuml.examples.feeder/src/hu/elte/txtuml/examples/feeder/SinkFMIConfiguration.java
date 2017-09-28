package hu.elte.txtuml.examples.feeder;

import hu.elte.txtuml.api.deployment.fmi.FMIConfiguration;
import hu.elte.txtuml.api.deployment.fmi.FMU;
import hu.elte.txtuml.api.deployment.fmi.FMUAssociationEnd;
import hu.elte.txtuml.api.deployment.fmi.FMUInput;
import hu.elte.txtuml.api.deployment.fmi.FMUOutput;
import hu.elte.txtuml.api.deployment.fmi.InitialRealValue;
import hu.elte.txtuml.examples.feeder.model.RequestSignal;
import hu.elte.txtuml.examples.feeder.model.ResponseSignal;
import hu.elte.txtuml.examples.feeder.model.Sink;
import hu.elte.txtuml.examples.feeder.model.SinkSourceAssoc;

@FMU(fmuClass = Sink.class)
@FMUAssociationEnd(fmuAssociationEnd = SinkSourceAssoc.sink.class)
@FMUInput(inputSignal = ResponseSignal.class)
@FMUOutput(outputSignal = RequestSignal.class)
@InitialRealValue(variableName = "data", value = 0)
public class SinkFMIConfiguration extends FMIConfiguration {

}
