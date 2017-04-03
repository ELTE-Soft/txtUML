/** @file association.hpp
*/

#ifndef ASSOCIATION_H
#define ASSOCIATION_H

#include <list>

namespace Model
{

/*! Represents an association end. */
template <typename EndType>
class AssociationEnd
{
public:

    AssociationEnd(int lower, int upper): limits(lower,upper) {}
	
	/*!
	Appends a new object to this association end.
	*/
    void addAssoc(EndType* o)
    {
        if ((int)linkedObjects.size() < limits.second || limits.second == AssociationEnd::UNLIMITED())
        {
            linkedObjects.push_back(o);
        }
    }
			
	/*!
	Remove an object from this association end.
	*/
    void removeAssoc(EndType* o)
    {
        linkedObjects.remove(o);
    }
	
	/*!
	Returns an object from this association end for which the specified condition holds.
	*/
    EndType* getOne(bool cond(EndType*) = [](EndType*){return true;})
    {
        EndType* object = nullptr;
        for(EndType* e : linkedObjects)
        {
            if(cond(e))
            {
                object = e;
            }
        }
        return object;
    }
	
	/*!
	Returns all objects from this association end for which the specified condition holds.
	*/
    std::list<EndType*> getAll(bool cond(EndType*) = [](EndType*){return true;})
    {
        std::list<EndType*> conditionedObjects;
        for(EndType* e : linkedObjects)
        {
            if(cond(e))
            {
                conditionedObjects.push_back(e);
            }
        }
        return conditionedObjects;
    }

private:

    std::list<EndType*> linkedObjects;
    std::pair<int,int> limits; //lower, upper
	
	static int UNLIMITED() {return -1;}
};	

}



#endif // ASSOCIATION_H
