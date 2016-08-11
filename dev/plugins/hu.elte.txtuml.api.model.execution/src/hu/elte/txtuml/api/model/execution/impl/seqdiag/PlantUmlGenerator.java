package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.io.File;
import java.io.PrintWriter;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.seqdiag.InteractionWrapper;
import hu.elte.txtuml.api.model.seqdiag.LifelineWrapper;
import hu.elte.txtuml.api.model.seqdiag.MessageWrapper;

public class PlantUmlGenerator {
	
	private PrintWriter targetFile;
	private InteractionWrapper baseInteraction;
	
	public PlantUmlGenerator(String targetFile,InteractionWrapper interactionWrap)
	{
		File fileCreator = new File(targetFile);
		try {
			fileCreator.createNewFile();
			this.targetFile = new PrintWriter(targetFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.baseInteraction = interactionWrap;
	}
	
	
	public void generate()
	{
		targetFile.println("@startuml");
		for(LifelineWrapper<?> wrapper : this.baseInteraction.getLifelines())
		{
			String instanceName = wrapper.getWrapped().toString();
			targetFile.println("participant " + instanceName );
			
		}
		
		for(MessageWrapper wrapper : this.baseInteraction.getMessages())
		{
			ModelClass from = wrapper.sender;
			ModelClass to = wrapper.receiver;
			Signal sig = wrapper.signal;
			targetFile.println(from.toString() + "->" + to.toString() + ":" + sig.toString());
			
		}
		
		targetFile.println("@enduml");
		targetFile.flush();
		targetFile.close();
	}
}
