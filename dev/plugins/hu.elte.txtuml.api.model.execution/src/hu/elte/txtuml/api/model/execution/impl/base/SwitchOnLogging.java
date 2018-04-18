package hu.elte.txtuml.api.model.execution.impl.base;

import hu.elte.txtuml.api.model.execution.LogLevel;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.api.model.execution.diagnostics.DiagnosticsService;
import hu.elte.txtuml.api.model.execution.log.ErrorLogger;
import hu.elte.txtuml.api.model.execution.log.TraceLogger;
import hu.elte.txtuml.api.model.execution.log.WarningLogger;

public enum SwitchOnLogging {
	NONE {
		@Override
		public void switchOnFor(ModelExecutor executor) {
			// nothing to do
		}
	},
	DEFAULT_LOGGING {
		@Override
		public void switchOnFor(ModelExecutor executor) {
			LogLevel logLevel = executor.getLogLevel();
			if (logLevel.isAtLeast(LogLevel.WARNING)) {
				executor.addWarningListener(new WarningLogger(executor.getName()));
				if (logLevel.isAtLeast(LogLevel.TRACE)) {
					executor.addTraceListener(new TraceLogger(executor.getName()));
				}
			}
			executor.addErrorListener(new ErrorLogger(executor.getName()));
		}
	},
	DIAGNOSTICS_SERVICE {
		@Override
		public void switchOnFor(ModelExecutor executor) {
			DiagnosticsService service = new DiagnosticsService();
			executor.addTraceListener(service);
			executor.addTerminationListener(service::shutdown);
		}
	},
	DEFAULT_LOGGING_AND_DIAGNOSTICS_SERVICE {
		@Override
		public void switchOnFor(ModelExecutor executor) {
			DEFAULT_LOGGING.switchOnFor(executor);
			DIAGNOSTICS_SERVICE.switchOnFor(executor);
		}
	};

	abstract void switchOnFor(ModelExecutor executor);
}
