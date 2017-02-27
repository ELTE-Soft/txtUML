package hu.elte.txtuml.export.javascript.json.model.cd;

/***
 * 
 * Possible types of a node in a class diagram
 *
 */
public enum CDNodeType {
	CLASS("class"),
	ABSTRACT_CLASS("abstract");
	
	private final String literal;
	
	CDNodeType(String literal){
		this.literal = literal;
	}
	
	@Override
	public String toString(){
		return literal;
	}
}
