package hu.elte.txtuml.api.stdlib.math;

import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;

/**
 * Proxy class for the {@linkplain java.lang.Math} class.
 */
@SuppressWarnings("all")
public class Math extends ModelClass {
  /**
   * This class cannot be instantiated from the model.
   */
  @ExternalBody
  private Math() {
  }
  
  @ExternalBody
  public static int round(final double d) {
    long _round = java.lang.Math.round(d);
    return ((int) _round);
  }
  
  @ExternalBody
  public static double sin(final double d) {
    return java.lang.Math.sin(d);
  }
  
  @ExternalBody
  public static double cos(final double d) {
    return java.lang.Math.cos(d);
  }
  
  @ExternalBody
  public static double toRadians(final double d) {
    return java.lang.Math.toRadians(d);
  }
}
