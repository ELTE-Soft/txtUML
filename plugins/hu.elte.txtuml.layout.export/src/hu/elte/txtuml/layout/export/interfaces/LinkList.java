package hu.elte.txtuml.layout.export.interfaces;

import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.impl.LinkListImpl;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import java.util.List;
import java.util.Set;

public interface LinkList extends List<LinkInfo> {

    static LinkList create() {
        return new LinkListImpl();
    }
    
    Set<LineAssociation> convert();
       
}
