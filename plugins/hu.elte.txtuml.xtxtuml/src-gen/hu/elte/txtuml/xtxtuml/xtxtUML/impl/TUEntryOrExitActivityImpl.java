/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML.impl;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUEntryOrExitActivity;
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.xtext.xbase.XExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TU Entry Or Exit Activity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUEntryOrExitActivityImpl#isEntry <em>Entry</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUEntryOrExitActivityImpl#isExit <em>Exit</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUEntryOrExitActivityImpl#getBody <em>Body</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TUEntryOrExitActivityImpl extends TUStateMemberImpl implements TUEntryOrExitActivity
{
  /**
   * The default value of the '{@link #isEntry() <em>Entry</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isEntry()
   * @generated
   * @ordered
   */
  protected static final boolean ENTRY_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isEntry() <em>Entry</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isEntry()
   * @generated
   * @ordered
   */
  protected boolean entry = ENTRY_EDEFAULT;

  /**
   * The default value of the '{@link #isExit() <em>Exit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isExit()
   * @generated
   * @ordered
   */
  protected static final boolean EXIT_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isExit() <em>Exit</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isExit()
   * @generated
   * @ordered
   */
  protected boolean exit = EXIT_EDEFAULT;

  /**
   * The cached value of the '{@link #getBody() <em>Body</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBody()
   * @generated
   * @ordered
   */
  protected XExpression body;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TUEntryOrExitActivityImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return XtxtUMLPackage.Literals.TU_ENTRY_OR_EXIT_ACTIVITY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isEntry()
  {
    return entry;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEntry(boolean newEntry)
  {
    boolean oldEntry = entry;
    entry = newEntry;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__ENTRY, oldEntry, entry));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isExit()
  {
    return exit;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setExit(boolean newExit)
  {
    boolean oldExit = exit;
    exit = newExit;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__EXIT, oldExit, exit));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XExpression getBody()
  {
    return body;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBody(XExpression newBody, NotificationChain msgs)
  {
    XExpression oldBody = body;
    body = newBody;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__BODY, oldBody, newBody);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setBody(XExpression newBody)
  {
    if (newBody != body)
    {
      NotificationChain msgs = null;
      if (body != null)
        msgs = ((InternalEObject)body).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__BODY, null, msgs);
      if (newBody != null)
        msgs = ((InternalEObject)newBody).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__BODY, null, msgs);
      msgs = basicSetBody(newBody, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__BODY, newBody, newBody));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__BODY:
        return basicSetBody(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__ENTRY:
        return isEntry();
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__EXIT:
        return isExit();
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__BODY:
        return getBody();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__ENTRY:
        setEntry((Boolean)newValue);
        return;
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__EXIT:
        setExit((Boolean)newValue);
        return;
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__BODY:
        setBody((XExpression)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__ENTRY:
        setEntry(ENTRY_EDEFAULT);
        return;
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__EXIT:
        setExit(EXIT_EDEFAULT);
        return;
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__BODY:
        setBody((XExpression)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__ENTRY:
        return entry != ENTRY_EDEFAULT;
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__EXIT:
        return exit != EXIT_EDEFAULT;
      case XtxtUMLPackage.TU_ENTRY_OR_EXIT_ACTIVITY__BODY:
        return body != null;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (entry: ");
    result.append(entry);
    result.append(", exit: ");
    result.append(exit);
    result.append(')');
    return result.toString();
  }

} //TUEntryOrExitActivityImpl
