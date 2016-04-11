package hu.elte.txtuml.export.papyrus.elementproviders;

import java.util.Collection;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Signal;

import hu.elte.txtuml.layout.export.DiagramExportationReport;

public class TxtUMLClassDiagramElementsProvider implements ClassDiagramElementsProvider {

	private DiagramExportationReport report;

	public TxtUMLClassDiagramElementsProvider(DiagramExportationReport report) {
		this.report = report;
	}

	@Override
	public Collection<Class> getClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DataType> getDataTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Enumeration> getEnumerations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<InstanceSpecification> getInstanceSpecifications() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Interface> getInterfaces() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Model> getModels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Package> getPackages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Comment> getComments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Signal> getSignals() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Association> getAssociations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Generalization> getGeneralizations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<InterfaceRealization> getInterfaceRealizations() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Realization> getRealizations() {
		// TODO Auto-generated method stub
		return null;
	}

}
