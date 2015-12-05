/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TU Transition Vertex</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex#isFrom <em>From</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex#getVertex <em>Vertex</em>}</li>
 * </ul>
 *
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUTransitionVertex()
 * @model
 * @generated
 */
public interface TUTransitionVertex extends TUTransitionMember
{
  /**
   * Returns the value of the '<em><b>From</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>From</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>From</em>' attribute.
   * @see #setFrom(boolean)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUTransitionVertex_From()
   * @model
   * @generated
   */
  boolean isFrom();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex#isFrom <em>From</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>From</em>' attribute.
   * @see #isFrom()
   * @generated
   */
  void setFrom(boolean value);

  /**
   * Returns the value of the '<em><b>Vertex</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Vertex</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Vertex</em>' reference.
   * @see #setVertex(TUState)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUTransitionVertex_Vertex()
   * @model
   * @generated
   */
  TUState getVertex();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex#getVertex <em>Vertex</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Vertex</em>' reference.
   * @see #getVertex()
   * @generated
   */
  void setVertex(TUState value);

} // TUTransitionVertex
