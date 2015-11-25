/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
 * @generated
 */
public interface XtxtUMLFactory extends EFactory
{
  /**
   * The singleton instance of the factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  XtxtUMLFactory eINSTANCE = hu.elte.txtuml.xtxtuml.xtxtUML.impl.XtxtUMLFactoryImpl.init();

  /**
   * Returns a new object of class '<em>TU File</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU File</em>'.
   * @generated
   */
  TUFile createTUFile();

  /**
   * Returns a new object of class '<em>TU Model Declaration</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Model Declaration</em>'.
   * @generated
   */
  TUModelDeclaration createTUModelDeclaration();

  /**
   * Returns a new object of class '<em>TU Model Element</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Model Element</em>'.
   * @generated
   */
  TUModelElement createTUModelElement();

  /**
   * Returns a new object of class '<em>TU Execution</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Execution</em>'.
   * @generated
   */
  TUExecution createTUExecution();

  /**
   * Returns a new object of class '<em>TU Signal</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Signal</em>'.
   * @generated
   */
  TUSignal createTUSignal();

  /**
   * Returns a new object of class '<em>TU Class</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Class</em>'.
   * @generated
   */
  TUClass createTUClass();

  /**
   * Returns a new object of class '<em>TU Association</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Association</em>'.
   * @generated
   */
  TUAssociation createTUAssociation();

  /**
   * Returns a new object of class '<em>TU Signal Attribute</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Signal Attribute</em>'.
   * @generated
   */
  TUSignalAttribute createTUSignalAttribute();

  /**
   * Returns a new object of class '<em>TU Class Member</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Class Member</em>'.
   * @generated
   */
  TUClassMember createTUClassMember();

  /**
   * Returns a new object of class '<em>TU Constructor</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Constructor</em>'.
   * @generated
   */
  TUConstructor createTUConstructor();

  /**
   * Returns a new object of class '<em>TU Attribute Or Operation Declaration Prefix</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Attribute Or Operation Declaration Prefix</em>'.
   * @generated
   */
  TUAttributeOrOperationDeclarationPrefix createTUAttributeOrOperationDeclarationPrefix();

  /**
   * Returns a new object of class '<em>TU State</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU State</em>'.
   * @generated
   */
  TUState createTUState();

  /**
   * Returns a new object of class '<em>TU State Member</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU State Member</em>'.
   * @generated
   */
  TUStateMember createTUStateMember();

  /**
   * Returns a new object of class '<em>TU Entry Or Exit Activity</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Entry Or Exit Activity</em>'.
   * @generated
   */
  TUEntryOrExitActivity createTUEntryOrExitActivity();

  /**
   * Returns a new object of class '<em>TU Transition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Transition</em>'.
   * @generated
   */
  TUTransition createTUTransition();

  /**
   * Returns a new object of class '<em>TU Transition Member</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Transition Member</em>'.
   * @generated
   */
  TUTransitionMember createTUTransitionMember();

  /**
   * Returns a new object of class '<em>TU Transition Trigger</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Transition Trigger</em>'.
   * @generated
   */
  TUTransitionTrigger createTUTransitionTrigger();

  /**
   * Returns a new object of class '<em>TU Transition Vertex</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Transition Vertex</em>'.
   * @generated
   */
  TUTransitionVertex createTUTransitionVertex();

  /**
   * Returns a new object of class '<em>TU Transition Effect</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Transition Effect</em>'.
   * @generated
   */
  TUTransitionEffect createTUTransitionEffect();

  /**
   * Returns a new object of class '<em>TU Transition Guard</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Transition Guard</em>'.
   * @generated
   */
  TUTransitionGuard createTUTransitionGuard();

  /**
   * Returns a new object of class '<em>TU Association End</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Association End</em>'.
   * @generated
   */
  TUAssociationEnd createTUAssociationEnd();

  /**
   * Returns a new object of class '<em>TU Multiplicity</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Multiplicity</em>'.
   * @generated
   */
  TUMultiplicity createTUMultiplicity();

  /**
   * Returns a new object of class '<em>TU Composition</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Composition</em>'.
   * @generated
   */
  TUComposition createTUComposition();

  /**
   * Returns a new object of class '<em>TU Attribute</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Attribute</em>'.
   * @generated
   */
  TUAttribute createTUAttribute();

  /**
   * Returns a new object of class '<em>TU Operation</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>TU Operation</em>'.
   * @generated
   */
  TUOperation createTUOperation();

  /**
   * Returns a new object of class '<em>RAlf Send Signal Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>RAlf Send Signal Expression</em>'.
   * @generated
   */
  RAlfSendSignalExpression createRAlfSendSignalExpression();

  /**
   * Returns a new object of class '<em>RAlf Delete Object Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>RAlf Delete Object Expression</em>'.
   * @generated
   */
  RAlfDeleteObjectExpression createRAlfDeleteObjectExpression();

  /**
   * Returns a new object of class '<em>RAlf Assoc Nav Expression</em>'.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return a new object of class '<em>RAlf Assoc Nav Expression</em>'.
   * @generated
   */
  RAlfAssocNavExpression createRAlfAssocNavExpression();

  /**
   * Returns the package supported by this factory.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the package supported by this factory.
   * @generated
   */
  XtxtUMLPackage getXtxtUMLPackage();

} //XtxtUMLFactory
