package control.model;

import control.model.signals.StepSignal;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class ControlUnit extends ModelClass{	
	
	public class Init extends Initial{};
	
	public class ProgramSelection extends State{
		@Override
		public void entry(){
			Action.log("Please select a program!");
		}
		
		@Override
		public void exit(){
			Action.log("Program selected ...");
		}
	};
	
	public class Finished extends State{
		@Override
		public void entry(){
			Action.log("[FINISHED!]");
		}
	};
	
	public class Aborted extends State{
		@Override
		public void entry(){
			Action.log("[ABORTED!]");
		}
	};
	
	@From(Init.class)
	@To(ProgramSelection.class)
	public class InitToSelection extends Transition{};
	
	/*Programs*/
	
	/*TODO: "generic" solution*/
	public class WhiteProgram extends CompositeState{		
		public class Init extends Initial{};
		public class Heat extends State{};
		public class Wash extends State{};
		public class Rinse extends State{};
		public class Centrifuge extends State{};
		
		@From(Init.class)
		@To(Wash.class)
		public class Start extends Transition{
			@Override
			public void effect() {
				Action.log("Starting [White]");
				//TODO: water
			}
		};
		
		@From(Wash.class)
		@To(Rinse.class)
		@Trigger(StepSignal.class)
		public class WtR extends Transition{};
		
		@From(Rinse.class)
		@To(Centrifuge.class)
		@Trigger(StepSignal.class)
		public class RtC extends Transition{};
		
		@From(Centrifuge.class)
		@To(Finished.class)
		@Trigger(StepSignal.class)
		public class ProgramFinished extends Transition{};

	}	

}
