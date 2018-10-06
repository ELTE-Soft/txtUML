import airlock.model.Airlock;
import airlock.model.CycleSignal;
import hu.elte.txtuml.api.deployment.fmi.FMUEnvironment;
import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;

public class Demo implements Execution{
	
	Airlock airlock;
	FMUEnvironment env;

	@Override
	public void initialization() {
		env = Action.create(FMUEnvironment.class);
		airlock = Action.create(Airlock.class, env);
		Action.start(airlock);
	}
	
	@Override
	public void during() {
		try{
			while(true){
				Thread.sleep(1000);
				API.send(new CycleSignal(), airlock);
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		new Demo().run();
	}

}
