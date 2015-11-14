/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.xtext.xbase.XbasePackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLFactory
 * @model kind="package"
 * @generated
 */
public interface XtxtUMLPackage extends EPackage
{
  /**
   * The package name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNAME = "xtxtUML";

  /**
   * The package namespace URI.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_URI = "http://www.elte.hu/txtuml/xtxtuml/XtxtUML";

  /**
   * The package namespace name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  String eNS_PREFIX = "xtxtUML";

  /**
   * The singleton instance of the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  XtxtUMLPackage eINSTANCE = hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl.init();

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUFileImpl <em>TU File</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUFileImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUFile()
   * @generated
   */
  int TU_FILE = 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_FILE__NAME = 0;

  /**
   * The feature id for the '<em><b>Import Section</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_FILE__IMPORT_SECTION = 1;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_FILE__ELEMENTS = 2;

  /**
   * The number of structural features of the '<em>TU File</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_FILE_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUFileElementImpl <em>TU File Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUFileElementImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUFileElement()
   * @generated
   */
  int TU_FILE_ELEMENT = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_FILE_ELEMENT__NAME = 0;

  /**
   * The number of structural features of the '<em>TU File Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_FILE_ELEMENT_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUModelImpl <em>TU Model</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUModelImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUModel()
   * @generated
   */
  int TU_MODEL = 2;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_MODEL__NAME = TU_FILE_ELEMENT__NAME;

  /**
   * The feature id for the '<em><b>Elements</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_MODEL__ELEMENTS = TU_FILE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>TU Model</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_MODEL_FEATURE_COUNT = TU_FILE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUExecutionImpl <em>TU Execution</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUExecutionImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUExecution()
   * @generated
   */
  int TU_EXECUTION = 3;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_EXECUTION__NAME = TU_FILE_ELEMENT__NAME;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_EXECUTION__BODY = TU_FILE_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>TU Execution</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_EXECUTION_FEATURE_COUNT = TU_FILE_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUModelElementImpl <em>TU Model Element</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUModelElementImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUModelElement()
   * @generated
   */
  int TU_MODEL_ELEMENT = 4;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_MODEL_ELEMENT__NAME = 0;

  /**
   * The number of structural features of the '<em>TU Model Element</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_MODEL_ELEMENT_FEATURE_COUNT = 1;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUSignalImpl <em>TU Signal</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUSignalImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUSignal()
   * @generated
   */
  int TU_SIGNAL = 5;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_SIGNAL__NAME = TU_MODEL_ELEMENT__NAME;

  /**
   * The feature id for the '<em><b>Attributes</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_SIGNAL__ATTRIBUTES = TU_MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>TU Signal</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_SIGNAL_FEATURE_COUNT = TU_MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUClassImpl <em>TU Class</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUClassImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUClass()
   * @generated
   */
  int TU_CLASS = 6;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_CLASS__NAME = TU_MODEL_ELEMENT__NAME;

  /**
   * The feature id for the '<em><b>Super Class</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_CLASS__SUPER_CLASS = TU_MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Members</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_CLASS__MEMBERS = TU_MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>TU Class</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_CLASS_FEATURE_COUNT = TU_MODEL_ELEMENT_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationImpl <em>TU Association</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUAssociation()
   * @generated
   */
  int TU_ASSOCIATION = 7;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ASSOCIATION__NAME = TU_MODEL_ELEMENT__NAME;

  /**
   * The feature id for the '<em><b>Ends</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ASSOCIATION__ENDS = TU_MODEL_ELEMENT_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>TU Association</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ASSOCIATION_FEATURE_COUNT = TU_MODEL_ELEMENT_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUSignalAttributeImpl <em>TU Signal Attribute</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUSignalAttributeImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUSignalAttribute()
   * @generated
   */
  int TU_SIGNAL_ATTRIBUTE = 8;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_SIGNAL_ATTRIBUTE__VISIBILITY = 0;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_SIGNAL_ATTRIBUTE__TYPE = 1;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_SIGNAL_ATTRIBUTE__NAME = 2;

  /**
   * The number of structural features of the '<em>TU Signal Attribute</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_SIGNAL_ATTRIBUTE_FEATURE_COUNT = 3;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUClassMemberImpl <em>TU Class Member</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUClassMemberImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUClassMember()
   * @generated
   */
  int TU_CLASS_MEMBER = 9;

  /**
   * The number of structural features of the '<em>TU Class Member</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_CLASS_MEMBER_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUConstructorImpl <em>TU Constructor</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUConstructorImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUConstructor()
   * @generated
   */
  int TU_CONSTRUCTOR = 10;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_CONSTRUCTOR__VISIBILITY = TU_CLASS_MEMBER_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_CONSTRUCTOR__NAME = TU_CLASS_MEMBER_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_CONSTRUCTOR__PARAMETERS = TU_CLASS_MEMBER_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_CONSTRUCTOR__BODY = TU_CLASS_MEMBER_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>TU Constructor</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_CONSTRUCTOR_FEATURE_COUNT = TU_CLASS_MEMBER_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAttributeOrOperationDeclarationPrefixImpl <em>TU Attribute Or Operation Declaration Prefix</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAttributeOrOperationDeclarationPrefixImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUAttributeOrOperationDeclarationPrefix()
   * @generated
   */
  int TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX = 11;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__VISIBILITY = TU_CLASS_MEMBER_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Type</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__TYPE = TU_CLASS_MEMBER_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>TU Attribute Or Operation Declaration Prefix</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX_FEATURE_COUNT = TU_CLASS_MEMBER_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUStateImpl <em>TU State</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUStateImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUState()
   * @generated
   */
  int TU_STATE = 12;

