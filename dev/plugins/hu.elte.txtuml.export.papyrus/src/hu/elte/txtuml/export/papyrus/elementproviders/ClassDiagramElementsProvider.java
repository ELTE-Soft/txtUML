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

/**
 * Class that provides the elements of a diagram. The getter functions return
 * the elements that should appear on the specified diagram
 *
 */
public interface ClassDiagramElementsProvider {

	/**
	 * Returns the {@link Class}es which should be presented on the diagram
	 * 
	 * @return Collection of {@link Class}es or empty Collection 
	 */
	Collection<Class> getClasses();

	/**
	 * Returns the {@link DataType}s which should be presented on the diagram
	 * 
	 * @return Collection of {@link DataType}s or empty Collection
	 */
	Collection<DataType> getDataTypes();

	/**
	 * Returns the {@link Enumeration}s which should be presented on the diagram
	 * 
	 * @return Collection of {@link Enumeration}s or empty Collection
	 */
	Collection<Enumeration> getEnumerations();

	/**
	 * Returns the {@link InstanceSpecification}s which should be presented on
	 * the diagram
	 * 
	 * @return Collection of {@link InstanceSpecification}s or empty Collection
	 */
	Collection<InstanceSpecification> getInstanceSpecifications();

	/**
	 * Returns the {@link Interface}s which should be presented on the diagram
	 * 
	 * @return Collection of {@link Interface}s or empty Collection
	 */
	Collection<Interface> getInterfaces();

	/**
	 * Returns the {@link Model}s which should be presented on the diagram
	 * 
	 * @return Collection of {@link Model}s or empty Collection
	 */
	Collection<Model> getModels();

	/**
	 * Returns the {@link Package}s which should be presented on the diagram
	 * 
	 * @return Collection of {@link Package}s or empty Collection
	 */
	Collection<Package> getPackages();

	/**
	 * Returns the {@link Comment}s which should be presented on the diagram
	 * 
	 * @return Collection of {@link Comment}s or empty Collection
	 */
	Collection<Comment> getComments();

	/**
	 * Returns the {@link Signal}s which should be presented on the diagram
	 * 
	 * @return Collection of {@link Signal}s or empty Collection
	 */
	Collection<Signal> getSignals();

	/**
	 * Returns the {@link Association}s which should be presented on the diagram
	 * 
	 * @return Collection of {@link Association}s or empty Collection
	 */
	Collection<Association> getAssociations();

	/**
	 * Returns the {@link Generalization}s which should be presented on the
	 * diagram
	 * 
	 * @return Collection of {@link Generalization}s or empty Collection
	 */
	Collection<Generalization> getGeneralizations();

	/**
	 * Returns the {@link InterfaceRealization}s which should be presented on
	 * the diagram
	 * 
	 * @return Collection of {@link InterfaceRealization}s or empty Collection
	 */
	Collection<InterfaceRealization> getInterfaceRealizations();

	/**
	 * Returns the {@link Realization}s which should be presented on the diagram
	 * 
	 * @return Collection of {@link Realization}s or empty Collection
	 */
	Collection<Realization> getRealizations();
}
