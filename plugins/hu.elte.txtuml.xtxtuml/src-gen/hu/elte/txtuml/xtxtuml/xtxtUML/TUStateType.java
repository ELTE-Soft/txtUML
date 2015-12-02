/**
 */
package hu.elte.txtuml.xtxtuml.xtxtUML;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>TU State Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage#getTUStateType()
 * @model
 * @generated
 */
public enum TUStateType implements Enumerator
{
  /**
   * The '<em><b>PLAIN</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #PLAIN_VALUE
   * @generated
   * @ordered
   */
  PLAIN(0, "PLAIN", "state"),

  /**
   * The '<em><b>INITIAL</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #INITIAL_VALUE
   * @generated
   * @ordered
   */
  INITIAL(1, "INITIAL", "initial"),

  /**
   * The '<em><b>CHOICE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #CHOICE_VALUE
   * @generated
   * @ordered
   */
  CHOICE(2, "CHOICE", "choice"),

  /**
   * The '<em><b>COMPOSITE</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #COMPOSITE_VALUE
   * @generated
   * @ordered
   */
  COMPOSITE(3, "COMPOSITE", "composite");

  /**
   * The '<em><b>PLAIN</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>PLAIN</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #PLAIN
   * @model literal="state"
   * @generated
   * @ordered
   */
  public static final int PLAIN_VALUE = 0;

  /**
   * The '<em><b>INITIAL</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>INITIAL</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #INITIAL
   * @model literal="initial"
   * @generated
   * @ordered
   */
  public static final int INITIAL_VALUE = 1;

  /**
   * The '<em><b>CHOICE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>CHOICE</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #CHOICE
   * @model literal="choice"
   * @generated
   * @ordered
   */
  public static final int CHOICE_VALUE = 2;

  /**
   * The '<em><b>COMPOSITE</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>COMPOSITE</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #COMPOSITE
   * @model literal="composite"
   * @generated
   * @ordered
   */
  public static final int COMPOSITE_VALUE = 3;

  /**
   * An array of all the '<em><b>TU State Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final TUStateType[] VALUES_ARRAY =
    new TUStateType[]
    {
      PLAIN,
      INITIAL,
      CHOICE,
      COMPOSITE,
    };

  /**
   * A public read-only list of all the '<em><b>TU State Type</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<TUStateType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>TU State Type</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param literal the literal.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static TUStateType get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      TUStateType result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>TU State Type</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param name the name.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static TUStateType getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      TUStateType result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>TU State Type</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the integer value.
   * @return the matching enumerator or <code>null</code>.
   * @generated
   */
  public static TUStateType get(int value)
  {
    switch (value)
    {
      case PLAIN_VALUE: return PLAIN;
      case INITIAL_VALUE: return INITIAL;
      case CHOICE_VALUE: return CHOICE;
      case COMPOSITE_VALUE: return COMPOSITE;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final int value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String name;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String literal;

  /**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private TUStateType(int value, String name, String literal)
  {
    this.value = value;
    this.name = name;
    this.literal = literal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getValue()
  {
    return value;
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
  public String getLiteral()
  {
    return literal;
  }

  /**
   * Returns the literal value of the enumerator, which is its string representation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    return literal;
  }
  
} //TUStateType
