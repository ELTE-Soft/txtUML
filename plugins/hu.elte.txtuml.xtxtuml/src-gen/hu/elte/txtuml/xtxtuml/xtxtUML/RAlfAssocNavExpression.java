/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import org.eclipse.xtext.xbase.XExpression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>RAlf Assoc Nav Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression#getLeft <em>Left</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression#getRight <em>Right</em>}</li>
 * </ul>
 *
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getRAlfAssocNavExpression()
 * @model
 * @generated
 */
public interface RAlfAssocNavExpression extends XExpression
{
  /**
   * Returns the value of the '<em><b>Left</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Left</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Left</em>' containment reference.
   * @see #setLeft(XExpression)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getRAlfAssocNavExpression_Left()
   * @model containment="true"
   * @generated
   */
  XExpression getLeft();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression#getLeft <em>Left</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Left</em>' containment reference.
   * @see #getLeft()
   * @generated
   */
  void setLeft(XExpression value);

  /**
   * Returns the value of the '<em><b>Right</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Right</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Right</em>' reference.
   * @see #setRight(TUAssociationEnd)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getRAlfAssocNavExpression_Right()
   * @model
   * @generated
   */
  TUAssociationEnd getRight();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.RAlfAssocNavExpression#getRight <em>Right</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Right</em>' reference.
   * @see #getRight()
   * @generated
   */
  void setRight(TUAssociationEnd value);

} // RAlfAssocNavExpression