  /**
   * The feature id for the '<em><b>Type</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_STATE__TYPE = TU_CLASS_MEMBER_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_STATE__NAME = TU_CLASS_MEMBER_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Members</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_STATE__MEMBERS = TU_CLASS_MEMBER_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>TU State</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_STATE_FEATURE_COUNT = TU_CLASS_MEMBER_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUStateMemberImpl <em>TU State Member</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUStateMemberImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUStateMember()
   * @generated
   */
  int TU_STATE_MEMBER = 13;

  /**
   * The number of structural features of the '<em>TU State Member</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_STATE_MEMBER_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUEntryOrExitActivityImpl <em>TU Entry Or Exit Activity</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUEntryOrExitActivityImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUEntryOrExitActivity()
   * @generated
   */
  int TU_ENTRY_OR_EXIT_ACTIVITY = 14;

  /**
   * The feature id for the '<em><b>Entry</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ENTRY_OR_EXIT_ACTIVITY__ENTRY = TU_STATE_MEMBER_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Exit</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ENTRY_OR_EXIT_ACTIVITY__EXIT = TU_STATE_MEMBER_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ENTRY_OR_EXIT_ACTIVITY__BODY = TU_STATE_MEMBER_FEATURE_COUNT + 2;

  /**
   * The number of structural features of the '<em>TU Entry Or Exit Activity</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ENTRY_OR_EXIT_ACTIVITY_FEATURE_COUNT = TU_STATE_MEMBER_FEATURE_COUNT + 3;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionImpl <em>TU Transition</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUTransition()
   * @generated
   */
  int TU_TRANSITION = 15;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION__NAME = TU_CLASS_MEMBER_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Members</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION__MEMBERS = TU_CLASS_MEMBER_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>TU Transition</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION_FEATURE_COUNT = TU_CLASS_MEMBER_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionMemberImpl <em>TU Transition Member</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionMemberImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUTransitionMember()
   * @generated
   */
  int TU_TRANSITION_MEMBER = 16;

  /**
   * The number of structural features of the '<em>TU Transition Member</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION_MEMBER_FEATURE_COUNT = 0;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionTriggerImpl <em>TU Transition Trigger</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionTriggerImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUTransitionTrigger()
   * @generated
   */
  int TU_TRANSITION_TRIGGER = 17;

  /**
   * The feature id for the '<em><b>Trigger</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION_TRIGGER__TRIGGER = TU_TRANSITION_MEMBER_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>TU Transition Trigger</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION_TRIGGER_FEATURE_COUNT = TU_TRANSITION_MEMBER_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionVertexImpl <em>TU Transition Vertex</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionVertexImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUTransitionVertex()
   * @generated
   */
  int TU_TRANSITION_VERTEX = 18;

  /**
   * The feature id for the '<em><b>From</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION_VERTEX__FROM = TU_TRANSITION_MEMBER_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Vertex</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION_VERTEX__VERTEX = TU_TRANSITION_MEMBER_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>TU Transition Vertex</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION_VERTEX_FEATURE_COUNT = TU_TRANSITION_MEMBER_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionEffectImpl <em>TU Transition Effect</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionEffectImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUTransitionEffect()
   * @generated
   */
  int TU_TRANSITION_EFFECT = 19;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION_EFFECT__BODY = TU_TRANSITION_MEMBER_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>TU Transition Effect</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION_EFFECT_FEATURE_COUNT = TU_TRANSITION_MEMBER_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionGuardImpl <em>TU Transition Guard</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionGuardImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUTransitionGuard()
   * @generated
   */
  int TU_TRANSITION_GUARD = 20;

  /**
   * The feature id for the '<em><b>Else</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION_GUARD__ELSE = TU_TRANSITION_MEMBER_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION_GUARD__EXPRESSION = TU_TRANSITION_MEMBER_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>TU Transition Guard</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_TRANSITION_GUARD_FEATURE_COUNT = TU_TRANSITION_MEMBER_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationEndImpl <em>TU Association End</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationEndImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUAssociationEnd()
   * @generated
   */
  int TU_ASSOCIATION_END = 21;

  /**
   * The feature id for the '<em><b>Visibility</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ASSOCIATION_END__VISIBILITY = 0;

  /**
   * The feature id for the '<em><b>Not Navigable</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ASSOCIATION_END__NOT_NAVIGABLE = 1;

  /**
   * The feature id for the '<em><b>Multiplicity</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ASSOCIATION_END__MULTIPLICITY = 2;

  /**
   * The feature id for the '<em><b>End Class</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ASSOCIATION_END__END_CLASS = 3;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ASSOCIATION_END__NAME = 4;

  /**
   * The number of structural features of the '<em>TU Association End</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ASSOCIATION_END_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUMultiplicityImpl <em>TU Multiplicity</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUMultiplicityImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUMultiplicity()
   * @generated
   */
  int TU_MULTIPLICITY = 22;

  /**
   * The feature id for the '<em><b>Lower</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_MULTIPLICITY__LOWER = 0;

  /**
   * The feature id for the '<em><b>Upper Set</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_MULTIPLICITY__UPPER_SET = 1;

  /**
   * The feature id for the '<em><b>Upper</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_MULTIPLICITY__UPPER = 2;

  /**
   * The feature id for the '<em><b>Upper Inf</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_MULTIPLICITY__UPPER_INF = 3;

  /**
   * The feature id for the '<em><b>Any</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_MULTIPLICITY__ANY = 4;

  /**
   * The number of structural features of the '<em>TU Multiplicity</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_MULTIPLICITY_FEATURE_COUNT = 5;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAttributeImpl <em>TU Attribute</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAttributeImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUAttribute()
   * @generated
   */
  int TU_ATTRIBUTE = 23;

