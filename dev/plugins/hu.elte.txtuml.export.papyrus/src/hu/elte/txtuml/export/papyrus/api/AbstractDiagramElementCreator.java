package hu.elte.txtuml.export.papyrus.api;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Bounds;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.tooling.runtime.providers.DiagramElementTypes;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.papyrus.uml.diagram.clazz.providers.UMLElementTypes;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.utils.Logger;

public class AbstractDiagramElementCreator {

	protected TransactionalEditingDomain domain;
	
	protected DiagramElementTypes types;

	protected AbstractDiagramElementCreator(){
		
		IProgressService progressService = PlatformUI.getWorkbench()
				.getProgressService();
		try {
			progressService.runInUI(progressService, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					types = UMLElementTypes.TYPED_INSTANCE;
				}
			}, ResourcesPlugin.getWorkspace().getRoot());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void runInTransactionalCommand(Runnable runnable, String commandName, IProgressMonitor monitor) {

		if (monitor == null)
			monitor = new NullProgressMonitor();

		ICommand cmd = new AbstractTransactionalCommand(this.domain, commandName, null) {
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

	/**
	 * creates the appropriate bounds that can be added as
	 * {@link org.eclipse.gmf.runtime.notation.LayoutConstraint
	 * LayoutConstraint} to a {@link org.eclipse.gmf.runtime.notation.Node Node}
	 * 
	 * @param bounds
	 * @param defaultBounds
	 * @return
	 */
	protected Bounds createBounds(Rectangle bounds, Rectangle defaultBounds) {
		bounds = bounds == null ? defaultBounds : bounds;

		Bounds layoutConstraint = NotationFactory.eINSTANCE.createBounds();
		layoutConstraint.setX(bounds.x);
		layoutConstraint.setY(bounds.y);
		layoutConstraint.setWidth(bounds.width);
		layoutConstraint.setHeight(bounds.height);
		return layoutConstraint;
	}

	protected static View getViewOfModel(Element model, View container) {
		@SuppressWarnings("unchecked")
		EList<View> children = container.getChildren();
		
		for(View child : children){
			if(child.getElement().equals(model))
				return child;
		}
		return null;
	}
}
