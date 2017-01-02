package hu.eltesoft.moonlander;

import hu.elte.txtuml.api.deployment.fmi.FMIConfiguration;
import hu.elte.txtuml.api.deployment.fmi.FMU;
import hu.elte.txtuml.api.deployment.fmi.FMUInput;
import hu.elte.txtuml.api.deployment.fmi.FMUOutput;
import hu.elte.txtuml.api.deployment.fmi.InitialRealOutputValue;
import hu.eltesoft.moonlander.model.ControlCycleSignal;
import hu.eltesoft.moonlander.model.ControlSignal;
import hu.eltesoft.moonlander.model.MoonLander;

@FMU(fmuClass = MoonLander.class)
@FMUInput(inputSignal = ControlCycleSignal.class)
@FMUOutput(outputSignal = ControlSignal.class)
@InitialRealOutputValue(variableName = "u", value = 0)
public class MoonLanderFMIConfiguration extends FMIConfiguration {

}
