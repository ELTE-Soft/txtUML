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
				if(param == null) {
					break;
				}
				params[i] = param;
			}
			if(paramTypes.length == params.length) {
				try {
					ret = (T)ctor.newInstance(params);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			if(ret != null) {
				break;
			}
		}
		return ret;
	}
	
}
