/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TU Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUModel#getElements <em>Elements</em>}</li>
 * </ul>
 *
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUModel()
 * @model
 * @generated
 */
public interface TUModel extends TUFileElement
{
  /**
   * Returns the value of the '<em><b>Elements</b></em>' containment reference list.
   * The list contents are of type {@link hu.elte.txtuml.xtxtuml.xtxtUML.TUModelElement}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Elements</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Elements</em>' containment reference list.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUModel_Elements()
   * @model containment="true"
   * @generated
   */
  EList<TUModelElement> getElements();

} // TUModel
