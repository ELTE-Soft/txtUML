package hu.elte.txtuml.export.cpp;

public interface ICppCompilationUnit {

	
	enum UnitType {
		Cpp,
		Header
	}
	
	String getUnitName();
	//String getUnitNamespace();
	//String createUnitCppCode();
	//String createUnitHeaderCode();
	//String getUnitDependencies(UnitType type);
	void addDependency(String type);
	void addCppOnlyDependency(String type);
	
	/*default String createUniSource() {
		String headerSource = createUnitHeaderCode();
		
	}
	 */

}
