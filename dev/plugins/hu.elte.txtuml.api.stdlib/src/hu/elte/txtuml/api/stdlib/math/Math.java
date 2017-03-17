package hu.elte.txtuml.api.stdlib.math;

import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;

/**
 * Proxy class for the {@linkplain java.lang.Math} class.
 */
public class Math extends ModelClass {

	@ExternalBody
	public static long round(double d) {
		return java.lang.Math.round(d);
	}
	
	@ExternalBody
	public static double sin(double d) {
		return java.lang.Math.sin(d);
	}
	
	@ExternalBody
	public static double cos(double d) {
		return java.lang.Math.cos(d);
	}
	
	@ExternalBody
	public static double toRadians(double d) {
		return java.lang.Math.toRadians(d);
	}
	
}
