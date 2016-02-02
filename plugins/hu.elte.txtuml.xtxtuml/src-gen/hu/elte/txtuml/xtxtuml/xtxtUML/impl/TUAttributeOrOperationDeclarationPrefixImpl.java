/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML.impl;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUAttributeOrOperationDeclarationPrefix;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility;
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.xtext.common.types.JvmTypeReference;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TU Attribute Or Operation Declaration Prefix</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAttributeOrOperationDeclarationPrefixImpl#getVisibility <em>Visibility</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAttributeOrOperationDeclarationPrefixImpl#getType <em>Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TUAttributeOrOperationDeclarationPrefixImpl extends TUClassMemberImpl implements TUAttributeOrOperationDeclarationPrefix
{
  /**
   * The default value of the '{@link #getVisibility() <em>Visibility</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVisibility()
   * @generated
   * @ordered
   */
  protected static final TUVisibility VISIBILITY_EDEFAULT = TUVisibility.PACKAGE;

  /**
   * The cached value of the '{@link #getVisibility() <em>Visibility</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVisibility()
   * @generated
   * @ordered
   */
  protected TUVisibility visibility = VISIBILITY_EDEFAULT;

  /**
   * The cached value of the '{@link #getType() <em>Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getType()
   * @generated
   * @ordered
   */
  protected JvmTypeReference type;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TUAttributeOrOperationDeclarationPrefixImpl()
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
    return XtxtUMLPackage.Literals.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUVisibility getVisibility()
  {
    return visibility;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVisibility(TUVisibility newVisibility)
  {
    TUVisibility oldVisibility = visibility;
    visibility = newVisibility == null ? VISIBILITY_EDEFAULT : newVisibility;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__VISIBILITY, oldVisibility, visibility));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JvmTypeReference getType()
  {
    return type;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetType(JvmTypeReference newType, NotificationChain msgs)
  {
    JvmTypeReference oldType = type;
    type = newType;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__TYPE, oldType, newType);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setType(JvmTypeReference newType)
  {
    if (newType != type)
    {
      NotificationChain msgs = null;
      if (type != null)
        msgs = ((InternalEObject)type).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__TYPE, null, msgs);
      if (newType != null)
        msgs = ((InternalEObject)newType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__TYPE, null, msgs);
      msgs = basicSetType(newType, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__TYPE, newType, newType));
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
      case XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__TYPE:
        return basicSetType(null, msgs);
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
      case XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__VISIBILITY:
        return getVisibility();
      case XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__TYPE:
        return getType();
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
      case XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__VISIBILITY:
        setVisibility((TUVisibility)newValue);
        return;
      case XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__TYPE:
        setType((JvmTypeReference)newValue);
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
      case XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__VISIBILITY:
        setVisibility(VISIBILITY_EDEFAULT);
        return;
      case XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__TYPE:
        setType((JvmTypeReference)null);
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
      case XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__VISIBILITY:
        return visibility != VISIBILITY_EDEFAULT;
      case XtxtUMLPackage.TU_ATTRIBUTE_OR_OPERATION_DECLARATION_PREFIX__TYPE:
        return type != null;
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
    result.append(" (visibility: ");
    result.append(visibility);
    result.append(')');
    return result.toString();
  }

} //TUAttributeOrOperationDeclarationPrefixImpl
