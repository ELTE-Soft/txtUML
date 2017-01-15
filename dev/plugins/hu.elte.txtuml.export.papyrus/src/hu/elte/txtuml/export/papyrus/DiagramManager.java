package hu.elte.txtuml.export.papyrus;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.papyrus.commands.ICreationCommand;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationModel;
import org.eclipse.uml2.uml.Element;

import hu.elte.txtuml.export.papyrus.utils.LayoutUtils;

/**
 * Controls the diagrams.
 */
public class DiagramManager {
	private ModelSet modelSet;

	/**
	 * The constructor
	 * 
	 * @param editor
	 *            - The editor to the instance will be attached.
	 */
	public DiagramManager(ServicesRegistry registry) {

		try {
			this.modelSet = registry.getService(ModelSet.class);
		} catch (ServiceException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Calls the createDiagram method of the {@link ICreationCommand} with the
	 * container. The ModelSet will be the ModelSet that is attached to the
	 * editor of the DiagramManager.
	 * 
	 * @param container
	 *            - the {@link Element} thats children will be placed on the
	 *            diagram.
	 * @param diagramName
	 *            - The name of the new Diagram
	 * @param diagramCreationCommand
	 *            - the Command that will be executed.
	 */
	public void createDiagram(Element container, String diagramName, ICreationCommand diagramCreationCommand,
			TransactionalEditingDomain domain) {

		LayoutUtils.getDisplay().syncExec(() -> {
			TransactionalCommandRunner.getInstance().runInTransactionalCommand(() ->{
				diagramCreationCommand.createDiagram(DiagramManager.this.modelSet, container, diagramName);
			}, domain, "Creating Diagram", null);
		});
	}

	/**
	 * Gets the previously created diagrams.
	 * 
	 * @return The list of the the Diagrams
	 */
	public List<Diagram> getDiagrams() {
		NotationModel notationmodel = (NotationModel) this.modelSet.getModel(NotationModel.MODEL_ID);
		Resource notationResource = notationmodel.getResource();
		@SuppressWarnings("unchecked")
		List<Diagram> list = (List<Diagram>) (List<?>) notationResource.getContents();
		return list;
	}

}
