package txtuml.utils;

import java.lang.reflect.*;

public class InstanceCreator {
	// TODO check warning
	@SuppressWarnings("unchecked")
	public static <T> T createInstance(Class<T> c, int depth, Object... givenParameters) {
		if(depth == 0) {
			return null;
		}
		T ret = null;
		Constructor<?>[] ctors = c.getDeclaredConstructors();
		for(Constructor<?> ctor : ctors) {
			ctor.setAccessible(true);
			Class<?>[] paramTypes = ctor.getParameterTypes();
			int len = paramTypes.length;
			Object[] params = new Object[len];
			for(int i=0; i<len; ++i) {
				Object param;
				if (givenParameters.length > i) { // if parameter was given
					param = paramTypes[i].cast(givenParameters[i]); // TODO unchecked cast 
				} else {
					param = createInstance(paramTypes[i], depth-1);
				}
				params[i] = param;
			}
			// TODO: Do not allow the creation of the new instance
			// if parameters were not created successfully.
			// Currently this cannot be fixed:
			//   When importing a 'new' operation on a model class,
			//   its constructor requires the enclosing class (the model),
			//   but 'givenParameters' is not filled in...
			//   As a result, trying to fix this TODO makes the examples fail.
			try {
				ret = (T)ctor.newInstance(params);
			} catch(Exception e) {
				// Using this ctor failed, 'ret' is null,
				// loop again if there are more ctors.
			}
			if(ret != null) {
				break;
			}
		}
		return ret;
	}
	
}
