package txtuml.importer;

import java.lang.reflect.Method;

import org.eclipse.uml2.uml.Activity;
import org.eclipse.uml2.uml.ActivityEdge;
import org.eclipse.uml2.uml.ActivityNode;
import org.eclipse.uml2.uml.ForkNode;
import org.eclipse.uml2.uml.MergeNode;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.ObjectNode;
import org.eclipse.uml2.uml.OpaqueExpression;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;

import txtuml.api.ModelClass;
import txtuml.api.ModelIdentifiedElement;
import txtuml.utils.InstanceCreator;

public abstract class AbstractMethodImporter extends AbstractImporter {
	

	public static boolean isImporting() {
		return importing;
	}
	
	protected static <T> T createLocalInstance(Class<T> typeClass, int depth, Object... givenParameters)
	{
		setLocalInstanceToBeCreated(true);
		T createdObject = InstanceCreator.createInstance(typeClass,depth);
		setLocalInstanceToBeCreated(false);
		return createdObject;
	}
	
	protected static String getObjectIdentifier(ModelIdentifiedElement object)
	{
		object.getIdentifier();
		try
		{
			if(object.getIdentifier()==self.getIdentifier())
			{
				return "self";
			}
		}
		catch(NullPointerException e)
		{
			
		}
		
		int i=0;
		for(Object param: currentParameters)
		{
			try
			{
				ModelIdentifiedElement p=(ModelIdentifiedElement) param;
				try
				{
					if(p.getIdentifier().equals(object.getIdentifier()))
					{
						return "arg"+i;
					}
				}
				catch(NullPointerException e)
				{
					
				}
			}
			catch(ClassCastException e)
			{
				e.printStackTrace();
			}
			++i;
		}
		return object.getIdentifier();
	}
	
	protected static void createFlowBetweenNodes(ActivityNode source, ActivityNode target)
	{
		if(source instanceof ObjectNode || target instanceof ObjectNode)
		{
			createObjectFlowBetweenNodes(source,target);
		}
		else
		{
			createControlFlowBetweenNodes(source,target);
		}
	}
	
	protected static ForkNode createForkNode(String name, ActivityNode node1, ActivityNode node2)
	{
		ForkNode result=(ForkNode) currentActivity.createOwnedNode(name,UMLPackage.Literals.FORK_NODE);
		
		createFlowBetweenNodes(result,node1);
		createFlowBetweenNodes(result,node2);

		return result;
	 }
	
	 protected static MergeNode createMergeNode(String name,ActivityNode node1,ActivityNode node2)
	 {
		 MergeNode result=(MergeNode) currentActivity.createOwnedNode(name,UMLPackage.Literals.MERGE_NODE);
		 createControlFlowBetweenNodes(node1,result);
		 createControlFlowBetweenNodes(node2,result);
		 return result;
	 }

	protected static ActivityEdge createControlFlowBetweenNodes(ActivityNode source,ActivityNode target)
	{
		ActivityEdge edge=currentActivity.createEdge("from_"+source.getName()+"_to_"+target.getName(), UMLPackage.Literals.CONTROL_FLOW);
		edge.setSource(source);
		edge.setTarget(target);
		return edge;
	}
	
	protected static ActivityEdge createObjectFlowBetweenNodes(ActivityNode source,ActivityNode target)
	{
		ActivityEdge edge=currentActivity.createEdge("from_"+source.getName()+"_to_"+target.getName(), UMLPackage.Literals.OBJECT_FLOW);
		edge.setSource(source);
		edge.setTarget(target);
		return edge;
	}
	
	protected static ValuePin addOpaqueExpressionToValuePin(ValuePin pin,String expression, Type type)
    {
    	OpaqueExpression opaqueExpression=(OpaqueExpression) pin.createValue("value",type,UMLPackage.Literals.OPAQUE_EXPRESSION);
		opaqueExpression.getBodies().add(expression);
		return pin;
    }
	

	protected static Operation getOperation(org.eclipse.uml2.uml.Class ownerClass,String name)
	{
		for(Operation op:ownerClass.getOperations())
		{
			if(op.getName().equals(name))
			{
				return op;
			}
		}
		return null;
	}
	
	protected static boolean importing=false;
	protected static Object[] currentParameters=null;
	protected static Method currentMethod=null;
	protected static ActivityNode lastNode=null;
	protected static Activity currentActivity=null;
	protected static ModelClass self=null;
	protected static Model currentModel=null;
}
