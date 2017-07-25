#ifndef PRIORITY_QUEUE_HPP
#define PRIORITY_QUEUE_HPP

#include <queue>
#include <vector>
#include <functional>

namespace ES
{

	template<typename T>
	class Queue : public std::queue<T> {
	public:
		void modifyElements(std::function<bool(const T&)> p, std::function<void(T&)> m) {
			for (container_type::iterator it = this->c.begin(); it != this->c.end(); it++) {
				if (p(*it)) {
					m(*it);
				}
			}

		}
	};

	template<typename T, typename Special>
	class SpecialPriorityQueue {
	public:

		typedef typename Queue<T>::size_type  size_type;
		typedef typename Queue<T>::value_type value_type;
	public:

		T front()
		{
			return specialQueue.empty() ? simpleQueue.front() : specialQueue.front();
		}

		void push(const T& item) {
			if (isSpecial(item)) {
				specialQueue.push(item);
			}
			else {
				simpleQueue.push(item);
			}
		}

		void pop() {
			specialQueue.empty() ? simpleQueue.pop() : specialQueue.pop();
		}

		void modifyElements(std::function<bool(const T&)> p, std::function<void(T&)> m) {
			simpleQueue.modifyElements(p, m);
			specialQueue.modifyElements(p, m);
		}

		bool empty() const {
			return simpleQueue.empty() && specialQueue.empty();
		}

		size_type size() const {
			return simpleQueue.size() + specialQueue.size();
		}

	private:
		Special isSpecial;
		Queue<T> simpleQueue;
		Queue<T> specialQueue;
	};




}



#endif