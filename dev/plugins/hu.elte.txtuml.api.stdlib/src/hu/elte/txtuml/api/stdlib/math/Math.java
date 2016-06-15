package hu.elte.txtuml.api.stdlib.math;

import hu.elte.txtuml.api.model.external.ExternalClass;

/**
 * Proxy class for the {@linkplain java.lang.Math} class.
 */
public interface Math extends ExternalClass {

	public static long round(double d) {
		return java.lang.Math.round(d);
	}
	
	public static double sin(double d) {
		return java.lang.Math.sin(d);
	}
	
	public static double cos(double d) {
		return java.lang.Math.cos(d);
	}
	
	public static double toRadians(double d) {
		return java.lang.Math.toRadians(d);
	}
	
}
