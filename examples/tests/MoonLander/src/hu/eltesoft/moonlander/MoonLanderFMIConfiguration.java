package hu.eltesoft.moonlander;

import hu.elte.txtuml.api.deployment.fmi.FMIConfiguration;
import hu.elte.txtuml.api.deployment.fmi.FMU;
import hu.elte.txtuml.api.deployment.fmi.FMUAssociationEnd;
import hu.elte.txtuml.api.deployment.fmi.FMUInput;
import hu.elte.txtuml.api.deployment.fmi.FMUOutput;
import hu.elte.txtuml.api.deployment.fmi.InitialRealValue;
import hu.eltesoft.moonlander.model.InputSignal;
import hu.eltesoft.moonlander.model.LanderWorld;
import hu.eltesoft.moonlander.model.MoonLander;
import hu.eltesoft.moonlander.model.OutputSignal;

@FMU(fmuClass = MoonLander.class)
@FMUAssociationEnd(fmuAssociationEnd = LanderWorld.lander.class)
@FMUInput(inputSignal = InputSignal.class)
@FMUOutput(outputSignal = OutputSignal.class)
@InitialRealValue(variableName = "h", value = 0)
@InitialRealValue(variableName = "v", value = 0)
@InitialRealValue(variableName = "u", value = 0)
public class MoonLanderFMIConfiguration extends FMIConfiguration {

}
