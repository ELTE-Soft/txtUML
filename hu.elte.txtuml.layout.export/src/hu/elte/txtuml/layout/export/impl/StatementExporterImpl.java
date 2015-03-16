package hu.elte.txtuml.layout.export.impl;

import hu.elte.txtuml.layout.export.interfaces.StatementExporter;
import hu.elte.txtuml.layout.export.interfaces.StatementList;
import hu.elte.txtuml.layout.lang.LinkEnd;
import hu.elte.txtuml.layout.lang.elements.LayoutElement;
import hu.elte.txtuml.layout.lang.elements.LayoutGroup;
import hu.elte.txtuml.layout.lang.statements.Above;
import hu.elte.txtuml.layout.lang.statements.Alignment;
import hu.elte.txtuml.layout.lang.statements.Below;
import hu.elte.txtuml.layout.lang.statements.BottomMost;
import hu.elte.txtuml.layout.lang.statements.Contains;
import hu.elte.txtuml.layout.lang.statements.East;
import hu.elte.txtuml.layout.lang.statements.Left;
import hu.elte.txtuml.layout.lang.statements.LeftMost;
import hu.elte.txtuml.layout.lang.statements.North;
import hu.elte.txtuml.layout.lang.statements.Priority;
import hu.elte.txtuml.layout.lang.statements.Right;
import hu.elte.txtuml.layout.lang.statements.RightMost;
import hu.elte.txtuml.layout.lang.statements.South;
import hu.elte.txtuml.layout.lang.statements.TopMost;
import hu.elte.txtuml.layout.lang.statements.West;
import hu.elte.txtuml.layout.lang.statements.containers.AboveContainer;
import hu.elte.txtuml.layout.lang.statements.containers.BelowContainer;
import hu.elte.txtuml.layout.lang.statements.containers.EastContainer;
import hu.elte.txtuml.layout.lang.statements.containers.LeftContainer;
import hu.elte.txtuml.layout.lang.statements.containers.NorthContainer;
import hu.elte.txtuml.layout.lang.statements.containers.PriorityContainer;
import hu.elte.txtuml.layout.lang.statements.containers.RightContainer;
import hu.elte.txtuml.layout.lang.statements.containers.SouthContainer;
import hu.elte.txtuml.layout.lang.statements.containers.WestContainer;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;

public class StatementExporterImpl implements StatementExporter {

	private final StatementList statements;

	public StatementExporterImpl(StatementList statements) {
		this.statements = statements;
	}

	private String asString(Class<? extends LayoutElement> element) {
		return element.getSimpleName();
	}

	private void newStatementWithLinkEndCheck(StatementType type, String param1, String param2, LinkEnd end) {
		if (end == LinkEnd.Default) {
			statements.addNew(type, param1, param2);
		} else {
			statements.addNew(type, param1, param2, end.toString());			
		}
	}
	
	// exporters

	@Override
	public void exportAlignment(Class<? extends LayoutGroup> group,
			Alignment annot) {
		// FIXME
		//statements.addNew(StatementType.layout, asString(group), annot.value()
			//	.toString());
	}

	@Override
	public void exportContains(Class<? extends LayoutGroup> group,
			Contains annot) {
		// FIXME
		//for (Class<? extends LayoutElement> element : annot.value()) {
			//statements.addNew(StatementType.group, asString(element),
				//	asString(group));
		//}
	}

	@Override
	public void exportAbove(Above annot) {
		statements.addNew(StatementType.above, asString(annot.val()),
				asString(annot.from()));
	}

	@Override
	public void exportBelow(Below annot) {
		statements.addNew(StatementType.below, asString(annot.val()),
				asString(annot.from()));
	}

	@Override
	public void exportRight(Right annot) {
		statements.addNew(StatementType.right, asString(annot.val()),
				asString(annot.from()));
	}

	@Override
	public void exportLeft(Left annot) {
		statements.addNew(StatementType.left, asString(annot.val()),
				asString(annot.from()));
	}

	@Override
	public void exportNorth(North annot) {
		newStatementWithLinkEndCheck(StatementType.north,
				asString(annot.val()), asString(annot.from()), annot.end());
	}

	@Override
	public void exportSouth(South annot) {
		newStatementWithLinkEndCheck(StatementType.south,
				asString(annot.val()), asString(annot.from()), annot.end());
	}

	@Override
	public void exportEast(East annot) {
		newStatementWithLinkEndCheck(StatementType.east,
				asString(annot.val()), asString(annot.from()), annot.end());
	}

	@Override
	public void exportWest(West annot) {
		newStatementWithLinkEndCheck(StatementType.west,
				asString(annot.val()), asString(annot.from()), annot.end());
	}

	@Override
	public void exportTopMost(TopMost annot) {
		// FIXME
		//statements.addNew(StatementType.topmost, asString(annot.value()));
	}

	@Override
	public void exportBottomMost(BottomMost annot) {
		// FIXME
		//statements.addNew(StatementType.bottommost, asString(annot.value()));
	}

	@Override
	public void exportLeftMost(LeftMost annot) {
		// FIXME
		//statements.addNew(StatementType.leftmost, asString(annot.value()));
	}

	@Override
	public void exportRightMost(RightMost annot) {
		// FIXME
		//statements.addNew(StatementType.rightmost, asString(annot.value()));
	}

	@Override
	public void exportPriority(Priority annot) {
		statements.addNew(StatementType.priority, asString(annot.val()),
				Integer.toString(annot.prior()));
	}

	// container exporters

	@Override
	public void exportAboveContainer(AboveContainer annot) {
		for (Above a : annot.value()) {
			exportAbove(a);
		}
	}

	@Override
	public void exportBelowContainer(BelowContainer annot) {
		for (Below a : annot.value()) {
			exportBelow(a);
		}
	}

	@Override
	public void exportRightContainer(RightContainer annot) {
		for (Right a : annot.value()) {
			exportRight(a);
		}
	}

	@Override
	public void exportLeftContainer(LeftContainer annot) {
		for (Left a : annot.value()) {
			exportLeft(a);
		}
	}

	@Override
	public void exportNorthContainer(NorthContainer annot) {
		for (North a : annot.value()) {
			exportNorth(a);
		}
	}

	@Override
	public void exportSouthContainer(SouthContainer annot) {
		for (South a : annot.value()) {
			exportSouth(a);
		}
	}

	@Override
	public void exportEastContainer(EastContainer annot) {
		for (East a : annot.value()) {
			exportEast(a);
		}
	}

	@Override
	public void exportWestContainer(WestContainer annot) {
		for (West a : annot.value()) {
			exportWest(a);
		}
	}

	@Override
	public void exportPriorityContainer(PriorityContainer annot) {
		for (Priority a : annot.value()) {
			exportPriority(a);
		}
	}

}
