/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML.impl;

import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression;
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfDeleteObjectExpression;
import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClassMember;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUExecution;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFile;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUFileElement;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModel;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUOperation;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUState;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateMember;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUStateType;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionEffect;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionMember;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionTrigger;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility;
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLFactory;
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.xtext.common.types.TypesPackage;

import org.eclipse.xtext.xbase.XbasePackage;

import org.eclipse.xtext.xtype.XtypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class XtxtUMLPackageImpl extends EPackageImpl implements XtxtUMLPackage
{
  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuFileEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuFileElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuModelEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuExecutionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuModelElementEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuSignalEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuClassEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuAssociationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuSignalAttributeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuClassMemberEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuConstructorEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuAttributeOrOperationDeclarationPrefixEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuStateEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuStateMemberEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuEntryOrExitActivityEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuTransitionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuTransitionMemberEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuTransitionTriggerEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuTransitionVertexEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuTransitionEffectEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuTransitionGuardEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuAssociationEndEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuMultiplicityEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuAttributeEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass tuOperationEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass rAlfSendSignalExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass rAlfDeleteObjectExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EClass rAlfAssocNavExpressionEClass = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum tuStateTypeEEnum = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private EEnum tuVisibilityEEnum = null;

  /**
   * Creates an instance of the model <b>Package</b>, registered with
   * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
   * package URI value.
   * <p>Note: the correct way to create the package is via the static
   * factory method {@link #init init()}, which also performs
   * initialization of the package, or returns the registered package,
   * if one already exists.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see org.eclipse.emf.ecore.EPackage.Registry
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#eNS_URI
   * @see #init()
   * @generated
   */
  private XtxtUMLPackageImpl()
  {
    super(eNS_URI, XtxtUMLFactory.eINSTANCE);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static boolean isInited = false;

  /**
   * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
   * 
   * <p>This method is used to initialize {@link XtxtUMLPackage#eINSTANCE} when that field is accessed.
   * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #eNS_URI
   * @see #createPackageContents()
   * @see #initializePackageContents()
   * @generated
   */
  public static XtxtUMLPackage init()
  {
    if (isInited) return (XtxtUMLPackage)EPackage.Registry.INSTANCE.getEPackage(XtxtUMLPackage.eNS_URI);

    // Obtain or create and register package
    XtxtUMLPackageImpl theXtxtUMLPackage = (XtxtUMLPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof XtxtUMLPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new XtxtUMLPackageImpl());

    isInited = true;

    // Initialize simple dependencies
    XbasePackage.eINSTANCE.eClass();
    XtypePackage.eINSTANCE.eClass();

    // Create package meta-data objects
    theXtxtUMLPackage.createPackageContents();

    // Initialize created meta-data
    theXtxtUMLPackage.initializePackageContents();

    // Mark meta-data to indicate it can't be changed
    theXtxtUMLPackage.freeze();

  
    // Update the registry and return the package
    EPackage.Registry.INSTANCE.put(XtxtUMLPackage.eNS_URI, theXtxtUMLPackage);
    return theXtxtUMLPackage;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUFile()
  {
    return tuFileEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUFile_Name()
  {
    return (EAttribute)tuFileEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUFile_ImportSection()
  {
    return (EReference)tuFileEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUFile_Elements()
  {
    return (EReference)tuFileEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUFileElement()
  {
    return tuFileElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUFileElement_Name()
  {
    return (EAttribute)tuFileElementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUModel()
  {
    return tuModelEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUModel_Elements()
  {
    return (EReference)tuModelEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUExecution()
  {
    return tuExecutionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUExecution_Body()
  {
    return (EReference)tuExecutionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUModelElement()
  {
    return tuModelElementEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUModelElement_Name()
  {
    return (EAttribute)tuModelElementEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUSignal()
  {
    return tuSignalEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUSignal_Attributes()
  {
    return (EReference)tuSignalEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUClass()
  {
    return tuClassEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUClass_SuperClass()
  {
    return (EReference)tuClassEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUClass_Members()
  {
    return (EReference)tuClassEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUAssociation()
  {
    return tuAssociationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUAssociation_Ends()
  {
    return (EReference)tuAssociationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUSignalAttribute()
  {
    return tuSignalAttributeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUSignalAttribute_Visibility()
  {
    return (EAttribute)tuSignalAttributeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUSignalAttribute_Type()
  {
    return (EReference)tuSignalAttributeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUSignalAttribute_Name()
  {
    return (EAttribute)tuSignalAttributeEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUClassMember()
  {
    return tuClassMemberEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUConstructor()
  {
    return tuConstructorEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUConstructor_Visibility()
  {
    return (EAttribute)tuConstructorEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUConstructor_Name()
  {
    return (EAttribute)tuConstructorEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUConstructor_Parameters()
  {
    return (EReference)tuConstructorEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUConstructor_Body()
  {
    return (EReference)tuConstructorEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUAttributeOrOperationDeclarationPrefix()
  {
    return tuAttributeOrOperationDeclarationPrefixEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUAttributeOrOperationDeclarationPrefix_Visibility()
  {
    return (EAttribute)tuAttributeOrOperationDeclarationPrefixEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUAttributeOrOperationDeclarationPrefix_Type()
  {
    return (EReference)tuAttributeOrOperationDeclarationPrefixEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUState()
  {
    return tuStateEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUState_Type()
  {
    return (EAttribute)tuStateEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUState_Name()
  {
    return (EAttribute)tuStateEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUState_Members()
  {
    return (EReference)tuStateEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUStateMember()
  {
    return tuStateMemberEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUEntryOrExitActivity()
  {
    return tuEntryOrExitActivityEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUEntryOrExitActivity_Entry()
  {
    return (EAttribute)tuEntryOrExitActivityEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUEntryOrExitActivity_Exit()
  {
    return (EAttribute)tuEntryOrExitActivityEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUEntryOrExitActivity_Body()
  {
    return (EReference)tuEntryOrExitActivityEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUTransition()
  {
    return tuTransitionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUTransition_Name()
  {
    return (EAttribute)tuTransitionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUTransition_Members()
  {
    return (EReference)tuTransitionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUTransitionMember()
  {
    return tuTransitionMemberEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUTransitionTrigger()
  {
    return tuTransitionTriggerEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUTransitionTrigger_Trigger()
  {
    return (EReference)tuTransitionTriggerEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUTransitionVertex()
  {
    return tuTransitionVertexEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUTransitionVertex_From()
  {
    return (EAttribute)tuTransitionVertexEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUTransitionVertex_Vertex()
  {
    return (EReference)tuTransitionVertexEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUTransitionEffect()
  {
    return tuTransitionEffectEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUTransitionEffect_Body()
  {
    return (EReference)tuTransitionEffectEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUTransitionGuard()
  {
    return tuTransitionGuardEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUTransitionGuard_Expression()
  {
    return (EReference)tuTransitionGuardEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUAssociationEnd()
  {
    return tuAssociationEndEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUAssociationEnd_Visibility()
  {
    return (EAttribute)tuAssociationEndEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUAssociationEnd_NotNavigable()
  {
    return (EAttribute)tuAssociationEndEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUAssociationEnd_Multiplicity()
  {
    return (EReference)tuAssociationEndEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUAssociationEnd_EndClass()
  {
    return (EReference)tuAssociationEndEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUAssociationEnd_Name()
  {
    return (EAttribute)tuAssociationEndEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUMultiplicity()
  {
    return tuMultiplicityEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUMultiplicity_Lower()
  {
    return (EAttribute)tuMultiplicityEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUMultiplicity_UpperSet()
  {
    return (EAttribute)tuMultiplicityEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUMultiplicity_Upper()
  {
    return (EAttribute)tuMultiplicityEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUMultiplicity_UpperInf()
  {
    return (EAttribute)tuMultiplicityEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUMultiplicity_Any()
  {
    return (EAttribute)tuMultiplicityEClass.getEStructuralFeatures().get(4);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUAttribute()
  {
    return tuAttributeEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUAttribute_Prefix()
  {
    return (EReference)tuAttributeEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUAttribute_Name()
  {
    return (EAttribute)tuAttributeEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getTUOperation()
  {
    return tuOperationEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUOperation_Prefix()
  {
    return (EReference)tuOperationEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EAttribute getTUOperation_Name()
  {
    return (EAttribute)tuOperationEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUOperation_Parameters()
  {
    return (EReference)tuOperationEClass.getEStructuralFeatures().get(2);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getTUOperation_Body()
  {
    return (EReference)tuOperationEClass.getEStructuralFeatures().get(3);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRAlfSendSignalExpression()
  {
    return rAlfSendSignalExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRAlfSendSignalExpression_Signal()
  {
    return (EReference)rAlfSendSignalExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRAlfSendSignalExpression_Target()
  {
    return (EReference)rAlfSendSignalExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRAlfDeleteObjectExpression()
  {
    return rAlfDeleteObjectExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRAlfDeleteObjectExpression_Object()
  {
    return (EReference)rAlfDeleteObjectExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EClass getRAlfAssocNavExpression()
  {
    return rAlfAssocNavExpressionEClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRAlfAssocNavExpression_Left()
  {
    return (EReference)rAlfAssocNavExpressionEClass.getEStructuralFeatures().get(0);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EReference getRAlfAssocNavExpression_Right()
  {
    return (EReference)rAlfAssocNavExpressionEClass.getEStructuralFeatures().get(1);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getTUStateType()
  {
    return tuStateTypeEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EEnum getTUVisibility()
  {
    return tuVisibilityEEnum;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XtxtUMLFactory getXtxtUMLFactory()
  {
    return (XtxtUMLFactory)getEFactoryInstance();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isCreated = false;

  /**
   * Creates the meta-model objects for the package.  This method is
   * guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void createPackageContents()
  {
    if (isCreated) return;
    isCreated = true;

    // Create classes and their features
    tuFileEClass = createEClass(TU_FILE);
    createEAttribute(tuFileEClass, TU_FILE__NAME);
    createEReference(tuFileEClass, TU_FILE__IMPORT_SECTION);
    createEReference(tuFileEClass, TU_FILE__ELEMENTS);

    tuFileElementEClass = createEClass(TU_FILE_ELEMENT);
    createEAttribute(tuFileElementEClass, TU_FILE_ELEMENT__NAME);

    tuModelEClass = createEClass(TU_MODEL);
    createEReference(tuModelEClass, TU_MODEL__ELEMENTS);

    tuExecutionEClass = createEClass(TU_EXECUTION);
    createEReference(tuExecutionEClass, TU_EXECUTION__BODY);

    tuModelElementEClass = createEClass(TU_MODEL_ELEMENT);
    createEAttribute(tuModelElementEClass, TU_MODEL_ELEMENT__NAME);

    tuSignalEClass = createEClass(TU_SIGNAL);
    createEReference(tuSignalEClass, TU_SIGNAL__ATTRIBUTES);

    tuClassEClass = createEClass(TU_CLASS);
    createEReference(tuClassEClass, TU_CLASS__SUPER_CLASS);
    createEReference(tuClassEClass, TU_CLASS__MEMBERS);

    tuAssociationEClass = createEClass(TU_ASSOCIATION);
    createEReference(tuAssociationEClass, TU_ASSOCIATION__ENDS);

    tuSignalAttributeEClass = createEClass(TU_SIGNAL_ATTRIBUTE);
    createEAttribute(tuSignalAttributeEClass, TU_SIGNAL_ATTRIBUTE__VISIBILITY);
    createEReference(tuSignalAttributeEClass, TU_SIGNAL_ATTRIBUTE__TYPE);
    createEAttribute(tuSignalAttributeEClass, TU_SIGNAL_ATTRIBUTE__NAME);

    tuClassMemberEClass = createEClass(TU_CLASS_MEMBER);

    tuConstructorEClass = createEClass(TU_CONSTRUCTOR);
    createEAttribute(tuConstructorEClass, TU_CONSTRUCTOR__VISIBILITY);
    createEAttribute(tuConstructorEClass, TU_CONSTRUCTOR__NAME);
    createEReference(tuConstructorEClass, TU_CONSTRUCTOR__PARAMETERS);
    createEReference(tuConstructorEClass, TU_CONSTRUCTOR__BODY);

    tuAttributeOrOperationDeclarationPrefixEClass = createEClass(TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX);
    createEAttribute(tuAttributeOrOperationDeclarationPrefixEClass, TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__VISIBILITY);
    createEReference(tuAttributeOrOperationDeclarationPrefixEClass, TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__TYPE);

    tuStateEClass = createEClass(TU_STATE);
    createEAttribute(tuStateEClass, TU_STATE__TYPE);
    createEAttribute(tuStateEClass, TU_STATE__NAME);
    createEReference(tuStateEClass, TU_STATE__MEMBERS);

    tuStateMemberEClass = createEClass(TU_STATE_MEMBER);

    tuEntryOrExitActivityEClass = createEClass(TU_ENTRY_OR_EXIT_ACTIVITY);
    createEAttribute(tuEntryOrExitActivityEClass, TU_ENTRY_OR_EXIT_ACTIVITY__ENTRY);
    createEAttribute(tuEntryOrExitActivityEClass, TU_ENTRY_OR_EXIT_ACTIVITY__EXIT);
    createEReference(tuEntryOrExitActivityEClass, TU_ENTRY_OR_EXIT_ACTIVITY__BODY);

    tuTransitionEClass = createEClass(TU_TRANSITION);
    createEAttribute(tuTransitionEClass, TU_TRANSITION__NAME);
    createEReference(tuTransitionEClass, TU_TRANSITION__MEMBERS);

    tuTransitionMemberEClass = createEClass(TU_TRANSITION_MEMBER);

    tuTransitionTriggerEClass = createEClass(TU_TRANSITION_TRIGGER);
    createEReference(tuTransitionTriggerEClass, TU_TRANSITION_TRIGGER__TRIGGER);

    tuTransitionVertexEClass = createEClass(TU_TRANSITION_VERTEX);
    createEAttribute(tuTransitionVertexEClass, TU_TRANSITION_VERTEX__FROM);
    createEReference(tuTransitionVertexEClass, TU_TRANSITION_VERTEX__VERTEX);

    tuTransitionEffectEClass = createEClass(TU_TRANSITION_EFFECT);
    createEReference(tuTransitionEffectEClass, TU_TRANSITION_EFFECT__BODY);

    tuTransitionGuardEClass = createEClass(TU_TRANSITION_GUARD);
    createEReference(tuTransitionGuardEClass, TU_TRANSITION_GUARD__EXPRESSION);

    tuAssociationEndEClass = createEClass(TU_ASSOCIATION_END);
    createEAttribute(tuAssociationEndEClass, TU_ASSOCIATION_END__VISIBILITY);
    createEAttribute(tuAssociationEndEClass, TU_ASSOCIATION_END__NOT_NAVIGABLE);
    createEReference(tuAssociationEndEClass, TU_ASSOCIATION_END__MULTIPLICITY);
    createEReference(tuAssociationEndEClass, TU_ASSOCIATION_END__END_CLASS);
    createEAttribute(tuAssociationEndEClass, TU_ASSOCIATION_END__NAME);

    tuMultiplicityEClass = createEClass(TU_MULTIPLICITY);
    createEAttribute(tuMultiplicityEClass, TU_MULTIPLICITY__LOWER);
    createEAttribute(tuMultiplicityEClass, TU_MULTIPLICITY__UPPER_SET);
    createEAttribute(tuMultiplicityEClass, TU_MULTIPLICITY__UPPER);
    createEAttribute(tuMultiplicityEClass, TU_MULTIPLICITY__UPPER_INF);
    createEAttribute(tuMultiplicityEClass, TU_MULTIPLICITY__ANY);

    tuAttributeEClass = createEClass(TU_ATTRIBUTE);
    createEReference(tuAttributeEClass, TU_ATTRIBUTE__PREFIX);
    createEAttribute(tuAttributeEClass, TU_ATTRIBUTE__NAME);

    tuOperationEClass = createEClass(TU_OPERATION);
    createEReference(tuOperationEClass, TU_OPERATION__PREFIX);
    createEAttribute(tuOperationEClass, TU_OPERATION__NAME);
    createEReference(tuOperationEClass, TU_OPERATION__PARAMETERS);
    createEReference(tuOperationEClass, TU_OPERATION__BODY);

    rAlfSendSignalExpressionEClass = createEClass(RALF_SEND_SIGNAL_EXPRESSION);
    createEReference(rAlfSendSignalExpressionEClass, RALF_SEND_SIGNAL_EXPRESSION__SIGNAL);
    createEReference(rAlfSendSignalExpressionEClass, RALF_SEND_SIGNAL_EXPRESSION__TARGET);

    rAlfDeleteObjectExpressionEClass = createEClass(RALF_DELETE_OBJECT_EXPRESSION);
    createEReference(rAlfDeleteObjectExpressionEClass, RALF_DELETE_OBJECT_EXPRESSION__OBJECT);

    rAlfAssocNavExpressionEClass = createEClass(RALF_ASSOC_NAV_EXPRESSION);
    createEReference(rAlfAssocNavExpressionEClass, RALF_ASSOC_NAV_EXPRESSION__LEFT);
    createEReference(rAlfAssocNavExpressionEClass, RALF_ASSOC_NAV_EXPRESSION__RIGHT);

    // Create enums
    tuStateTypeEEnum = createEEnum(TU_STATE_TYPE);
    tuVisibilityEEnum = createEEnum(TU_VISIBILITY);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private boolean isInitialized = false;

  /**
   * Complete the initialization of the package and its meta-model.  This
   * method is guarded to have no affect on any invocation but its first.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void initializePackageContents()
  {
    if (isInitialized) return;
    isInitialized = true;

    // Initialize package
    setName(eNAME);
    setNsPrefix(eNS_PREFIX);
    setNsURI(eNS_URI);

    // Obtain other dependent packages
    XtypePackage theXtypePackage = (XtypePackage)EPackage.Registry.INSTANCE.getEPackage(XtypePackage.eNS_URI);
    XbasePackage theXbasePackage = (XbasePackage)EPackage.Registry.INSTANCE.getEPackage(XbasePackage.eNS_URI);
    TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);

    // Create type parameters

    // Set bounds for type parameters

    // Add supertypes to classes
    tuModelEClass.getESuperTypes().add(this.getTUFileElement());
    tuExecutionEClass.getESuperTypes().add(this.getTUFileElement());
    tuSignalEClass.getESuperTypes().add(this.getTUModelElement());
    tuClassEClass.getESuperTypes().add(this.getTUModelElement());
    tuAssociationEClass.getESuperTypes().add(this.getTUModelElement());
    tuConstructorEClass.getESuperTypes().add(this.getTUClassMember());
    tuAttributeOrOperationDeclarationPrefixEClass.getESuperTypes().add(this.getTUClassMember());
    tuStateEClass.getESuperTypes().add(this.getTUClassMember());
    tuStateEClass.getESuperTypes().add(this.getTUStateMember());
    tuEntryOrExitActivityEClass.getESuperTypes().add(this.getTUStateMember());
    tuTransitionEClass.getESuperTypes().add(this.getTUClassMember());
    tuTransitionTriggerEClass.getESuperTypes().add(this.getTUTransitionMember());
    tuTransitionVertexEClass.getESuperTypes().add(this.getTUTransitionMember());
    tuTransitionEffectEClass.getESuperTypes().add(this.getTUTransitionMember());
    tuTransitionGuardEClass.getESuperTypes().add(this.getTUTransitionMember());
    tuAttributeEClass.getESuperTypes().add(this.getTUClassMember());
    tuOperationEClass.getESuperTypes().add(this.getTUClassMember());
    rAlfSendSignalExpressionEClass.getESuperTypes().add(theXbasePackage.getXExpression());
    rAlfDeleteObjectExpressionEClass.getESuperTypes().add(theXbasePackage.getXExpression());
    rAlfAssocNavExpressionEClass.getESuperTypes().add(theXbasePackage.getXExpression());

    // Initialize classes and features; add operations and parameters
    initEClass(tuFileEClass, TUFile.class, "TUFile", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTUFile_Name(), ecorePackage.getEString(), "name", null, 0, 1, TUFile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUFile_ImportSection(), theXtypePackage.getXImportSection(), null, "importSection", null, 0, 1, TUFile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUFile_Elements(), this.getTUFileElement(), null, "elements", null, 0, -1, TUFile.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuFileElementEClass, TUFileElement.class, "TUFileElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTUFileElement_Name(), ecorePackage.getEString(), "name", null, 0, 1, TUFileElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuModelEClass, TUModel.class, "TUModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTUModel_Elements(), this.getTUModelElement(), null, "elements", null, 0, -1, TUModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuExecutionEClass, TUExecution.class, "TUExecution", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTUExecution_Body(), theXbasePackage.getXExpression(), null, "body", null, 0, 1, TUExecution.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuModelElementEClass, TUModelElement.class, "TUModelElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTUModelElement_Name(), ecorePackage.getEString(), "name", null, 0, 1, TUModelElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuSignalEClass, TUSignal.class, "TUSignal", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTUSignal_Attributes(), this.getTUSignalAttribute(), null, "attributes", null, 0, -1, TUSignal.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuClassEClass, TUClass.class, "TUClass", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTUClass_SuperClass(), this.getTUClass(), null, "superClass", null, 0, 1, TUClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUClass_Members(), this.getTUClassMember(), null, "members", null, 0, -1, TUClass.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuAssociationEClass, TUAssociation.class, "TUAssociation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTUAssociation_Ends(), this.getTUAssociationEnd(), null, "ends", null, 0, -1, TUAssociation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuSignalAttributeEClass, TUSignalAttribute.class, "TUSignalAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTUSignalAttribute_Visibility(), this.getTUVisibility(), "visibility", null, 0, 1, TUSignalAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUSignalAttribute_Type(), theTypesPackage.getJvmTypeReference(), null, "type", null, 0, 1, TUSignalAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTUSignalAttribute_Name(), ecorePackage.getEString(), "name", null, 0, 1, TUSignalAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuClassMemberEClass, TUClassMember.class, "TUClassMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(tuConstructorEClass, TUConstructor.class, "TUConstructor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTUConstructor_Visibility(), this.getTUVisibility(), "visibility", null, 0, 1, TUConstructor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTUConstructor_Name(), ecorePackage.getEString(), "name", null, 0, 1, TUConstructor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUConstructor_Parameters(), theTypesPackage.getJvmFormalParameter(), null, "parameters", null, 0, -1, TUConstructor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUConstructor_Body(), theXbasePackage.getXExpression(), null, "body", null, 0, 1, TUConstructor.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuAttributeOrOperationDeclarationPrefixEClass, TUAttributeOrOperationDeclarationPrefix.class, "TUAttributeOrOperationDeclarationPrefix", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTUAttributeOrOperationDeclarationPrefix_Visibility(), this.getTUVisibility(), "visibility", null, 0, 1, TUAttributeOrOperationDeclarationPrefix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUAttributeOrOperationDeclarationPrefix_Type(), theTypesPackage.getJvmTypeReference(), null, "type", null, 0, 1, TUAttributeOrOperationDeclarationPrefix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuStateEClass, TUState.class, "TUState", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTUState_Type(), this.getTUStateType(), "type", null, 0, 1, TUState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTUState_Name(), ecorePackage.getEString(), "name", null, 0, 1, TUState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUState_Members(), this.getTUStateMember(), null, "members", null, 0, -1, TUState.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuStateMemberEClass, TUStateMember.class, "TUStateMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(tuEntryOrExitActivityEClass, TUEntryOrExitActivity.class, "TUEntryOrExitActivity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTUEntryOrExitActivity_Entry(), ecorePackage.getEBoolean(), "entry", null, 0, 1, TUEntryOrExitActivity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTUEntryOrExitActivity_Exit(), ecorePackage.getEBoolean(), "exit", null, 0, 1, TUEntryOrExitActivity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUEntryOrExitActivity_Body(), theXbasePackage.getXExpression(), null, "body", null, 0, 1, TUEntryOrExitActivity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuTransitionEClass, TUTransition.class, "TUTransition", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTUTransition_Name(), ecorePackage.getEString(), "name", null, 0, 1, TUTransition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUTransition_Members(), this.getTUTransitionMember(), null, "members", null, 0, -1, TUTransition.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuTransitionMemberEClass, TUTransitionMember.class, "TUTransitionMember", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

    initEClass(tuTransitionTriggerEClass, TUTransitionTrigger.class, "TUTransitionTrigger", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTUTransitionTrigger_Trigger(), this.getTUSignal(), null, "trigger", null, 0, 1, TUTransitionTrigger.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuTransitionVertexEClass, TUTransitionVertex.class, "TUTransitionVertex", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTUTransitionVertex_From(), ecorePackage.getEBoolean(), "from", null, 0, 1, TUTransitionVertex.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUTransitionVertex_Vertex(), this.getTUState(), null, "vertex", null, 0, 1, TUTransitionVertex.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuTransitionEffectEClass, TUTransitionEffect.class, "TUTransitionEffect", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTUTransitionEffect_Body(), theXbasePackage.getXExpression(), null, "body", null, 0, 1, TUTransitionEffect.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuTransitionGuardEClass, TUTransitionGuard.class, "TUTransitionGuard", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTUTransitionGuard_Expression(), theXbasePackage.getXExpression(), null, "expression", null, 0, 1, TUTransitionGuard.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuAssociationEndEClass, TUAssociationEnd.class, "TUAssociationEnd", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTUAssociationEnd_Visibility(), this.getTUVisibility(), "visibility", null, 0, 1, TUAssociationEnd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTUAssociationEnd_NotNavigable(), ecorePackage.getEBoolean(), "notNavigable", null, 0, 1, TUAssociationEnd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUAssociationEnd_Multiplicity(), this.getTUMultiplicity(), null, "multiplicity", null, 0, 1, TUAssociationEnd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUAssociationEnd_EndClass(), this.getTUClass(), null, "endClass", null, 0, 1, TUAssociationEnd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTUAssociationEnd_Name(), ecorePackage.getEString(), "name", null, 0, 1, TUAssociationEnd.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuMultiplicityEClass, TUMultiplicity.class, "TUMultiplicity", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEAttribute(getTUMultiplicity_Lower(), ecorePackage.getEInt(), "lower", null, 0, 1, TUMultiplicity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTUMultiplicity_UpperSet(), ecorePackage.getEBoolean(), "upperSet", null, 0, 1, TUMultiplicity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTUMultiplicity_Upper(), ecorePackage.getEInt(), "upper", null, 0, 1, TUMultiplicity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTUMultiplicity_UpperInf(), ecorePackage.getEBoolean(), "upperInf", null, 0, 1, TUMultiplicity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTUMultiplicity_Any(), ecorePackage.getEBoolean(), "any", null, 0, 1, TUMultiplicity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuAttributeEClass, TUAttribute.class, "TUAttribute", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTUAttribute_Prefix(), this.getTUAttributeOrOperationDeclarationPrefix(), null, "prefix", null, 0, 1, TUAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTUAttribute_Name(), ecorePackage.getEString(), "name", null, 0, 1, TUAttribute.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(tuOperationEClass, TUOperation.class, "TUOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getTUOperation_Prefix(), this.getTUAttributeOrOperationDeclarationPrefix(), null, "prefix", null, 0, 1, TUOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEAttribute(getTUOperation_Name(), ecorePackage.getEString(), "name", null, 0, 1, TUOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUOperation_Parameters(), theTypesPackage.getJvmFormalParameter(), null, "parameters", null, 0, -1, TUOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getTUOperation_Body(), theXbasePackage.getXExpression(), null, "body", null, 0, 1, TUOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(rAlfSendSignalExpressionEClass, RAlfSendSignalExpression.class, "RAlfSendSignalExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getRAlfSendSignalExpression_Signal(), theXbasePackage.getXExpression(), null, "signal", null, 0, 1, RAlfSendSignalExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getRAlfSendSignalExpression_Target(), theXbasePackage.getXExpression(), null, "target", null, 0, 1, RAlfSendSignalExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(rAlfDeleteObjectExpressionEClass, RAlfDeleteObjectExpression.class, "RAlfDeleteObjectExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getRAlfDeleteObjectExpression_Object(), theXbasePackage.getXExpression(), null, "object", null, 0, 1, RAlfDeleteObjectExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    initEClass(rAlfAssocNavExpressionEClass, RAlfAssocNavExpression.class, "RAlfAssocNavExpression", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
    initEReference(getRAlfAssocNavExpression_Left(), theXbasePackage.getXExpression(), null, "left", null, 0, 1, RAlfAssocNavExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
    initEReference(getRAlfAssocNavExpression_Right(), this.getTUAssociationEnd(), null, "right", null, 0, 1, RAlfAssocNavExpression.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

    // Initialize enums and add enum literals
    initEEnum(tuStateTypeEEnum, TUStateType.class, "TUStateType");
    addEEnumLiteral(tuStateTypeEEnum, TUStateType.PLAIN);
    addEEnumLiteral(tuStateTypeEEnum, TUStateType.INITIAL);
    addEEnumLiteral(tuStateTypeEEnum, TUStateType.CHOICE);
    addEEnumLiteral(tuStateTypeEEnum, TUStateType.COMPOSITE);

    initEEnum(tuVisibilityEEnum, TUVisibility.class, "TUVisibility");
    addEEnumLiteral(tuVisibilityEEnum, TUVisibility.PACKAGE);
    addEEnumLiteral(tuVisibilityEEnum, TUVisibility.PRIVATE);
    addEEnumLiteral(tuVisibilityEEnum, TUVisibility.PROTECTED);
    addEEnumLiteral(tuVisibilityEEnum, TUVisibility.PUBLIC);

    // Create resource
    createResource(eNS_URI);
  }

} //XtxtUMLPackageImpl
