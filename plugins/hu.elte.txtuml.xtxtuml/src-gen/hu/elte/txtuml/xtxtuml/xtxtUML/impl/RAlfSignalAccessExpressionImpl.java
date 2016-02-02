/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML.impl;

import hu.elte.txtuml.xtxtuml.xtxtUML.RAlfSignalAccessExpression;
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.xtext.xbase.impl.XExpressionImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>RAlf Signal Access Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.RAlfSignalAccessExpressionImpl#getSigdata <em>Sigdata</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RAlfSignalAccessExpressionImpl extends XExpressionImpl implements RAlfSignalAccessExpression
{
  /**
   * The default value of the '{@link #getSigdata() <em>Sigdata</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSigdata()
   * @generated
   * @ordered
   */
  protected static final String SIGDATA_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSigdata() <em>Sigdata</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSigdata()
   * @generated
   * @ordered
   */
  protected String sigdata = SIGDATA_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected RAlfSignalAccessExpressionImpl()
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
    return XtxtUMLPackage.Literals.RALF_SIGNAL_ACCESS_EXPRESSION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getSigdata()
  {
    return sigdata;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setSigdata(String newSigdata)
  {
    String oldSigdata = sigdata;
    sigdata = newSigdata;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.RALF_SIGNAL_ACCESS_EXPRESSION__SIGDATA, oldSigdata, sigdata));
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
      case XtxtUMLPackage.RALF_SIGNAL_ACCESS_EXPRESSION__SIGDATA:
        return getSigdata();
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
      case XtxtUMLPackage.RALF_SIGNAL_ACCESS_EXPRESSION__SIGDATA:
        setSigdata((String)newValue);
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
      case XtxtUMLPackage.RALF_SIGNAL_ACCESS_EXPRESSION__SIGDATA:
        setSigdata(SIGDATA_EDEFAULT);
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
      case XtxtUMLPackage.RALF_SIGNAL_ACCESS_EXPRESSION__SIGDATA:
        return SIGDATA_EDEFAULT == null ? sigdata != null : !SIGDATA_EDEFAULT.equals(sigdata);
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
    result.append(" (sigdata: ");
    result.append(sigdata);
    result.append(')');
    return result.toString();
  }

} //RAlfSignalAccessExpressionImpl
