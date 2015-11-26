/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML.util;

import hu.elte.txtuml.xtxtuml.xtxtUML.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.xtext.xbase.XExpression;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
 * @generated
 */
public class XtxtUMLAdapterFactory extends AdapterFactoryImpl
{
  /**
   * The cached model package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static XtxtUMLPackage modelPackage;

  /**
   * Creates an instance of the adapter factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XtxtUMLAdapterFactory()
  {
    if (modelPackage == null)
    {
      modelPackage = XtxtUMLPackage.eINSTANCE;
    }
  }

  /**
   * Returns whether this factory is applicable for the type of the object.
   * <!-- begin-user-doc -->
   * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
   * <!-- end-user-doc -->
   * @return whether this factory is applicable for the type of the object.
   * @generated
   */
  @Override
  public boolean isFactoryForType(Object object)
  {
    if (object == modelPackage)
    {
      return true;
    }
    if (object instanceof EObject)
    {
      return ((EObject)object).eClass().getEPackage() == modelPackage;
    }
    return false;
  }

  /**
   * The switch that delegates to the <code>createXXX</code> methods.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected XtxtUMLSwitch<Adapter> modelSwitch =
    new XtxtUMLSwitch<Adapter>()
    {
      @Override
      public Adapter caseTUFile(TUFile object)
      {
        return createTUFileAdapter();
      }
      @Override
      public Adapter caseTUModelDeclaration(TUModelDeclaration object)
      {
        return createTUModelDeclarationAdapter();
      }
      @Override
      public Adapter caseTUModelElement(TUModelElement object)
      {
        return createTUModelElementAdapter();
      }
      @Override
      public Adapter caseTUExecution(TUExecution object)
      {
        return createTUExecutionAdapter();
      }
      @Override
      public Adapter caseTUSignal(TUSignal object)
      {
        return createTUSignalAdapter();
      }
      @Override
      public Adapter caseTUClass(TUClass object)
      {
        return createTUClassAdapter();
      }
      @Override
      public Adapter caseTUAssociation(TUAssociation object)
      {
        return createTUAssociationAdapter();
      }
      @Override
      public Adapter caseTUSignalAttribute(TUSignalAttribute object)
      {
        return createTUSignalAttributeAdapter();
      }
      @Override
      public Adapter caseTUClassMember(TUClassMember object)
      {
        return createTUClassMemberAdapter();
      }
      @Override
      public Adapter caseTUConstructor(TUConstructor object)
      {
        return createTUConstructorAdapter();
      }
      @Override
      public Adapter caseTUAttributeOrOperationDeclarationPrefix(TUAttributeOrOperationDeclarationPrefix object)
      {
        return createTUAttributeOrOperationDeclarationPrefixAdapter();
      }
      @Override
      public Adapter caseTUState(TUState object)
      {
        return createTUStateAdapter();
      }
      @Override
      public Adapter caseTUStateMember(TUStateMember object)
      {
        return createTUStateMemberAdapter();
      }
      @Override
      public Adapter caseTUEntryOrExitActivity(TUEntryOrExitActivity object)
      {
        return createTUEntryOrExitActivityAdapter();
      }
      @Override
      public Adapter caseTUTransition(TUTransition object)
      {
        return createTUTransitionAdapter();
      }
      @Override
      public Adapter caseTUTransitionMember(TUTransitionMember object)
      {
        return createTUTransitionMemberAdapter();
      }
      @Override
      public Adapter caseTUTransitionTrigger(TUTransitionTrigger object)
      {
        return createTUTransitionTriggerAdapter();
      }
      @Override
      public Adapter caseTUTransitionVertex(TUTransitionVertex object)
      {
        return createTUTransitionVertexAdapter();
      }
      @Override
      public Adapter caseTUTransitionEffect(TUTransitionEffect object)
      {
        return createTUTransitionEffectAdapter();
      }
      @Override
      public Adapter caseTUTransitionGuard(TUTransitionGuard object)
      {
        return createTUTransitionGuardAdapter();
      }
      @Override
      public Adapter caseTUAssociationEnd(TUAssociationEnd object)
      {
        return createTUAssociationEndAdapter();
      }
      @Override
      public Adapter caseTUMultiplicity(TUMultiplicity object)
      {
        return createTUMultiplicityAdapter();
      }
      @Override
      public Adapter caseTUAttribute(TUAttribute object)
      {
        return createTUAttributeAdapter();
      }
      @Override
      public Adapter caseTUOperation(TUOperation object)
      {
        return createTUOperationAdapter();
      }
      @Override
      public Adapter caseRAlfSendSignalExpression(RAlfSendSignalExpression object)
      {
        return createRAlfSendSignalExpressionAdapter();
      }
      @Override
      public Adapter caseRAlfDeleteObjectExpression(RAlfDeleteObjectExpression object)
      {
        return createRAlfDeleteObjectExpressionAdapter();
      }
      @Override
      public Adapter caseRAlfSignalAccessExpression(RAlfSignalAccessExpression object)
      {
        return createRAlfSignalAccessExpressionAdapter();
      }
      @Override
      public Adapter caseRAlfAssocNavExpression(RAlfAssocNavExpression object)
      {
        return createRAlfAssocNavExpressionAdapter();
      }
      @Override
      public Adapter caseXExpression(XExpression object)
      {
        return createXExpressionAdapter();
      }
      @Override
      public Adapter defaultCase(EObject object)
      {
        return createEObjectAdapter();
      }
    };

  /**
   * Creates an adapter for the <code>target</code>.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param target the object to adapt.
   * @return the adapter for the <code>target</code>.
   * @generated
   */
  @Override
  public Adapter createAdapter(Notifier target)
  {
    return modelSwitch.doSwitch((EObject)target);
  }


  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUFile <em>TU File</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
   * @generated
   */
  public Adapter createTUFileAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUModelDeclaration <em>TU Model Declaration</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUModelDeclaration
   * @generated
   */
  public Adapter createTUModelDeclarationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement <em>TU Model Element</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement
   * @generated
   */
  public Adapter createTUModelElementAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution <em>TU Execution</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution
   * @generated
   */
  public Adapter createTUExecutionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal <em>TU Signal</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
   * @generated
   */
  public Adapter createTUSignalAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUClass <em>TU Class</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
   * @generated
   */
  public Adapter createTUClassAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation <em>TU Association</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
   * @generated
   */
  public Adapter createTUAssociationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute <em>TU Signal Attribute</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
   * @generated
   */
  public Adapter createTUSignalAttributeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUClassMember <em>TU Class Member</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUClassMember
   * @generated
   */
  public Adapter createTUClassMemberAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor <em>TU Constructor</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
   * @generated
   */
  public Adapter createTUConstructorAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix <em>TU Attribute Or Operation Declaration Prefix</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix
   * @generated
   */
  public Adapter createTUAttributeOrOperationDeclarationPrefixAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUState <em>TU State</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUState
   * @generated
   */
  public Adapter createTUStateAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUStateMember <em>TU State Member</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUStateMember
   * @generated
   */
  public Adapter createTUStateMemberAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity <em>TU Entry Or Exit Activity</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
   * @generated
   */
  public Adapter createTUEntryOrExitActivityAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition <em>TU Transition</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
   * @generated
   */
  public Adapter createTUTransitionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionMember <em>TU Transition Member</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionMember
   * @generated
   */
  public Adapter createTUTransitionMemberAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger <em>TU Transition Trigger</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
   * @generated
   */
  public Adapter createTUTransitionTriggerAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex <em>TU Transition Vertex</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
   * @generated
   */
  public Adapter createTUTransitionVertexAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect <em>TU Transition Effect</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect
   * @generated
   */
  public Adapter createTUTransitionEffectAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard <em>TU Transition Guard</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
   * @generated
   */
  public Adapter createTUTransitionGuardAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd <em>TU Association End</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
   * @generated
   */
  public Adapter createTUAssociationEndAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity <em>TU Multiplicity</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity
   * @generated
   */
  public Adapter createTUMultiplicityAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute <em>TU Attribute</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
   * @generated
   */
  public Adapter createTUAttributeAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation <em>TU Operation</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
   * @generated
   */
  public Adapter createTUOperationAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression <em>RAlf Send Signal Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression
   * @generated
   */
  public Adapter createRAlfSendSignalExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression <em>RAlf Delete Object Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression
   * @generated
   */
  public Adapter createRAlfDeleteObjectExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression <em>RAlf Signal Access Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression
   * @generated
   */
  public Adapter createRAlfSignalAccessExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression <em>RAlf Assoc Nav Expression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression
   * @generated
   */
  public Adapter createRAlfAssocNavExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for an object of class '{@link org.eclipse.xtext.xbase.XExpression <em>XExpression</em>}'.
   * <!-- begin-user-doc -->
   * This default implementation returns null so that we can easily ignore cases;
   * it's useful to ignore a case when inheritance will catch all the cases anyway.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @see org.eclipse.xtext.xbase.XExpression
   * @generated
   */
  public Adapter createXExpressionAdapter()
  {
    return null;
  }

  /**
   * Creates a new adapter for the default case.
   * <!-- begin-user-doc -->
   * This default implementation returns null.
   * <!-- end-user-doc -->
   * @return the new adapter.
   * @generated
   */
  public Adapter createEObjectAdapter()
  {
    return null;
  }

} //XtxtUMLAdapterFactory