  /**
   * The feature id for the '<em><b>Prefix</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ATTRIBUTE__PREFIX = TU_CLASS_MEMBER_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ATTRIBUTE__NAME = TU_CLASS_MEMBER_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>TU Attribute</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_ATTRIBUTE_FEATURE_COUNT = TU_CLASS_MEMBER_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUOperationImpl <em>TU Operation</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUOperationImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUOperation()
   * @generated
   */
  int TU_OPERATION = 24;

  /**
   * The feature id for the '<em><b>Prefix</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_OPERATION__PREFIX = TU_CLASS_MEMBER_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_OPERATION__NAME = TU_CLASS_MEMBER_FEATURE_COUNT + 1;

  /**
   * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_OPERATION__PARAMETERS = TU_CLASS_MEMBER_FEATURE_COUNT + 2;

  /**
   * The feature id for the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_OPERATION__BODY = TU_CLASS_MEMBER_FEATURE_COUNT + 3;

  /**
   * The number of structural features of the '<em>TU Operation</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int TU_OPERATION_FEATURE_COUNT = TU_CLASS_MEMBER_FEATURE_COUNT + 4;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfSendSignalExpressionImpl <em>RAlf Send Signal Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfSendSignalExpressionImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getRAlfSendSignalExpression()
   * @generated
   */
  int RALF_SEND_SIGNAL_EXPRESSION = 25;

  /**
   * The feature id for the '<em><b>Signal</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RALF_SEND_SIGNAL_EXPRESSION__SIGNAL = XbasePackage.XEXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Target</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RALF_SEND_SIGNAL_EXPRESSION__TARGET = XbasePackage.XEXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>RAlf Send Signal Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RALF_SEND_SIGNAL_EXPRESSION_FEATURE_COUNT = XbasePackage.XEXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfDeleteObjectExpressionImpl <em>RAlf Delete Object Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfDeleteObjectExpressionImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getRAlfDeleteObjectExpression()
   * @generated
   */
  int RALF_DELETE_OBJECT_EXPRESSION = 26;

  /**
   * The feature id for the '<em><b>Object</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RALF_DELETE_OBJECT_EXPRESSION__OBJECT = XbasePackage.XEXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>RAlf Delete Object Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RALF_DELETE_OBJECT_EXPRESSION_FEATURE_COUNT = XbasePackage.XEXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfSignalAccessExpressionImpl <em>RAlf Signal Access Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfSignalAccessExpressionImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getRAlfSignalAccessExpression()
   * @generated
   */
  int RALF_SIGNAL_ACCESS_EXPRESSION = 27;

  /**
   * The feature id for the '<em><b>Sigdata</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RALF_SIGNAL_ACCESS_EXPRESSION__SIGDATA = XbasePackage.XEXPRESSION_FEATURE_COUNT + 0;

  /**
   * The number of structural features of the '<em>RAlf Signal Access Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RALF_SIGNAL_ACCESS_EXPRESSION_FEATURE_COUNT = XbasePackage.XEXPRESSION_FEATURE_COUNT + 1;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfAssocNavExpressionImpl <em>RAlf Assoc Nav Expression</em>}' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfAssocNavExpressionImpl
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getRAlfAssocNavExpression()
   * @generated
   */
  int RALF_ASSOC_NAV_EXPRESSION = 28;

  /**
   * The feature id for the '<em><b>Left</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RALF_ASSOC_NAV_EXPRESSION__LEFT = XbasePackage.XEXPRESSION_FEATURE_COUNT + 0;

  /**
   * The feature id for the '<em><b>Right</b></em>' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RALF_ASSOC_NAV_EXPRESSION__RIGHT = XbasePackage.XEXPRESSION_FEATURE_COUNT + 1;

  /**
   * The number of structural features of the '<em>RAlf Assoc Nav Expression</em>' class.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   * @ordered
   */
  int RALF_ASSOC_NAV_EXPRESSION_FEATURE_COUNT = XbasePackage.XEXPRESSION_FEATURE_COUNT + 2;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType <em>TU State Type</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUStateType()
   * @generated
   */
  int TU_STATE_TYPE = 29;

  /**
   * The meta object id for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility <em>TU Visibility</em>}' enum.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUVisibility()
   * @generated
   */
  int TU_VISIBILITY = 30;


  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUFile <em>TU File</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU File</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUFile
   * @generated
   */
  EClass getTUFile();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUFile#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUFile#getName()
   * @see #getTUFile()
   * @generated
   */
  EAttribute getTUFile_Name();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUFile#getImportSection <em>Import Section</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Import Section</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUFile#getImportSection()
   * @see #getTUFile()
   * @generated
   */
  EReference getTUFile_ImportSection();

  /**
   * Returns the meta object for the containment reference list '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUFile#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUFile#getElements()
   * @see #getTUFile()
   * @generated
   */
  EReference getTUFile_Elements();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUFileElement <em>TU File Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU File Element</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUFileElement
   * @generated
   */
  EClass getTUFileElement();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUFileElement#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUFileElement#getName()
   * @see #getTUFileElement()
   * @generated
   */
  EAttribute getTUFileElement_Name();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUModel <em>TU Model</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Model</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUModel
   * @generated
   */
  EClass getTUModel();

  /**
   * Returns the meta object for the containment reference list '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUModel#getElements <em>Elements</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Elements</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUModel#getElements()
   * @see #getTUModel()
   * @generated
   */
  EReference getTUModel_Elements();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution <em>TU Execution</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Execution</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution
   * @generated
   */
  EClass getTUExecution();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution#getBody()
   * @see #getTUExecution()
   * @generated
   */
  EReference getTUExecution_Body();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement <em>TU Model Element</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Model Element</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement
   * @generated
   */
  EClass getTUModelElement();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement#getName()
   * @see #getTUModelElement()
   * @generated
   */
  EAttribute getTUModelElement_Name();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal <em>TU Signal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Signal</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal
   * @generated
   */
  EClass getTUSignal();

