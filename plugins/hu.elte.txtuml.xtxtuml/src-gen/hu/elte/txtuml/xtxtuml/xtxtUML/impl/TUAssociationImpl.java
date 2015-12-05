/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML.impl;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd;
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TU Association</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUAssociationImpl#getEnds <em>Ends</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TUAssociationImpl extends TUModelElementImpl implements TUAssociation
{
  /**
   * The cached value of the '{@link #getEnds() <em>Ends</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnds()
   * @generated
   * @ordered
   */
  protected EList<TUAssociationEnd> ends;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TUAssociationImpl()
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
    return XtxtUMLPackage.Literals.TU_ASSOCIATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<TUAssociationEnd> getEnds()
  {
    if (ends == null)
    {
      ends = new EObjectContainmentEList<TUAssociationEnd>(TUAssociationEnd.class, this, XtxtUMLPackage.TU_ASSOCIATION__ENDS);
    }
    return ends;
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
      case XtxtUMLPackage.TU_ASSOCIATION__ENDS:
        return ((InternalEList<?>)getEnds()).basicRemove(otherEnd, msgs);
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
      case XtxtUMLPackage.TU_ASSOCIATION__ENDS:
        return getEnds();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case XtxtUMLPackage.TU_ASSOCIATION__ENDS:
        getEnds().clear();
        getEnds().addAll((Collection<? extends TUAssociationEnd>)newValue);
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
      case XtxtUMLPackage.TU_ASSOCIATION__ENDS:
        getEnds().clear();
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
      case XtxtUMLPackage.TU_ASSOCIATION__ENDS:
        return ends != null && !ends.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //TUAssociationImpl
