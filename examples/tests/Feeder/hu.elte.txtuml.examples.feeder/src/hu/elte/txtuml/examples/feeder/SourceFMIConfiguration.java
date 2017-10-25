package hu.elte.txtuml.examples.feeder;

import hu.elte.txtuml.api.deployment.fmi.FMIConfiguration;
import hu.elte.txtuml.api.deployment.fmi.FMU;
import hu.elte.txtuml.api.deployment.fmi.FMUAssociationEnd;
import hu.elte.txtuml.api.deployment.fmi.FMUInput;
import hu.elte.txtuml.api.deployment.fmi.FMUOutput;
import hu.elte.txtuml.api.deployment.fmi.InitialIntegerValue;
import hu.elte.txtuml.examples.feeder.model.RequestSignal;
import hu.elte.txtuml.examples.feeder.model.ResponseSignal;
import hu.elte.txtuml.examples.feeder.model.Source;
import hu.elte.txtuml.examples.feeder.model.SourceEnvAssoc;

@FMU(fmuClass = Source.class)
@FMUAssociationEnd(fmuAssociationEnd = SourceEnvAssoc.source.class)
@FMUInput(inputSignal = RequestSignal.class)
@FMUOutput(outputSignal = ResponseSignal.class)
@InitialIntegerValue(variableName = "amount", value = 0)
public class SourceFMIConfiguration extends FMIConfiguration {

}