  /**
   * Returns the meta object for the containment reference list '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal#getAttributes <em>Attributes</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Attributes</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal#getAttributes()
   * @see #getTUSignal()
   * @generated
   */
  EReference getTUSignal_Attributes();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUClass <em>TU Class</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Class</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
   * @generated
   */
  EClass getTUClass();

  /**
   * Returns the meta object for the reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUClass#getSuperClass <em>Super Class</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Super Class</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUClass#getSuperClass()
   * @see #getTUClass()
   * @generated
   */
  EReference getTUClass_SuperClass();

  /**
   * Returns the meta object for the containment reference list '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUClass#getMembers <em>Members</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Members</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUClass#getMembers()
   * @see #getTUClass()
   * @generated
   */
  EReference getTUClass_Members();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation <em>TU Association</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Association</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
   * @generated
   */
  EClass getTUAssociation();

  /**
   * Returns the meta object for the containment reference list '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation#getEnds <em>Ends</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Ends</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation#getEnds()
   * @see #getTUAssociation()
   * @generated
   */
  EReference getTUAssociation_Ends();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute <em>TU Signal Attribute</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Signal Attribute</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute
   * @generated
   */
  EClass getTUSignalAttribute();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute#getVisibility <em>Visibility</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Visibility</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute#getVisibility()
   * @see #getTUSignalAttribute()
   * @generated
   */
  EAttribute getTUSignalAttribute_Visibility();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute#getType()
   * @see #getTUSignalAttribute()
   * @generated
   */
  EReference getTUSignalAttribute_Type();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute#getName()
   * @see #getTUSignalAttribute()
   * @generated
   */
  EAttribute getTUSignalAttribute_Name();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUClassMember <em>TU Class Member</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Class Member</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUClassMember
   * @generated
   */
  EClass getTUClassMember();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor <em>TU Constructor</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Constructor</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor
   * @generated
   */
  EClass getTUConstructor();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getVisibility <em>Visibility</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Visibility</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getVisibility()
   * @see #getTUConstructor()
   * @generated
   */
  EAttribute getTUConstructor_Visibility();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getName()
   * @see #getTUConstructor()
   * @generated
   */
  EAttribute getTUConstructor_Name();

  /**
   * Returns the meta object for the containment reference list '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getParameters <em>Parameters</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Parameters</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getParameters()
   * @see #getTUConstructor()
   * @generated
   */
  EReference getTUConstructor_Parameters();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getBody()
   * @see #getTUConstructor()
   * @generated
   */
  EReference getTUConstructor_Body();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix <em>TU Attribute Or Operation Declaration Prefix</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Attribute Or Operation Declaration Prefix</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix
   * @generated
   */
  EClass getTUAttributeOrOperationDeclarationPrefix();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix#getVisibility <em>Visibility</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Visibility</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix#getVisibility()
   * @see #getTUAttributeOrOperationDeclarationPrefix()
   * @generated
   */
  EAttribute getTUAttributeOrOperationDeclarationPrefix_Visibility();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Type</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix#getType()
   * @see #getTUAttributeOrOperationDeclarationPrefix()
   * @generated
   */
  EReference getTUAttributeOrOperationDeclarationPrefix_Type();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUState <em>TU State</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU State</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUState
   * @generated
   */
  EClass getTUState();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUState#getType <em>Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Type</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUState#getType()
   * @see #getTUState()
   * @generated
   */
  EAttribute getTUState_Type();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUState#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUState#getName()
   * @see #getTUState()
   * @generated
   */
  EAttribute getTUState_Name();

  /**
   * Returns the meta object for the containment reference list '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUState#getMembers <em>Members</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Members</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUState#getMembers()
   * @see #getTUState()
   * @generated
   */
  EReference getTUState_Members();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUStateMember <em>TU State Member</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU State Member</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUStateMember
   * @generated
   */
  EClass getTUStateMember();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity <em>TU Entry Or Exit Activity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Entry Or Exit Activity</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity
   * @generated
   */
  EClass getTUEntryOrExitActivity();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity#isEntry <em>Entry</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Entry</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity#isEntry()
   * @see #getTUEntryOrExitActivity()
   * @generated
   */
  EAttribute getTUEntryOrExitActivity_Entry();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity#isExit <em>Exit</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Exit</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity#isExit()
   * @see #getTUEntryOrExitActivity()
   * @generated
   */
  EAttribute getTUEntryOrExitActivity_Exit();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity#getBody()
   * @see #getTUEntryOrExitActivity()
   * @generated
   */
  EReference getTUEntryOrExitActivity_Body();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition <em>TU Transition</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Transition</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition
   * @generated
   */
  EClass getTUTransition();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition#getName()
   * @see #getTUTransition()
   * @generated
   */
  EAttribute getTUTransition_Name();

  /**
   * Returns the meta object for the containment reference list '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition#getMembers <em>Members</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Members</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition#getMembers()
   * @see #getTUTransition()
   * @generated
   */
  EReference getTUTransition_Members();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionMember <em>TU Transition Member</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Transition Member</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionMember
   * @generated
   */
  EClass getTUTransitionMember();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger <em>TU Transition Trigger</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Transition Trigger</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger
   * @generated
   */
  EClass getTUTransitionTrigger();

