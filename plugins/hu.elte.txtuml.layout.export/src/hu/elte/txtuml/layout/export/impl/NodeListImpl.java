package hu.elte.txtuml.layout.export.impl;

import hu.elte.txtuml.layout.export.elementinfo.NodeInfo;
import hu.elte.txtuml.layout.export.interfaces.NodeList;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class NodeListImpl extends ArrayList<NodeInfo> implements NodeList {

    @Override
    public Set<RectangleObject> convert() {
        Set<RectangleObject> set = new HashSet<>();
        this.forEach(node -> {
            set.add(node.convert());
        });

        return set;
    }

}
