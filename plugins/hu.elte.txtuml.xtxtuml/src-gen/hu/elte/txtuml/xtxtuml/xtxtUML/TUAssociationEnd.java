/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TU Association End</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getVisibility <em>Visibility</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#isNotNavigable <em>Not Navigable</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getMultiplicity <em>Multiplicity</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getEndClass <em>End Class</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUAssociationEnd()
 * @model
 * @generated
 */
public interface TUAssociationEnd extends EObject
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
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUAssociationEnd_Visibility()
   * @model
   * @generated
   */
  TUVisibility getVisibility();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getVisibility <em>Visibility</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Visibility</em>' attribute.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility
   * @see #getVisibility()
   * @generated
   */
  void setVisibility(TUVisibility value);

  /**
   * Returns the value of the '<em><b>Not Navigable</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Not Navigable</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Not Navigable</em>' attribute.
   * @see #setNotNavigable(boolean)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUAssociationEnd_NotNavigable()
   * @model
   * @generated
   */
  boolean isNotNavigable();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#isNotNavigable <em>Not Navigable</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Not Navigable</em>' attribute.
   * @see #isNotNavigable()
   * @generated
   */
  void setNotNavigable(boolean value);

  /**
   * Returns the value of the '<em><b>Multiplicity</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Multiplicity</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Multiplicity</em>' containment reference.
   * @see #setMultiplicity(TUMultiplicity)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUAssociationEnd_Multiplicity()
   * @model containment="true"
   * @generated
   */
  TUMultiplicity getMultiplicity();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getMultiplicity <em>Multiplicity</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Multiplicity</em>' containment reference.
   * @see #getMultiplicity()
   * @generated
   */
  void setMultiplicity(TUMultiplicity value);

  /**
   * Returns the value of the '<em><b>End Class</b></em>' reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>End Class</em>' reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>End Class</em>' reference.
   * @see #setEndClass(TUClass)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUAssociationEnd_EndClass()
   * @model
   * @generated
   */
  TUClass getEndClass();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getEndClass <em>End Class</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>End Class</em>' reference.
   * @see #getEndClass()
   * @generated
   */
  void setEndClass(TUClass value);

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
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUAssociationEnd_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

} // TUAssociationEnd
