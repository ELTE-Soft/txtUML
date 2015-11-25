package hu.elte.txtuml.layout.export;

import hu.elte.txtuml.api.layout.Alignment;
import hu.elte.txtuml.api.layout.AlignmentType;
import hu.elte.txtuml.api.layout.Contains;
import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Diamond;
import hu.elte.txtuml.api.layout.East;
import hu.elte.txtuml.api.layout.LinkEnd;
import hu.elte.txtuml.api.layout.Priority;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.Show;
import hu.elte.txtuml.api.layout.West;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.export.Model2.BaseOfColumnTop;
import hu.elte.txtuml.layout.export.Model2.BaseOfDiamond;
import hu.elte.txtuml.layout.export.Model2.BetweenBases;
import hu.elte.txtuml.layout.export.Model2.ColumnBottom;
import hu.elte.txtuml.layout.export.Model2.ColumnBottom_ColumnBottom;
import hu.elte.txtuml.layout.export.Model2.ColumnMiddle;
import hu.elte.txtuml.layout.export.Model2.ColumnMiddle_ColumnBottom;
import hu.elte.txtuml.layout.export.Model2.ColumnTop;
import hu.elte.txtuml.layout.export.Model2.ColumnTop_ColumnMiddle;
import hu.elte.txtuml.layout.export.Model2.DiamondBottom;
import hu.elte.txtuml.layout.export.Model2.DiamondBottom_DiamondLeft;
import hu.elte.txtuml.layout.export.Model2.DiamondBottom_DiamondRight;
import hu.elte.txtuml.layout.export.Model2.DiamondLeft;
import hu.elte.txtuml.layout.export.Model2.DiamondLeft_DiamondRight;
import hu.elte.txtuml.layout.export.Model2.DiamondRight;
import hu.elte.txtuml.layout.export.Model2.DiamondTop;
import hu.elte.txtuml.layout.export.Model2.DiamondTop_DiamondBottom;
import hu.elte.txtuml.layout.export.Model2.DiamondTop_DiamondLeft;
import hu.elte.txtuml.layout.export.Model2.DiamondTop_DiamondRight;
import hu.elte.txtuml.layout.export.Model2.Random;
import hu.elte.txtuml.layout.export.Model2.Random_BaseOfColumnTop;
import hu.elte.txtuml.layout.export.Model2.Random_BaseOfDiamond;
import hu.elte.txtuml.layout.export.Model2.Random_BetweenBases;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

import org.junit.Test;

class Model2 extends Model {

	// Classes
	class Random extends ModelClass {
	}

	class BaseOfColumnTop extends ModelClass {
	}

	class ColumnTop extends BaseOfColumnTop {
	}

	class ColumnMiddle extends ModelClass {
	}

	class ColumnBottom extends ModelClass {
	}

	class BetweenBases extends ModelClass {
	}

	class BaseOfDiamond extends ModelClass {
	}

	class DiamondTop extends BaseOfDiamond {
	}

	class DiamondLeft extends BaseOfDiamond {
	}

	class DiamondBottom extends BaseOfDiamond {
	}

	class DiamondRight extends BaseOfDiamond {
	}

	// Associations
	class ColumnTop_ColumnMiddle extends Association {
		class end1 extends One<ColumnTop> {
		}

		class end2 extends One<ColumnMiddle> {
		}
	}

	class ColumnMiddle_ColumnBottom extends Association {
		class end1 extends One<ColumnMiddle> {
		}

		class end2 extends One<ColumnBottom> {
		}
	}

	class ColumnBottom_ColumnBottom extends Association {
		class end1 extends One<ColumnBottom> {
		}

		class end2 extends One<ColumnBottom> {
		}
	}

	class DiamondTop_DiamondLeft extends Association {
		class end1 extends One<DiamondTop> {
		}

		class end2 extends One<DiamondLeft> {
		}
	}

	class DiamondTop_DiamondBottom extends Association {
		class end1 extends One<DiamondTop> {
		}

		class end2 extends One<DiamondBottom> {
		}
	}

	class DiamondTop_DiamondRight extends Association {
		class end1 extends One<DiamondTop> {
		}

		class end2 extends One<DiamondRight> {
		}
	}

	class DiamondLeft_DiamondRight extends Association {
		class end1 extends One<DiamondLeft> {
		}

		class end2 extends One<DiamondRight> {
		}
	}

	class DiamondBottom_DiamondLeft extends Association {
		class end1 extends One<DiamondBottom> {
		}

		class end2 extends One<DiamondLeft> {
		}
	}

	class DiamondBottom_DiamondRight extends Association {
		class end1 extends One<DiamondBottom> {
		}

		class end2 extends One<DiamondRight> {
		}
	}

	class Random_BaseOfColumnTop extends Association {
		class end1 extends One<Random> {
		}

		class end2 extends One<BaseOfColumnTop> {
		}
	}

	class Random_BetweenBases extends Association {
		class end1 extends One<Random> {
		}

		class end2 extends One<BetweenBases> {
		}
	}

	class Random_BaseOfDiamond extends Association {
		class end1 extends One<Random> {
		}

		class end2 extends One<BaseOfDiamond> {
		}
	}

}

class Diagram2 extends Diagram {

