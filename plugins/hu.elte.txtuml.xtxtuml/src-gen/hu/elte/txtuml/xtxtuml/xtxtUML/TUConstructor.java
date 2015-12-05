/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import org.eclipse.emf.common.util.EList;

import org.eclipse.xtext.common.types.JvmFormalParameter;

import org.eclipse.xtext.xbase.XExpression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TU Constructor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getVisibility <em>Visibility</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getName <em>Name</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getParameters <em>Parameters</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getBody <em>Body</em>}</li>
 * </ul>
 *
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUConstructor()
 * @model
 * @generated
 */
public interface TUConstructor extends TUClassMember
{
  /**
   * Returns the value of the '<em><b>Visibility</b></em>' attribute.
   * The literals are from the enumeration {@link hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Visibility</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Visibility</em>' attribute.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility
   * @see #setVisibility(TUVisibility)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUConstructor_Visibility()
   * @model
   * @generated
   */
  TUVisibility getVisibility();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getVisibility <em>Visibility</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Visibility</em>' attribute.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility
   * @see #getVisibility()
   * @generated
   */
  void setVisibility(TUVisibility value);

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
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUConstructor_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

  /**
   * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
   * The list contents are of type {@link org.eclipse.xtext.common.types.JvmFormalParameter}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Parameters</em>' containment reference list.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUConstructor_Parameters()
   * @model containment="true"
   * @generated
   */
  EList<JvmFormalParameter> getParameters();

  /**
   * Returns the value of the '<em><b>Body</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Body</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Body</em>' containment reference.
   * @see #setBody(XExpression)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUConstructor_Body()
   * @model containment="true"
   * @generated
   */
  XExpression getBody();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUConstructor#getBody <em>Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Body</em>' containment reference.
   * @see #getBody()
   * @generated
   */
  void setBody(XExpression value);

} // TUConstructor
