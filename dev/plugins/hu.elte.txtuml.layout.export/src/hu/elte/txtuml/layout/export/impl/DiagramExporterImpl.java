package hu.elte.txtuml.layout.export.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.BottomMost;
import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.CompositeDiagram;
import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.api.layout.Diagram.Layout;
import hu.elte.txtuml.api.layout.Diamond;
import hu.elte.txtuml.api.layout.East;
import hu.elte.txtuml.api.layout.Inside;
import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.api.layout.LeftMost;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.Priority;
import hu.elte.txtuml.api.layout.Right;
import hu.elte.txtuml.api.layout.RightMost;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.Show;
import hu.elte.txtuml.api.layout.South;
import hu.elte.txtuml.api.layout.Spacing;
import hu.elte.txtuml.api.layout.TopMost;
import hu.elte.txtuml.api.layout.West;
import hu.elte.txtuml.api.layout.containers.AboveContainer;
import hu.elte.txtuml.api.layout.containers.BelowContainer;
import hu.elte.txtuml.api.layout.containers.ColumnContainer;
import hu.elte.txtuml.api.layout.containers.DiamondContainer;
import hu.elte.txtuml.api.layout.containers.EastContainer;
import hu.elte.txtuml.api.layout.containers.LeftContainer;
import hu.elte.txtuml.api.layout.containers.NorthContainer;
import hu.elte.txtuml.api.layout.containers.PriorityContainer;
import hu.elte.txtuml.api.layout.containers.RightContainer;
import hu.elte.txtuml.api.layout.containers.RowContainer;
import hu.elte.txtuml.api.layout.containers.ShowContainer;
import hu.elte.txtuml.api.layout.containers.SouthContainer;
import hu.elte.txtuml.api.layout.containers.WestContainer;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramExporter;
import hu.elte.txtuml.layout.export.elementinfo.NodeGroupInfo;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.interfaces.StatementExporter;
import hu.elte.txtuml.layout.export.problems.ElementExportationException;
import hu.elte.txtuml.layout.export.problems.ProblemReporter;

/**
 * Default implementation for {@link DiagramExporter}.
 */
public class DiagramExporterImpl implements DiagramExporter {

	private final DiagramExportationReport report;
	private final ProblemReporter problemReporter;
	private final Class<? extends Diagram> diagClass;
	private final ElementExporter elementExporter;
	private final StatementExporter statementExporter;

	public DiagramExporterImpl(String sourceProjectName, Class<? extends Diagram> diagClass) {
		this(sourceProjectName, diagClass, null);
	}

	public DiagramExporterImpl(String sourceProjectName, Class<? extends Diagram> diagClass,
			DiagramExportationReport report) {

		if (report == null) {
			this.report = new DiagramExportationReport();
		} else {
			this.report = report;
		}

		this.problemReporter = new ProblemReporter(this.report);
		this.diagClass = diagClass;
		this.elementExporter = ElementExporter.create(sourceProjectName, problemReporter);
		this.statementExporter = StatementExporter.create(elementExporter,
				problemReporter);
	}

	@Override
	public DiagramExportationReport export() {
		exportDiagram();

		if (report.isSuccessful()) {
			report.setModelName(elementExporter.getModelName());
			report.setType(elementExporter.getDiagramTypeBasedOnElements());
			report.setStatements(statementExporter.getStatements());
			
			report.setNodes(elementExporter.getNodesAsObjects());
			report.setLinks(elementExporter.getLinksAsLines());
		}

		return report;
	}
	
	private void exportDiagram() {
		exportDiagramBody(diagClass);
		elementExporter.exportDefaultParentage();
		// exportation finalizers
		statementExporter.resolveMosts();
		statementExporter.exportPhantoms();
		elementExporter.exportImpliedLinks();
		
		if(isClassDiagram(diagClass))
		{
			report.setReferencedElementName(elementExporter.getModelName());
		} 
		else if(isStateMachineDiagram(diagClass) || isCompositeDiagram(diagClass)) {
			Class<?> cls = (Class<?>)((ParameterizedType)diagClass.getGenericSuperclass()).getActualTypeArguments()[0];
			report.setReferencedElementName(cls.getCanonicalName());
		}
		else {
			report.error("No proper Diagram class found (ClassDiagram, StateMachineDiagram<T> or CompositeStructureDiagram)");
		}
	}
	
	private void exportDiagramBody(Class<?> diagClass)
	{
		exportDiagramBody(diagClass, null);
	}
	
	@SuppressWarnings("unchecked")
	// All casts are checked with reflection.
	private void exportDiagramBody(Class<?> diagClass, Class<?> phantomParent)
	{
		Class<? extends Diagram.Layout> layoutClass = null;

		for (Class<?> innerClass : diagClass.getDeclaredClasses()) {
			try {
				if (ElementExporter.isNodeGroup(innerClass)) {
					NodeGroupInfo info = elementExporter
							.exportNodeGroup(innerClass);

					if (info.getAlignment() != null) {
						statementExporter.exportAlignment(info);
					}

				} else if (ElementExporter.isLinkGroup(innerClass)) {
					elementExporter.exportLinkGroup(innerClass);

				} else if (ElementExporter.isPhantom(innerClass)) {
					elementExporter.exportPhantom(innerClass);
					elementExporter.startOfParent(innerClass);
					exportDiagramBody(innerClass, innerClass);
					elementExporter.endOfParent();
					
				} else if(ElementExporter.isBoxContainer(innerClass)){
					Class<?> boxClass = statementExporter.exportInside(innerClass.getAnnotation(Inside.class));
					elementExporter.startOfParent(boxClass);
					exportDiagramBody(innerClass);
					elementExporter.endOfParent();

				} else if (isLayout(innerClass)) {
					if (layoutClass != null) {
						problemReporter.moreThanOneLayoutClass(layoutClass,
								innerClass);
					} else {
						layoutClass = (Class<? extends Layout>) innerClass;
					}

				} else {
					problemReporter.unknownInnerClassOfDiagram(innerClass);

				}
			} catch (ElementExportationException e) {
				// No report is needed.
			}

		}

		if (layoutClass == null) {
			problemReporter.noLayoutClass();
		} else {
			exportLayout(layoutClass);
		}
	}
	
