package hu.elte.txtuml.layout.export.impl;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import hu.elte.txtuml.layout.export.elementinfo.ElementInfo;
import hu.elte.txtuml.layout.export.elementinfo.ElementType;
import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeGroupInfo;
import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.interfaces.ElementExporter;
import hu.elte.txtuml.layout.export.interfaces.NodeMap;
import hu.elte.txtuml.layout.export.interfaces.StatementExporter;
import hu.elte.txtuml.layout.export.interfaces.StatementList;
import hu.elte.txtuml.layout.lang.LinkEnd;
import hu.elte.txtuml.layout.lang.elements.LayoutAbstractLink;
import hu.elte.txtuml.layout.lang.elements.LayoutAbstractNode;
import hu.elte.txtuml.layout.lang.elements.LayoutElement;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.lang.elements.LayoutNonGroupElement;
import hu.elte.txtuml.layout.lang.statements.Above;
import hu.elte.txtuml.layout.lang.statements.Below;
import hu.elte.txtuml.layout.lang.statements.BottomMost;
import hu.elte.txtuml.layout.lang.statements.Column;
import hu.elte.txtuml.layout.lang.statements.Diamond;
import hu.elte.txtuml.layout.lang.statements.East;
import hu.elte.txtuml.layout.lang.statements.Left;
import hu.elte.txtuml.layout.lang.statements.LeftMost;
import hu.elte.txtuml.layout.lang.statements.North;
import hu.elte.txtuml.layout.lang.statements.Priority;
import hu.elte.txtuml.layout.lang.statements.Right;
import hu.elte.txtuml.layout.lang.statements.RightMost;
import hu.elte.txtuml.layout.lang.statements.Row;
import hu.elte.txtuml.layout.lang.statements.Show;
import hu.elte.txtuml.layout.lang.statements.South;
import hu.elte.txtuml.layout.lang.statements.TopMost;
import hu.elte.txtuml.layout.lang.statements.West;
import hu.elte.txtuml.layout.lang.statements.containers.AboveContainer;
import hu.elte.txtuml.layout.lang.statements.containers.BelowContainer;
import hu.elte.txtuml.layout.lang.statements.containers.ColumnContainer;
import hu.elte.txtuml.layout.lang.statements.containers.DiamondContainer;
import hu.elte.txtuml.layout.lang.statements.containers.EastContainer;
import hu.elte.txtuml.layout.lang.statements.containers.LeftContainer;
import hu.elte.txtuml.layout.lang.statements.containers.NorthContainer;
import hu.elte.txtuml.layout.lang.statements.containers.PriorityContainer;
import hu.elte.txtuml.layout.lang.statements.containers.RightContainer;
import hu.elte.txtuml.layout.lang.statements.containers.RowContainer;
import hu.elte.txtuml.layout.lang.statements.containers.ShowContainer;
import hu.elte.txtuml.layout.lang.statements.containers.SouthContainer;
import hu.elte.txtuml.layout.lang.statements.containers.WestContainer;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;

/**
 * Default implementation for {@link StatementExporter}.
 * 
 * @author Gábor Ferenc Kovács
 *
 */
public class StatementExporterImpl implements StatementExporter {

	private final StatementList statements;
	private final ElementExporter elementExporter;
	private final Map<Class<? extends Annotation>, NodeGroupInfo> mostMap;

	public StatementExporterImpl(ElementExporter elementExporter) {
	    this.statements = StatementList.create();
	    this.elementExporter = elementExporter;
	    this.mostMap = new HashMap<Class<? extends Annotation>, NodeGroupInfo>();
	}
	
	// unused constructor
	// TODO check if additional constructors are needed
	
	/*public StatementExporterImpl(StatementList statements, ElementExporter elementExporter) {
		this.statements = statements;
		this.elementExporter = elementExporter;
	}*/
	
	public StatementList getStatements() {
	    return statements;
	}
	
	// public statement exporters

