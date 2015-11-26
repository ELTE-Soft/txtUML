/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML.impl;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUState;
import hu.elte.txtuml.xtxtuml.xtxtUML.TUTransitionVertex;
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>TU Transition Vertex</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionVertexImpl#isFrom <em>From</em>}</li>
 *   <li>{@link hu.elte.txtuml.xtxtuml.xtxtUML.impl.TUTransitionVertexImpl#getVertex <em>Vertex</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TUTransitionVertexImpl extends TUTransitionMemberImpl implements TUTransitionVertex
{
  /**
   * The default value of the '{@link #isFrom() <em>From</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isFrom()
   * @generated
   * @ordered
   */
  protected static final boolean FROM_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isFrom() <em>From</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isFrom()
   * @generated
   * @ordered
   */
  protected boolean from = FROM_EDEFAULT;

  /**
   * The cached value of the '{@link #getVertex() <em>Vertex</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getVertex()
   * @generated
   * @ordered
   */
  protected TUState vertex;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected TUTransitionVertexImpl()
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
    return XtxtUMLPackage.Literals.TU_TRANSITION_VERTEX;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isFrom()
  {
    return from;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFrom(boolean newFrom)
  {
    boolean oldFrom = from;
    from = newFrom;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_TRANSITION_VERTEX__FROM, oldFrom, from));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUState getVertex()
  {
    if (vertex != null && vertex.eIsProxy())
    {
      InternalEObject oldVertex = (InternalEObject)vertex;
      vertex = (TUState)eResolveProxy(oldVertex);
      if (vertex != oldVertex)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, XtxtUMLPackage.TU_TRANSITION_VERTEX__VERTEX, oldVertex, vertex));
      }
    }
    return vertex;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TUState basicGetVertex()
  {
    return vertex;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setVertex(TUState newVertex)
  {
    TUState oldVertex = vertex;
    vertex = newVertex;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, XtxtUMLPackage.TU_TRANSITION_VERTEX__VERTEX, oldVertex, vertex));
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
      case XtxtUMLPackage.TU_TRANSITION_VERTEX__FROM:
        return isFrom();
      case XtxtUMLPackage.TU_TRANSITION_VERTEX__VERTEX:
        if (resolve) return getVertex();
        return basicGetVertex();
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
      case XtxtUMLPackage.TU_TRANSITION_VERTEX__FROM:
        setFrom((Boolean)newValue);
        return;
      case XtxtUMLPackage.TU_TRANSITION_VERTEX__VERTEX:
        setVertex((TUState)newValue);
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
      case XtxtUMLPackage.TU_TRANSITION_VERTEX__FROM:
        setFrom(FROM_EDEFAULT);
        return;
      case XtxtUMLPackage.TU_TRANSITION_VERTEX__VERTEX:
        setVertex((TUState)null);
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
      case XtxtUMLPackage.TU_TRANSITION_VERTEX__FROM:
        return from != FROM_EDEFAULT;
      case XtxtUMLPackage.TU_TRANSITION_VERTEX__VERTEX:
        return vertex != null;
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
    result.append(" (from: ");
    result.append(from);
    result.append(')');
    return result.toString();
  }

} //TUTransitionVertexImpl
