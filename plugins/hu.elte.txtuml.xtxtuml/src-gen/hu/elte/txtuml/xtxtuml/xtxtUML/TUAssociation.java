/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TU Association</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation#getEnds <em>Ends</em>}</li>
 * </ul>
 * </p>
 *
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUAssociation()
 * @model
 * @generated
 */
public interface TUAssociation extends TUModelElement
{
  /**
   * Returns the value of the '<em><b>Ends</b></em>' containment reference list.
   * The list contents are of type {@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Ends</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Ends</em>' containment reference list.
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUAssociation_Ends()
   * @model containment="true"
   * @generated
   */
  EList<TUAssociationEnd> getEnds();

} // TUAssociation
