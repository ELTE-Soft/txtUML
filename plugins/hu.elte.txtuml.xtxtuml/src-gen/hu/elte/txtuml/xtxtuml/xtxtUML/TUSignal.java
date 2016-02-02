/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TU Signal</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUSignal#getAttributes <em>Attributes</em>}</li>
 * </ul>
 *
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUSignal()
 * @model
 * @generated
 */
public interface TUSignal extends TUModelElement
{
  /**
   * Returns the value of the '<em><b>Attributes</b></em>' containment reference list.
   * The list contents are of type {@link hu.elte.txtuml.xtxtuml.xtxtUML.TUSignalAttribute}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Attributes</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Attributes</em>' containment reference list.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUSignal_Attributes()
   * @model containment="true"
   * @generated
   */
  EList<TUSignalAttribute> getAttributes();

} // TUSignal