	@Override
	public void exportAlignment(NodeGroupInfo info) {
        StatementType type;
	    switch (info.getAlignment()) {
    	    case LeftToRight:
    	        type = StatementType.west;
    	        break;
	        
    	    case RightToLeft:
    	        type = StatementType.east;
    	        break;
	    
    	    case TopToBottom:
    	        type = StatementType.north;
    	        break;
    	    
    	    case BottomToTop:
    	        type = StatementType.south;
    	        break;
    	        
    	    default:
    	        type = StatementType.unknown;
	    }
	    
	    exportAlignmentWithGivenStatement(info, type);
	}

	@Override
	public void exportAbove(Above annot) {
		exportAdjacencyStatement(StatementType.above, annot.val(), annot.from());
	}

	@Override
	public void exportBelow(Below annot) {
		exportAdjacencyStatement(StatementType.below, annot.val(), annot.from());
	}

	@Override
	public void exportRight(Right annot) {
		exportAdjacencyStatement(StatementType.right, annot.val(), annot.from());
	}

	@Override
	public void exportLeft(Left annot) {
		exportAdjacencyStatement(StatementType.left, annot.val(), annot.from());
	}

	@Override
	public void exportNorth(North annot) {
	    exportCardinalStatement(StatementType.north, annot.val(), annot.from(), annot.end());
	}

	@Override
	public void exportSouth(South annot) {
	    exportCardinalStatement(StatementType.south, annot.val(), annot.from(), annot.end());
	}

	@Override
	public void exportEast(East annot) {
	    exportCardinalStatement(StatementType.east, annot.val(), annot.from(), annot.end());
	}

	@Override
	public void exportWest(West annot) {
	    exportCardinalStatement(StatementType.west, annot.val(), annot.from(), annot.end());
	}

	@Override
	public void exportTopMost(TopMost annot) {
		processMostStatement(TopMost.class, annot.value());
	}

	@Override
	public void exportBottomMost(BottomMost annot) {
	    processMostStatement(BottomMost.class, annot.value());
	}

	@Override
	public void exportLeftMost(LeftMost annot) {
	    processMostStatement(LeftMost.class, annot.value());
	}

	@Override
	public void exportRightMost(RightMost annot) {
	    processMostStatement(RightMost.class, annot.value());
	}

	@Override
	public void exportPriority(Priority annot) {
		for (Class<? extends LayoutAbstractLink> link : annot.val()) {
		    ElementInfo info = elementExporter.exportElement(link);
		    ElementType type = info.getType();
		    
		    if (type.equals(ElementType.Link)) {
		        statements.addNew(StatementType.priority, info.toString(), Integer.toString(annot.prior()));
		    } else if (type.equals(ElementType.LinkGroup)) {
		        for (LinkInfo innerLink : info.asLinkGroupInfo().getAllLinks().values()) {
		            statements.addNew(StatementType.priority, innerLink.toString(), Integer.toString(annot.prior()));
		        }
		    }
		}	
	}

	@Override
	public void exportShow(Show annot) {
		for (Class<? extends LayoutNonGroupElement> element : annot.value()) {
			elementExporter.exportNonGroupElement(element);
		}
		// TODO show warning if empty
	}

	@Override
	public void exportColumn(Column annot) {
        NodeGroupInfo info = elementExporter.exportAnonNodeGroup(annot.value()).asNodeGroupInfo();
        
        if (info == null) {
            // TODO show error
            return;
        }
        
        exportAlignmentWithGivenStatement(info, StatementType.above);	
	}

	@Override
	public void exportRow(Row annot) {
        NodeGroupInfo info = elementExporter.exportAnonNodeGroup(annot.value()).asNodeGroupInfo();
        
        if (info == null) {
            // TODO show error
            return;
        }
        
        exportAlignmentWithGivenStatement(info, StatementType.left);
	}

