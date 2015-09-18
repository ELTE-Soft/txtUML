/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TU Class</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUClass#getSuperClass <em>Super Class</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUClass#getMembers <em>Members</em>}</li>
 * </ul>
 * </p>
 *
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUClass()
 * @model
 * @generated
 */
public interface TUClass extends TUModelElement
{
  /**
   * Returns the value of the '<em><b>Super Class</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Super Class</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Super Class</em>' reference.
   * @see #setSuperClass(TUClass)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUClass_SuperClass()
   * @model
   * @generated
   */
  TUClass getSuperClass();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUClass#getSuperClass <em>Super Class</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Super Class</em>' reference.
   * @see #getSuperClass()
   * @generated
   */
  void setSuperClass(TUClass value);

  /**
   * Returns the value of the '<em><b>Members</b></em>' containment reference list.
   * The list contents are of type {@link hu.elte.txtuml.xtxtuml.xtxtUML.TUClassMember}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Members</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Members</em>' containment reference list.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUClass_Members()
   * @model containment="true"
   * @generated
   */
  EList<TUClassMember> getMembers();

} // TUClass