	@Contains({ ColumnTop.class, ColumnMiddle.class, ColumnBottom.class })
	@Alignment(AlignmentType.TopToBottom)
	class ColumnGroup extends NodeGroup {
	}

	@Diamond(top = DiamondTop.class, left = DiamondLeft.class, bottom = DiamondBottom.class, right = DiamondRight.class)
	@West(val = ColumnGroup.class, from = { DiamondTop.class,
			DiamondLeft.class, DiamondRight.class, DiamondBottom.class })
	@Row({ BaseOfColumnTop.class, BetweenBases.class, BaseOfDiamond.class })
	@Show(Random.class)
	@West(val = ColumnBottom_ColumnBottom.class, from = ColumnBottom.class, end = LinkEnd.Start)
	@East(val = ColumnBottom_ColumnBottom.class, from = ColumnBottom.class, end = LinkEnd.End)
	@Priority(val = DiamondTop_DiamondBottom.class, prior = 10)
	class L extends Layout {
	}

}

public class Test2 extends BaseTest {

	@Test
	public void run() {

		type = DiagramType.Class;

		node(Random.class);
		node(BaseOfColumnTop.class);
		node(ColumnTop.class);
		node(ColumnMiddle.class);
		node(ColumnBottom.class);
		node(BetweenBases.class);
		node(BaseOfDiamond.class);
		node(DiamondTop.class);
		node(DiamondLeft.class);
		node(DiamondBottom.class);
		node(DiamondRight.class);

		generalization(BaseOfColumnTop.class, ColumnTop.class);
		generalization(BaseOfDiamond.class, DiamondTop.class);
		generalization(BaseOfDiamond.class, DiamondLeft.class);
		generalization(BaseOfDiamond.class, DiamondBottom.class);
		generalization(BaseOfDiamond.class, DiamondRight.class);

		undirectedLink(ColumnTop_ColumnMiddle.class, ColumnTop.class,
				ColumnMiddle.class);
		undirectedLink(ColumnMiddle_ColumnBottom.class, ColumnMiddle.class,
				ColumnBottom.class);
		undirectedLink(ColumnBottom_ColumnBottom.class, ColumnBottom.class,
				ColumnBottom.class);
		undirectedLink(DiamondTop_DiamondLeft.class, DiamondTop.class,
				DiamondLeft.class);
		undirectedLink(DiamondTop_DiamondBottom.class, DiamondTop.class,
				DiamondBottom.class);
		undirectedLink(DiamondTop_DiamondRight.class, DiamondTop.class,
				DiamondRight.class);
		undirectedLink(DiamondLeft_DiamondRight.class, DiamondLeft.class,
				DiamondRight.class);
		undirectedLink(DiamondBottom_DiamondLeft.class, DiamondBottom.class,
				DiamondLeft.class);
		undirectedLink(DiamondBottom_DiamondRight.class, DiamondBottom.class,
				DiamondRight.class);
		undirectedLink(Random_BaseOfColumnTop.class, Random.class,
				BaseOfColumnTop.class);
		undirectedLink(Random_BetweenBases.class, Random.class,
				BetweenBases.class);
		undirectedLink(Random_BaseOfDiamond.class, Random.class,
				BaseOfDiamond.class);

		statement(StatementType.above, DiamondTop.class, "#phantom_1");
		statement(StatementType.right, DiamondRight.class, "#phantom_1");
		statement(StatementType.below, DiamondBottom.class, "#phantom_1");
		statement(StatementType.left, DiamondLeft.class, "#phantom_1");

		statement(StatementType.west, ColumnTop.class, DiamondTop.class);
		statement(StatementType.west, ColumnTop.class, DiamondRight.class);
		statement(StatementType.west, ColumnTop.class, DiamondBottom.class);
		statement(StatementType.west, ColumnTop.class, DiamondLeft.class);

		statement(StatementType.west, ColumnMiddle.class, DiamondTop.class);
		statement(StatementType.west, ColumnMiddle.class, DiamondRight.class);
		statement(StatementType.west, ColumnMiddle.class, DiamondBottom.class);
		statement(StatementType.west, ColumnMiddle.class, DiamondLeft.class);

		statement(StatementType.west, ColumnBottom.class, DiamondTop.class);
		statement(StatementType.west, ColumnBottom.class, DiamondRight.class);
		statement(StatementType.west, ColumnBottom.class, DiamondBottom.class);
		statement(StatementType.west, ColumnBottom.class, DiamondLeft.class);

		statement(StatementType.left, BaseOfColumnTop.class, BetweenBases.class);
		statement(StatementType.left, BetweenBases.class, BaseOfDiamond.class);

		statement(StatementType.west, ColumnBottom_ColumnBottom.class,
				ColumnBottom.class, LinkEnd.Start.name());
		statement(StatementType.east, ColumnBottom_ColumnBottom.class,
				ColumnBottom.class, LinkEnd.End.name());

		statement(StatementType.priority, DiamondTop_DiamondBottom.class, 10);

		statement(StatementType.north, ColumnTop.class, ColumnMiddle.class);
		statement(StatementType.north, ColumnMiddle.class, ColumnBottom.class);

		statement(StatementType.phantom, "#phantom_1");
		
		testForSuccess(Diagram2.class);

	}
}