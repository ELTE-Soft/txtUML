#ifndef ELEMENTS_HPP
#define ElEMENTS_HPP

#include "Types.hpp"
#include <type_traits>

namespace Model {


template<typename T> struct isPrimitive {enum {value = false};};
template<> struct isPrimitive<int> {enum {value = true};};
template<> struct isPrimitive<std::string> {enum {value = true};};
template<> struct isPrimitive<double> {enum {value = true};};

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
	template<typename T, int oLow, int oUp, typename Container> friend class MultipliedElement;

	MultipliedElement() = default;
	MultipliedElement(const MultipliedElement& m) = default;

	template<int oLow, int oUp, typename Container> MultipliedElement(const MultipliedElement<T, oLow, oUp, Container>& e) {
		if (e.count() >= low && (e.count() <= up || up == -1)) {
				objects = e.objects;
		}
	}

	template<int oLow> MultipliedElement(const MultipliedElement<T, oLow, 1>& e) {
		if (e.count() == 1) {
			add(e.one());
		}
	}

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
	template<typename T, int oLow, int oUp, typename Container> friend class MultipliedElement;

	MultipliedElement() = default;
	MultipliedElement(const MultipliedElement& m) = default;

	template<int oLow, int oUp, typename Container> MultipliedElement(const MultipliedElement<T, oLow, oUp, Container>& e) {
		if (e.count() >= low && e.count() <= 1) {
			add(e.one());
		}
	}

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

/*template<typename T, int low, int up, typename Container = std::list<T*>>
class Property : public MultipliedElement<T,low,up,Container> {
	
};

template<typename T, int low, int up, typename Container = std::list<T*>>
class Variable : public MultipliedElement<T,low,up,Container> {
	
};*/

}


#endif