  /**
   * Returns the meta object for the reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger#getTrigger <em>Trigger</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Trigger</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger#getTrigger()
   * @see #getTUTransitionTrigger()
   * @generated
   */
  EReference getTUTransitionTrigger_Trigger();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex <em>TU Transition Vertex</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Transition Vertex</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex
   * @generated
   */
  EClass getTUTransitionVertex();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex#isFrom <em>From</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>From</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex#isFrom()
   * @see #getTUTransitionVertex()
   * @generated
   */
  EAttribute getTUTransitionVertex_From();

  /**
   * Returns the meta object for the reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex#getVertex <em>Vertex</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Vertex</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex#getVertex()
   * @see #getTUTransitionVertex()
   * @generated
   */
  EReference getTUTransitionVertex_Vertex();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect <em>TU Transition Effect</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Transition Effect</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect
   * @generated
   */
  EClass getTUTransitionEffect();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect#getBody()
   * @see #getTUTransitionEffect()
   * @generated
   */
  EReference getTUTransitionEffect_Body();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard <em>TU Transition Guard</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Transition Guard</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard
   * @generated
   */
  EClass getTUTransitionGuard();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard#isElse <em>Else</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Else</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard#isElse()
   * @see #getTUTransitionGuard()
   * @generated
   */
  EAttribute getTUTransitionGuard_Else();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard#getExpression <em>Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Expression</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard#getExpression()
   * @see #getTUTransitionGuard()
   * @generated
   */
  EReference getTUTransitionGuard_Expression();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd <em>TU Association End</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Association End</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
   * @generated
   */
  EClass getTUAssociationEnd();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getVisibility <em>Visibility</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Visibility</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getVisibility()
   * @see #getTUAssociationEnd()
   * @generated
   */
  EAttribute getTUAssociationEnd_Visibility();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#isNotNavigable <em>Not Navigable</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Not Navigable</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#isNotNavigable()
   * @see #getTUAssociationEnd()
   * @generated
   */
  EAttribute getTUAssociationEnd_NotNavigable();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getMultiplicity <em>Multiplicity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Multiplicity</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getMultiplicity()
   * @see #getTUAssociationEnd()
   * @generated
   */
  EReference getTUAssociationEnd_Multiplicity();

  /**
   * Returns the meta object for the reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getEndClass <em>End Class</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>End Class</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getEndClass()
   * @see #getTUAssociationEnd()
   * @generated
   */
  EReference getTUAssociationEnd_EndClass();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getName()
   * @see #getTUAssociationEnd()
   * @generated
   */
  EAttribute getTUAssociationEnd_Name();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity <em>TU Multiplicity</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Multiplicity</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity
   * @generated
   */
  EClass getTUMultiplicity();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity#getLower <em>Lower</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Lower</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity#getLower()
   * @see #getTUMultiplicity()
   * @generated
   */
  EAttribute getTUMultiplicity_Lower();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity#isUpperSet <em>Upper Set</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Upper Set</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity#isUpperSet()
   * @see #getTUMultiplicity()
   * @generated
   */
  EAttribute getTUMultiplicity_UpperSet();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity#getUpper <em>Upper</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Upper</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity#getUpper()
   * @see #getTUMultiplicity()
   * @generated
   */
  EAttribute getTUMultiplicity_Upper();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity#isUpperInf <em>Upper Inf</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Upper Inf</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity#isUpperInf()
   * @see #getTUMultiplicity()
   * @generated
   */
  EAttribute getTUMultiplicity_UpperInf();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity#isAny <em>Any</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Any</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity#isAny()
   * @see #getTUMultiplicity()
   * @generated
   */
  EAttribute getTUMultiplicity_Any();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute <em>TU Attribute</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Attribute</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute
   * @generated
   */
  EClass getTUAttribute();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute#getPrefix <em>Prefix</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Prefix</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute#getPrefix()
   * @see #getTUAttribute()
   * @generated
   */
  EReference getTUAttribute_Prefix();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute#getName()
   * @see #getTUAttribute()
   * @generated
   */
  EAttribute getTUAttribute_Name();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation <em>TU Operation</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>TU Operation</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation
   * @generated
   */
  EClass getTUOperation();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation#getPrefix <em>Prefix</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Prefix</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation#getPrefix()
   * @see #getTUOperation()
   * @generated
   */
  EReference getTUOperation_Prefix();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation#getName <em>Name</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Name</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation#getName()
   * @see #getTUOperation()
   * @generated
   */
  EAttribute getTUOperation_Name();

  /**
   * Returns the meta object for the containment reference list '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation#getParameters <em>Parameters</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference list '<em>Parameters</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation#getParameters()
   * @see #getTUOperation()
   * @generated
   */
  EReference getTUOperation_Parameters();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation#getBody <em>Body</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Body</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation#getBody()
   * @see #getTUOperation()
   * @generated
   */
  EReference getTUOperation_Body();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression <em>RAlf Send Signal Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>RAlf Send Signal Expression</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression
   * @generated
   */
  EClass getRAlfSendSignalExpression();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression#getSignal <em>Signal</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Signal</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression#getSignal()
   * @see #getRAlfSendSignalExpression()
   * @generated
   */
  EReference getRAlfSendSignalExpression_Signal();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression#getTarget <em>Target</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Target</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression#getTarget()
   * @see #getRAlfSendSignalExpression()
   * @generated
   */
  EReference getRAlfSendSignalExpression_Target();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression <em>RAlf Delete Object Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>RAlf Delete Object Expression</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression
   * @generated
   */
  EClass getRAlfDeleteObjectExpression();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression#getObject <em>Object</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Object</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression#getObject()
   * @see #getRAlfDeleteObjectExpression()
   * @generated
   */
  EReference getRAlfDeleteObjectExpression_Object();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression <em>RAlf Signal Access Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>RAlf Signal Access Expression</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression
   * @generated
   */
  EClass getRAlfSignalAccessExpression();

