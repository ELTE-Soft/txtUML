package hu.elte.txtuml.layout.export.interfaces;

import java.util.List;
import java.util.Set;

import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.impl.LinkListImpl;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

public interface LinkList extends List<LinkInfo> {

    static LinkList create() {
        return new LinkListImpl();
    }
    
    Set<LineAssociation> convert();
       
}
