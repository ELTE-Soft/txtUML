/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML.impl;

import hu.elte.txtuml.xtxtuml.xtxtUML.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class XtxtUMLFactoryImpl extends EFactoryImpl implements XtxtUMLFactory
{
  /**
   * Creates the default factory implementation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static XtxtUMLFactory init()
  {
    try
    {
      XtxtUMLFactory theXtxtUMLFactory = (XtxtUMLFactory)EPackage.Registry.INSTANCE.getEFactory(XtxtUMLPackage.eNS_URI);
      if (theXtxtUMLFactory != null)
      {
        return theXtxtUMLFactory;
      }
    }
    catch (Exception exception)
    {
      EcorePlugin.INSTANCE.log(exception);
    }
    return new XtxtUMLFactoryImpl();
  }

  /**
   * Creates an instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XtxtUMLFactoryImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EObject create(EClass eClass)
  {
    switch (eClass.getClassifierID())
    {
      case XtxtUMLPackage.TU_FILE: return createTUFile();
      case XtxtUMLPackage.TU_FILE_ELEMENT: return createTUFileElement();
      case XtxtUMLPackage.TU_MODEL: return createTUModel();
      case XtxtUMLPackage.TU_EXECUTION: return createTUExecution();
      case XtxtUMLPackage.TU_MODEL_ELEMENT: return createTUModelElement();
      case XtxtUMLPackage.TU_SIGNAL: return createTUSignal();
      case XtxtUMLPackage.TU_CLASS: return createTUClass();
      case XtxtUMLPackage.TU_ASSOCIATION: return createTUAssociation();
      case XtxtUMLPackage.TU_SIGNAL_ATTRIBUTE: return createTUSignalAttribute();
      case XtxtUMLPackage.TU_CLASS_MEMBER: return createTUClassMember();
      case XtxtUMLPackage.TU_CONSTRUCTOR: return createTUConstructor();
      case XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX: return createTUAttributeOrOperationDeclarationPrefix();
      case XtxtUMLPackage.TU_STATE: return createTUState();
      case XtxtUMLPackage.TU_STATE_MEMBER: return createTUStateMember();
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY: return createTUEntryOrExitActivity();
      case XtxtUMLPackage.TU_TRANSITION: return createTUTransition();
      case XtxtUMLPackage.TU_TRANSITION_MEMBER: return createTUTransitionMember();
      case XtxtUMLPackage.TU_TRANSITION_TRIGGER: return createTUTransitionTrigger();
      case XtxtUMLPackage.TU_TRANSITION_VERTEX: return createTUTransitionVertex();
      case XtxtUMLPackage.TU_TRANSITION_EFFECT: return createTUTransitionEffect();
      case XtxtUMLPackage.TU_TRANSITION_GUARD: return createTUTransitionGuard();
      case XtxtUMLPackage.TU_ASSOCIATION_END: return createTUAssociationEnd();
      case XtxtUMLPackage.TU_MULTIPLICITY: return createTUMultiplicity();
      case XtxtUMLPackage.TU_ATTRIBUTE: return createTUAttribute();
      case XtxtUMLPackage.TU_OPERATION: return createTUOperation();
      case XtxtUMLPackage.RALF_SEND_SIGNAL_EXPRESSION: return createRAlfSendSignalExpression();
      case XtxtUMLPackage.RALF_DELETE_OBJECT_EXPRESSION: return createRAlfDeleteObjectExpression();
      case XtxtUMLPackage.RALF_SIGNAL_ACCESS_EXPRESSION: return createRAlfSignalAccessExpression();
      case XtxtUMLPackage.RALF_ASSOC_NAV_EXPRESSION: return createRAlfAssocNavExpression();
      default:
        throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object createFromString(EDataType eDataType, String initialValue)
  {
    switch (eDataType.getClassifierID())
    {
      case XtxtUMLPackage.TU_STATE_TYPE:
        return createTUStateTypeFromString(eDataType, initialValue);
      case XtxtUMLPackage.TU_VISIBILITY:
        return createTUVisibilityFromString(eDataType, initialValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String convertToString(EDataType eDataType, Object instanceValue)
  {
    switch (eDataType.getClassifierID())
    {
      case XtxtUMLPackage.TU_STATE_TYPE:
        return convertTUStateTypeToString(eDataType, instanceValue);
      case XtxtUMLPackage.TU_VISIBILITY:
        return convertTUVisibilityToString(eDataType, instanceValue);
      default:
        throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
    }
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUFile createTUFile()
  {
    TUFileImpl tuFile = new TUFileImpl();
    return tuFile;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUFileElement createTUFileElement()
  {
    TUFileElementImpl tuFileElement = new TUFileElementImpl();
    return tuFileElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUModel createTUModel()
  {
    TUModelImpl tuModel = new TUModelImpl();
    return tuModel;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUExecution createTUExecution()
  {
    TUExecutionImpl tuExecution = new TUExecutionImpl();
    return tuExecution;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUModelElement createTUModelElement()
  {
    TUModelElementImpl tuModelElement = new TUModelElementImpl();
    return tuModelElement;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUSignal createTUSignal()
  {
    TUSignalImpl tuSignal = new TUSignalImpl();
    return tuSignal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUClass createTUClass()
  {
    TUClassImpl tuClass = new TUClassImpl();
    return tuClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUAssociation createTUAssociation()
  {
    TUAssociationImpl tuAssociation = new TUAssociationImpl();
    return tuAssociation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUSignalAttribute createTUSignalAttribute()
  {
    TUSignalAttributeImpl tuSignalAttribute = new TUSignalAttributeImpl();
    return tuSignalAttribute;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUClassMember createTUClassMember()
  {
    TUClassMemberImpl tuClassMember = new TUClassMemberImpl();
    return tuClassMember;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUConstructor createTUConstructor()
  {
    TUConstructorImpl tuConstructor = new TUConstructorImpl();
    return tuConstructor;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUAttributeOrOperationDeclarationPrefix createTUAttributeOrOperationDeclarationPrefix()
  {
    TUAttributeOrOperationDeclarationPrefixImpl tuAttributeOrOperationDeclarationPrefix = new TUAttributeOrOperationDeclarationPrefixImpl();
    return tuAttributeOrOperationDeclarationPrefix;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUState createTUState()
  {
    TUStateImpl tuState = new TUStateImpl();
    return tuState;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUStateMember createTUStateMember()
  {
    TUStateMemberImpl tuStateMember = new TUStateMemberImpl();
    return tuStateMember;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUEntryOrExitActivity createTUEntryOrExitActivity()
  {
    TUEntryOrExitActivityImpl tuEntryOrExitActivity = new TUEntryOrExitActivityImpl();
    return tuEntryOrExitActivity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUTransition createTUTransition()
  {
    TUTransitionImpl tuTransition = new TUTransitionImpl();
    return tuTransition;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUTransitionMember createTUTransitionMember()
  {
    TUTransitionMemberImpl tuTransitionMember = new TUTransitionMemberImpl();
    return tuTransitionMember;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUTransitionTrigger createTUTransitionTrigger()
  {
    TUTransitionTriggerImpl tuTransitionTrigger = new TUTransitionTriggerImpl();
    return tuTransitionTrigger;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUTransitionVertex createTUTransitionVertex()
  {
    TUTransitionVertexImpl tuTransitionVertex = new TUTransitionVertexImpl();
    return tuTransitionVertex;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUTransitionEffect createTUTransitionEffect()
  {
    TUTransitionEffectImpl tuTransitionEffect = new TUTransitionEffectImpl();
    return tuTransitionEffect;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUTransitionGuard createTUTransitionGuard()
  {
    TUTransitionGuardImpl tuTransitionGuard = new TUTransitionGuardImpl();
    return tuTransitionGuard;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUAssociationEnd createTUAssociationEnd()
  {
    TUAssociationEndImpl tuAssociationEnd = new TUAssociationEndImpl();
    return tuAssociationEnd;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUMultiplicity createTUMultiplicity()
  {
    TUMultiplicityImpl tuMultiplicity = new TUMultiplicityImpl();
    return tuMultiplicity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUAttribute createTUAttribute()
  {
    TUAttributeImpl tuAttribute = new TUAttributeImpl();
    return tuAttribute;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUOperation createTUOperation()
  {
    TUOperationImpl tuOperation = new TUOperationImpl();
    return tuOperation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RAlfSendSignalExpression createRAlfSendSignalExpression()
  {
    RAlfSendSignalExpressionImpl rAlfSendSignalExpression = new RAlfSendSignalExpressionImpl();
    return rAlfSendSignalExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RAlfDeleteObjectExpression createRAlfDeleteObjectExpression()
  {
    RAlfDeleteObjectExpressionImpl rAlfDeleteObjectExpression = new RAlfDeleteObjectExpressionImpl();
    return rAlfDeleteObjectExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RAlfSignalAccessExpression createRAlfSignalAccessExpression()
  {
    RAlfSignalAccessExpressionImpl rAlfSignalAccessExpression = new RAlfSignalAccessExpressionImpl();
    return rAlfSignalAccessExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public RAlfAssocNavExpression createRAlfAssocNavExpression()
  {
    RAlfAssocNavExpressionImpl rAlfAssocNavExpression = new RAlfAssocNavExpressionImpl();
    return rAlfAssocNavExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUStateType createTUStateTypeFromString(EDataType eDataType, String initialValue)
  {
    TUStateType result = TUStateType.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTUStateTypeToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUVisibility createTUVisibilityFromString(EDataType eDataType, String initialValue)
  {
    TUVisibility result = TUVisibility.get(initialValue);
    if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
    return result;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String convertTUVisibilityToString(EDataType eDataType, Object instanceValue)
  {
    return instanceValue == null ? null : instanceValue.toString();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XtxtUMLPackage getXtxtUMLPackage()
  {
    return (XtxtUMLPackage)getEPackage();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @deprecated
   * @generated
   */
  @Deprecated
  public static XtxtUMLPackage getPackage()
  {
    return XtxtUMLPackage.eINSTANCE;
  }

} //XtxtUMLFactoryImpl