	private boolean isClassDiagram(Class<? extends Diagram> cls)
	{
		return ClassDiagram.class.isAssignableFrom(cls);
	}
	
	private boolean isStateMachineDiagram(Class<? extends Diagram> cls)
	{
		return StateMachineDiagram.class.isAssignableFrom(cls);
	}
	
	private boolean isCompositeDiagram(Class<? extends Diagram> cls) {
		return CompositeDiagram.class.isAssignableFrom(cls);
	}
	
	private boolean isLayout(Class<?> cls) {
		return Layout.class.isAssignableFrom(cls);
	}

	private void exportLayout(Class<?> layoutClass) {
		for (Annotation annot : layoutClass.getAnnotations()) {
			if (isOfType(Above.class, annot)) {
				statementExporter.exportAbove((Above) annot);

			} else if (isOfType(Below.class, annot)) {
				statementExporter.exportBelow((Below) annot);

			} else if (isOfType(Right.class, annot)) {
				statementExporter.exportRight((Right) annot);

			} else if (isOfType(Left.class, annot)) {
				statementExporter.exportLeft((Left) annot);

			} else if (isOfType(North.class, annot)) {
				statementExporter.exportNorth((North) annot);

			} else if (isOfType(South.class, annot)) {
				statementExporter.exportSouth((South) annot);

			} else if (isOfType(East.class, annot)) {
				statementExporter.exportEast((East) annot);

			} else if (isOfType(West.class, annot)) {
				statementExporter.exportWest((West) annot);

			} else if (isOfType(TopMost.class, annot)) {
				statementExporter.exportTopMost((TopMost) annot);

			} else if (isOfType(BottomMost.class, annot)) {
				statementExporter.exportBottomMost((BottomMost) annot);

			} else if (isOfType(RightMost.class, annot)) {
				statementExporter.exportRightMost((RightMost) annot);

			} else if (isOfType(LeftMost.class, annot)) {
				statementExporter.exportLeftMost((LeftMost) annot);

			} else if (isOfType(Priority.class, annot)) {
				statementExporter.exportPriority((Priority) annot);

			} else if (isOfType(Show.class, annot)) {
				statementExporter.exportShow((Show) annot);

			} else if (isOfType(Column.class, annot)) {
				statementExporter.exportColumn((Column) annot);

			} else if (isOfType(Row.class, annot)) {
				statementExporter.exportRow((Row) annot);

			} else if (isOfType(Diamond.class, annot)) {
				statementExporter.exportDiamond((Diamond) annot);

			} else if (isOfType(Spacing.class, annot)) {
				statementExporter.exportSpacing((Spacing) annot);
				
			} else if (isOfType(AboveContainer.class, annot)) {
				statementExporter.exportAboveContainer((AboveContainer) annot);

			} else if (isOfType(BelowContainer.class, annot)) {
				statementExporter.exportBelowContainer((BelowContainer) annot);

			} else if (isOfType(RightContainer.class, annot)) {
				statementExporter.exportRightContainer((RightContainer) annot);

			} else if (isOfType(LeftContainer.class, annot)) {
				statementExporter.exportLeftContainer((LeftContainer) annot);

			} else if (isOfType(NorthContainer.class, annot)) {
				statementExporter.exportNorthContainer((NorthContainer) annot);

			} else if (isOfType(SouthContainer.class, annot)) {
				statementExporter.exportSouthContainer((SouthContainer) annot);

			} else if (isOfType(EastContainer.class, annot)) {
				statementExporter.exportEastContainer((EastContainer) annot);

			} else if (isOfType(WestContainer.class, annot)) {
				statementExporter.exportWestContainer((WestContainer) annot);

			} else if (isOfType(PriorityContainer.class, annot)) {
				statementExporter
						.exportPriorityContainer((PriorityContainer) annot);

			} else if (isOfType(ShowContainer.class, annot)) {
				statementExporter.exportShowContainer((ShowContainer) annot);

			} else if (isOfType(ColumnContainer.class, annot)) {
				statementExporter
						.exportColumnContainer((ColumnContainer) annot);

			} else if (isOfType(RowContainer.class, annot)) {
				statementExporter.exportRowContainer((RowContainer) annot);

			} else if (isOfType(DiamondContainer.class, annot)) {
				statementExporter
						.exportDiamondContainer((DiamondContainer) annot);

			} else {
				problemReporter.unknownAnnotationOnClass(annot, layoutClass);

			}

		}
	}

	private static boolean isOfType(
			Class<? extends Annotation> annotationClass, Annotation annot) {
		return annot.annotationType() == annotationClass;
	}

}
