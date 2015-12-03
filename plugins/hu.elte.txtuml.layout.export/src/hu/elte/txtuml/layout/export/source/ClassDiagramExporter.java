package hu.elte.txtuml.layout.export.source;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.eclipseutils.NotFoundException;
import hu.elte.txtuml.eclipseutils.PackageUtils;
import hu.elte.txtuml.eclipseutils.ProjectUtils;
import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.export.uml2.utils.SharedUtils;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.layout.export.problems.ElementExportationException;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.Sneaky;

/**
 * 
 * @author Gabor Ferenc Kovacs
 * @author D�vid J�nos N�meth
 *
 */
public class ClassDiagramExporter extends AbstractSourceExporter {

	@Override
	public DiagramType getType() {
		return DiagramType.Class;
	}

	@Override
	public boolean isNode(Class<?> cls) {
		return ModelClass.class.isAssignableFrom(cls);
	}

	@Override
	public boolean isLink(Class<?> cls) {
		return Association.class.isAssignableFrom(cls);
	}

	@Override
	public Pair<Class<?>, Class<?>> getStartAndEndOfLink(Class<?> link) throws ElementExportationException {
		Class<?>[] classes = link.getDeclaredClasses();
		if (classes.length < 2) {
			throw new ElementExportationException();
		}
		Class<?> end1 = getClassTypeFromAssocEnd(classes[0]);
		Class<?> end2 = getClassTypeFromAssocEnd(classes[1]);
		return new Pair<>(end1, end2);
	}

	@Override
	public void exportImpliedLinks(ModelId modelId, ElementExporter elementExporter) {

		if (modelId instanceof ModelIdImpl) {
			String modelName = ((ModelIdImpl) modelId).getPackage();

			try {
				List<Class<?>> allLinks = loadAllLinksFromModel(modelName, elementExporter);
				elementExporter.getNodes().keySet()
					.forEach(node -> exportImpliedLinksFromSpecifiedNode(elementExporter, allLinks, node));
			} catch (JavaModelException | NotFoundException | IOException e) {
				// Links could not be loaded. Nothing to do.
			}
		}

	}

	private List<Class<?>> loadAllLinksFromModel(String modelName, ElementExporter elementExporter)
			throws NotFoundException, JavaModelException, IOException {
		IJavaProject javaProject = ProjectUtils.findJavaProject(elementExporter.getSourceProjectName());

		// Sneaky.<JavaModelException> Throw();
		Stream<ICompilationUnit> stream = Stream.of(PackageUtils.findPackageFragments(javaProject, modelName))
				.flatMap(Sneaky.unchecked(pf -> Stream.of(pf.getCompilationUnits())));

		CompilationUnit[] units = SharedUtils.parseICompilationUnitStream(stream, javaProject);

		Iterator<Class<?>> it = elementExporter.getNodes().keySet().iterator();
		if (it.hasNext()) {
			List<Class<?>> allLinks = new ArrayList<>();
			ClassLoader loader = it.next().getClassLoader();
			Stream.of(units).forEach(cu -> cu.accept(new AssociationVisitor(allLinks, loader)));

			return allLinks;
		} else {
			// No exported nodes found.
			throw new NotFoundException(); 	
		}		
	}

	private void exportImpliedLinksFromSpecifiedNode(ElementExporter elementExporter,
			List<Class<?>> allLinks, Class<?> node) {
		NodeMap allNodes = elementExporter.getNodes();
		for (Class<?> link : allLinks) { // Load associations.
			try {
				Pair<Class<?>, Class<?>> p = getStartAndEndOfLink(link);

				if ((p.getFirst().equals(node) && allNodes.containsKey(p.getSecond()))
						|| ((p.getSecond().equals(node) && allNodes.containsKey(p.getFirst())))) {
					elementExporter.exportLink(link);
				}
			} catch (ElementExportationException e) {
				// Exportation of this implied link failed. Step to next one.
			}
		}

		Class<?> base = node.getSuperclass();
		if (base != null && allNodes.containsKey(base)) { // Load generalization.
			try {
				elementExporter.exportGeneralization(base, node);
			} catch (ElementExportationException e) {
				// Exportation of implied generalization failed. Nothing to do.
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static Class<?> getClassTypeFromAssocEnd(Class<?> end) throws ElementExportationException {
		if (!AssociationEnd.class.isAssignableFrom(end)) {
			return null;
		}
		try {
			return (Class<? extends ModelClass>) ((ParameterizedType) end.getGenericSuperclass())
					.getActualTypeArguments()[0];
		} catch (Exception e) {
			throw new ElementExportationException();
		}
	}

	private static class AssociationVisitor extends ASTVisitor {

		private final List<Class<?>> allLinks;
		private final ClassLoader loader;

		public AssociationVisitor(List<Class<?>> allLinks, ClassLoader loader) {
			this.allLinks = allLinks;
			this.loader = loader;
		}

		@Override
		public boolean visit(TypeDeclaration node) {

			if (ElementTypeTeller.isAssociation(node)) {
				ITypeBinding typeBinding = node.resolveBinding();
				if (typeBinding != null) {
					try {
						allLinks.add(loader.loadClass(typeBinding.getQualifiedName()));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}

			return false;
		}

	}

}
