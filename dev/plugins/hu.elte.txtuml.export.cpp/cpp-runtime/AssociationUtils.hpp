/** @file AssociationUtils.hpp
*/

#ifndef ASSOCIATION_UTILS_H
#define ASSOCIATION_UTILS_H

#include <list>
#include <map>
#include "ModelObject.hpp"
#include "ESRoot/Elements.hpp"

namespace Model
{


template<class FirstClassRole, class SecondClassRole>
class Association : public FirstClassRole, public SecondClassRole {
public:

	Association() {
		FirstClassRole::association = this;
		SecondClassRole::association = this;
	}

	void link(typename FirstClassRole::RoleType* first, FirstClassRole*, typename SecondClassRole::RoleType* second, SecondClassRole*) {
		LeftRoleTable[first].add(second);
		RigthRoleTable[second].add(first);
	}

	void link(typename SecondClassRole::RoleType* first, SecondClassRole*, typename FirstClassRole::RoleType* second, FirstClassRole*) {
		RigthRoleTable[first].add(second);
		LeftRoleTable[second].add(first);
	}

	void unlink(typename FirstClassRole::RoleType* first, FirstClassRole*, typename SecondClassRole::RoleType* second, SecondClassRole*) {
		LeftRoleTable[first].remove(second);
		RigthRoleTable[second].remove(first);
	}

	void unlink(typename SecondClassRole::RoleType* first, SecondClassRole*, typename FirstClassRole::RoleType* second, FirstClassRole*) {
		RigthRoleTable[first].remove(second);
		LeftRoleTable[second].remove(first);
	}

	const typename SecondClassRole::CollectionType& get(typename FirstClassRole::RoleType* left, SecondClassRole*) {
		return LeftRoleTable[left];
	}

	const typename FirstClassRole::CollectionType& get(typename SecondClassRole::RoleType* rigth, FirstClassRole*) {
		return RigthRoleTable[rigth];
	}

	const typename SecondClassRole::CollectionType& get(ES::ModelObject* left, SecondClassRole* r) {
		return get(static_cast<typename FirstClassRole::RoleType*>(left), r);
	}

	const typename FirstClassRole::CollectionType& get(ES::ModelObject* rigth, FirstClassRole* r) {
		return get(static_cast<typename SecondClassRole::RoleType*>(rigth), r);
	}

private:
	std::map<typename FirstClassRole::RoleType*, typename SecondClassRole::CollectionType> LeftRoleTable;
	std::map<typename SecondClassRole::RoleType*, typename FirstClassRole::CollectionType> RigthRoleTable;

};


template<class FirstClassRole, class SecondClassRole>
class AssocOwner {
public:
	using AssocType = Association<FirstClassRole, SecondClassRole>;

	typename AssocOwner<FirstClassRole, SecondClassRole>::AssocType* association = nullptr;

};

template<typename FirstClassRole, class SecondClassRole, class EndType, int lowMul, int upMul>
struct AssocEnd : public AssocOwner<FirstClassRole, SecondClassRole> {
	typedef Property<EndType, lowMul, upMul> CollectionType;
	typedef EndType RoleType;
};

}
/*
class A {};

// A a1(1)<->a2(0..1) A

struct a1End;
struct a2End;
struct a1End : public AssocEnd<a1End, a2End, A, 1,1
        //ha tobb van,
        //akar a collection tipusat is meg lehet adni,
        //pl: list vagy vector
>
{
    a1End * a1 = this;
};

struct a2End : public AssocEnd<a1End, a2End, A, 0,1> {
    a2End * a2 = this;
};



Association<a1End, a2End> assocName;
A *a1,*a2;
Action::link(assocName.a1, a1, assocName.a2, a2);
Action::unlink(assocName.a1, a1, assocName.a2, a2);
a2End::CollectionType //a tipus a vegponttol fugg
a2endCollection = a1.assoc(assocName.a2);
A* selectA2  = a2endCollection.one(); //count, meg ilyesmik ugyanugy mukodnek
*/

#endif
