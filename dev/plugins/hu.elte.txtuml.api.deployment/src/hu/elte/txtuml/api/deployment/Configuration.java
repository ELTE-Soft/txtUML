package hu.elte.txtuml.api.deployment;

/**
 * Base class for thread management configuration, controlled by various
 * annotations in this package.
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>@Group(contains = {A.class,B.class}, rate = 0.7)</code>
 * <code>@Group(contains = {C.class, rate = 0.3})</code>
 * <code>@Runtime(RuntimeType.THREADED)</code>
 * public class C extends Configuration {}
 * </pre>
 */

public abstract class Configuration {

}
