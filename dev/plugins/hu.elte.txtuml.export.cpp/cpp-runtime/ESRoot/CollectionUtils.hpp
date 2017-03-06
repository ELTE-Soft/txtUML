#include <list>

namespace CollectionUtils
{

template<typename E>
E* select(std::list<E*> elements)
{
	return elements.front();
}

template<typename E>
int count(std::list<E*> elements)
{
	return (int)elements.size();
}

}