  /**
   * Returns the meta object for the attribute '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression#getSigdata <em>Sigdata</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the attribute '<em>Sigdata</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression#getSigdata()
   * @see #getRAlfSignalAccessExpression()
   * @generated
   */
  EAttribute getRAlfSignalAccessExpression_Sigdata();

  /**
   * Returns the meta object for class '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression <em>RAlf Assoc Nav Expression</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for class '<em>RAlf Assoc Nav Expression</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression
   * @generated
   */
  EClass getRAlfAssocNavExpression();

  /**
   * Returns the meta object for the containment reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression#getLeft <em>Left</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the containment reference '<em>Left</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression#getLeft()
   * @see #getRAlfAssocNavExpression()
   * @generated
   */
  EReference getRAlfAssocNavExpression_Left();

  /**
   * Returns the meta object for the reference '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression#getRight <em>Right</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for the reference '<em>Right</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression#getRight()
   * @see #getRAlfAssocNavExpression()
   * @generated
   */
  EReference getRAlfAssocNavExpression_Right();

  /**
   * Returns the meta object for enum '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType <em>TU State Type</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>TU State Type</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType
   * @generated
   */
  EEnum getTUStateType();

  /**
   * Returns the meta object for enum '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility <em>TU Visibility</em>}'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the meta object for enum '<em>TU Visibility</em>'.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility
   * @generated
   */
  EEnum getTUVisibility();

  /**
   * Returns the factory that creates the instances of the model.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the factory that creates the instances of the model.
   * @generated
   */
  XtxtUMLFactory getXtxtUMLFactory();

  /**
   * <!-- begin-user-doc -->
   * Defines literals for the meta objects that represent
   * <ul>
   *   <li>each class,</li>
   *   <li>each feature of each class,</li>
   *   <li>each enum,</li>
   *   <li>and each data type</li>
   * </ul>
   * <!-- end-user-doc -->
   * @generated
   */
  interface Literals
  {
    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUFileImpl <em>TU File</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUFileImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUFile()
     * @generated
     */
    EClass TU_FILE = eINSTANCE.getTUFile();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_FILE__NAME = eINSTANCE.getTUFile_Name();

    /**
     * The meta object literal for the '<em><b>Import Section</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_FILE__IMPORT_SECTION = eINSTANCE.getTUFile_ImportSection();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_FILE__ELEMENTS = eINSTANCE.getTUFile_Elements();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUFileElementImpl <em>TU File Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUFileElementImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUFileElement()
     * @generated
     */
    EClass TU_FILE_ELEMENT = eINSTANCE.getTUFileElement();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_FILE_ELEMENT__NAME = eINSTANCE.getTUFileElement_Name();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUModelImpl <em>TU Model</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUModelImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUModel()
     * @generated
     */
    EClass TU_MODEL = eINSTANCE.getTUModel();

    /**
     * The meta object literal for the '<em><b>Elements</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_MODEL__ELEMENTS = eINSTANCE.getTUModel_Elements();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUExecutionImpl <em>TU Execution</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUExecutionImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUExecution()
     * @generated
     */
    EClass TU_EXECUTION = eINSTANCE.getTUExecution();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_EXECUTION__BODY = eINSTANCE.getTUExecution_Body();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUModelElementImpl <em>TU Model Element</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUModelElementImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUModelElement()
     * @generated
     */
    EClass TU_MODEL_ELEMENT = eINSTANCE.getTUModelElement();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_MODEL_ELEMENT__NAME = eINSTANCE.getTUModelElement_Name();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUSignalImpl <em>TU Signal</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUSignalImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUSignal()
     * @generated
     */
    EClass TU_SIGNAL = eINSTANCE.getTUSignal();

    /**
     * The meta object literal for the '<em><b>Attributes</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_SIGNAL__ATTRIBUTES = eINSTANCE.getTUSignal_Attributes();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUClassImpl <em>TU Class</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUClassImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUClass()
     * @generated
     */
    EClass TU_CLASS = eINSTANCE.getTUClass();

    /**
     * The meta object literal for the '<em><b>Super Class</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_CLASS__SUPER_CLASS = eINSTANCE.getTUClass_SuperClass();

    /**
     * The meta object literal for the '<em><b>Members</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_CLASS__MEMBERS = eINSTANCE.getTUClass_Members();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationImpl <em>TU Association</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUAssociation()
     * @generated
     */
    EClass TU_ASSOCIATION = eINSTANCE.getTUAssociation();

    /**
     * The meta object literal for the '<em><b>Ends</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_ASSOCIATION__ENDS = eINSTANCE.getTUAssociation_Ends();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUSignalAttributeImpl <em>TU Signal Attribute</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUSignalAttributeImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUSignalAttribute()
     * @generated
     */
    EClass TU_SIGNAL_ATTRIBUTE = eINSTANCE.getTUSignalAttribute();

    /**
     * The meta object literal for the '<em><b>Visibility</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_SIGNAL_ATTRIBUTE__VISIBILITY = eINSTANCE.getTUSignalAttribute_Visibility();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_SIGNAL_ATTRIBUTE__TYPE = eINSTANCE.getTUSignalAttribute_Type();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_SIGNAL_ATTRIBUTE__NAME = eINSTANCE.getTUSignalAttribute_Name();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUClassMemberImpl <em>TU Class Member</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUClassMemberImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUClassMember()
     * @generated
     */
    EClass TU_CLASS_MEMBER = eINSTANCE.getTUClassMember();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUConstructorImpl <em>TU Constructor</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUConstructorImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUConstructor()
     * @generated
     */
    EClass TU_CONSTRUCTOR = eINSTANCE.getTUConstructor();

