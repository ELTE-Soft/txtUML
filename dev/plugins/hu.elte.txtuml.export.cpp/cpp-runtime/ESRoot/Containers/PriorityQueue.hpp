#ifndef PRIORITY_QUEUE_HPP
#define PRIORITY_QUEUE_HPP

#include <queue>

namespace ES
{

template<typename T, typename Container, typename Compare>
class PriorityQueue : public std::priority_queue<T, Container, Compare> {
public:
	T front()
	{
		return top();
	}
};


}



#endif