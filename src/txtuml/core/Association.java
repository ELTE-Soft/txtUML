package txtuml.core;

public class Association {
    public Association(String n, AssociationEnd l, AssociationEnd r) {
        name = n;
        left = l;
        right = r;
    }
    
    public String getName() {
        return name;
    }
    
    public AssociationEnd getLeft() {
        return left;
    }

    public AssociationEnd getRight() {
        return right;
    }
    
    String name;
    AssociationEnd left;
    AssociationEnd right;
}
