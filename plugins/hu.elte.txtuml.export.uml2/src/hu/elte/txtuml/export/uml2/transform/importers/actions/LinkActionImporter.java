package hu.elte.txtuml.export.uml2.transform.importers.actions;

import hu.elte.txtuml.export.uml2.transform.importers.IActionImporter;
import hu.elte.txtuml.export.uml2.transform.importers.MethodBodyImporter;

import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.LinkAction;
import org.eclipse.uml2.uml.LinkEndData;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.ValuePin;

/**
 * @author Turi Zoltán
 * @Todo Ask Ádám about Unlink?
 *
 *
 * Class responsible for importing the link and unlink actions
 * ACTION_LINK_NAME constant defines the link action name, maybe better if separated by a superclass, and only association-name-additions where given
 */
public class LinkActionImporter extends AbstractActionImporter implements IActionImporter {

	protected static String ACTION_LINK_NAME = "link";
	
	public LinkActionImporter(MethodBodyImporter methodBodyImporter,Model importedModel) 
	{
		super(methodBodyImporter, importedModel);
	}

	protected String obtainAssocName(MethodInvocation methodInvocation)
	{
		Expression argExpr = (Expression) methodInvocation.arguments().get(0);
		ITypeBinding type = argExpr.resolveTypeBinding();
		
		ITypeBinding declaringClass = type.getTypeArguments()[0].getDeclaringClass();
		
		return declaringClass.getName();
	}
	
	@Override
	public void importFromMethodInvocation(MethodInvocation methodInvocation) 
	{	
			String assocName 	= 	this.obtainAssocName(methodInvocation);
			Association association 	=	( Association ) this.importedModel.getOwnedMember(assocName);
			
			if(methodInvocation.getName().toString().equals(LinkActionImporter.ACTION_LINK_NAME) )
			{
				this.importLinkAction(methodInvocation,association, true);
			}
			else
			{
				this.importLinkAction(methodInvocation, association,false);
			}
	}
	
	protected void importLinkAction(MethodInvocation methodInvocation,Association association,Boolean link)
	{
		String leftName 	= 	this.obtainExpressionOfNthArgument(methodInvocation, 1);
		String rightName 	=	this.obtainExpressionOfNthArgument(methodInvocation, 3);
		Type rightType 		=	this.obtainTypeOfNthArgument(methodInvocation, 2);
		Type leftType		=	this.obtainTypeOfNthArgument(methodInvocation, 4);
		String leftPhrase 	=	leftType.getName();
		String rightPhrase 	= 	rightType.getName();
		
		String linkActionName="link_"+leftName+"_and_"+rightName;
		
		LinkAction linkAction;
		
		if(link)
		{
			linkAction = (LinkAction) this.activity.createOwnedNode(linkActionName, UMLPackage.Literals.CREATE_LINK_ACTION);
		}
		else
		{
			linkActionName = "un" + linkActionName;
			linkAction = (LinkAction) this.activity.createOwnedNode(linkActionName, UMLPackage.Literals.DESTROY_LINK_ACTION);
		}
		
		this.addEndToLinkAction(linkAction,association,leftName,leftPhrase,leftType,1);
		this.addEndToLinkAction(linkAction,association,rightName,rightPhrase,rightType,2);
		
		this.methodBodyImporter.getBodyNode().getExecutableNodes().add(linkAction);
		
		/*this.createControlFlowBetweenActivityNodes(this.getLastNode(),linkAction);
		this.setLastNode(linkAction);*/
	}
	
	protected void addEndToLinkAction(LinkAction linkAction, Association association, String phrase, String instName,Type endType,int endNum)
	{

		ValuePin endValuePin=(ValuePin) 
				linkAction.createInputValue(linkAction.getName()+"_end"+endNum+"input",endType,UMLPackage.Literals.VALUE_PIN);

		this.createAndAddOpaqueExpressionToValuePin(endValuePin,instName,endType);

		LinkEndData end=linkAction.createEndData();
		Property endProp=association.getMemberEnd(phrase,endType);

		end.setEnd(endProp);
		end.setValue(endValuePin);
	}
	
	

}
