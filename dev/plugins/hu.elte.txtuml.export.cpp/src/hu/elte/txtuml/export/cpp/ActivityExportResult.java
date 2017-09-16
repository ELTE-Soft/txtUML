package hu.elte.txtuml.export.cpp;

public class ActivityExportResult {
	public ActivityExportResult() {
		activitySource = new StringBuilder("");
		containsSignalAccess = false;
	}
	
	public void appendToSource(String source) {
		activitySource.append(source);
	}
	
	public void reduceSource(String regex) {
		activitySource = new StringBuilder(activitySource.toString().replaceAll(regex, ""));
	}
	
	public void setSignalReferenceContainment() {
		containsSignalAccess = true;
	}
	
	public String getActivitySource() {
		return activitySource.toString();
	}
	
	public Boolean sourceHasSignalReference() {
		return containsSignalAccess;
	}
	
	private StringBuilder activitySource;
	private Boolean containsSignalAccess;
}
