#ifndef PRIORITY_QUEUE_HPP
#define PRIORITY_QUEUE_HPP

#include <queue>
#include <vector>
#include <functional>

namespace ES
{



template<typename T, typename CI>
void modifyContainerElements(CI begin, CI end, std::function<bool(const T&)> p, std::function<void(T&)> m) {
	for (CI it = begin; it != end; it++) {
		if (p(*it)) {
			m(*it);
		}
	}
}

template<typename T, typename Compare>
class PriorityQueue : public std::priority_queue<T, std::vector<T>, Compare> {
	typedef typename std::vector<T>::iterator ContainerIterator;
public:
	
	T front()
	{
		return this->top();
	}

	void modifyElements(std::function<bool(const T&)> p, std::function<void(T&)> m) {
		modifyContainerElements<T, ContainerIterator>(this->c.begin(), this->c.end(), p, m);		
	}
};

template<typename T>
class Queue : public std::queue<T> {
	typedef typename std::vector<T>::iterator ContainerIterator;
public:
	void modifyElements(std::function<bool(const T&)> p, std::function<void(T&)> m) {
		modifyContainerElements<T, ContainerIterator>(this->c.begin(), this->c.end(), p, m);

	}
};


}



#endif