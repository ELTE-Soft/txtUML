import java.util.Scanner;

import airlock.model.Airlock;
import airlock.model.CycleSignal;
import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.Execution;

public class Demo implements Execution{
	
	Airlock airlock; 

	@Override
	public void initialization() {
		airlock = Action.create(Airlock.class);
		Action.start(airlock);
	}
	
	@Override
	public void during() {
		try(Scanner scanner = new Scanner(System.in)){
		while(true){
			scanner.nextLine();
			API.send(new CycleSignal(), airlock);
		}
		}
	}

	public static void main(String[] args) throws Exception {
		new Demo().run();
	}

}
