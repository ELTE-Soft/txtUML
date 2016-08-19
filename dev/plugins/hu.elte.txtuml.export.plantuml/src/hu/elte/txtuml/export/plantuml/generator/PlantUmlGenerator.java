package hu.elte.txtuml.export.plantuml.generator;

import java.io.PrintWriter;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.CompilationUnit;

import hu.elte.txtuml.api.model.seqdiag.Interaction;

public class PlantUmlGenerator {

	private PrintWriter targetFile;
	private Class<Interaction> sourceResource;
	private CompilationUnit sourceCU;

	private MethodStatementWalker walker;
	private ArrayList<String> activeLifelines;

	public PlantUmlGenerator(IFile targetFile, Class<Interaction> sourceResource, CompilationUnit source) {

		try {
			targetFile.create(null, false, null);
			this.targetFile = new PrintWriter(targetFile.getLocation().toOSString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.sourceResource = sourceResource;
		this.sourceCU = source;
		this.activeLifelines = new ArrayList<String>();
	}

	public void generate() {

		walker = new MethodStatementWalker(this);
		sourceCU.accept(walker);

		targetFile.flush();
		targetFile.close();

		/*
		 * for(LifelineWrapper<?> wrapper : this.baseInteraction.getLifelines())
		 * { String instanceName = wrapper.getWrapped().toString();
		 * targetFile.println("participant " + instanceName );
		 * 
		 * }
		 * 
		 * MessageWrapper wrapper = this.baseInteraction.getMessages().get(0);
		 * 
		 * targetFile.println("activate " + wrapper.sender.toString());
		 * ModelClass from = wrapper.sender; ModelClass to = wrapper.receiver;
		 * Signal sig = wrapper.signal; targetFile.println(from.toString() +
		 * "->" + to.toString() + ":" + sig.toString());
		 * generateMessages(wrapper.receiver,1); targetFile.println(
		 * "deactivate " + wrapper.sender.toString());
		 * 
		 */

	}

	PrintWriter getTargetFile() {
		return this.targetFile;
	}

	/*
	 * protected int generateMessages(ModelClass receiver,int continueIndex) {
	 * boolean sameReceiver = true; int i = continueIndex;
	 * 
	 * while(sameReceiver && i < this.baseInteraction.getMessages().size()) {
	 * MessageWrapper wrapper = this.baseInteraction.getMessages().get(i);
	 * targetFile.println("activate " + wrapper.sender.toString()); ModelClass
	 * from = wrapper.sender; ModelClass to = wrapper.receiver; Signal sig =
	 * wrapper.signal; targetFile.println(from.toString() + "->" + to.toString()
	 * + ":" + sig.toString()); if(i + 1 <
	 * this.baseInteraction.getMessages().size()) {
	 * if(this.baseInteraction.getMessages().get(i+1).sender.equals(from)) {
	 * ++i; } } else { i = generateMessages(to,++i); } targetFile.println(
	 * "deactivate " + wrapper.sender.toString());
	 * 
	 * if(i < this.baseInteraction.getMessages().size()) { sameReceiver =
	 * this.baseInteraction.getMessages().get(i).receiver.equals(receiver); } }
	 * 
	 * return i; }
	 */

	public boolean lifelineIsActive(String lifeline) {
		if (activeLifelines.contains(lifeline)) {
			return true;
		}

		return false;
	}

	public void activateLifeline(String lifeline) {


		if (!lifelineIsActive(lifeline)) {
			targetFile.println("activate " + lifeline);
		}
		
		activeLifelines.add(lifeline);
	}

	public void deactivateLifeline(String lifeline) {
		if (lifelineIsActive(lifeline)) {
			activeLifelines.remove(lifeline);
			
			if (!lifelineIsActive(lifeline)) {
				targetFile.println("deactivate " + lifeline);
			}
		}
	}
	
	public void deactivateAllLifelines()
	{
		while(activeLifelines.size() > 0)
		{
			this.deactivateLifeline(activeLifelines.get(0));
		}
	}
}