	@Override
	public void exportDiamond(Diamond annot) {		
		NodeInfo top = elementExporter.exportNode(annot.top()).asNodeInfo();
		if (top == null) {
		    // show error
		    return;
		}
		
		NodeInfo right = elementExporter.exportNode(annot.right()).asNodeInfo();
		if (right == null) {
		    // show error
		    return;
		}
		
		NodeInfo bottom = elementExporter.exportNode(annot.bottom()).asNodeInfo();
		if (bottom == null) {
		    // show error
		    return;
		}
		
		NodeInfo left = elementExporter.exportNode(annot.left()).asNodeInfo();
		if (left == null) {
		    // show error
		    return;
		}
		
	    NodeInfo phantom = elementExporter.createPhantom();
		
		statements.addNew(StatementType.above, top.toString(), phantom.toString());
		statements.addNew(StatementType.right, right.toString(), phantom.toString());
		statements.addNew(StatementType.below, bottom.toString(), phantom.toString());
		statements.addNew(StatementType.left, left.toString(), phantom.toString());	
	}
	
	// public statement container exporters

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

	@Override
	public void exportShowContainer(ShowContainer annot) {
		for (Show a : annot.value()) {
			exportShow(a);
		}		
	}

	@Override
	public void exportColumnContainer(ColumnContainer annot) {
		for (Column a : annot.value()) {
			exportColumn(a);
		}		
	}

	@Override
	public void exportRowContainer(RowContainer annot) {
		for (Row a : annot.value()) {
			exportRow(a);
		}		
	}

	@Override
	public void exportDiamondContainer(DiamondContainer annot) {
		for (Diamond a : annot.value()) {
			exportDiamond(a);
		}		
	}

    @Override
    public void resolveMosts() {
        NodeGroupInfo topMostInfo = mostMap.get(TopMost.class);
        if (topMostInfo != null) {
            exportMostStatement(StatementType.north, topMostInfo);
        }

        NodeGroupInfo bottomMostInfo = mostMap.get(BottomMost.class);
        if (bottomMostInfo != null) {
            exportMostStatement(StatementType.south, bottomMostInfo);
        }

        NodeGroupInfo rightMostInfo = mostMap.get(RightMost.class);
        if (rightMostInfo != null) {
            exportMostStatement(StatementType.east, rightMostInfo);
        }

        NodeGroupInfo leftMostInfo = mostMap.get(LeftMost.class);
        if (leftMostInfo != null) {
            exportMostStatement(StatementType.west, leftMostInfo);
        }
    }
    
    @Override
    public void exportPhantoms() {
        for (NodeInfo phantom : elementExporter.getPhantoms()) {
            statements.addNew(StatementType.phantom, phantom.toString());
        }
    }
    
    // inner exporter methods

    private void newStatementWithLinkEndCheck(StatementType type, String param1, String param2, LinkEnd end) {
        if (end == LinkEnd.Default) {
            statements.addNew(type, param1, param2);
        } else {
            statements.addNew(type, param1, param2, end.toString());            
        }
    }
    
    private void exportAdjacencyStatement(StatementType type, Class<? extends LayoutNode> val, Class<? extends LayoutNode> from) {
        NodeInfo valInfo = elementExporter.exportNode(val).asNodeInfo();
        NodeInfo fromInfo = elementExporter.exportNode(from).asNodeInfo();
        if (valInfo == null || fromInfo == null) {
            // TODO show error
            return;
        }
        statements.addNew(type, valInfo.toString(), fromInfo.toString());
    }
    
