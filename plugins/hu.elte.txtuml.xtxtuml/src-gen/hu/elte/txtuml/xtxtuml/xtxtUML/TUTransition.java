/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TU Transition</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition#getName <em>Name</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition#getMembers <em>Members</em>}</li>
 * </ul>
 *
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUTransition()
 * @model
 * @generated
 */
public interface TUTransition extends TUClassMember, TUStateMember
{
  /**
   * Returns the value of the '<em><b>Name</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Name</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Name</em>' attribute.
   * @see #setName(String)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUTransition_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransition#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Members</b></em>' containment reference list.
   * The list contents are of type {@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionMember}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Members</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Members</em>' containment reference list.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUTransition_Members()
   * @model containment="true"
   * @generated
   */
  EList<TUTransitionMember> getMembers();

} // TUTransition
