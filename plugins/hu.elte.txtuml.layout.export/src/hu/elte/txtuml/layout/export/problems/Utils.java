package hu.elte.txtuml.layout.export.problems;

import hu.elte.txtuml.layout.lang.LinkEnd;
import hu.elte.txtuml.layout.lang.elements.LayoutAbstractLink;
import hu.elte.txtuml.layout.lang.elements.LayoutAbstractNode;
import hu.elte.txtuml.layout.lang.elements.LayoutElement;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.visualizer.annotations.StatementType;

import java.lang.annotation.Annotation;

public class Utils {
    
    public static String classArrayAsString(Class<?>[] clss) {
        String result = "";
        for (int i = 0; i < clss.length - 1; ++i) {
            result += clss[i].getCanonicalName() + ", ";
        }
        
        if (clss.length > 0) {
            result += clss[clss.length - 1].getCanonicalName();
        }
        
        return result;
    }
    
    public static String lineStatementAsString(String name, Class <? extends LayoutAbstractNode>[] nodes) {
        return name + "({" + classArrayAsString(nodes) + "})";
    }
    
    public static String diamondStatementAsString(Class<? extends LayoutNode> top, Class<? extends LayoutNode> right,
        Class<? extends LayoutNode> bottom, Class<? extends LayoutNode> left)
    {
        return "diamond(top = " + top.getCanonicalName() + ", right = " + right.getCanonicalName() +
            ", bottom = " + bottom.getCanonicalName() + ", left = " + left.getCanonicalName();
    }
    
    public static String adjacencyStatementAsString(StatementType type, Class<? extends LayoutNode> val,
        Class<? extends LayoutNode> from)
    {
        return type.toString().toLowerCase() + "(val = " + val.getCanonicalName()
            + ", from = " + from.getCanonicalName() + ")";
    }
    
    public static String cardinalStatementAsString(StatementType type,
        Class<? extends LayoutElement>[] val,
        Class<? extends LayoutElement>[] from,
        LinkEnd end)
    {       
        return type.toString().toLowerCase() + "(val = {" + classArrayAsString(val) + "}, from = {" +
            classArrayAsString(from) + "}" + (end != LinkEnd.Default ? ", " + end.toString().toLowerCase() : "") + ")";
    }

    public static String mostStatementAsString(Class<? extends Annotation> type, Class<? extends LayoutAbstractNode>[] val) {
        return type.getSimpleName().toLowerCase() + "({" + classArrayAsString(val) + "})";
    }
    
    public static String priorityStatementAsString(Class<? extends LayoutAbstractLink>[] val, int prior) {
        return "priority(val = {" + classArrayAsString(val) + "}, prior = " + Integer.toString(prior) + ")";
    }
    
}
