package hu.elte.txtuml.export.papyrus;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;

import hu.elte.txtuml.utils.Logger;

public class TransactionalCommandRunner {
	private static TransactionalCommandRunner instance;
	
	public static TransactionalCommandRunner getInstance(){
		if(instance == null){
			instance = new TransactionalCommandRunner();
		}
		return instance;
	}
	public static void setInstance(TransactionalCommandRunner newInstance) {
		instance = newInstance;
	}
	
	public void runInTransactionalCommand(Runnable runnable, TransactionalEditingDomain domain, String commandName, IProgressMonitor monitor) {

		if (monitor == null)
			monitor = new NullProgressMonitor();

		ICommand cmd = new AbstractTransactionalCommand(domain, commandName, null) {
			@Override
			protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info)
					throws ExecutionException {
				runnable.run();
				return CommandResult.newOKCommandResult();
			}
		};

		try {
			cmd.execute(monitor, null);
		} catch (ExecutionException e) {
			Logger.executor.error("Could not execute command " + cmd + " (" + commandName + ")");
		}
	}
}
