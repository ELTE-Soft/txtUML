package hu.elte.txtuml.layout.export.impl;

import hu.elte.txtuml.layout.export.elementinfo.LinkInfo;
import hu.elte.txtuml.layout.export.interfaces.LinkList;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class LinkListImpl extends ArrayList<LinkInfo> implements LinkList {

    @Override
    public Set<LineAssociation> convert() {
        Set<LineAssociation> set = new HashSet<>();
        this.forEach(link -> {
            set.add(link.convert());
        });

        return set;
    }

}
