/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML.impl;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity;
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TU Multiplicity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUMultiplicityImpl#getLower <em>Lower</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUMultiplicityImpl#isUpperSet <em>Upper Set</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUMultiplicityImpl#getUpper <em>Upper</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUMultiplicityImpl#isUpperInf <em>Upper Inf</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUMultiplicityImpl#isAny <em>Any</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TUMultiplicityImpl extends MinimalEObjectImpl.Container implements TUMultiplicity
{
  /**
   * The default value of the '{@link #getLower() <em>Lower</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLower()
   * @generated
   * @ordered
   */
  protected static final int LOWER_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getLower() <em>Lower</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLower()
   * @generated
   * @ordered
   */
  protected int lower = LOWER_EDEFAULT;

  /**
   * The default value of the '{@link #isUpperSet() <em>Upper Set</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUpperSet()
   * @generated
   * @ordered
   */
  protected static final boolean UPPER_SET_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isUpperSet() <em>Upper Set</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUpperSet()
   * @generated
   * @ordered
   */
  protected boolean upperSet = UPPER_SET_EDEFAULT;

  /**
   * The default value of the '{@link #getUpper() <em>Upper</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUpper()
   * @generated
   * @ordered
   */
  protected static final int UPPER_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getUpper() <em>Upper</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getUpper()
   * @generated
   * @ordered
   */
  protected int upper = UPPER_EDEFAULT;

  /**
   * The default value of the '{@link #isUpperInf() <em>Upper Inf</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUpperInf()
   * @generated
   * @ordered
   */
  protected static final boolean UPPER_INF_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isUpperInf() <em>Upper Inf</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUpperInf()
   * @generated
   * @ordered
   */
  protected boolean upperInf = UPPER_INF_EDEFAULT;

  /**
   * The default value of the '{@link #isAny() <em>Any</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isAny()
   * @generated
   * @ordered
   */
  protected static final boolean ANY_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isAny() <em>Any</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isAny()
   * @generated
   * @ordered
   */
  protected boolean any = ANY_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TUMultiplicityImpl()
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
    return XtxtUMLPackage.Literals.TU_MULTIPLICITY;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getLower()
  {
    return lower;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLower(int newLower)
  {
    int oldLower = lower;
    lower = newLower;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_MULTIPLICITY__LOWER, oldLower, lower));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isUpperSet()
  {
    return upperSet;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUpperSet(boolean newUpperSet)
  {
    boolean oldUpperSet = upperSet;
    upperSet = newUpperSet;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_MULTIPLICITY__UPPER_SET, oldUpperSet, upperSet));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getUpper()
  {
    return upper;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUpper(int newUpper)
  {
    int oldUpper = upper;
    upper = newUpper;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_MULTIPLICITY__UPPER, oldUpper, upper));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isUpperInf()
  {
    return upperInf;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUpperInf(boolean newUpperInf)
  {
    boolean oldUpperInf = upperInf;
    upperInf = newUpperInf;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_MULTIPLICITY__UPPER_INF, oldUpperInf, upperInf));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isAny()
  {
    return any;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setAny(boolean newAny)
  {
    boolean oldAny = any;
    any = newAny;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_MULTIPLICITY__ANY, oldAny, any));
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
      case XtxtUMLPackage.TU_MULTIPLICITY__LOWER:
        return getLower();
      case XtxtUMLPackage.TU_MULTIPLICITY__UPPER_SET:
        return isUpperSet();
      case XtxtUMLPackage.TU_MULTIPLICITY__UPPER:
        return getUpper();
      case XtxtUMLPackage.TU_MULTIPLICITY__UPPER_INF:
        return isUpperInf();
      case XtxtUMLPackage.TU_MULTIPLICITY__ANY:
        return isAny();
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
      case XtxtUMLPackage.TU_MULTIPLICITY__LOWER:
        setLower((Integer)newValue);
        return;
      case XtxtUMLPackage.TU_MULTIPLICITY__UPPER_SET:
        setUpperSet((Boolean)newValue);
        return;
      case XtxtUMLPackage.TU_MULTIPLICITY__UPPER:
        setUpper((Integer)newValue);
        return;
      case XtxtUMLPackage.TU_MULTIPLICITY__UPPER_INF:
        setUpperInf((Boolean)newValue);
        return;
      case XtxtUMLPackage.TU_MULTIPLICITY__ANY:
        setAny((Boolean)newValue);
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
      case XtxtUMLPackage.TU_MULTIPLICITY__LOWER:
        setLower(LOWER_EDEFAULT);
        return;
      case XtxtUMLPackage.TU_MULTIPLICITY__UPPER_SET:
        setUpperSet(UPPER_SET_EDEFAULT);
        return;
      case XtxtUMLPackage.TU_MULTIPLICITY__UPPER:
        setUpper(UPPER_EDEFAULT);
        return;
      case XtxtUMLPackage.TU_MULTIPLICITY__UPPER_INF:
        setUpperInf(UPPER_INF_EDEFAULT);
        return;
      case XtxtUMLPackage.TU_MULTIPLICITY__ANY:
        setAny(ANY_EDEFAULT);
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
      case XtxtUMLPackage.TU_MULTIPLICITY__LOWER:
        return lower != LOWER_EDEFAULT;
      case XtxtUMLPackage.TU_MULTIPLICITY__UPPER_SET:
        return upperSet != UPPER_SET_EDEFAULT;
      case XtxtUMLPackage.TU_MULTIPLICITY__UPPER:
        return upper != UPPER_EDEFAULT;
      case XtxtUMLPackage.TU_MULTIPLICITY__UPPER_INF:
        return upperInf != UPPER_INF_EDEFAULT;
      case XtxtUMLPackage.TU_MULTIPLICITY__ANY:
        return any != ANY_EDEFAULT;
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
    result.append(" (lower: ");
    result.append(lower);
    result.append(", upperSet: ");
    result.append(upperSet);
    result.append(", upper: ");
    result.append(upper);
    result.append(", upperInf: ");
    result.append(upperInf);
    result.append(", any: ");
    result.append(any);
    result.append(')');
    return result.toString();
  }

} //TUMultiplicityImpl
