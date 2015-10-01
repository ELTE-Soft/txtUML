package hu.elte.txtuml.export.uml2.transform.importers;

import java.util.Stack;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.MergeNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.ValuePin;

public abstract class AbstractActionCodeImporter {

	protected static int LITERAL_ID = 0;
	
	protected static Stack<ActivityEdge> blockFirstEdges;
	protected static int blockBodiesBeingImported = 0;
	
	protected MethodBodyImporter methodBodyImporter;
	protected Activity activity;
	protected TypeImporter typeImporter;
	protected Model importedModel;
	
	public AbstractActionCodeImporter(MethodBodyImporter methodBodyImporter, Model importedModel) {
		
		AbstractActionCodeImporter.blockFirstEdges = new Stack<ActivityEdge>();
		
		this.methodBodyImporter = methodBodyImporter;
		this.activity = methodBodyImporter.getActivity();
		this.typeImporter = methodBodyImporter.getTypeImporter();
		this.importedModel = importedModel;
	}
	
	protected void createAndAddOpaqueExpressionToValuePin(ValuePin target,String InstanceName,Type type)
	{		
		this.methodBodyImporter.createAndAddOpaqueExpressionToValuePin(target, InstanceName, type);
	}
	
	protected ActivityEdge createControlFlowBetweenActivityNodes(ActivityNode node1, ActivityNode node2)
	{		
		ActivityEdge edge = this.methodBodyImporter.createControlFlowBetweenActivityNodes(node1, node2);
		
		if(AbstractActionCodeImporter.blockBodiesBeingImported > 0 && AbstractActionCodeImporter.blockFirstEdges.size() < AbstractActionCodeImporter.blockBodiesBeingImported)
			AbstractActionCodeImporter.blockFirstEdges.push(edge);
		
		return edge;
	}
	
	protected MergeNode createMergeNode(ActivityNode node1, ActivityNode node2)
	{
		return this.methodBodyImporter.createMergeNode(node1, node2);
	}
	
	protected void addGuardToActivityEdge(ActivityEdge edge, String expression)
	{
		OpaqueExpression opaqueExpression=UMLFactory.eINSTANCE.createOpaqueExpression();
		opaqueExpression.getBodies().add(expression);
		edge.setGuard(opaqueExpression);
	}
	
	protected ActivityNode getLastNode()
	{	
		return this.methodBodyImporter.getLastNode();
	}
	
	protected void setLastNode(ActivityNode node)
	{
		this.methodBodyImporter.setLastNode(node);
	}
	
	protected void createAndAddValueExpressionToValuePin(ValuePin pin, Expression exp, Type attributeType)
	{
		this.methodBodyImporter.createAndAddValueExpressionToValuePin(pin, exp, attributeType);
	}
	
	protected void createAndAddValueExpressionToValuePin(ValuePin pin, String exp, Type attributeType)
	{
		this.methodBodyImporter.createAndAddValueExpressionToValuePin(pin, exp, attributeType);
	}
	
	protected ActivityEdge createEdgeBetweenActivityNodes(ActivityNode node1, ActivityNode node2)
	{
		return this.methodBodyImporter.createEdgeBetweenActivityNodes(node1, node2);
	}

}
