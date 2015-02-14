package txtuml.api;

import txtuml.api.Association.*;

//TODO upcoming feature
/*
 * public final class AssocEnd <T extends ModelClass> { AssocEnd(Class<? extends
 * AssociationEnd<T>> m, T object) { this.m = m; this.object = object;
 * this.followingObjects = null; }
 * 
 * AssocEnd(Class<? extends AssociationEnd<T>> m, T object,
 * 
 * @SuppressWarnings("unchecked") T... followingObjects) { this.m = m;
 * this.object = object; this.followingObjects = followingObjects; }
 * 
 * Class<? extends AssociationEnd<T>> getMuliplicityClass() { return m; }
 * 
 * T getObject() { return object; }
 * 
 * T[] getFollowingObjects() { return followingObjects; }
 * 
 * <S extends ModelClass> void link(AssocEnd<S> otherEnd) {
 * linkToObject(getObject(), otherEnd); for (T followingObj :
 * getFollowingObjects()) { linkToObject(followingObj, otherEnd); } }
 * 
 * private <S extends ModelClass> void linkToObject(T obj, AssocEnd<S> otherEnd)
 * { obj.addToAssoc(otherEnd.getMuliplicityClass(), otherEnd.getObject()); for
 * (S otherObj : otherEnd.getFollowingObjects()) {
 * obj.addToAssoc(otherEnd.getMuliplicityClass(), otherObj); } // TODO needs
 * optimization }
 * 
 * private final Class<? extends AssociationEnd<T>> m; private final T object;
 * private final T[] followingObjects; }
 */