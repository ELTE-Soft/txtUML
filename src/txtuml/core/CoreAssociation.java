package txtuml.core;

public class CoreAssociation extends CoreNamedObject {
    public CoreAssociation(String name, CoreAssociationEnd l, CoreAssociationEnd r) {
        super(name);
        left = l;
        right = r;
    }
        
    public CoreAssociationEnd getLeft() {
        return left;
    }

    public CoreAssociationEnd getRight() {
        return right;
    }
    
    private CoreAssociationEnd left;
    private CoreAssociationEnd right;
}
