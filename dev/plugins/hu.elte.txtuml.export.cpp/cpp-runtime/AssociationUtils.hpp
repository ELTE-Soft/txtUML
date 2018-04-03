/** @file AssociationUtils.hpp
*/

#ifndef ASSOCIATION_UTILS_H
#define ASSOCIATION_UTILS_H


template<class FirstClassRole, class SecondClassRole>
class Association : public FirstClassRole , public SecondClassRole {
public:

	Association()  {
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

	const typename SecondClassRole::CollectionType& get(typename FirstClassRole::RoleType* left, SecondClassRole*)  {
		return LeftRoleTable[left];
	}

	const typename FirstClassRole::CollectionType& get(typename SecondClassRole::RoleType* rigth, FirstClassRole*) {
		return RigthRoleTable[rigth];
	}

private:
	std::map<typename FirstClassRole::RoleType*, typename SecondClassRole::CollectionType> LeftRoleTable;
	std::map<typename SecondClassRole::RoleType*, typename FirstClassRole::CollectionType> RigthRoleTable;

};

template<typename T, int low, int up, typename Container = std::list<T*>>
class Property {
public:
	void add(T* o) {
		objects.push_back(object);
	}

	T* selectAny() {
		return objects.back();
	}

	typename Container::size_type count() {
		return objects.size();
	}



private:
	Container objects;
};


template<typename T, int low>
class Property<T, low, 1> {
public:
	void add(T* o) {
		object = o;
	}

	T* selectAny() {
		return object;
	}

	int count() {
		return object == nullptr ? 0 : 1;
	}

private:
	T * object = nullptr;
};


template<class FirstClassRole, class SecondClassRole>
class AssocOwner {
public:
	using AssocType = Association<FirstClassRole, SecondClassRole>;

	typename AssocOwner<FirstClassRole, SecondClassRole>::AssocType* association = nullptr;

};

template<typename FirstClassRole, class SecondClassRole, class EndType, int lowMul, int upMul>
struct AssocEnd : public AssocOwner<FirstClassRole, SecondClassRole> {
	typedef Property<typename EndType, lowMul, upMul> CollectionType;
	typedef EndType RoleType;
};

#endif