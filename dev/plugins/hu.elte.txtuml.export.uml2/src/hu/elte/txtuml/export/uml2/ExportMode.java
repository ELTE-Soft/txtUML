package hu.elte.txtuml.export.uml2;

public enum ExportMode {

	ExportActionsErrorHandling(true, true), ExportActionsPedantic(true, false), ErrorHandlingNoActions(false,
			true), NoActionsPedantic(false, false);

	private ExportMode(boolean exportActions, boolean errorHandlingExport) {
				this.exportActions = exportActions;
				this.errorHandlingExport = errorHandlingExport;
			}

	private boolean exportActions;
	private boolean errorHandlingExport;

	public boolean exportActions() {
		return exportActions;
	}

	public void handleErrors(Runnable code) {
		if (errorHandlingExport) {
			try {
				code.run();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			code.run();
		}
	}

	public boolean isErrorHandler() {
		return errorHandlingExport;
	}
}