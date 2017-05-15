package hu.elte.txtuml.xd.tests.parser
import java.util.List

class XDiagramDefinitionParsingTestUtils {
	
	public def Object findElement(Object context, String path) {
		context.findElement(path, Object);
	}
	
	public def <T> T findElement(Object context, String path, Class<T> clazz) {
		var ctx = context;
		for (String segment : path.split("\\.")){
			ctx = ctx.resolvePathSegment(segment);
		}
		if (clazz.isInstance(ctx)){
			return clazz.cast(ctx);
		}
		if (ctx != null){
			println("object " + ctx + " is not an instance of " + clazz);		
		}
		return null;
	}
	
	private def Object resolvePathSegment(Object context, String segment){
		if (context == null) {
			return null;
		}
		if (segment == null || segment.isEmpty()) {
			return null;
		}
		
		try {
			if (segment.matches("^[0-9]+$")){ // list index
				return (context as List<?>).get(Integer.valueOf(segment));
			} else { // property
				return context.class.getMethod("get" + segment.substring(0, 1).toUpperCase() + segment.substring(1)).invoke(context);
			}
		} catch (Exception ignored){
			println("could not resolve " + context + "." + segment)
			return null;
		}
	}
}