    private void exportCardinalStatement(StatementType type,
            Class<? extends LayoutElement>[] val,
            Class<? extends LayoutElement>[] from,
            LinkEnd end)
    {
        if (val.length == 0 || from.length == 0) {
            // TODO show error
            return;
        }
        
        // type(link, node, [linkend])
        if (val.length == 1 && from.length == 1) {
            LinkInfo valInfo = elementExporter.exportElement(val[0]).asLinkInfo();
            NodeInfo fromInfo = elementExporter.exportElement(from[0]).asNodeInfo();
            
            if (valInfo != null && fromInfo != null) {
                newStatementWithLinkEndCheck(type, valInfo.toString(), fromInfo.toString(), end);
                return;
            }
        }
        
        // else, TODO type checks
        for (Class<? extends LayoutElement> valClass : val) {
            ElementInfo valInfo = elementExporter.exportElement(valClass);
            ElementType valType = valInfo.getType();
            
            for (Class<? extends LayoutElement> fromClass : from) {           
                ElementInfo fromInfo = elementExporter.exportElement(fromClass);
                ElementType fromType = fromInfo.getType();
                
                // type(node, node) || type(link, node)
                // not sure if the latter is necessary
                if ((valType.equals(ElementType.Node) || valType.equals(ElementType.Link)) && fromType.equals(ElementType.Node)) {
                    statements.addNew(type, valInfo.toString(), fromInfo.toString());
                }
                
                // type(linkGroup, node)
                else if (valType.equals(ElementType.LinkGroup) && fromType.equals(ElementType.Node)) {
                    for (LinkInfo innerValLinkInfo : valInfo.asLinkGroupInfo().getAllLinks().values()) {
                        statements.addNew(type, innerValLinkInfo.toString(), fromInfo.toString());
                    }
                }
                
                // type(node, nodegroup)
                else if (valType.equals(ElementType.Node) && fromType.equals(ElementType.NodeGroup)) {
                    for (NodeInfo innerFromNodeInfo : fromInfo.asNodeGroupInfo().getAllNodes().values()) {
                        statements.addNew(type, valInfo.toString(), innerFromNodeInfo.toString());
                    }
                }
                
                // type(nodegroup, node)
                else if (valType.equals(ElementType.NodeGroup) && fromType.equals(ElementType.Node)) {
                    for (NodeInfo innerValNodeInfo : valInfo.asNodeGroupInfo().getAllNodes().values()) {
                        statements.addNew(type, innerValNodeInfo.toString(), fromInfo.toString());
                    }
                }
                
                // type(nodegroup, nodegroup)
                else if (valType.equals(ElementType.NodeGroup) && fromType.equals(ElementType.NodeGroup)) {
                    for (NodeInfo innerValNodeInfo : valInfo.asNodeGroupInfo().getAllNodes().values()) {
                        for (NodeInfo innerFromNodeInfo : fromInfo.asNodeGroupInfo().getAllNodes().values()) {
                            statements.addNew(type, innerValNodeInfo.toString(), innerFromNodeInfo.toString());
                        }
                    }
                }
                
                else {
                    // TODO show error
                }
            }
        }
    }
    
    private void processMostStatement(Class<? extends Annotation> type, Class<? extends LayoutAbstractNode>[] val) {
        if (mostMap.containsKey(type)) {
            // TODO show error
            return;
        }

        NodeGroupInfo info = elementExporter.exportAnonNodeGroup(val).asNodeGroupInfo();
        
        if (info == null) {
            // TODO show error
            return;
        }
        
        mostMap.put(type, info);
    }
    
    private void exportMostStatement(StatementType type, NodeGroupInfo info) {
        NodeMap allNodes = elementExporter.getNodes();
        NodeMap mostedNodes = info.getAllNodes();
        
        for (Class<? extends LayoutNode> nodeClass : allNodes.keySet()) {       
            if (!mostedNodes.containsKey(nodeClass)) {
                for (NodeInfo valNodeInfo : mostedNodes.values()) {
                    statements.addNew(type, valNodeInfo.toString(), allNodes.get(nodeClass).toString());
                }
            }
        }
    }
    
    private void exportAlignmentWithGivenStatement(NodeGroupInfo info, StatementType type) {
        NodeInfo prevNode = null;
        for (NodeInfo currNode : info.getAllNodes().values()) {
            if (prevNode != null) {
                statements.addNew(type, prevNode.toString(), currNode.toString());
            }
            
            prevNode = currNode;
        }
    }
	
}
