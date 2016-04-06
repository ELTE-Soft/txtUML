package hu.elte.txtuml.api.model.execution.defaults;

import java.util.List;

import hu.elte.txtuml.api.model.execution.ErrorListener;
import hu.elte.txtuml.api.model.execution.LockedModelExecutorException;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.api.model.execution.TraceListener;
import hu.elte.txtuml.api.model.execution.WarningListener;

public class DefaultModelExecutor implements ModelExecutor {

	public DefaultModelExecutor() {
		// TODO Auto-generated constructor stub
	}

	public DefaultModelExecutor(String name) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status getStatus() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor start() throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor start(Runnable initialization) throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor setInitialization(Runnable initialization) throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor shutdown() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor shutdownNow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor awaitInitialization() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor awaitInitializationNoCatch() throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void awaitTermination() {
		// TODO Auto-generated method stub

	}

	@Override
	public void awaitTerminationNoCatch() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public ModelExecutor launch() throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor launch(Runnable initialization) throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() throws LockedModelExecutorException {
		// TODO Auto-generated method stub

	}

	@Override
	public void run(Runnable initialization) throws LockedModelExecutorException {
		// TODO Auto-generated method stub

	}

	@Override
	public ModelExecutor addTerminationListener(Runnable listener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor removeTerminationListener(Runnable listener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor addTerminationBlocker(Object blocker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor removeTerminationBlocker(Object blocker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor addTraceListener(TraceListener listener) throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor addErrorListener(ErrorListener listener) throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor addWarningListener(WarningListener listener) throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor removeTraceListener(TraceListener listener) throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor removeErrorListener(ErrorListener listener) throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor removeWarningListener(WarningListener listener) throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor setDynamicChecks(boolean newValue) throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor setExecutionTimeMultiplier(double newMultiplier) throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModelExecutor setTraceLogging(boolean newValue) throws LockedModelExecutorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TraceListener> getTraceListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ErrorListener> getErrorListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WarningListener> getWarningListeners() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean dynamicChecks() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getExecutionTimeMultiplier() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean traceLogging() {
		// TODO Auto-generated method stub
		return false;
	}

}
