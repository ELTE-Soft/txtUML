package hu.elte.txtuml.api.stdlib.timers;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.stdlib.timers.SendLater;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * An external class which enhances the txtUML models with the ability of using
 * timed events.
 * <p>
 * By calling the {@link #start(ModelClass, Signal, int)} method, a new delayed
 * send operation can be started, which means that a signal will be
 * asynchronously sent to the a target model object after a specified timeout.
 * <p>
 * See the documentation of {@link Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("all")
public class Timer extends ModelClass {
  @External
  private Callable<Void> action;
  
  @External
  private ScheduledFuture<Void> future;
  
  /**
   * Starts a new delayed send operation. Sends asynchronously a signal to the
   * target model object after a specified timeout.
   * 
   * @param targetObj
   *            the target model object of the delayed send operation
   * @param signal
   *            the signal which is to be sent after the delay
   * @param millisecs
   *            the time in millisecs to wait before sending the signal
   * @return a handle object to manage this delayed send operation before it
   *         happens
   */
  @ExternalBody
  public static Timer start(final Signal sig, final ModelClass targetObj, final int millisecs) {
    return Action.<Timer>create(Timer.class, sig, targetObj, Integer.valueOf(millisecs));
  }
  
  /**
   * This class cannot be directly instantiated from the model.
   */
  @ExternalBody
  private Timer() {
  }
  
  /**
   * @param obj
   *            the target of the delayed send operation
   * @param s
   *            the signal to send after the timeout
   * @param millisecs
   *            millisecs to wait before the timeout
   */
  @External
  private Timer(final Signal sig, final ModelClass targetObj, final int millisecs) {
    SendLater _sendLater = new SendLater(sig, targetObj);
    this.action = _sendLater;
    ScheduledFuture<Void> _schedule = this.schedule(millisecs);
    this.future = _schedule;
  }
  
  /**
   * @return the remaining delay in millisecs; zero or negative values
   *         indicate that the delay has already elapsed
   */
  @ExternalBody
  public int query() {
    long _delay = this.future.getDelay(TimeUnit.MILLISECONDS);
    return ((int) _delay);
  }
  
  /**
   * Reschedules the timed event this handle manages to happen after the
   * specified time from now. If it has already happened, it will be scheduled
   * for a second time.
   * 
   * @param millisecs
   *            new delay in millisecs
   * @throws NullPointerException
   *             if <code>millisecs</code> is <code>null</code>
   */
  @ExternalBody
  public void reset(final int millisecs) {
    ScheduledFuture<Void> newFuture = this.schedule(millisecs);
    this.cancel();
    this.future = newFuture;
  }
  
  /**
   * Reschedules the timed event this handle manages to have a delay increased
   * by the specified amount of time. If it has already happened, it will be
   * scheduled for a second time.
   * 
   * @param millisecs
   *            the amount of time to add in millisecs
   * @throws NullPointerException
   *             if <code>millisecs</code> is <code>null</code>
   */
  @ExternalBody
  public void add(final int millisecs) {
    int _query = this.query();
    int _plus = (_query + millisecs);
    this.reset(_plus);
  }
  
  /**
   * Cancels the timed event managed by this handle object.
   * 
   * @return <code>true</code> if the cancel was successful, so the timed
   *         event managed by this handle was <i>not</i> yet cancelled or
   *         performed; <code>false</code> otherwise
   */
  @ExternalBody
  public boolean cancel() {
    boolean isDone = this.future.isDone();
    this.future.cancel(false);
    return isDone;
  }
  
  @External
  private ScheduledFuture<Void> schedule(final int millisecs) {
    hu.elte.txtuml.api.model.Runtime _currentRuntime = hu.elte.txtuml.api.model.Runtime.currentRuntime();
    return _currentRuntime.<Void>schedule(this.action, millisecs, TimeUnit.MILLISECONDS);
  }
}