    /**
     * The meta object literal for the '<em><b>Visibility</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_CONSTRUCTOR__VISIBILITY = eINSTANCE.getTUConstructor_Visibility();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_CONSTRUCTOR__NAME = eINSTANCE.getTUConstructor_Name();

    /**
     * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_CONSTRUCTOR__PARAMETERS = eINSTANCE.getTUConstructor_Parameters();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_CONSTRUCTOR__BODY = eINSTANCE.getTUConstructor_Body();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAttributeOrOperationDeclarationPrefixImpl <em>TU Attribute Or Operation Declaration Prefix</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAttributeOrOperationDeclarationPrefixImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUAttributeOrOperationDeclarationPrefix()
     * @generated
     */
    EClass TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX = eINSTANCE.getTUAttributeOrOperationDeclarationPrefix();

    /**
     * The meta object literal for the '<em><b>Visibility</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__VISIBILITY = eINSTANCE.getTUAttributeOrOperationDeclarationPrefix_Visibility();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__TYPE = eINSTANCE.getTUAttributeOrOperationDeclarationPrefix_Type();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUStateImpl <em>TU State</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUStateImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUState()
     * @generated
     */
    EClass TU_STATE = eINSTANCE.getTUState();

    /**
     * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_STATE__TYPE = eINSTANCE.getTUState_Type();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_STATE__NAME = eINSTANCE.getTUState_Name();

    /**
     * The meta object literal for the '<em><b>Members</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_STATE__MEMBERS = eINSTANCE.getTUState_Members();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUStateMemberImpl <em>TU State Member</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUStateMemberImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUStateMember()
     * @generated
     */
    EClass TU_STATE_MEMBER = eINSTANCE.getTUStateMember();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUEntryOrExitActivityImpl <em>TU Entry Or Exit Activity</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUEntryOrExitActivityImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUEntryOrExitActivity()
     * @generated
     */
    EClass TU_ENTRY_OR_EXIT_ACTIVITY = eINSTANCE.getTUEntryOrExitActivity();

    /**
     * The meta object literal for the '<em><b>Entry</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_ENTRY_OR_EXIT_ACTIVITY__ENTRY = eINSTANCE.getTUEntryOrExitActivity_Entry();

    /**
     * The meta object literal for the '<em><b>Exit</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_ENTRY_OR_EXIT_ACTIVITY__EXIT = eINSTANCE.getTUEntryOrExitActivity_Exit();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_ENTRY_OR_EXIT_ACTIVITY__BODY = eINSTANCE.getTUEntryOrExitActivity_Body();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionImpl <em>TU Transition</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUTransition()
     * @generated
     */
    EClass TU_TRANSITION = eINSTANCE.getTUTransition();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_TRANSITION__NAME = eINSTANCE.getTUTransition_Name();

    /**
     * The meta object literal for the '<em><b>Members</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_TRANSITION__MEMBERS = eINSTANCE.getTUTransition_Members();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionMemberImpl <em>TU Transition Member</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionMemberImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUTransitionMember()
     * @generated
     */
    EClass TU_TRANSITION_MEMBER = eINSTANCE.getTUTransitionMember();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionTriggerImpl <em>TU Transition Trigger</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionTriggerImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUTransitionTrigger()
     * @generated
     */
    EClass TU_TRANSITION_TRIGGER = eINSTANCE.getTUTransitionTrigger();

    /**
     * The meta object literal for the '<em><b>Trigger</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_TRANSITION_TRIGGER__TRIGGER = eINSTANCE.getTUTransitionTrigger_Trigger();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionVertexImpl <em>TU Transition Vertex</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionVertexImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUTransitionVertex()
     * @generated
     */
    EClass TU_TRANSITION_VERTEX = eINSTANCE.getTUTransitionVertex();

    /**
     * The meta object literal for the '<em><b>From</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_TRANSITION_VERTEX__FROM = eINSTANCE.getTUTransitionVertex_From();

    /**
     * The meta object literal for the '<em><b>Vertex</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_TRANSITION_VERTEX__VERTEX = eINSTANCE.getTUTransitionVertex_Vertex();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionEffectImpl <em>TU Transition Effect</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionEffectImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUTransitionEffect()
     * @generated
     */
    EClass TU_TRANSITION_EFFECT = eINSTANCE.getTUTransitionEffect();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_TRANSITION_EFFECT__BODY = eINSTANCE.getTUTransitionEffect_Body();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionGuardImpl <em>TU Transition Guard</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionGuardImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUTransitionGuard()
     * @generated
     */
    EClass TU_TRANSITION_GUARD = eINSTANCE.getTUTransitionGuard();

    /**
     * The meta object literal for the '<em><b>Else</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_TRANSITION_GUARD__ELSE = eINSTANCE.getTUTransitionGuard_Else();

    /**
     * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_TRANSITION_GUARD__EXPRESSION = eINSTANCE.getTUTransitionGuard_Expression();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationEndImpl <em>TU Association End</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationEndImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUAssociationEnd()
     * @generated
     */
    EClass TU_ASSOCIATION_END = eINSTANCE.getTUAssociationEnd();

