package hu.elte.txtuml.layout.export.problems;

import java.lang.annotation.Annotation;

import hu.elte.txtuml.api.layout.LinkEnd;
import hu.elte.txtuml.layout.visualizer.statements.StatementType;

public class Utils {
    
    public static String classAsString(Class<?> cls) {
        return cls.getCanonicalName();
    }
    
    public static String classArrayAsString(Class<?>[] clss) {
        String result = "";
        for (int i = 0; i < clss.length - 1; ++i) {
            result += classAsString(clss[i]) + ", ";
        }
        
        if (clss.length > 0) {
            result += classAsString(clss[clss.length - 1]);
        }
        
        return result;
    }
    
    public static String lineStatementAsString(String name, Class <?>[] nodes) {
        return name + "({" + classArrayAsString(nodes) + "})";
    }
    
    public static String diamondStatementAsString(Class<?> top, Class<?> right,
        Class<?> bottom, Class<?> left)
    {
        return "diamond(top = " + classAsString(top) + ", right = " + classAsString(right) +
            ", bottom = " + classAsString(bottom) + ", left = " + classAsString(left);
    }
    
    public static String adjacencyStatementAsString(StatementType type, Class<?> val,
        Class<?> from)
    {
        return type.toString().toLowerCase() + "(val = " + classAsString(val)
            + ", from = " + classAsString(from) + ")";
    }
    
    public static String cardinalStatementAsString(StatementType type,
        Class<?>[] val,
        Class<?>[] from,
        LinkEnd end)
    {       
        return type.toString().toLowerCase() + "(val = {" + classArrayAsString(val) + "}, from = {" +
            classArrayAsString(from) + "}" + (end != LinkEnd.Default ? ", " + end.toString().toLowerCase() : "") + ")";
    }

    public static String mostStatementAsString(Class<? extends Annotation> type, Class<?>[] val) {
        return type.getSimpleName().toLowerCase() + "({" + classArrayAsString(val) + "})";
    }
    
    public static String spacingStatementAsString(Double val) {
        return "spacing(" + val.toString() + ")";
    }
    
    public static String priorityStatementAsString(Class<?>[] val, int prior) {
        return "priority(val = {" + classArrayAsString(val) + "}, prior = " + Integer.toString(prior) + ")";
    }

	public static String showStatementAsString(Class<?>[] value) {
		return "show({" + classArrayAsString(value) + "})";
	}
    
}
