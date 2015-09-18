/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML.impl;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUVisibility;
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TU Association End</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationEndImpl#getVisibility <em>Visibility</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationEndImpl#isNotNavigable <em>Not Navigable</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationEndImpl#getMultiplicity <em>Multiplicity</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationEndImpl#getEndClass <em>End Class</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationEndImpl#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TUAssociationEndImpl extends MinimalEObjectImpl.Container implements TUAssociationEnd
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
   * The default value of the '{@link #isNotNavigable() <em>Not Navigable</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isNotNavigable()
   * @generated
   * @ordered
   */
  protected static final boolean NOT_NAVIGABLE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isNotNavigable() <em>Not Navigable</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isNotNavigable()
   * @generated
   * @ordered
   */
  protected boolean notNavigable = NOT_NAVIGABLE_EDEFAULT;

  /**
   * The cached value of the '{@link #getMultiplicity() <em>Multiplicity</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMultiplicity()
   * @generated
   * @ordered
   */
  protected TUMultiplicity multiplicity;

  /**
   * The cached value of the '{@link #getEndClass() <em>End Class</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEndClass()
   * @generated
   * @ordered
   */
  protected TUClass endClass;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TUAssociationEndImpl()
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
    return XtxtUMLPackage.Literals.TU_ASSOCIATION_END;
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
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_ASSOCIATION_END__VISIBILITY, oldVisibility, visibility));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isNotNavigable()
  {
    return notNavigable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNotNavigable(boolean newNotNavigable)
  {
    boolean oldNotNavigable = notNavigable;
    notNavigable = newNotNavigable;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_ASSOCIATION_END__NOT_NAVIGABLE, oldNotNavigable, notNavigable));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUMultiplicity getMultiplicity()
  {
    return multiplicity;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetMultiplicity(TUMultiplicity newMultiplicity, NotificationChain msgs)
  {
    TUMultiplicity oldMultiplicity = multiplicity;
    multiplicity = newMultiplicity;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_ASSOCIATION_END__MULTIPLICITY, oldMultiplicity, newMultiplicity);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setMultiplicity(TUMultiplicity newMultiplicity)
  {
    if (newMultiplicity != multiplicity)
    {
      NotificationChain msgs = null;
      if (multiplicity != null)
        msgs = ((InternalEObject)multiplicity).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - XtxtUMLPackage.TU_ASSOCIATION_END__MULTIPLICITY, null, msgs);
      if (newMultiplicity != null)
        msgs = ((InternalEObject)newMultiplicity).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - XtxtUMLPackage.TU_ASSOCIATION_END__MULTIPLICITY, null, msgs);
      msgs = basicSetMultiplicity(newMultiplicity, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_ASSOCIATION_END__MULTIPLICITY, newMultiplicity, newMultiplicity));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUClass getEndClass()
  {
    if (endClass != null && endClass.eIsProxy())
    {
      InternalEObject oldEndClass = (InternalEObject)endClass;
      endClass = (TUClass)eResolveProxy(oldEndClass);
      if (endClass != oldEndClass)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, XtxtUMLPackage.TU_ASSOCIATION_END__END_CLASS, oldEndClass, endClass));
      }
    }
    return endClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUClass basicGetEndClass()
  {
    return endClass;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEndClass(TUClass newEndClass)
  {
    TUClass oldEndClass = endClass;
    endClass = newEndClass;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_ASSOCIATION_END__END_CLASS, oldEndClass, endClass));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_ASSOCIATION_END__NAME, oldName, name));
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
      case XtxtUMLPackage.TU_ASSOCIATION_END__MULTIPLICITY:
        return basicSetMultiplicity(null, msgs);
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
      case XtxtUMLPackage.TU_ASSOCIATION_END__VISIBILITY:
        return getVisibility();
      case XtxtUMLPackage.TU_ASSOCIATION_END__NOT_NAVIGABLE:
        return isNotNavigable();
      case XtxtUMLPackage.TU_ASSOCIATION_END__MULTIPLICITY:
        return getMultiplicity();
      case XtxtUMLPackage.TU_ASSOCIATION_END__END_CLASS:
        if (resolve) return getEndClass();
        return basicGetEndClass();
      case XtxtUMLPackage.TU_ASSOCIATION_END__NAME:
        return getName();
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
      case XtxtUMLPackage.TU_ASSOCIATION_END__VISIBILITY:
        setVisibility((TUVisibility)newValue);
        return;
      case XtxtUMLPackage.TU_ASSOCIATION_END__NOT_NAVIGABLE:
        setNotNavigable((Boolean)newValue);
        return;
      case XtxtUMLPackage.TU_ASSOCIATION_END__MULTIPLICITY:
        setMultiplicity((TUMultiplicity)newValue);
        return;
      case XtxtUMLPackage.TU_ASSOCIATION_END__END_CLASS:
        setEndClass((TUClass)newValue);
        return;
      case XtxtUMLPackage.TU_ASSOCIATION_END__NAME:
        setName((String)newValue);
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
      case XtxtUMLPackage.TU_ASSOCIATION_END__VISIBILITY:
        setVisibility(VISIBILITY_EDEFAULT);
        return;
      case XtxtUMLPackage.TU_ASSOCIATION_END__NOT_NAVIGABLE:
        setNotNavigable(NOT_NAVIGABLE_EDEFAULT);
        return;
      case XtxtUMLPackage.TU_ASSOCIATION_END__MULTIPLICITY:
        setMultiplicity((TUMultiplicity)null);
        return;
      case XtxtUMLPackage.TU_ASSOCIATION_END__END_CLASS:
        setEndClass((TUClass)null);
        return;
      case XtxtUMLPackage.TU_ASSOCIATION_END__NAME:
        setName(NAME_EDEFAULT);
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
      case XtxtUMLPackage.TU_ASSOCIATION_END__VISIBILITY:
        return visibility != VISIBILITY_EDEFAULT;
      case XtxtUMLPackage.TU_ASSOCIATION_END__NOT_NAVIGABLE:
        return notNavigable != NOT_NAVIGABLE_EDEFAULT;
      case XtxtUMLPackage.TU_ASSOCIATION_END__MULTIPLICITY:
        return multiplicity != null;
      case XtxtUMLPackage.TU_ASSOCIATION_END__END_CLASS:
        return endClass != null;
      case XtxtUMLPackage.TU_ASSOCIATION_END__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
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
    result.append(", notNavigable: ");
    result.append(notNavigable);
    result.append(", name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //TUAssociationEndImpl