    /**
     * The meta object literal for the '<em><b>Visibility</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_ASSOCIATION_END__VISIBILITY = eINSTANCE.getTUAssociationEnd_Visibility();

    /**
     * The meta object literal for the '<em><b>Not Navigable</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_ASSOCIATION_END__NOT_NAVIGABLE = eINSTANCE.getTUAssociationEnd_NotNavigable();

    /**
     * The meta object literal for the '<em><b>Multiplicity</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_ASSOCIATION_END__MULTIPLICITY = eINSTANCE.getTUAssociationEnd_Multiplicity();

    /**
     * The meta object literal for the '<em><b>End Class</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_ASSOCIATION_END__END_CLASS = eINSTANCE.getTUAssociationEnd_EndClass();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_ASSOCIATION_END__NAME = eINSTANCE.getTUAssociationEnd_Name();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUMultiplicityImpl <em>TU Multiplicity</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUMultiplicityImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUMultiplicity()
     * @generated
     */
    EClass TU_MULTIPLICITY = eINSTANCE.getTUMultiplicity();

    /**
     * The meta object literal for the '<em><b>Lower</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_MULTIPLICITY__LOWER = eINSTANCE.getTUMultiplicity_Lower();

    /**
     * The meta object literal for the '<em><b>Upper Set</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_MULTIPLICITY__UPPER_SET = eINSTANCE.getTUMultiplicity_UpperSet();

    /**
     * The meta object literal for the '<em><b>Upper</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_MULTIPLICITY__UPPER = eINSTANCE.getTUMultiplicity_Upper();

    /**
     * The meta object literal for the '<em><b>Upper Inf</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_MULTIPLICITY__UPPER_INF = eINSTANCE.getTUMultiplicity_UpperInf();

    /**
     * The meta object literal for the '<em><b>Any</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_MULTIPLICITY__ANY = eINSTANCE.getTUMultiplicity_Any();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAttributeImpl <em>TU Attribute</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAttributeImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUAttribute()
     * @generated
     */
    EClass TU_ATTRIBUTE = eINSTANCE.getTUAttribute();

    /**
     * The meta object literal for the '<em><b>Prefix</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_ATTRIBUTE__PREFIX = eINSTANCE.getTUAttribute_Prefix();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_ATTRIBUTE__NAME = eINSTANCE.getTUAttribute_Name();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUOperationImpl <em>TU Operation</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUOperationImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUOperation()
     * @generated
     */
    EClass TU_OPERATION = eINSTANCE.getTUOperation();

    /**
     * The meta object literal for the '<em><b>Prefix</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_OPERATION__PREFIX = eINSTANCE.getTUOperation_Prefix();

    /**
     * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute TU_OPERATION__NAME = eINSTANCE.getTUOperation_Name();

    /**
     * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_OPERATION__PARAMETERS = eINSTANCE.getTUOperation_Parameters();

    /**
     * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference TU_OPERATION__BODY = eINSTANCE.getTUOperation_Body();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfSendSignalExpressionImpl <em>RAlf Send Signal Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfSendSignalExpressionImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getRAlfSendSignalExpression()
     * @generated
     */
    EClass RALF_SEND_SIGNAL_EXPRESSION = eINSTANCE.getRAlfSendSignalExpression();

    /**
     * The meta object literal for the '<em><b>Signal</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RALF_SEND_SIGNAL_EXPRESSION__SIGNAL = eINSTANCE.getRAlfSendSignalExpression_Signal();

    /**
     * The meta object literal for the '<em><b>Target</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RALF_SEND_SIGNAL_EXPRESSION__TARGET = eINSTANCE.getRAlfSendSignalExpression_Target();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfDeleteObjectExpressionImpl <em>RAlf Delete Object Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfDeleteObjectExpressionImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getRAlfDeleteObjectExpression()
     * @generated
     */
    EClass RALF_DELETE_OBJECT_EXPRESSION = eINSTANCE.getRAlfDeleteObjectExpression();

    /**
     * The meta object literal for the '<em><b>Object</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RALF_DELETE_OBJECT_EXPRESSION__OBJECT = eINSTANCE.getRAlfDeleteObjectExpression_Object();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfSignalAccessExpressionImpl <em>RAlf Signal Access Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfSignalAccessExpressionImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getRAlfSignalAccessExpression()
     * @generated
     */
    EClass RALF_SIGNAL_ACCESS_EXPRESSION = eINSTANCE.getRAlfSignalAccessExpression();

    /**
     * The meta object literal for the '<em><b>Sigdata</b></em>' attribute feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EAttribute RALF_SIGNAL_ACCESS_EXPRESSION__SIGDATA = eINSTANCE.getRAlfSignalAccessExpression_Sigdata();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfAssocNavExpressionImpl <em>RAlf Assoc Nav Expression</em>}' class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfAssocNavExpressionImpl
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getRAlfAssocNavExpression()
     * @generated
     */
    EClass RALF_ASSOC_NAV_EXPRESSION = eINSTANCE.getRAlfAssocNavExpression();

    /**
     * The meta object literal for the '<em><b>Left</b></em>' containment reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RALF_ASSOC_NAV_EXPRESSION__LEFT = eINSTANCE.getRAlfAssocNavExpression_Left();

    /**
     * The meta object literal for the '<em><b>Right</b></em>' reference feature.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    EReference RALF_ASSOC_NAV_EXPRESSION__RIGHT = eINSTANCE.getRAlfAssocNavExpression_Right();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType <em>TU State Type</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUStateType()
     * @generated
     */
    EEnum TU_STATE_TYPE = eINSTANCE.getTUStateType();

    /**
     * The meta object literal for the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility <em>TU Visibility</em>}' enum.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility
     * @see hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLPackageImpl#getTUVisibility()
     * @generated
     */
    EEnum TU_VISIBILITY = eINSTANCE.getTUVisibility();

  }

} //XtxtUMLPackage
