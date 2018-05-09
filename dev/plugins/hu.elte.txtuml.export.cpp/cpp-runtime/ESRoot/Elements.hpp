#ifndef ELEMENTS_HPP
#define ElEMENTS_HPP

namespace Model {

template<typename T, int low, int up, typename Container = std::list<T*>>
class MultipliedElement {
public:
	void add(T* o) {
		objects.push_back(o);
	}

	void remove (T* o) {
		objects.remove (o);
	}

	T* one() const {
		return objects.back();
	}

	typename Container::size_type count() const{
		return objects.size();
	}



private:
	Container objects;
};


template<typename T>
struct Type
{
	typedef T* P;
};

template<>
struct Type<int>
{
	typedef int P;
};



template<typename T, int low>
class MultipliedElement<T, low, 1> {
public:

	MultipliedElement(typename Type<T>::P o = nullptr) : object(o) {}
	typename Type<T>::P operator->() {
		return object;
	}

	void add(typename Type<T>::P o) {
		object = o;
	}

	void remove (typename Type<T>::P o) {
		assert (o == object);
		if (o == object) {
			object = nullptr;
		}
	}

	typename Type<T>::P one() const {
		return object;
	}

	int count() const {
		return object == nullptr ? 0 : 1;
	}

private:
	typename Type<T>::P object = nullptr;
};

template<typename T, int low, int up, typename Container = std::list<T*>>
class Property : public MultipliedElement<T,low,up,Container> {
	
};

template<typename T, int low, int up, typename Container = std::list<T*>>
class Variable : public MultipliedElement<T,low,up,Container> {
	
};

}


#endif