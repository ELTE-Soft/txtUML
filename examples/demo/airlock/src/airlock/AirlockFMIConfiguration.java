package airlock;


import airlock.model.Airlock;
import airlock.model.AirlockEnv;
import airlock.model.InputSignal;
import airlock.model.OutputSignal;
import hu.elte.txtuml.api.deployment.fmi.FMIConfiguration;
import hu.elte.txtuml.api.deployment.fmi.FMU;
import hu.elte.txtuml.api.deployment.fmi.FMUAssociationEnd;
import hu.elte.txtuml.api.deployment.fmi.FMUInput;
import hu.elte.txtuml.api.deployment.fmi.FMUOutput;

@FMU(fmuClass = Airlock.class)
@FMUAssociationEnd(fmuAssociationEnd = AirlockEnv.airlock.class)
@FMUInput(inputSignal = InputSignal.class)
@FMUOutput(outputSignal = OutputSignal.class)
public class AirlockFMIConfiguration extends FMIConfiguration {

}
