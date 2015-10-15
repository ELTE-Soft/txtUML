package hu.elte.txtuml.export.uml2.transform.backend;

/**
 * Runtime exception used by the exporter. Always contains an
 * {@link ExportException}.
 */
@SuppressWarnings("serial")
public class RuntimeExportException extends RuntimeException {

	public RuntimeExportException(ExportException ex) {
		super(ex);
	}

	@Override
	public ExportException getCause() {
		return (ExportException) super.getCause();
	}

}
