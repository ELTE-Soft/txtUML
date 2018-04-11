#include <list>

#ifndef COLLECTION_UTILS_HPP
#define COLLECTION_UTILS_HPP

namespace CollectionUtils
{

template<typename E, typename CollectionType>
E* select(const CollectionType& elements)
{
	return elements.selectAny ();
}

template<typename CollectionType>
int count(const CollectionType& elements)
{
	return (int) elements.count ();
}

}

#endif


