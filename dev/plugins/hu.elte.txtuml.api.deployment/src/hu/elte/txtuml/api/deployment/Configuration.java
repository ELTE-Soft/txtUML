package hu.elte.txtuml.api.deployment;

/**
 * Base class for thread management configuration, controlled by various
 * annotations in this package.
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>@Group(contains = {A.class,B.class}, max = 10, constant = 5, gradient = 0.1)</code>
 * <code>@Group(contains = {C.class})</code>
 * <code>@Multithreading(false)</code>
 * public class C extends Configuration {}
 * </pre>
 */

public abstract class Configuration {

}
