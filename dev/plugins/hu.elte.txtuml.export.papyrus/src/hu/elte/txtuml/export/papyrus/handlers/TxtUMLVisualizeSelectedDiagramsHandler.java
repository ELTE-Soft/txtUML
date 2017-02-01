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

import hu.elte.txtuml.export.papyrus.wizardz.TxtUMLVisuzalizeWizard;
import hu.elte.txtuml.export.papyrus.wizardz.VisualizeTxtUMLPage;

public class TxtUMLVisualizeSelectedDiagramsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TxtUMLVisuzalizeWizard wizard = new TxtUMLVisuzalizeWizard();
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
			page.selectElementsInDiagramTree(types.toArray());
		} catch (JavaModelException ex) {
		}

		wizardDialog.open();
		return null;
	}

}
