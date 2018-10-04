package airlock.model;

import hu.elte.txtuml.api.deployment.fmi.FMUEnvironment;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class Airlock extends ModelClass{
	
	public void add(){
		
	}
	
	public Airlock(FMUEnvironment env) {
		add();
		Action.link(AirlockEnv.env.class, env, AirlockEnv.airlock.class, this);
	}
	
	private double pressure = 100;
	
	public class Init extends Initial {
	}
	
	@From(Init.class)
	@To(InnerDoorOpen.class)
	public class InitToInner extends Transition{
	}
	
	/*@From(InnerDoorOpen.class)
	@To(OuterDoorOpen.class)
	@Trigger(CycleSignal.class)
	public class Depressurizing extends Transition {
		@Override
		public void effect() {
			//try {
				//Action.log("De-pressurizing: ");
				double t = 0;
				while(pressure > 0){
					//Thread.sleep(250);
					pressure = 100 * (1-(t*(2-t)));
					//Action.log(pressure + "%, ");
					t+=0.25;
				}
				//Action.log("Done!\n");
				pressure = 0;
		}
	}*/
	
	/*@From(OuterDoorOpen.class)
	@To(InnerDoorOpen.class)
	@Trigger(CycleSignal.class)
	public class Pressurizing extends Transition {
		@Override
		public void effect() {
			//try {
				//Action.log("Pressurizing: ");
				double t = 0;
				while(pressure < 100){
					//Thread.sleep(250);
					pressure = 100 * (t*(2-t));
					//Action.log(pressure + "%, ");
					t+=0.25;
				}
				//Action.log("Done!\n");
		}
	}*/
	
	public class InnerDoorOpen extends State{
		//public void entry(){
			//Action.log("Opening Inner door ...");
			/*try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			//Action.log(" open!\n");
		//}
		//public void exit(){
			//Action.log("Closing Inner door ...");
			/*try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			//Action.log(" closed!\n");
		//}	
	}
	
	public class OuterDoorOpen extends State{
		//public void entry(){
			//Action.log("Opening Outer door ...");
			/*try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			//Action.log(" open!\n");
		//}
		//public void exit(){
			//Action.log("Closing Outer door ...");
			/*try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}*/
			//Action.log(" closed!\n");
		//}	
	}
	
	@From(InnerDoorOpen.class)
	@To(OuterDoorOpen.class)
	@Trigger(InputSignal.class)
	public class DepressurizingFMI extends Transition {
		/*@Override
		public boolean guard() {
			return getTrigger(InputSignal.class).doCycle;
		}*/
		@Override
		public void effect() {
			//FMUEnvironment env = assoc(AirlockEnv.env.class).one();
			//try {
				//Action.log("De-pressurizing: ");
				double t = 0;
				while(pressure > 0){
					//Thread.sleep(250);
					pressure = 100 * (1-(t*(2-t)));
					//Action.log(pressure + "%, ");
					t+=0.25;
				}
				//Action.log("Done!\n");
				pressure = 0;
			//Action.send(new OutputSignal(pressure), env);
		}
	}
	
	@From(OuterDoorOpen.class)
	@To(InnerDoorOpen.class)
	@Trigger(InputSignal.class)
	public class PressurizingFMI extends Transition {
		/*@Override
		public boolean guard() {
			return getTrigger(InputSignal.class).doCycle;
		}*/
		@Override
		public void effect() {
			//FMUEnvironment env = assoc(AirlockEnv.env.class).one();
			//try {
				//Action.log("Pressurizing: ");
				double t = 0;
				while(pressure < 100 || t>2){
					//Thread.sleep(250);
					pressure = 100 * (t*(2-t));
					//Action.log(pressure + "%, ");
					t+=0.25;
				}
				//Action.log("Done!\n");
				pressure = 0;
			//Action.send(new OutputSignal(pressure), env);
		}
	}
}
