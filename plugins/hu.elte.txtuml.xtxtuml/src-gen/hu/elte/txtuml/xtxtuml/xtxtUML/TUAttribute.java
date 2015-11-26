/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TU Attribute</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute#getPrefix <em>Prefix</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUAttribute()
 * @model
 * @generated
 */
public interface TUAttribute extends TUClassMember
{
  /**
   * Returns the value of the '<em><b>Prefix</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Prefix</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Prefix</em>' containment reference.
   * @see #setPrefix(TUAttributeOrOperationDeclarationPrefix)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUAttribute_Prefix()
   * @model containment="true"
   * @generated
   */
  TUAttributeOrOperationDeclarationPrefix getPrefix();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute#getPrefix <em>Prefix</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Prefix</em>' containment reference.
   * @see #getPrefix()
   * @generated
   */
  void setPrefix(TUAttributeOrOperationDeclarationPrefix value);

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
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUAttribute_Name()
   * @model
   * @generated
   */
  String getName();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUAttribute#getName <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Name</em>' attribute.
   * @see #getName()
   * @generated
   */
  void setName(String value);

} // TUAttribute
