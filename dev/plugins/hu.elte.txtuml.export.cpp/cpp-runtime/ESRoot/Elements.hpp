#ifndef ElEMENTS_HPP
#define ElEMENTS_HPP

#include "Types.hpp"
#include <type_traits>
#include <list>
#include <ostream>
#include <assert.h>

template<typename T> struct isPrimitive { enum { value = false }; };
template<> struct isPrimitive<int> { enum { value = true }; };
template<> struct isPrimitive<std::string> { enum { value = true }; };
template<> struct isPrimitive<double> { enum { value = true }; };
template<> struct isPrimitive<bool> { enum { value = true }; };

namespace Model {

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
	MultipliedElement& operator=(const MultipliedElement& m) = default;


	MultipliedElement(const ElementType& e) {
		add(e);
	}

	MultipliedElement& operator=(const ElementType& e) {
		if (count() > 0) {
			hasValue = false;
		}

		add(e);
		return *this;
	}

	template<int oLow, int oUp, typename Container> MultipliedElement(const MultipliedElement<T, oLow, oUp, Container>& e) {
		if (e.count() >= low && e.count() <= 1) {
			add(e.one());
		}
	}


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

}

template<typename T>
bool inline operator<(const Model::MultipliedElement<T,1,1>& e1, const Model::MultipliedElement<T, 1, 1>& e2) {return e1.one() < e2.one();}
template<typename T>
bool inline operator<(const Model::MultipliedElement<T, 1, 1>& e1, const T& e2) {return e1.one() < e2;}
template<typename T>
bool inline operator<(const T& e1, const Model::MultipliedElement<T, 1, 1>& e2) {return e1 < e2.one(); }


template<typename T>
inline bool operator==(const Model::MultipliedElement<T, 1, 1>& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1.one() == e2.one(); }
template<typename T>
bool inline operator==(const Model::MultipliedElement<T, 1, 1>& e1, const T& e2) { return e1.one() == e2; }
template<typename T>
bool inline operator==(const T& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1 == e2.one(); }

template<typename T>
inline bool operator!=(const Model::MultipliedElement<T, 1, 1>& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1.one() != e2.one(); }
template<typename T>
bool inline operator!=(const Model::MultipliedElement<T, 1, 1>& e1, const T& e2) { return e1.one() != e2; }
template<typename T>
bool inline operator!=(const T& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1 != e2.one(); }

template<typename T>
inline bool operator<=(const Model::MultipliedElement<T, 1, 1>& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return  e1.one() <= e2.one(); }
template<typename T>
bool inline operator<=(const Model::MultipliedElement<T, 1, 1>& e1, const T& e2) { return e1.one() <= e2; }
template<typename T>
bool inline operator<=(const T& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1 <= e2.one(); }

template<typename T>
inline bool operator>=(const Model::MultipliedElement<T, 1, 1>& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1.one() >= e2.one(); }
template<typename T>
bool inline operator>=(const Model::MultipliedElement<T, 1, 1>& e1, const T& e2) { return e1.one() >= e2; }
template<typename T>
bool inline operator>=(const T& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1 >= e2.one(); }

template<typename T>
inline bool operator>(const Model::MultipliedElement<T, 1, 1>& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1.one() > e2.one(); }
template<typename T>
bool inline operator>(const Model::MultipliedElement<T, 1, 1>& e1, const T& e2) { return e1.one() > e2; }
template<typename T>
bool inline operator>(const T& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1 > e2.one(); }


template<typename T>
inline T operator+(const Model::MultipliedElement<T, 1, 1>& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1.one() + e2.one(); }
template<typename T>
T inline operator+(const Model::MultipliedElement<T, 1, 1>& e1, const T& e2) { return e1.one() + e2; }
template<typename T>
T inline operator+(const T& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1 + e2.one(); }

template<typename T>
inline T operator-(const Model::MultipliedElement<T, 1, 1>& e1, const Model::MultipliedElement<T, 1, 1>& e2) {return e1.one() - e2.one();}
template<typename T>
T inline operator-(const Model::MultipliedElement<T, 1, 1>& e1, const T& e2) { return e1.one() - e2; }
template<typename T>
T inline operator-(const T& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1 - e2.one(); }


template<typename T>
inline T operator&&(const Model::MultipliedElement<T, 1, 1>& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1.one() && e2.one(); }
template<typename T>
T inline operator&&(const Model::MultipliedElement<T, 1, 1>& e1, const T& e2) { return e1.one() && e2; }
template<typename T>
T inline operator&&(const T& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1 && e2.one(); }

template<typename T>
inline bool operator||(const Model::MultipliedElement<T, 1, 1>& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1.one() || e2.one(); }
template<typename T>
bool inline operator||(const Model::MultipliedElement<T, 1, 1>& e1, const T& e2) { return e1.one() || e2; }
template<typename T>
bool inline operator||(const T& e1, const Model::MultipliedElement<T, 1, 1>& e2) { return e1 || e2.one(); }

template<typename T>
std::ostream& operator<<(std::ostream& out, const Model::MultipliedElement<T, 1, 1>& e) { return out << e.one(); }

#endif