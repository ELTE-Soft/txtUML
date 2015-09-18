/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import org.eclipse.xtext.xbase.XExpression;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>TU Entry Or Exit Activity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity#isEntry <em>Entry</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity#isExit <em>Exit</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity#getBody <em>Body</em>}</li>
 * </ul>
 * </p>
 *
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUEntryOrExitActivity()
 * @model
 * @generated
 */
public interface TUEntryOrExitActivity extends TUStateMember
{
  /**
   * Returns the value of the '<em><b>Entry</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Entry</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Entry</em>' attribute.
   * @see #setEntry(boolean)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUEntryOrExitActivity_Entry()
   * @model
   * @generated
   */
  boolean isEntry();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity#isEntry <em>Entry</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Entry</em>' attribute.
   * @see #isEntry()
   * @generated
   */
  void setEntry(boolean value);

  /**
   * Returns the value of the '<em><b>Exit</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Exit</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Exit</em>' attribute.
   * @see #setExit(boolean)
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUEntryOrExitActivity_Exit()
   * @model
   * @generated
   */
  boolean isExit();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity#isExit <em>Exit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Exit</em>' attribute.
   * @see #isExit()
   * @generated
   */
  void setExit(boolean value);

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
   * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUEntryOrExitActivity_Body()
   * @model containment="true"
   * @generated
   */
  XExpression getBody();

  /**
   * Sets the value of the '{@link hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity#getBody <em>Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Body</em>' containment reference.
   * @see #getBody()
   * @generated
   */
  void setBody(XExpression value);

} // TUEntryOrExitActivity
