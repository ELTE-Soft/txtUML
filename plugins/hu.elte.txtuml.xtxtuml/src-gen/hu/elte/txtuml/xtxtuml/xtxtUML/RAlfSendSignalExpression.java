/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import org.eclipse.xtext.xbase.XExpression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>RAlf Send Signal Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression#getSignal <em>Signal</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getRAlfSendSignalExpression()
 * @model
 * @generated
 */
public interface RAlfSendSignalExpression extends XExpression
{
  /**
   * Returns the value of the '<em><b>Signal</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Signal</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Signal</em>' containment reference.
   * @see #setSignal(XExpression)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getRAlfSendSignalExpression_Signal()
   * @model containment="true"
   * @generated
   */
  XExpression getSignal();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression#getSignal <em>Signal</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Signal</em>' containment reference.
   * @see #getSignal()
   * @generated
   */
  void setSignal(XExpression value);

  /**
   * Returns the value of the '<em><b>Target</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Target</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Target</em>' containment reference.
   * @see #setTarget(XExpression)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getRAlfSendSignalExpression_Target()
   * @model containment="true"
   * @generated
   */
  XExpression getTarget();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSendSignalExpression#getTarget <em>Target</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Target</em>' containment reference.
   * @see #getTarget()
   * @generated
   */
  void setTarget(XExpression value);

} // RAlfSendSignalExpression
