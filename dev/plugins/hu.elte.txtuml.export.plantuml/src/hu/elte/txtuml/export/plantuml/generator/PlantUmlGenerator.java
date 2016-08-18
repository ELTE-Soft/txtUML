package hu.elte.txtuml.export.plantuml.generator;

import java.io.File;
import java.io.PrintWriter;

import org.eclipse.jdt.core.dom.CompilationUnit;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.InteractionWrapper;
import hu.elte.txtuml.api.model.seqdiag.LifelineWrapper;
import hu.elte.txtuml.api.model.seqdiag.MessageWrapper;

public class PlantUmlGenerator {
	
	private PrintWriter targetFile;
	private Class<Interaction> sourceResource;
	private CompilationUnit sourceCU;
	private String targetPath;
	
	public PlantUmlGenerator(String targetFile,Class<Interaction> sourceResource,CompilationUnit source)
	{
		File fileCreator = new File(targetFile);
		try {
			fileCreator.createNewFile();
			this.targetFile = new PrintWriter(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.sourceResource = sourceResource;
		this.sourceCU = source;
	}
	
	
	/*public void generate()
	{
		targetFile.println("@startuml");
		
		for(LifelineWrapper<?> wrapper : this.baseInteraction.getLifelines())
		{
			String instanceName = wrapper.getWrapped().toString();
			targetFile.println("participant " + instanceName );
			
		}
		
		MessageWrapper wrapper = this.baseInteraction.getMessages().get(0);
		
		targetFile.println("activate " + wrapper.sender.toString());
		ModelClass from = wrapper.sender;
		ModelClass to = wrapper.receiver;
		Signal sig = wrapper.signal;
		targetFile.println(from.toString() + "->" + to.toString() + ":" + sig.toString());
		generateMessages(wrapper.receiver,1);
		targetFile.println("deactivate " + wrapper.sender.toString());
		
		targetFile.println("@enduml");
		targetFile.flush();
		targetFile.close();
	}*/
	
	/*protected int generateMessages(ModelClass receiver,int continueIndex)
	{
		boolean sameReceiver = true;
		int i = continueIndex;
		
		while(sameReceiver && i < this.baseInteraction.getMessages().size())
		{
			MessageWrapper wrapper = this.baseInteraction.getMessages().get(i);
			targetFile.println("activate " + wrapper.sender.toString());
			ModelClass from = wrapper.sender;
			ModelClass to = wrapper.receiver;
			Signal sig = wrapper.signal;
			targetFile.println(from.toString() + "->" + to.toString() + ":" + sig.toString());
			if(i + 1 < this.baseInteraction.getMessages().size())
			{
				if(this.baseInteraction.getMessages().get(i+1).sender.equals(from))
				{
					++i;
				}
			}
			else
			{
				i = generateMessages(to,++i);	
			}
			targetFile.println("deactivate " + wrapper.sender.toString());
			
			if(i < this.baseInteraction.getMessages().size())
			{
				sameReceiver = this.baseInteraction.getMessages().get(i).receiver.equals(receiver);
			}
		}
		
		return i;
	}*/
}
