package hu.elte.txtuml.examples.feeder;

import hu.elte.txtuml.api.deployment.fmi.FMIConfiguration;
import hu.elte.txtuml.api.deployment.fmi.FMU;
import hu.elte.txtuml.api.deployment.fmi.FMUInput;
import hu.elte.txtuml.api.deployment.fmi.FMUOutput;
import hu.elte.txtuml.api.deployment.fmi.InitialRealValue;
import hu.elte.txtuml.examples.feeder.model.RequestSignal;
import hu.elte.txtuml.examples.feeder.model.ResponseSignal;
import hu.elte.txtuml.examples.feeder.model.Source;

@FMU(fmuClass = Source.class)
@FMUInput(inputSignal = RequestSignal.class)
@FMUOutput(outputSignal = ResponseSignal.class)
@InitialRealValue(variableName = "amount", value = 0)
public class SourceFMIConfiguration extends FMIConfiguration {

}
