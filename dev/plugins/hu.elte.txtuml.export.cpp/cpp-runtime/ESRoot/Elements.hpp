#ifndef ELEMENTS_HPP
#define ElEMENTS_HPP

#include "Types.hpp"

namespace Model {


template<typename T> isPrimitive {enum {value = false};};
template<> isPrimitive<int> {enum {value = true};};
template<> isPrimitive<std::string> {enum {value = true};};
template<> isPrimitive<double> {enum {value = true};};

template<typename T, bool primitive>
struct EType
{
	typedef T* Type;
};

template<typename T>
struct EType<T, true>
{
	typedef T Type;
};

template<typename T, int low, int up, typename Container = std::list<typename EType<T,isPrimitive<T>::value>::Type>>
class MultipliedElement {
public:
	using ElementType = typename EType<T,isPrimitive<T>::value>::Type;

	void add(ElementType o) {
		objects.push_back(o);
	}

	void remove (ElementType o) {
		objects.remove (o);
	}

	ElementType one() const {
		return objects.back();
	}

	typename Container::size_type count() const{
		return objects.size();
	}

private:
	Container objects;
};

template<typename T, int low>
class MultipliedElement<T, low, 1> {
public:
	using ElementType = typename EType<T,isPrimitive<T>::value>::Type;
	
	MultipliedElement(ElementType o) : object(o) {}
	ElementType operator->() {
		return object;
	}

	void add(ElementType o) {
		assert (!hasValue);
		
		object = o;
		hasValue = true;
	}

	void remove (ElementType o) {
		assert (hasValue && o == object);
		
		if (o == object) {
			hasValue = false;
		}
	}

	ElementType one() const {
		return object;
	}

	int count() const {
		return hasValue ? 1 : 0;
	}

private:
	ElementType object;
	bool hasValue = false;
};

template<typename T, int low, int up, typename Container = std::list<T*>>
class Property : public MultipliedElement<T,low,up,Container> {
	
};

template<typename T, int low, int up, typename Container = std::list<T*>>
class Variable : public MultipliedElement<T,low,up,Container> {
	
};

}


#endif