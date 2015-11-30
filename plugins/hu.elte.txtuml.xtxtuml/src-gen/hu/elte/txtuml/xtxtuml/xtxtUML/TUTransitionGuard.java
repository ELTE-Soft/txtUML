/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import org.eclipse.xtext.xbase.XExpression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TU Transition Guard</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard#isElse <em>Else</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard#getExpression <em>Expression</em>}</li>
 * </ul>
 *
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUTransitionGuard()
 * @model
 * @generated
 */
public interface TUTransitionGuard extends TUTransitionMember
{
  /**
   * Returns the value of the '<em><b>Else</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Else</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Else</em>' attribute.
   * @see #setElse(boolean)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUTransitionGuard_Else()
   * @model
   * @generated
   */
  boolean isElse();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard#isElse <em>Else</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Else</em>' attribute.
   * @see #isElse()
   * @generated
   */
  void setElse(boolean value);

  /**
   * Returns the value of the '<em><b>Expression</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Expression</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expression</em>' containment reference.
   * @see #setExpression(XExpression)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUTransitionGuard_Expression()
   * @model containment="true"
   * @generated
   */
  XExpression getExpression();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionGuard#getExpression <em>Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Expression</em>' containment reference.
   * @see #getExpression()
   * @generated
   */
  void setExpression(XExpression value);

} // TUTransitionGuard
