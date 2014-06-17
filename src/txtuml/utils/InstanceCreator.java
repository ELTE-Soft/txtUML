package txtuml.utils;

import java.lang.reflect.*;

public class InstanceCreator {
	public static <T> T createInstance(Class<T> c, int depth) {
		if(depth == 0) {
			return null;
		}
		T ret = null;
		Constructor[] ctors = c.getDeclaredConstructors();
		for(Constructor ctor : ctors) {
			ctor.setAccessible(true);
			Class[] paramTypes = ctor.getParameterTypes();
			int len = paramTypes.length;
			Object[] params = new Object[len];
			for(int i=0; i<len; ++i) {
				Object param = createInstance(paramTypes[i], depth-1);
				if(param == null) {
					break;
				}
				params[i] = param;
			}
			if(paramTypes.length == params.length) {
				try {
					ret = (T)ctor.newInstance(params);
				} catch(Exception e) {
				}
			}
			if(ret != null) {
				break;
			}
		}
		return ret;
	}
	
}
