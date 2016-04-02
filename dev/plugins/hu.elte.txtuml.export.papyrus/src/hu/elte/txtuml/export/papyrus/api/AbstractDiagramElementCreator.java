package hu.elte.txtuml.export.papyrus.api;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;

import hu.elte.txtuml.utils.Logger;

public class AbstractDiagramElementCreator {

	protected TransactionalEditingDomain domain;
	
	protected void runInTransactionalCommand(Runnable runnable, String commandName, IProgressMonitor monitor){
		
		ICommand cmd =new AbstractTransactionalCommand(this.domain, commandName, null) {
			@Override
			protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
				runnable.run();
				return CommandResult.newOKCommandResult();
			}
		};
		
		try {
			cmd.execute(monitor, null);
		} catch (ExecutionException e) {
			Logger.executor.error("Could not execute command "+cmd+" ("+commandName+")");
		}
	}

}
