/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML.util;

import hu.elte.txtuml.xtxtuml.xtxtUML.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.xtext.xbase.XExpression;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
 * @generated
 */
public class XtxtUMLSwitch<T> extends Switch<T>
{
  /**
   * The cached model package
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected static XtxtUMLPackage modelPackage;

  /**
   * Creates an instance of the switch.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XtxtUMLSwitch()
  {
    if (modelPackage == null)
    {
      modelPackage = XtxtUMLPackage.eINSTANCE;
    }
  }

  /**
   * Checks whether this is a switch for the given package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @parameter ePackage the package in question.
   * @return whether this is a switch for the given package.
   * @generated
   */
  @Override
  protected boolean isSwitchFor(EPackage ePackage)
  {
    return ePackage == modelPackage;
  }

  /**
   * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the first non-null result returned by a <code>caseXXX</code> call.
   * @generated
   */
  @Override
  protected T doSwitch(int classifierID, EObject theEObject)
  {
    switch (classifierID)
    {
      case XtxtUMLPackage.TU_FILE:
      {
        TUFile tuFile = (TUFile)theEObject;
        T result = caseTUFile(tuFile);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_FILE_ELEMENT:
      {
        TUFileElement tuFileElement = (TUFileElement)theEObject;
        T result = caseTUFileElement(tuFileElement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_MODEL:
      {
        TUModel tuModel = (TUModel)theEObject;
        T result = caseTUModel(tuModel);
        if (result == null) result = caseTUFileElement(tuModel);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_EXECUTION:
      {
        TUExecution tuExecution = (TUExecution)theEObject;
        T result = caseTUExecution(tuExecution);
        if (result == null) result = caseTUFileElement(tuExecution);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_MODEL_ELEMENT:
      {
        TUModelElement tuModelElement = (TUModelElement)theEObject;
        T result = caseTUModelElement(tuModelElement);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_SIGNAL:
      {
        TUSignal tuSignal = (TUSignal)theEObject;
        T result = caseTUSignal(tuSignal);
        if (result == null) result = caseTUModelElement(tuSignal);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_CLASS:
      {
        TUClass tuClass = (TUClass)theEObject;
        T result = caseTUClass(tuClass);
        if (result == null) result = caseTUModelElement(tuClass);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_ASSOCIATION:
      {
        TUAssociation tuAssociation = (TUAssociation)theEObject;
        T result = caseTUAssociation(tuAssociation);
        if (result == null) result = caseTUModelElement(tuAssociation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_SIGNAL_ATTRIBUTE:
      {
        TUSignalAttribute tuSignalAttribute = (TUSignalAttribute)theEObject;
        T result = caseTUSignalAttribute(tuSignalAttribute);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_CLASS_MEMBER:
      {
        TUClassMember tuClassMember = (TUClassMember)theEObject;
        T result = caseTUClassMember(tuClassMember);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_CONSTRUCTOR:
      {
        TUConstructor tuConstructor = (TUConstructor)theEObject;
        T result = caseTUConstructor(tuConstructor);
        if (result == null) result = caseTUClassMember(tuConstructor);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX:
      {
        TUAttributeOrOperationDeclarationPrefix tuAttributeOrOperationDeclarationPrefix = (TUAttributeOrOperationDeclarationPrefix)theEObject;
        T result = caseTUAttributeOrOperationDeclarationPrefix(tuAttributeOrOperationDeclarationPrefix);
        if (result == null) result = caseTUClassMember(tuAttributeOrOperationDeclarationPrefix);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_STATE:
      {
        TUState tuState = (TUState)theEObject;
        T result = caseTUState(tuState);
        if (result == null) result = caseTUClassMember(tuState);
        if (result == null) result = caseTUStateMember(tuState);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_STATE_MEMBER:
      {
        TUStateMember tuStateMember = (TUStateMember)theEObject;
        T result = caseTUStateMember(tuStateMember);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY:
      {
        TUEntryOrExitActivity tuEntryOrExitActivity = (TUEntryOrExitActivity)theEObject;
        T result = caseTUEntryOrExitActivity(tuEntryOrExitActivity);
        if (result == null) result = caseTUStateMember(tuEntryOrExitActivity);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_TRANSITION:
      {
        TUTransition tuTransition = (TUTransition)theEObject;
        T result = caseTUTransition(tuTransition);
        if (result == null) result = caseTUClassMember(tuTransition);
        if (result == null) result = caseTUStateMember(tuTransition);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_TRANSITION_MEMBER:
      {
        TUTransitionMember tuTransitionMember = (TUTransitionMember)theEObject;
        T result = caseTUTransitionMember(tuTransitionMember);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_TRANSITION_TRIGGER:
      {
        TUTransitionTrigger tuTransitionTrigger = (TUTransitionTrigger)theEObject;
        T result = caseTUTransitionTrigger(tuTransitionTrigger);
        if (result == null) result = caseTUTransitionMember(tuTransitionTrigger);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_TRANSITION_VERTEX:
      {
        TUTransitionVertex tuTransitionVertex = (TUTransitionVertex)theEObject;
        T result = caseTUTransitionVertex(tuTransitionVertex);
        if (result == null) result = caseTUTransitionMember(tuTransitionVertex);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_TRANSITION_EFFECT:
      {
        TUTransitionEffect tuTransitionEffect = (TUTransitionEffect)theEObject;
        T result = caseTUTransitionEffect(tuTransitionEffect);
        if (result == null) result = caseTUTransitionMember(tuTransitionEffect);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_TRANSITION_GUARD:
      {
        TUTransitionGuard tuTransitionGuard = (TUTransitionGuard)theEObject;
        T result = caseTUTransitionGuard(tuTransitionGuard);
        if (result == null) result = caseTUTransitionMember(tuTransitionGuard);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_ASSOCIATION_END:
      {
        TUAssociationEnd tuAssociationEnd = (TUAssociationEnd)theEObject;
        T result = caseTUAssociationEnd(tuAssociationEnd);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_MULTIPLICITY:
      {
        TUMultiplicity tuMultiplicity = (TUMultiplicity)theEObject;
        T result = caseTUMultiplicity(tuMultiplicity);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_ATTRIBUTE:
      {
        TUAttribute tuAttribute = (TUAttribute)theEObject;
        T result = caseTUAttribute(tuAttribute);
        if (result == null) result = caseTUClassMember(tuAttribute);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.TU_OPERATION:
      {
        TUOperation tuOperation = (TUOperation)theEObject;
        T result = caseTUOperation(tuOperation);
        if (result == null) result = caseTUClassMember(tuOperation);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.RALF_SEND_SIGNAL_EXPRESSION:
      {
        RAlfSendSignalExpression rAlfSendSignalExpression = (RAlfSendSignalExpression)theEObject;
        T result = caseRAlfSendSignalExpression(rAlfSendSignalExpression);
        if (result == null) result = caseXExpression(rAlfSendSignalExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.RALF_DELETE_OBJECT_EXPRESSION:
      {
        RAlfDeleteObjectExpression rAlfDeleteObjectExpression = (RAlfDeleteObjectExpression)theEObject;
        T result = caseRAlfDeleteObjectExpression(rAlfDeleteObjectExpression);
        if (result == null) result = caseXExpression(rAlfDeleteObjectExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.RALF_SIGNAL_ACCESS_EXPRESSION:
      {
        RAlfSignalAccessExpression rAlfSignalAccessExpression = (RAlfSignalAccessExpression)theEObject;
        T result = caseRAlfSignalAccessExpression(rAlfSignalAccessExpression);
        if (result == null) result = caseXExpression(rAlfSignalAccessExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      case XtxtUMLPackage.RALF_ASSOC_NAV_EXPRESSION:
      {
        RAlfAssocNavExpression rAlfAssocNavExpression = (RAlfAssocNavExpression)theEObject;
        T result = caseRAlfAssocNavExpression(rAlfAssocNavExpression);
        if (result == null) result = caseXExpression(rAlfAssocNavExpression);
        if (result == null) result = defaultCase(theEObject);
        return result;
      }
      default: return defaultCase(theEObject);
    }
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU File</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU File</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUFile(TUFile object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU File Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU File Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUFileElement(TUFileElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Model</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Model</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUModel(TUModel object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Execution</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Execution</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUExecution(TUExecution object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Model Element</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Model Element</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUModelElement(TUModelElement object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Signal</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Signal</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUSignal(TUSignal object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Class</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Class</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUClass(TUClass object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Association</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Association</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUAssociation(TUAssociation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Signal Attribute</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Signal Attribute</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUSignalAttribute(TUSignalAttribute object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Class Member</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Class Member</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUClassMember(TUClassMember object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Constructor</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Constructor</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUConstructor(TUConstructor object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Attribute Or Operation Declaration Prefix</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Attribute Or Operation Declaration Prefix</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUAttributeOrOperationDeclarationPrefix(TUAttributeOrOperationDeclarationPrefix object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU State</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU State</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUState(TUState object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU State Member</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU State Member</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUStateMember(TUStateMember object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Entry Or Exit Activity</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Entry Or Exit Activity</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUEntryOrExitActivity(TUEntryOrExitActivity object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Transition</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Transition</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUTransition(TUTransition object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Transition Member</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Transition Member</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUTransitionMember(TUTransitionMember object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Transition Trigger</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Transition Trigger</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUTransitionTrigger(TUTransitionTrigger object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Transition Vertex</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Transition Vertex</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUTransitionVertex(TUTransitionVertex object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Transition Effect</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Transition Effect</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUTransitionEffect(TUTransitionEffect object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Transition Guard</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Transition Guard</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUTransitionGuard(TUTransitionGuard object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Association End</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Association End</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUAssociationEnd(TUAssociationEnd object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Multiplicity</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Multiplicity</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUMultiplicity(TUMultiplicity object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Attribute</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Attribute</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUAttribute(TUAttribute object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>TU Operation</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>TU Operation</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseTUOperation(TUOperation object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>RAlf Send Signal Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>RAlf Send Signal Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRAlfSendSignalExpression(RAlfSendSignalExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>RAlf Delete Object Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>RAlf Delete Object Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRAlfDeleteObjectExpression(RAlfDeleteObjectExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>RAlf Signal Access Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>RAlf Signal Access Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRAlfSignalAccessExpression(RAlfSignalAccessExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>RAlf Assoc Nav Expression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>RAlf Assoc Nav Expression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseRAlfAssocNavExpression(RAlfAssocNavExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>XExpression</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>XExpression</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
   * @generated
   */
  public T caseXExpression(XExpression object)
  {
    return null;
  }

  /**
   * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
   * <!-- begin-user-doc -->
   * This implementation returns null;
   * returning a non-null result will terminate the switch, but this is the last case anyway.
   * <!-- end-user-doc -->
   * @param object the target of the switch.
   * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
   * @see #doSwitch(org.eclipse.emf.ecore.EObject)
   * @generated
   */
  @Override
  public T defaultCase(EObject object)
  {
    return null;
  }

} //XtxtUMLSwitch
