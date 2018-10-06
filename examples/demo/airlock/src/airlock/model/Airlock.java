package airlock.model;

import hu.elte.txtuml.api.deployment.fmi.FMUEnvironment;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class Airlock extends ModelClass{
	
	public Airlock(FMUEnvironment env) {
		Action.link(AirlockEnv.env.class, env, AirlockEnv.airlock.class, this);
	}
	
	private double pressure = 100;
	
	public class Init extends Initial {
	}
	
	@From(Init.class)
	@To(InnerDoorOpen.class)
	public class InitToInner extends Transition{
		@Override
		public void effect(){
			pressure = 100;
			FMUEnvironment env = assoc(AirlockEnv.env.class).one();
			Action.send(new OutputSignal(pressure), env);
		}
	}
	
	@From(InnerDoorOpen.class)
	@To(OuterDoorOpen.class)
	@Trigger(CycleSignal.class)
	public class Depressurizing extends Transition {
		@Override
		public void effect() {
			FMUEnvironment env = assoc(AirlockEnv.env.class).one();
			Action.log("\nDe-pressurizing: ");
			double t = 0;
			while(pressure > 0){
				pressure = 100 * (1-(t*(2-t)));
				Action.log("\t" + pressure + "%");
				Action.send(new OutputSignal(pressure), env);
				t+=0.25;
			}
			Action.log("Done!\n");
			pressure = 0;
			Action.send(new OutputSignal(pressure), env);
		}
	}
	
	@From(OuterDoorOpen.class)
	@To(InnerDoorOpen.class)
	@Trigger(CycleSignal.class)
	public class Pressurizing extends Transition {
		@Override
		public void effect() {
			FMUEnvironment env = assoc(AirlockEnv.env.class).one();
				Action.log("\nPressurizing: ");
				double t = 0;
				while(pressure < 100 || t>2){
					pressure = 100 * (t*(2-t));
					Action.log("\t" + pressure + "%");
					Action.send(new OutputSignal(pressure), env);
					t+=0.25;
				}
				Action.log("Done!\n");
			Action.send(new OutputSignal(pressure), env);
		}
	}
	
	public class InnerDoorOpen extends State{
		public void entry(){
			Action.log("\nOpening Inner door ...");
			Action.log("\topen!\n");
		}
		public void exit(){
			Action.log("\nClosing Inner door ...");
			Action.log("\tclosed!\n");
		}	
	}
	
	public class OuterDoorOpen extends State{
		public void entry(){
			Action.log("\nOpening Outer door ...");
			Action.log("\topen!\n");
		}
		public void exit(){
			Action.log("\nClosing Outer door ...");
			Action.log("\tclosed!\n");
		}	
	}
	
	@From(InnerDoorOpen.class)
	@To(OuterDoorOpen.class)
	@Trigger(InputSignal.class)
	public class DepressurizingFMI extends Transition {
		@Override
		public boolean guard() {
			return getTrigger(InputSignal.class).doCycle;
		}
		@Override
		public void effect() {
			FMUEnvironment env = assoc(AirlockEnv.env.class).one();
			Action.log("\nDe-pressurizing: ");
			double t = 0;
			while(pressure > 0){
				pressure = 100 * (1-(t*(2-t)));
				Action.log("\t" + pressure + "%");
				Action.send(new OutputSignal(pressure), env);
				t+=0.25;
			}
			Action.log("Done!\n");
			pressure = 0;
			Action.send(new OutputSignal(pressure), env);
		}
	}
	
	@From(OuterDoorOpen.class)
	@To(InnerDoorOpen.class)
	@Trigger(InputSignal.class)
	public class PressurizingFMI extends Transition {
		@Override
		public boolean guard() {
			return getTrigger(InputSignal.class).doCycle;
		}
		@Override
		public void effect() {
			FMUEnvironment env = assoc(AirlockEnv.env.class).one();
				Action.log("\nPressurizing: ");
				double t = 0;
				while(pressure < 100 || t>2){
					pressure = 100 * (t*(2-t));
					Action.log("\t" + pressure + "%");
					Action.send(new OutputSignal(pressure), env);
					t+=0.25;
				}
				Action.log("Done!\n");
			Action.send(new OutputSignal(pressure), env);
		}
	}
}
