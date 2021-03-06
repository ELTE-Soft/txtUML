package hu.elte.txtuml.export.papyrus.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.handlers.HandlerUtil;

import hu.elte.txtuml.export.papyrus.wizardz.PapyrusVisualizeWizard;
import hu.elte.txtuml.utils.eclipse.wizards.VisualizeTxtUMLPage;

public class PapyrusVisualizeSelectedDiagramsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		PapyrusVisualizeWizard wizard = new PapyrusVisualizeWizard();
		WizardDialog wizardDialog = new WizardDialog(null, wizard);
		wizardDialog.create();

		VisualizeTxtUMLPage page = ((VisualizeTxtUMLPage) wizardDialog.getSelectedPage());

		// get selected files
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IStructuredSelection strSelection = (IStructuredSelection) selection;

		List<ICompilationUnit> selectedCompilationUnits = new ArrayList<>();
		Stream.of(strSelection.toArray()).forEach(s -> selectedCompilationUnits
				.add((ICompilationUnit) ((IAdaptable) s).getAdapter(ICompilationUnit.class)));
		try {
			List<IType> types = new ArrayList<>();
			for (ICompilationUnit cu : selectedCompilationUnits) {
				types.addAll(Arrays.asList(cu.getTypes()));
			}
			page.selectElementsInDiagramTree(types.toArray(), false);
			page.setExpandedLayouts(types);
		} catch (JavaModelException ex) {
		}

		wizardDialog.open();
		return null;
	}

}
